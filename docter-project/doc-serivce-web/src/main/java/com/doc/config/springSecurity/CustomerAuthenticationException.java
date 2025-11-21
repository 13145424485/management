package com.doc.config.springSecurity;

import org.springframework.security.core.AuthenticationException;

/**
 * 只定义认证异常处理类
 */
public class CustomerAuthenticationException extends AuthenticationException {

    public CustomerAuthenticationException(String msg){
        super(msg);
    }
}

