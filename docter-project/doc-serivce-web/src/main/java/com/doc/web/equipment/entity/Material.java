package com.doc.web.equipment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("material")
public class Material {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String details;
    private Integer numTotal;
}
