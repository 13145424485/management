package com.doc.web.sys_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doc.web.sys_menu.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {



    List<SysMenu> getParent();
    //根据员工id查询菜单信息
    List<SysMenu> getMenuByUserId(Long userId);
    //根据会员id查询菜单信息
    List<SysMenu> getMenuByMemberId(Long userId);

    //根据角色id查询菜单信息
    List<SysMenu> getMenuByRoleId(Long roleId);
}
