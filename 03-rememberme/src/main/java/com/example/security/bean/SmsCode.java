package com.example.security.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsCode {
    private String code; //验证码
    private LocalDateTime expireTime; //过期时间

    public SmsCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public SmsCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * 判断验证码是否过期
     * @return
     */
    public boolean isExpire(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
