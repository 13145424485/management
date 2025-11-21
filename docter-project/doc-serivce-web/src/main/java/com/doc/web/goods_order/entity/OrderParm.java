package com.doc.web.goods_order.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderParm {
    private Long UserId;

    ///OrderItem 是一个 订单项实体类 ，用于封装单个订单中的商品信息
    private List<OrderItem> orderList;
}
