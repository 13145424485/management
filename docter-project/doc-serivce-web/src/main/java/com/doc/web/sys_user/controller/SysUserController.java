package com.doc.web.sys_user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.sys_role.entity.SelectType;
import com.doc.web.sys_role.service.SysRoleService;
import com.doc.web.sys_user.entity.PageParam;
import com.doc.web.sys_user.entity.SysUser;
import com.doc.web.sys_user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class SysUserController {
    @Autowired
     SysUserService sysUserService;

    @Autowired
     SysRoleService sysRoleService;

    @Autowired
    PasswordEncoder passwordEncoder;
    //新增员工
    @PostMapping
    public ResultVo addUser(@RequestBody SysUser sysUser) {
        //判断用户名字是否存在
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        //指定列，指定值
        query.lambda().eq(SysUser::getNickName, sysUser.getNickName());
        SysUser one = sysUserService.getOne(query);
        if (one != null) {
            return ResultUtils.error("员工名称已存在，请重新输入");
        }

//        //密码加密
//        if (StringUtils.isNotEmpty(sysUser.getPassword())) {
//            sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));
//
//        }
        //密码加密
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        //设置默认角色
        sysUser.setIsAdmin("0");
        sysUser.setCreateTime(new Date());
        //存储到数据库中
        boolean save = sysUserService.save(sysUser);
        if (save) {
            return ResultUtils.success("新增用户成功");
        }

        return ResultUtils.error("新增用户失败");

    }

    //编辑员工
    @PutMapping
    public ResultVo editUser(@RequestBody SysUser sysUser) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        //指定列，指定值
        query.lambda().eq(SysUser::getUsername, sysUser.getUsername());
        SysUser one = sysUserService.getOne(query);
        if(one != null && one.getUserId()!= sysUser.getUserId()){
            return ResultUtils.error("用户名已存在，请重新输入");
        }
        //密码加密
        if (StringUtils.isNotEmpty(sysUser.getPassword())) {
            sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));

        }
        sysUser.setUpdateTime(new Date());
        //存入数据库
        boolean save = sysUserService.updateById(sysUser);
        if (save) {
            return ResultUtils.success("编辑用户成功");
        }

        return ResultUtils.error("编辑用户失败");

    }

    //删除员工
    @DeleteMapping("/{userId}")
    public ResultVo deleteUser(@PathVariable("userId") Long userId) {
        boolean remove = sysUserService.removeById(userId);
        if (remove) {
            return ResultUtils.success("删除用户成功");
        }
        return ResultUtils.error("删除用户失败");
    }

    //查询用户列表
    @GetMapping("/list")
    public ResultVo getlist(PageParam param) {
        IPage<SysUser> list = sysUserService.list(param);
        //查询出来不显示密码
        list.getRecords().forEach(user -> user.setPassword(null));
        return ResultUtils.success("查询成功", list);
    }
    //获取角色选择列表
    @GetMapping("/role/getSelect")
    public ResultVo getRoleSelect() {
        //查询所有角色列表
        return ResultUtils.success("查询成功", sysRoleService.list());
    }


    //查询课程教练
    @GetMapping("/getTeacher")
    public ResultVo getTeacher() {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.lambda().eq(SysUser::getUserType, "2");
        //把刚刚拼好的查询条件真正发到数据库，并把查出来的所有记录一次性变成 Java 对象列表返回
        List<SysUser> list = sysUserService.list(query);
        //组装后的select数据
       // 每循环到一个 SysUser，就 new 一个 SelectType；
        //把教练姓名塞进 label，把用户 ID 塞进 value；
       // selectTypeList.add(selectType) → 把这个小对象追加到列表尾部；
        //循环结束后，selectTypeList 里就是 [ {label:"张三",value:1}, {label:"李四",value:2}, ... ]，
       // 最终 return ResultUtils.success(...) 给前端做下拉框/选择器使用。
        List<SelectType> selectTypeList = new ArrayList<>();
        if(list.size() > 0){
            list.stream().forEach(item -> {
                SelectType selectType = new SelectType();
                selectType.setValue(item.getRoleId());
                selectType.setLabel(item.getNickName());
                //把刚装完数据的小对象 selectType 塞进集合 selectTypeList，等全部循环结束后一次性返回给前端。
                selectTypeList.add(selectType);
            });
        }
        return ResultUtils.success("查询成功", selectTypeList);
    }

}
