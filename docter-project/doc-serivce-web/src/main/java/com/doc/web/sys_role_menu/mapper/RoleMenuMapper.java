package com.doc.web.sys_role_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doc.web.sys_role_menu.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    //保存
    boolean saveRoleMenu(@Param("roleId") Long roleId, @Param("menuId") List<Long> menuId);
}
