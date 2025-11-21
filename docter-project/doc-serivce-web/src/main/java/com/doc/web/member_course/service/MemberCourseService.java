package com.doc.web.member_course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doc.web.member_course.entity.MemberCourse;
import org.springframework.stereotype.Service;


public interface MemberCourseService extends IService<MemberCourse> {
    void joinCourse(MemberCourse memberCourse);
}
