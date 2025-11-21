package com.doc.web.goods.entity;

import lombok.Data;

@Data
public class GoodsParam {

    private Long currentPage;
    private Long pageSize;
    private String name;
}
