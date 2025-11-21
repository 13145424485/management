package com.doc.web.suggest.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.suggest.entity.Suggest;
import com.doc.web.suggest.mapper.SuggestMapper;
import org.springframework.stereotype.Service;

@Service

public class SuggestServiceImpl extends ServiceImpl<SuggestMapper, Suggest> implements SuggestService{
}
