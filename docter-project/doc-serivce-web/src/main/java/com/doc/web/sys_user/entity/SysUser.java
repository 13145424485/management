package com.doc.web.sys_user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Data
@TableName("sys_user")
public class SysUser implements UserDetails {

    //声明“这是主键，并且它的值由数据库自增”。
    @TableId(type = IdType.AUTO)
    private Long userId;
    //表明roleId字段不属于sys_user表，需要排除
    @TableField(exist = false)
    private Long roleId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String sex;
    private String isAdmin;
    //类型（1：员工 2：教练）
    private String userType;
    //状态 0：停用 1：启用
    private String status;
    private BigDecimal salary;
    private String nickName;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //帐户是否过期(1 未过期，0已过期)
    private boolean isAccountNonExpired = true;
    //帐户是否被锁定(1 未锁定，0已锁定)
    private boolean isAccountNonLocked = true;
    //密码是否过期(1 未过期，0已过期)
    private boolean isCredentialsNonExpired = true;
    //帐户是否可用(1 可用，0 删除用户)
    private boolean isEnabled = true;
    //用户权限字段的集合 authorities字段不属于sys_user表，需要排除
    //(exist = false) → 注解参数；给注解的 exist 属性赋值 false，告诉框架“这个字段在表里不存在”。
    @TableField(exist = false)
    Collection<? extends GrantedAuthority> authorities;
}