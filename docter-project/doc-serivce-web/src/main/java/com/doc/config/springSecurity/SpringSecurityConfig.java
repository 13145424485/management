package com.doc.config.springSecurity;

import com.doc.config.springSecurity.CustomizedUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启SpringSecurity注解的使用权限
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    CustomerAccessDeniedHandler customerAccessDeniedHandler;

    @Autowired
    CheckTokenFilter checkTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //解决跨域问题
        http.cors().and().headers().frameOptions().disable();
        //关闭跨域请求伪造过滤器
        http.csrf().disable().authorizeRequests()
                //登录请求，回显验证码请求直接放行
                .antMatchers("/api/login/login","/api/login/image").permitAll()
                // 其他的请求需要认证
                .anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(loginFailureHandler)
                .accessDeniedHandler(customerAccessDeniedHandler);
        //设置token校验过滤器
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //密码加密器的配置
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    CustomizedUserDetailService customizedUserDetailService;

    //配置自定义的UserDetailService
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customizedUserDetailService);
    }

    //注入认证管理器AuthenticationManager
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }





}
