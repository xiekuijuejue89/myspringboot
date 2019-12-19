package com.example.security.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsCode {
    private String code;
    private LocalDateTime expireTime;

    public SmsCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }
    public SmsCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
    public boolean isExpire(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
