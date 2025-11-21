package com.doc.web.goods_order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.goods_order.entity.GoodsOrder;
import com.doc.web.goods_order.mapper.GoodsOrderMapper;
import com.doc.web.home.entity.EchartItem;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderMapper, GoodsOrder> implements GoodsOrderService {
    //实现接口
    @Override
    public List<EchartItem> hotGoods() {
        return this.baseMapper.hotGoods();
    }
    @Override
    public List<EchartItem> hotCards() {
        return this.baseMapper.hotCard();
    }
    @Override
    public List<EchartItem> hotCourse() {
        return this.baseMapper.hotCourse();
    }

}
