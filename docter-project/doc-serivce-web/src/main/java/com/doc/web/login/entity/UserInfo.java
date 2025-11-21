package com.doc.web.login.entity;

import lombok.Data;

@Data
public class UserInfo {

    private Long userId;
    private String name;
    private Object[] permissions;
}
