package com.doc.web.member_apply.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.member_apply.entity.MemberApply;
import com.doc.web.member_apply.mapper.MemberApplyMapper;
import com.doc.web.member_apply.service.MemberApplyService;
import org.springframework.stereotype.Service;

@Service
public class MemberApplyServiceImpl extends ServiceImpl<MemberApplyMapper, MemberApply> implements MemberApplyService {
}
