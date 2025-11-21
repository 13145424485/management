package com.doc.web.sys_user.entity;

import lombok.Data;

@Data
public class PageParam {

    private Long currentPage;
    private Long pageSize;
    private String phone;
    private String nickName;
}
