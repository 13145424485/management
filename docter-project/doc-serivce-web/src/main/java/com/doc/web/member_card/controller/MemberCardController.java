package com.doc.web.member_card.controller;


import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doc.utils.ResultVo;
import com.doc.web.member_card.entity.ListCard;
import com.doc.web.member_card.entity.MemberCard;
import com.doc.web.member_card.service.MemberCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberCard")
public class MemberCardController {

    @Autowired
    private MemberCardService memberCardService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody MemberCard memberCard){
        if(memberCardService.save(memberCard)){
            return new ResultVo("新增成功", 200, null);
        }
        return new ResultVo("新增失败", 500, null);
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody MemberCard memberCard) {
        if (memberCardService.updateById(memberCard)) {
            return new ResultVo("编辑成功", 200, null);
        }
        return new ResultVo("编辑失败", 500, null);
    }

    //删除
    @DeleteMapping("/{cardId}")
    public ResultVo delete(@PathVariable("cardId") Long cardId) {
        if (memberCardService.removeById(cardId)){
            return new ResultVo("删除成功", 200, null);
        }
        return new ResultVo("删除失败", 500, null);
    }


    //列表
    //按标题模糊搜会员卡，分页返回，没传标题就查全部
    @GetMapping("/list")
    public ResultVo list(ListCard listCard) {
        //分页查询
        IPage<MemberCard> page = new Page<>(listCard.getCurrentPage(), listCard.getPageSize());

        //构造查询
        QueryWrapper<MemberCard> query = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(listCard.getTitle())){
            query.lambda().like(MemberCard::getTitle, listCard.getTitle());
        }
        IPage<MemberCard> memberCardIPage = memberCardService.page(page, query);
        return new ResultVo("查询成功", 200, memberCardIPage);
     }
}
