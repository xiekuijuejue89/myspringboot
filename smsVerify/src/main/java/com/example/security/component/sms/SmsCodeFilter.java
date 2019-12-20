package com.example.security.component.sms;

import com.example.security.bean.ImageCode;
import com.example.security.bean.SmsCode;
import com.example.security.controller.ValidateController;
import com.example.security.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SmsCodeFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equalsIgnoreCase("/login/mobile", request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(),"post")){
            try {
                validateSmsCode(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validateSmsCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        String smsCodeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "smsCode");
        System.out.println("----SmsCodeFilter.java smsCodeInRequest = " + smsCodeInRequest);
        String mobile = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "mobile");
        System.out.println("----SmsCodeFilter.java mobile = " + mobile);
        SmsCode codeInSession = (SmsCode) sessionStrategy.getAttribute(servletWebRequest, ValidateController.SESSION_KEY_SMS_CODE + mobile);
        System.out.println("----SmsCodeFilter.java codeInSession = " + codeInSession.toString());

        if(StringUtils.isBlank(smsCodeInRequest)){
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (codeInSession == null){
            throw new ValidateCodeException("验证码不存在！");
        }
        if (codeInSession.isExpire()){
            sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_SMS_CODE);
            throw new ValidateCodeException("验证码已过期！");
        }if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), smsCodeInRequest)){
            throw new ValidateCodeException("验证码不正确！");
        }
        sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_SMS_CODE);
    }
}
