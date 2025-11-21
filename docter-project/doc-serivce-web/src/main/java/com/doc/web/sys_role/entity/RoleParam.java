package com.doc.web.sys_role.entity;


import lombok.Data;

/**
 * 角色参数类
 * 封装Role相关的分页查询参数
 */
@Data
public class RoleParam {

    private Long currentPage; // 当前页码

    private Long pageSize; // 每页显示容量

    private String roleName; // 角色名称，用于模糊查询
}
