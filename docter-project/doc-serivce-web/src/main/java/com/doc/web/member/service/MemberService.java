package com.doc.web.member.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.doc.web.member.entity.JoinParam;
import com.doc.web.member.entity.Member;
import com.doc.web.member.entity.RechargeParam;

import java.text.ParseException;

public interface MemberService extends IService<Member> {
    void addMember(Member member);
    void editMember(Member member);
    void deleteMember(Long memberId);

    void recharge(RechargeParam parm);
    void joinApply(JoinParam joinParam) throws ParseException;
    //根据用户名查询用户
    Member loadUser(String username);
}
