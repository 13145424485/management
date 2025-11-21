package com.doc.web.sys_user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.sys_user.entity.PageParam;
import com.doc.web.sys_user.entity.SysUser;
import com.doc.web.sys_user.mapper.SysUserMapper;
import com.doc.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
//拿到通用 CRUD；泛型 <Mapper, Entity> 指定操作谁
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public IPage<SysUser> list(PageParam param) {
        // 实现分页查询逻辑,构造分页对象
        // 这里可以使用 MyBatis-Plus 的分页查询方法
        IPage<SysUser> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getCurrentPage());
        //构造查询条件
        //动态 SQL 条件构造器 QueryWrapper
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        //Spring 的判空工具
        if(StringUtils.isNotEmpty(param.getNickName())){
            query.lambda().like(SysUser::getNickName, param.getNickName());
        }

        if(StringUtils.isNotEmpty(param.getPhone())){
            query.lambda().like(SysUser::getPhone, param.getPhone());
        }

        return this.baseMapper.selectPage(page, query);
    }

    @Override
    public SysUser loadUser(String username) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUsername, username);
        return this.getOne(query);
    }
}
