package com.doc.web.sys_user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.doc.web.sys_user.entity.PageParam;
import com.doc.web.sys_user.entity.SysUser;
//声明一个用户业务接口，它天生自带 MyBatis-Plus 的全部单表 CRUD 能力，额外方法自己再补
public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> list(PageParam param);
    //用户认证
    //根据员工姓名查询员工信息
    SysUser loadUser(String username);

}
