package com.doc.web.member_recharge.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.doc.web.member.entity.RechargeParam;
import com.doc.web.member.entity.RechargeParamList;
import com.doc.web.member_recharge.entity.MemberRecharge;

public interface MemberRechargeService extends IService<MemberRecharge> {
    IPage<MemberRecharge> getRechargeList(RechargeParamList paramList);

    IPage<MemberRecharge> getRechargeByMember(RechargeParamList paramList);
}
