package com.doc.web.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doc.web.member.entity.Member;
import com.doc.web.member.entity.RechargeParam;
import com.doc.web.member_recharge.entity.MemberRecharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
    int addMoney(@Param("param")RechargeParam parm);

    int  subMoney(@Param("param") RechargeParam parm);
}
