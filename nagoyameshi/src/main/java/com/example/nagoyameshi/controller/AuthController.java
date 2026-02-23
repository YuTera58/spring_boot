package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.form.SignupForm;
//import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.service.UserService;
//import com.example.nagoyameshi.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
	private final UserService userService;
	//private final SignupEventPublisher signupEventPublisher;
	//private final VerificationTokenService verificationTokenService;

	public AuthController(UserService userService) {
        this.userService = userService;
        //this.signupEventPublisher = signupEventPublisher;
        //this.verificationTokenService = verificationTokenService;
    }
	
	@GetMapping("/login")
    public String login() {
        return "auth/login";
    }
	
    @GetMapping("/signup")
    public String signup(Model model) {                       //ビューにフォームクラスのインスタンスを渡す
        model.addAttribute("signupForm", new SignupForm());   //Modelクラスを使ってビューにデータを渡す
        return "auth/signup";
    }
    
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest httpServletRequest,
                         Model model)
    {
    	/*
    	・FieldErrorクラスのインスタンスを作成し、それをaddError()メソッドに渡す
　　　　・FieldErrorクラスのコンストラクタに渡す引数は以下のとおり
　　　　　第1引数：エラー内容を格納するオブジェクト名
　　　　　第2引数：エラーを発生させるフィールド名
　　　　　第3引数：エラーメッセージ
    	 */
        // メールアドレスが登録済みであれば、BindingResultオブジェクトにエラー内容を追加する
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);
        }

        // パスワードとパスワード（確認用）の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("signupForm", signupForm);

            return "auth/signup";
        }

        /* 旧コード
        ・RedirectAttributesは、リダイレクト先にデータを渡すための機能を提供するインターフェース
　　　　・RedirectAttributesインターフェースが提供するaddFlashAttribute()メソッドを使うことで、
　　　　　リダイレクト先にデータを渡すことができる
　　　　　引数は以下のとおり
　　　　　　第1引数：リダイレクト先から参照する変数名
　　　　　　第2引数：リダイレクト先に渡すデータ
　　　　・なお、addFlashAttribute()メソッドで渡されたデータはリダイレクト先で取得されたあと、自動的に削除されます。
　　　　　よって、リダイレクトの直後に1回限り利用するデータを渡す際に使います。
         */
        userService.createUser(signupForm);
        redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。");
        
        //User createdUser = userService.createUser(signupForm);
        //String requestUrl = new String(httpServletRequest.getRequestURL());
        //signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        //redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");

        return "redirect:/";
    }
}
