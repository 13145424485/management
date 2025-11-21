package com.doc.web.member.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeParam {

    private Long userId;
    private Long memberId;
    private BigDecimal money;
}
