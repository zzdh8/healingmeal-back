package com.example.thehealingmeal.email.api.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCheckReq {
    private String email;

    public EmailCheckReq(String email) {
        this.email = email;
    }
}
