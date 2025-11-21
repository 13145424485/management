package com.doc.web.sys_role_menu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.sys_role_menu.entity.RoleMenu;
import com.doc.web.sys_role_menu.entity.SaveMenuParam;
import com.doc.web.sys_role_menu.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService{
    @Transactional
    @Override
    public void saveMenu(SaveMenuParam parm) {
        //先删除角色原来的权限
        QueryWrapper<RoleMenu> query = new QueryWrapper<>();
        query.lambda().eq(RoleMenu::getRoleId,parm.getRoleId());
        //this.baseMapper：继承 MyBatis-Plus 的 BaseMapper 后，自动拥有的基础 CRUD 方法
        this.baseMapper.delete(query);

        //重新保存
        this.baseMapper.saveRoleMenu(parm.getRoleId(),parm.getList());
    }
}
