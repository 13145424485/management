package com.doc.web.sys_role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import  lombok.Data;

import java.util.Date;

@Data
@TableName("sys_role")//跟那张表进行映射
public class SysRole {

    @TableId(type = IdType.AUTO)
    private Long roleId;

    private  String roleName;

    private  String remark;

    private Date createTime;

    private  Date updateTime;
}
