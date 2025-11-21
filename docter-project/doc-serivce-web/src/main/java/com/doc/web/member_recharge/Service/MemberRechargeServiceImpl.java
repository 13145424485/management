package com.doc.web.member_recharge.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.member.entity.RechargeParamList;
import com.doc.web.member_recharge.entity.MemberRecharge;
import com.doc.web.member_recharge.mapper.MemberRechargeMapper;
import org.springframework.stereotype.Service;

@Service
public class MemberRechargeServiceImpl extends ServiceImpl<MemberRechargeMapper, MemberRecharge> implements MemberRechargeService {
    //构造分页对象
    @Override
    public IPage<MemberRecharge> getRechargeList(RechargeParamList paramList) {
    //构造分页对象
        IPage<MemberRecharge> page= new Page<>(paramList.getCurrentPage(), paramList.getPageSize());
        return this.baseMapper.getRechargeList(page);
    }

    @Override
    public IPage<MemberRecharge> getRechargeByMember(RechargeParamList paramList) {
        //构造分页对象
        IPage<MemberRecharge> page= new Page<>(paramList.getCurrentPage(), paramList.getPageSize());
        return this.baseMapper.getRechargeByMemberId(page, paramList.getMemberId());
    }
}
