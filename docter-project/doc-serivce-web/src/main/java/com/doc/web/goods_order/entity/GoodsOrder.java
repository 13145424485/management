package com.doc.web.goods_order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("goods_order")
public class GoodsOrder {
    @TableId(type = IdType.AUTO)
    private Long orderId;
    private Long goodsId;
    private String name;
    private String image;
    private String details;
    private String unit;
    private String specs;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer num;
    private String controlUser;
}
