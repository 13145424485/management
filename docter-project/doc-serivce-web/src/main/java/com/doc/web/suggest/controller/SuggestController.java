package com.doc.web.suggest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.suggest.entity.Suggest;
import com.doc.web.suggest.entity.SuggestParm;
import com.doc.web.suggest.service.SuggestService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @Autowired
    private SuggestService suggestService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Suggest suggest){
        suggest.setDateTime(new Date());
        if(suggestService.save(suggest)){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    //编辑
    @PutMapping
    public ResultVo update(@RequestBody Suggest suggest) {
        if (suggestService.updateById(suggest)) {
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败");
    }
    //删除
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable Long id){
        if(suggestService.removeById(id)){
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }


    //列表
    @GetMapping("/list")
    public ResultVo list(SuggestParm suggestParm){
        //构造分页对象
        IPage<Suggest> page = new Page<>(suggestParm.getCurrentPage(),suggestParm.getPageSize());
        //构造查询条件
        QueryWrapper<Suggest> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(suggestParm.getTitle())){
            query.lambda().like(Suggest::getTitle, suggestParm.getTitle());
        }
        query.lambda().orderByDesc(Suggest::getDateTime);
        IPage<Suggest> list = suggestService.page(page,query);
        return ResultUtils.success("查询成功",list);
    }
}
