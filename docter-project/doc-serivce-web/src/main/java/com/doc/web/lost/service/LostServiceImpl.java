package com.doc.web.lost.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.lost.entity.Lost;
import com.doc.web.lost.mapper.LostMapper;
import org.springframework.stereotype.Service;

@Service
public class LostServiceImpl extends ServiceImpl<LostMapper, Lost> implements LostService{
}
