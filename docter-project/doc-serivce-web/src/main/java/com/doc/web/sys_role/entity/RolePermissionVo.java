package com.doc.web.sys_role.entity;

import com.doc.web.sys_menu.entity.SysMenu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//封装角色权限相关数据的视图对象
@Data
public class RolePermissionVo{
    //当前登录系统用户的菜单数据

    List<SysMenu> listmenu = new ArrayList<>();
    //角色原来分配的菜单,存储当前角色已分配的菜单 ID 数组
    private Object[] checkList;
}
