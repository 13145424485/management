package com.doc.web.member_card.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("member_card")
public class MemberCard {
    @TableId(type= IdType.AUTO)
    private Long cardId;
    private String title;
    private String cardType;
    private BigDecimal price;
    private Integer cardDay;
    private String status;
}
