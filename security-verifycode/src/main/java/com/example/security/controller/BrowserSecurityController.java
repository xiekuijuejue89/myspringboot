package com.example.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 备注：和BrowserSecurityConfig中的.loginPage("/authentication/require")配套使用
     * 目的：在未登录的情况，当用户访问html资源的时候跳转到登录页，如果不是html资源，返回错误信息
     * 说明：1.HttpSessionRequestCache为spring security提供用于缓存请求的对象，通过调用它的getRequest方法可以获取到本次请求的http信息
     * 2.DefaultRedirectStrategy的sendRedirect为spring security提供用于处理重定向的方法
     * 3.获取引发跳转的请求，判断请求是否以.html为结尾(不论该html是否存在)来对应不同的处理方法。如果是.html结尾，重定向到登录页面；如果不是，返回"访问的资源需要身份认证!"，并且http状态码为401 HttpStatus.UNAUTHORIZED
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        System.out.println("---1");
        if(savedRequest != null){
            String targetUrl = savedRequest.getRedirectUrl();
            System.out.println("---targetUrl: " + targetUrl);
            redirectStrategy.sendRedirect(request, response, "/login.html");
//            if(StringUtils.endsWithIgnoreCase(targetUrl, ".html")){
//                redirectStrategy.sendRedirect(request, response, "/login.html");
//            }
        }
        return "访问的资源需要身份认证!";
    }
}
