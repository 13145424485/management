package com.doc.web.member_apply.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("member_apply")
public class MemberApply {

    @TableId(type = IdType.AUTO)
    private Long applyId;
    private Long memberId;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private Date createTime;
    private String createUser;
}
