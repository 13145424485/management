package com.doc.web.member_recharge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.doc.web.member_recharge.entity.MemberRecharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberRechargeMapper extends BaseMapper<MemberRecharge> {
    //分页查询充值记录
    IPage<MemberRecharge> getRechargeList(IPage<MemberRecharge> page);

    //根据用户id查询充值记录
    IPage<MemberRecharge> getRechargeByMemberId(IPage<MemberRecharge> page, @Param("memberId") Long memberId);
}
