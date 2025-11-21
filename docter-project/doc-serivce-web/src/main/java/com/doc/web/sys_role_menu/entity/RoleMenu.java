package com.doc.web.sys_role_menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//分配权限树保存与前端对接
@Data
@TableName("sys_role_menu")
public class RoleMenu {
    @TableId(type= IdType.AUTO)
    private Long roleMenuId;
    private Long roleId;
    private long menuId;

}
