package com.doc.web.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.member.entity.JoinParam;
import com.doc.web.member.entity.Member;
import com.doc.web.member.entity.RechargeParam;
import com.doc.web.member.mapper.MemberMapper;
import com.doc.web.member.service.MemberService;
import com.doc.web.member_apply.entity.MemberApply;
import com.doc.web.member_apply.mapper.MemberApplyMapper;
import com.doc.web.member_card.entity.MemberCard;
import com.doc.web.member_card.mapper.MemberCardMapper;
import com.doc.web.member_recharge.Service.MemberRechargeService;
import com.doc.web.member_recharge.entity.MemberRecharge;
import com.doc.web.member_role.entity.MemberRole;
import com.doc.web.member_role.service.MemberRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper,  Member> implements MemberService {
    @Resource
    private MemberCardMapper memberCardMapper;

    @Resource
    private MemberApplyMapper memberApplyMapper;

    @Autowired
    MemberRoleService memberRoleService;

    @Autowired
    MemberRechargeService memberRechargeService;

    @Override
    @Transactional
    public void addMember(Member member) {
        //新增会员
        int insert = this.baseMapper.insert(member);
        //设置会员角色
        if(insert>0){
            MemberRole role = new MemberRole();
            role.setMemberId(member.getMemberId());
            role.setRoleId(member.getRoleId());
            memberRoleService.save(role);
        }
    }


    @Override
    @Transactional
    public void editMember(Member member) {
        int i = this.baseMapper.updateById(member);
        //设置角色 先删除后插入
        if (i > 0) {
            //删除角色
            QueryWrapper<MemberRole> query = new QueryWrapper();
            query.lambda().eq(MemberRole::getMemberId, member.getMemberId());
            memberRoleService.remove(query);

            //重新插入
            MemberRole role = new MemberRole();
            role.setMemberId(member.getMemberId());
            role.setRoleId(member.getRoleId());
            memberRoleService.save(role);
        }
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        //删除
        int i = this.baseMapper.deleteById(memberId);
        if (i > 0) {
            //删除角色
            QueryWrapper<MemberRole> query = new QueryWrapper<>();
            query.lambda().eq(MemberRole::getMemberId, memberId);
            memberRoleService.remove(query);
        }
    }

    @Override
    @Transactional
    public void joinApply(JoinParam joinParm) throws ParseException {
        Member select = this.baseMapper.selectById(joinParm.getMemberId());
        //更新会员
        MemberCard card = memberCardMapper.selectById(joinParm.getCardId()); //获取会员卡相关信息
        //根据前端传来的卡种 ID 把卡规则读出来
        Member member = new Member();
        member.setMemberId(joinParm.getMemberId());
        member.setCardType(card.getTitle());
        member.setCardDay(card.getCardDay());
        member.setPrice(card.getPrice());
        //取出会员旧截止日字符串
        String endTime = select.getEndTime();
        //拿到日历工具类实例，用来加减天数
        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isNotEmpty(endTime)){
            //如果会员旧截止日不为空，则更新截止日
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(select.getEndTime());
            calendar.setTime(date);
            //在旧日期上加上卡天数
            calendar.add(Calendar.DATE, card.getCardDay());
        }else{
            Date date = new Date();
            calendar.setTime(date);
            //如果会员旧截止日为空，则更新截止日为当前日期加上卡天数
            calendar.add(Calendar.DATE, card.getCardDay());
        }
        member.setEndTime(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        this.baseMapper.updateById(member);
        //插入明细
        MemberApply memberApply = new MemberApply();
        memberApply.setCardDay(card.getCardDay());
        memberApply.setCardType(card.getTitle());
        memberApply.setMemberId(joinParm.getMemberId());
        memberApply.setPrice(card.getPrice());
        memberApply.setCreateTime(new Date());
        memberApplyMapper.insert(memberApply);
    }

    @Override
    @Transactional
    public void recharge(RechargeParam parm) {
        //生成充值明细
        MemberRecharge recharge = new MemberRecharge();
        recharge.setMemberId(parm.getMemberId());
        recharge.setMoney(parm.getMoney());
        boolean save = memberRechargeService.save(recharge);
        if(save){
            //更新余额
            this.baseMapper.addMoney(parm);
        }
    }

    @Override
    public Member loadUser(String username) {
        QueryWrapper<Member> query = new QueryWrapper<>();
        query.lambda().eq(Member::getUsername,username);
        return this.baseMapper.selectOne(query);
    }
}
