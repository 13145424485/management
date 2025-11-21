package com.doc.web.equipment.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.equipment.Service.MaterialService;
import com.doc.web.equipment.entity.Material;
import org.springframework.stereotype.Service;


@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

}
