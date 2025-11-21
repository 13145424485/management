package com.doc.web.course.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.course.entity.Course;
import com.doc.web.course.mapper.CourseMapper;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService{
}
