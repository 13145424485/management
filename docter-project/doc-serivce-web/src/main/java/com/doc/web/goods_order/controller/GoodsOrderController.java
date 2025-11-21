package com.doc.web.goods_order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.goods.entity.Goods;
import com.doc.web.goods.entity.GoodsParam;
import com.doc.web.goods.service.GoodsService;
import com.doc.web.goods_order.entity.GoodsOrder;
import com.doc.web.goods_order.entity.OrderItem;

import com.doc.web.goods_order.entity.OrderParm;
import com.doc.web.goods_order.service.GoodsOrderService;
import com.doc.web.sys_user.entity.SysUser;
import com.doc.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * - 用户验证 ：根据userId查询用户信息
 * - 商品处理 ：遍历订单中的每个商品项
 * - 价格计算 ：单价 × 数量 = 总价（保留2位小数）
 * - 数据组装 ：将商品信息、用户信息、价格等组装成订单记录
 * - 批量保存 ：使用 saveBatch 方法批量插入订单数据
 */
@RestController
@RequestMapping("/api/goods_order")
public class GoodsOrderController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsOrderService goodsOrderService;

    //下单
    @PostMapping("/down")
    public ResultVo down(@RequestBody OrderParm parm) {
        //查询用户信息
        SysUser sysUser = sysUserService.getById(parm.getUserId());

        List<OrderItem> list = parm.getOrderList();
        List<GoodsOrder> orderList = new ArrayList<>();

        // 3. 遍历订单中的每个商品项，创建对应的订单记录
        for (int i = 0; i < list.size(); i++) {
            Long goodsId = list.get(i).getGoodsId();
            Integer num = list.get(i).getNum();

            //查询商品信息
            Goods goods = goodsService.getById(goodsId);
            // 创建订单对象
            GoodsOrder order = new GoodsOrder();
            BeanUtils.copyProperties(goods, order);
            // 复制商品属性到订单
            // 设置订单数量和总价
            order.setNum(num);
            //BigDecimal 是Java中用于 精确小数运算 的类，避免浮点数精度问题
            //价格计算 - 转换为BigDecimal
            BigDecimal number = BigDecimal.valueOf(list.get(i).getNum());
            BigDecimal price = goods.getPrice();
            //数量 × 单价 = 总价
            BigDecimal total = number.multiply(price);
            //设置小数位数和舍入规则
            BigDecimal totalPrice = total.setScale(2, BigDecimal.ROUND_HALF_UP);

            //设置总价到订单对象
            order.setTotalPrice(totalPrice);
            //设置操作用户
            order.setControlUser(sysUser.getNickName());
            //将处理好的订单对象添加到列表中
            orderList.add(order);
        }
        if (orderList.size() > 0) {
            //saveBatch 是 MyBatis-Plus 框架提供的一个批量保存方法
            goodsOrderService.saveBatch(orderList);
        }
        return ResultUtils.success("下单成功");
    }

    //查询订单列表
    @GetMapping("/list")
    public ResultVo list(GoodsParam param){
        //构造分页对象
        IPage<GoodsOrder> page = new Page<>(param.getCurrentPage(),param.getPageSize());
        //构造查询条件
        QueryWrapper<GoodsOrder> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(param.getName())){
            query.lambda().like(GoodsOrder::getName,param.getName());
        }
        IPage<GoodsOrder> list = goodsOrderService.page(page,query);
        return ResultUtils.success("查询成功",list);
    }
}
