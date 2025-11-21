package com.doc.web.goods.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.goods.entity.Goods;
import com.doc.web.goods.entity.GoodsParam;
import com.doc.web.goods.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    //新增
    @PostMapping("/add")
    public ResultVo add(@RequestBody Goods goods){
        if(goodsService.save(goods)){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    //编辑
    @PutMapping
    public ResultVo update(@RequestBody Goods goods) {
        if (goodsService.updateById(goods)) {
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败");
    }

    //删除
    @DeleteMapping("/{goodsId}")
    public ResultVo delete(@PathVariable("goodsId") Long goodsId) {
        if (goodsService.removeById(goodsId)) {
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }
    //列表
    @GetMapping("/list")
    public ResultVo list(GoodsParam parm) {
        IPage<Goods> page = new Page<>(parm.getCurrentPage(), parm.getPageSize());
        //构造查询条件
        QueryWrapper<Goods> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(parm.getName())){
            query.lambda().like(Goods::getName,parm.getName());
        }
        IPage<Goods> list = goodsService.page(page,query);
        return ResultUtils.success("查询成功",list);
    }

}
