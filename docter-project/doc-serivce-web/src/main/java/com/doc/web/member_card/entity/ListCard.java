package com.doc.web.member_card.entity;

import lombok.Data;

@Data
public class ListCard {

    private Long currentPage;
    private Long pageSize;
    private String title;
}
