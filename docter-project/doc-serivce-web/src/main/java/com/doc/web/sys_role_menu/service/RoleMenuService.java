package com.doc.web.sys_role_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doc.web.sys_role_menu.entity.RoleMenu;
import com.doc.web.sys_role_menu.entity.SaveMenuParam;
import org.springframework.transaction.annotation.Transactional;

public interface RoleMenuService extends IService<RoleMenu> {



    @Transactional
    void saveMenu(SaveMenuParam param);
}
