package com.doc.web.equipment.entity;


import lombok.Data;

@Data
public class ListParam {
    private Long currentPage;
    private Long pageSize;
    private String name;
}
