package com.example.security.config;

import com.example.security.component.MyAuthenticationFailureHandler;
import com.example.security.component.MyAuthenticationSucessHandler;
import com.example.security.component.ValidateCodeFilter;
import com.example.security.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationSucessHandler authenticationSucessHandler;
    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MyUserDetailService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) //将validateCodeFilter拦截器加到UsernamePasswordAuthenticationFilter之前
                .formLogin()//表单形式登录
                //.loginPage("/login.html") //指定跳转到登录页面的请求URL
                .loginPage("/authentication/require") //登录跳转 URL 配合 BrowserSecurityController中的requireAuthentication使用
                .loginProcessingUrl("/login") //对应登录页面form表单的action = "/login"
                .successHandler(authenticationSucessHandler)   //处理登录成功
                .failureHandler(authenticationFailureHandler)  //处理登录失败
                .and()
                .rememberMe() //记住我功能
                .tokenRepository(persistentTokenRepository()) //配置token持久化仓库
                .tokenValiditySeconds(3600)  //remember过期时间，单位秒
                .userDetailsService(userDetailsService)// 处理自动登录逻辑
                .and()
                .authorizeRequests() //授权配置
                .antMatchers("/authentication/require","/login.html","/code/image").permitAll() //表示跳转到登录页面的请求不被拦截
                .anyRequest() //所有请求
                .authenticated()//都要认证
                .and().csrf().disable();// 关闭CSRF攻击防御
        //http.httpBasic().and().authorizeRequests().anyRequest().authenticated();//http basic认证模式
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security CSS静态资源拦截问题，不加这个配置，css样式无法使用，而且登录后一直重定向到了css文件
     * @param web
     */
    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/css/**");
    }


    /**
     * 记住我功能，需要将token持久化到数据库中，在这里配置token持久化对象
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }
}
