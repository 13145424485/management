package com.doc.web.sys_role_menu.entity;

import lombok.Data;

import java.util.List;

@Data
public class SaveMenuParam {
    private Long roleId;
    private List<Long> list;
}
