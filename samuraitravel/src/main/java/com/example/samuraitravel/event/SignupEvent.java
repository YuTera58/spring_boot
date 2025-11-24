package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEvent;

import com.example.samuraitravel.entity.User;

import lombok.Getter;


/* ポイントは以下の3つです。
1.ApplicationEventクラスを継承する
2.イベントに関する情報を保持する
3.クラスに@Getterアノテーションをつける
 */
@Getter
public class SignupEvent extends ApplicationEvent {
    private User user;
    private String requestUrl;

    public SignupEvent(Object source, User user, String requestUrl) {
        super(source);

        this.user = user;
        this.requestUrl = requestUrl;
    }
}