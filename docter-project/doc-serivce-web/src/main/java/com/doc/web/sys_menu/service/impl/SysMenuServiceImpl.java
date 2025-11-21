package com.doc.web.sys_menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.sys_menu.entity.MakeMenuTree;
import com.doc.web.sys_menu.entity.SysMenu;
import com.doc.web.sys_menu.mapper.SysMenuMapper;
import com.doc.web.sys_menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Override
    public List<SysMenu> getMenuByUserId(Long userId){
        return this.baseMapper.getMenuByUserId(userId);
    }

    @Override
    public List<SysMenu> getMenuByRoleId(Long roleId) {
        return this.baseMapper.getMenuByRoleId(roleId);
    }

    @Override
    public List<SysMenu> getMenuByMemberId(Long userId) {
        return this.baseMapper.getMenuByMemberId(userId);
    }



    @Override
    public List<SysMenu> getParent() {
        //查询目录和菜单
        String[] types = {"0","1"};
        List<String> Stirngs = Arrays.asList(types);
        //构造查询条件
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        //LambdaQueryWrapper 链式写法
        // n(SysMenu::getType,Stirngs),type IN (?, ?)	把集合 strings 里的值作为参数，生成 IN 条件  过滤
        // orderByAsc(SysMenu::getOrderNum)  按照 orderNum 升序排序
        query.lambda().in(SysMenu::getType,Stirngs).orderByAsc(SysMenu::getOrderNum);

        //用 MyBatis-Plus 查库，拿到 扁平菜单列表（不含任何树结构
        List<SysMenu> menus = this.baseMapper.selectList(query);

        //组装顶级菜单
        SysMenu menu = new SysMenu();
        menu.setMenuId(0L);
        //顶级节点没有父id
        menu.setParentId(-1L);
        menu.setTitle("顶级菜单");
        menus.add(menu);
        //组装成树返回

        return MakeMenuTree.makeTree(menus,-1L);



    }
}
