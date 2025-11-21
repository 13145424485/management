package com.doc.web.member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@TableName("member")
public class Member implements UserDetails {
    @TableId(type = IdType.AUTO)
    private Long memberId;
    //排除roleId字段
    @TableField(exist = false)
    private Long roleId;
    private String name;
    private String sex;
    private String phone;
    private Integer age;
    private String birthday;
    private Integer height;
    private Integer weight;
    private Integer waist;
    private String joinTime;
    private String endTime;
    private String username;
    private String password;
    private String status;

    private String cardType;
    private Integer cardDay;
    private BigDecimal money;
    private BigDecimal price;

    //帐户是否过期(1 未过期，0已过期)
    @TableField(exist = false)
    private boolean isAccountNonExpired = true;
    //帐户是否被锁定(1 未锁定，0已锁定)
    @TableField(exist = false)
    private boolean isAccountNonLocked = true;
    //密码是否过期(1 未过期，0已过期)
    @TableField(exist = false)
    private boolean isCredentialsNonExpired = true;
    //帐户是否可用(1 可用，0 删除用户)
    @TableField(exist = false)
    private boolean isEnabled = true;
    //用户权限字段的集合
    //表明authorities字段不属于sys_user表，需要排除
    @TableField(exist = false)
    Collection<? extends GrantedAuthority> authorities;
}