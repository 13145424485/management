package com.doc.web.goods.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.goods.entity.Goods;
import com.doc.web.goods.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService{
}
