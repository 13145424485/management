package com.doc.web.member_role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//创建会员和角色关联表实体类
@Data
@TableName("member_role")
public class MemberRole {

    @TableId(type = IdType.AUTO)
    private Long memberRoleId;
    private Long memberId;
    private Long roleId;

}
