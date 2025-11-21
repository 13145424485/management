package com.doc.web.equipment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.equipment.Service.MaterialService;
import com.doc.web.equipment.entity.ListParam;
import com.doc.web.equipment.entity.Material;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Material material){
        if(materialService.save(material)){
            return new ResultVo("新增成功", 200, null);
        }
        return new ResultVo("新增失败", 500, null);
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody Material material){
        if(materialService.updateById(material)){
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败");
    }

    //删除
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id) {
        if (materialService.removeById(id)) {
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }
    //列表
    @GetMapping("/list")
    public ResultVo list(ListParam parm) {
        //制造分页对象
        IPage<Material> page = new Page<>(parm.getCurrentPage(), parm.getPageSize());
        //构造查询条件
        QueryWrapper<Material> query = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(parm.getName())){
            query.lambda().like(Material::getName, parm.getName());
         }
        IPage<Material> list = materialService.page(page, query);
        return ResultUtils.success("查询成功", list);

    }
}
