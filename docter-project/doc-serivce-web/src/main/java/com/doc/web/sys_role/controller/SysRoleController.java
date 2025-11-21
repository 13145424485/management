package com.doc.web.sys_role.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.sys_role.entity.*;
import com.doc.web.sys_role.service.SysRoleService;
import com.doc.web.sys_role_menu.entity.SaveMenuParam;
import com.doc.web.sys_role_menu.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    //新增角色信息
    @PostMapping
    @PreAuthorize("hasAuthority('sys:role:add')")
    public ResultVo addRole(@RequestBody SysRole role){
        role.setCreateTime(new Date());
        boolean save = sysRoleService.save(role);
        if(save){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑角色
    @PutMapping("/{roleId}")
    public ResultVo editRole(@RequestBody SysRole role){
        role.setUpdateTime(new Date());
        boolean save = sysRoleService.updateById(role);
        if(save){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除角色
    @DeleteMapping("/{roleId}")
    public ResultVo deleteRole(@PathVariable Long roleId){
        boolean remove = sysRoleService.removeById(roleId);
        if(remove){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }

    //角色列表分页查询
    @GetMapping("/list")
    public ResultVo getList(RoleParam param){
        IPage<SysRole> list = sysRoleService.list(param);
        return ResultUtils.success("查询成功", list);

    }
    //查询页面需要的显示的角色信息
    @GetMapping("/getSelect")
    public ResultVo getListSelect(){
        List<SysRole> list = sysRoleService.list();
        List<SelectType> selectTypeList = new ArrayList<>();
        if(list != null && list.size() > 0){
            list.stream().forEach(item -> {
                SelectType type = new SelectType();
                type.setValue(item.getRoleId());
                type.setLabel(item.getRoleName());
            });
        }
        return ResultUtils.success("查询成功", selectTypeList);
    }

    //分配权限树数据回显
    @GetMapping("/getMenuTree")
    public ResultVo getMenuTree(RoleAssignParam param){
        RolePermissionVo tree= sysRoleService.getMenuTree(param);
        return ResultUtils.success("查询成功",tree);

    }

    @Autowired
    private RoleMenuService roleMenuService;
    //分配权限
    @PostMapping("/saveRoleMenu")
    public ResultVo saveRoleMenu(@RequestBody SaveMenuParam param){
        roleMenuService.saveMenu(param);
        return ResultUtils.success("保存成功");
    }

}
