package com.doc.web.sys_role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.sys_menu.entity.MakeMenuTree;
import com.doc.web.sys_menu.entity.SysMenu;
import com.doc.web.sys_menu.service.SysMenuService;
import com.doc.web.sys_role.entity.RoleAssignParam;
import com.doc.web.sys_role.entity.RoleParam;
import com.doc.web.sys_role.entity.RolePermissionVo;
import com.doc.web.sys_role.entity.SysRole;
import com.doc.web.sys_role.mapper.SysRoleMapper;
import com.doc.web.sys_role.service.SysRoleService;
import com.doc.web.sys_user.entity.SysUser;
import com.doc.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysUserService sysUserService;

    @Override
    public RolePermissionVo getMenuTree(RoleAssignParam param){
        //查询用户信息
        SysUser user = sysUserService.getById(param.getUserId());
        List<SysMenu> list = null;
        //如果是超级管理员，直接查询所有的菜单
        if(StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")){
            list = sysMenuService.list();
        }else{
            list = sysMenuService.getMenuByUserId(user.getUserId());
        }
        //组装树形结构
        List<SysMenu> menuList = MakeMenuTree.makeTree(list,0L);
        //查询角色原来的数据
        List<SysMenu> roleList = sysMenuService.getMenuByRoleId(param.getRoleId());
        // 初始化菜单 ID 集合
        List<Long> ids = new ArrayList<>();
        //避免 roleList 为 null 时抛出 NullPointerException。
        //若 roleList 不为 null，则使用该集合；若为 null，则用空集合 new ArrayList<>() 替代。
        Optional.ofNullable(roleList).orElse(new ArrayList<>())
                .stream()
                //过滤流中的 null 元素：若集合中存在 null 的 SysMenu 对象（数据异常情况），则排除这些元素，避免后续调用 item.getMenuId() 时抛空指针
                .filter(item -> item != null)
                //遍历流中的每个非空 SysMenu 对象，调用 item.getMenuId() 获取菜单 ID，并添加到 ids 集合中
                .forEach(item ->{ ids.add(item.getMenuId());});
        //组装数据
        RolePermissionVo vo = new RolePermissionVo();
        vo.setListmenu(menuList);
        vo.setCheckList(ids.toArray());
        return vo;
    }

    @Override
    public IPage<SysRole> list(RoleParam param) {

        //制造分页对象
        IPage<SysRole> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getCurrentPage());
        //构造查询条件
        QueryWrapper<SysRole> query = new QueryWrapper<>();
        //动态拼接过滤条件
        if(StringUtils.isNotEmpty(param.getRoleName())){
            query.lambda().like(SysRole::getRoleName,param.getRoleName());
        }
        return this.baseMapper.selectPage(page, query);

    }
}

