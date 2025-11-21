package com.doc.web.member.entity;

import lombok.Data;

@Data
public class RechargeParamList {
    private Long currentPage;
    private Long pageSize;
    private Long memberId;
    private String userType;
}
