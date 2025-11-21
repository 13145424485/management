package com.doc.web.sys_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doc.web.sys_menu.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    //根据员工id查询菜单
    //@Param("userId"), 明确指定该参数在 SQL 中的名称为 userId，避免 MyBatis 因参数名匹配失败导致的查询异常
    List<SysMenu> getMenuByUserId(@Param("userId")Long userId);

    //根据会员id查询菜单
    List<SysMenu> getMenuByMemberId(@Param("userId") Long userId);

    //根据角色id查询菜单
    List<SysMenu> getMenuByRoleId(@Param("roleId") Long roldId);

}
