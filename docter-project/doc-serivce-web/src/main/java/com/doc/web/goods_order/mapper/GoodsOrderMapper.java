package com.doc.web.goods_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doc.web.goods_order.entity.GoodsOrder;
import com.doc.web.home.entity.EchartItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface GoodsOrderMapper extends BaseMapper<GoodsOrder> {

    //热销商品
    List<EchartItem> hotGoods();

    //热销卡
    List<EchartItem> hotCard();

    //热销课程
    List<EchartItem> hotCourse();
}
