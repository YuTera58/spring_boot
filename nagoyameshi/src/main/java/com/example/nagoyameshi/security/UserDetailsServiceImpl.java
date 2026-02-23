package com.example.nagoyameshi.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    //@Autowired ← コンストラクタが1つしかない場合は無くても良い
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email);                 // リポジトリを利用し、引数で受け取ったメールアドレスに一致する Userエンティティを取得
            String userRoleName = user.getRole().getName();                // 上記のUserエンティティのロール名（String型） を取得
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(userRoleName));     // SimpleGrantedAuthorityクラス（GrantedAuthorityインターフェースを実装したクラス）のコンストラクタにロール名を渡し、インスタンスを生成
                                                                           // 作成した Collection<GrantedAuthority>型のArrayList に、上記の userRoleName インスタンスを追加
            return new UserDetailsImpl(user, authorities);                 // UserDetailsImplクラスのコンストラクタにUserエンティティとArrayListを渡し、インスタンスを生成
                                                                           // 上記のインスタンスを戻り値として返す
        } catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");  // ユーザーが存在しない場合や例外が発生した場合には、UsernameNotFoundExceptionクラスの例外をスロー
        }
    }
}
