package com.doc.web.login.entity;

import lombok.Data;

@Data
public class LoginResult {
    private Long userId;
    private String uername;
    private String token;
    private String userType;
}
