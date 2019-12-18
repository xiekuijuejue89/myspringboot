package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()//表单形式登录
                .loginPage("/login.html") //指定跳转到登录页面的请求URL
                .loginProcessingUrl("/login") //对应登录页面form表单的action = "/login"
                .and()
                .authorizeRequests() //授权配置
                .antMatchers("/login.html").permitAll() //表示跳转到登录页面的请求不被拦截
                .anyRequest() //所有请求
                .authenticated()//都要认证
                .and().csrf().disable();// 关闭CSRF攻击防御
        //http.httpBasic().and().authorizeRequests().anyRequest().authenticated();//http basic认证模式
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
