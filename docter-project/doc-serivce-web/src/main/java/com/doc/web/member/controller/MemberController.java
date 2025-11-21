package com.doc.web.member.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.member.entity.*;
import com.doc.web.member.service.MemberService;
import com.doc.web.member_card.entity.MemberCard;
import com.doc.web.member_card.service.MemberCardService;
import com.doc.web.member_recharge.Service.MemberRechargeService;
import com.doc.web.member_recharge.entity.MemberRecharge;
import com.doc.web.member_recharge.mapper.MemberRechargeMapper;
import com.doc.web.member_role.entity.MemberRole;
import com.doc.web.member_role.service.MemberRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRoleService memberRoleService;
    @Autowired
    MemberRechargeMapper memberRechargeMapper;
    @Autowired
    MemberRechargeService memberRechargeService;

    @Autowired
    PasswordEncoder passwordEncoder;
    //新增
    @PostMapping
    public ResultVo add(@RequestBody Member member){
        //判断卡号是否被占用
        QueryWrapper<Member> query = new QueryWrapper<>();
        query.lambda().eq(Member::getUsername, member.getUsername());
        Member one = memberService.getOne(query);
        if(one!=null){
            return ResultUtils.error("会员卡号已被占用");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberService.addMember(member);
        return ResultUtils.success("新增成功");
    }

    //编辑
    @PutMapping
    public ResultVo edit(@RequestBody Member member) {
        //判断卡号是否被占用
        QueryWrapper<Member> query = new QueryWrapper<>();
        query.lambda().eq(Member::getUsername, member.getUsername());
        Member one = memberService.getOne(query);
        if(one != null && !one.getMemberId().equals(member.getMemberId())){
            return ResultUtils.error("会员卡号已被占用");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberService.editMember(member);
        return ResultUtils.success("编辑成功");

    }

    //删除
    @DeleteMapping("/{memberId}")
    public ResultVo delete(@PathVariable Long memberId) {
        QueryWrapper<Member> query = new QueryWrapper<>();
        query.lambda().eq(Member::getMemberId, memberId);
        Member one = memberService.getOne(query);
        if(one == null){
            return ResultUtils.error("会员不存在");
        }
        memberService.deleteMember(memberId);
        return ResultUtils.success("删除成功");
    }

    //查询
    @GetMapping("/list")
    public ResultVo get(PageParam pageParam) {
        if(pageParam.getUserType().equals("1")){ //会员只能查询自己的信息
            //构造分页对象
            IPage<Member> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());

            //构造查询条件
            QueryWrapper<Member> query = new QueryWrapper<>();
            if(StringUtils.isNotEmpty(pageParam.getName())){
                query.lambda().like(Member::getName,pageParam.getName());
            }
            if(StringUtils.isNotEmpty(pageParam.getPhone())){
                query.lambda().like(Member::getPhone,pageParam.getPhone());
            }
            if(StringUtils.isNotEmpty(pageParam.getUsername())){
                query.lambda().like(Member::getUsername,pageParam.getUsername());
            }
            query.lambda().orderByDesc(Member::getJoinTime);
            IPage<Member> list = memberService.page(page, query);
            return ResultUtils.success("查询成功",list);
        }else{
            //构造分页对象
            IPage<Member> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
            //构造查询条件
            QueryWrapper<Member> query = new QueryWrapper<>();
            if(StringUtils.isNotEmpty(pageParam.getName())){
                query.lambda().like(Member::getName,pageParam.getName());
            }
            if(StringUtils.isNotEmpty(pageParam.getPhone())) {
                query.lambda().like(Member::getPhone, pageParam.getPhone());
            }
            if(StringUtils.isNotEmpty(pageParam.getUsername())){
                query.lambda().like(Member::getUsername,pageParam.getUsername());
            }
            query.lambda().orderByDesc(Member::getJoinTime);
            IPage<Member> list = memberService.page(page, query);
            return ResultUtils.success("查询成功",list);
        }
    }

    //根据会员ID查询角色ID（兼容老路径）
    @GetMapping({"/getRole","/getRoleByMemberId"})
    public ResultVo getRoleByMemberId(@RequestParam("memberId") Long memberId) {
        QueryWrapper<MemberRole> query = new QueryWrapper<>();
        query.lambda().eq(MemberRole::getMemberId,memberId);
        MemberRole one = memberRoleService.getOne(query);
        if(one == null){
            return ResultUtils.error("未找到该会员的角色信息");
        }
        return ResultUtils.success("查询成功",one.getRoleId());
    }


    @Autowired
    private MemberCardService memberCardService;

    //查询会员卡列表
    @GetMapping("/getCardList")
    public ResultVo getCardList() {
        QueryWrapper<MemberCard> query = new QueryWrapper<>();
        query.lambda().eq(MemberCard::getStatus,"1");
        List<MemberCard> list = memberCardService.list(query);
        return ResultUtils.success("查询成功",list);
    }

    //办卡提交
    @PostMapping("/joinApply")
    public ResultVo joinApply(@RequestBody JoinParam joinParm) throws ParseException {
        memberService.joinApply(joinParm);
        return ResultUtils.success("办卡成功");
    }

    //充值
    @PostMapping("/recharge")
    public ResultVo recharge(@RequestBody RechargeParam parm) {
        memberService.recharge(parm);
        return ResultUtils.success("充值成功");
    }


    //我的充值查询
    @GetMapping("/getMyRecharge")
    public ResultVo getMyRecharge(RechargeParamList parm){
        //参数校验
        if(parm == null || parm.getUserType() == null || parm.getUserType().trim().isEmpty()){
            return ResultUtils.error("用户类型参数不能为空");
        }
        System.out.println("接收到的参数 - memberId: " + parm.getMemberId() + ", userType: " + parm.getUserType());
        
        try {
            //判断是会员还是员工
            if(parm.getUserType().equals("1")){ //会员
                IPage<MemberRecharge> list = memberRechargeService.getRechargeByMember(parm);
                return ResultUtils.success("查询成功",list);
            }else if(parm.getUserType().equals("2")) { //员工
                IPage<MemberRecharge> list = memberRechargeService.getRechargeList(parm);
                return ResultUtils.success("查询成功", list);
            }else{
                return ResultUtils.error("用户类型错误,实际值为: " + parm.getUserType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error("查询失败: " + e.getMessage());
        }
    }
}
