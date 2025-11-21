package com.doc.web.sys_role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.doc.web.sys_role.entity.RoleAssignParam;
import com.doc.web.sys_role.entity.RoleParam;
import com.doc.web.sys_role.entity.RolePermissionVo;
import com.doc.web.sys_role.entity.SysRole;
//业务接口
public interface SysRoleService  extends IService<SysRole> {
    IPage<SysRole> list(RoleParam roleParam);

    //查询权限树数据回显
    RolePermissionVo getMenuTree(RoleAssignParam param);

}
