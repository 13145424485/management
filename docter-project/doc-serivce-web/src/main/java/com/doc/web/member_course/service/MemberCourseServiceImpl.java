package com.doc.web.member_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doc.web.course.Service.CourseService;
import com.doc.web.course.entity.Course;
import com.doc.web.member.entity.RechargeParam;
import com.doc.web.member.mapper.MemberMapper;
import com.doc.web.member_course.entity.MemberCourse;
import com.doc.web.member_course.mapper.MemberCourseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MemberCourseServiceImpl extends ServiceImpl<MemberCourseMapper, MemberCourse> implements MemberCourseService{
    @Autowired
    private CourseService courseService;
    @Resource
    private MemberMapper memberMapper;

    @Override
    @Transactional
    public void joinCourse(MemberCourse memberCourse) {
        //根据课程id查询课程信息
        Course course = courseService.getById(memberCourse.getCourseId());
        BeanUtils.copyProperties(course,memberCourse);
        //插入报名表
        //将课程报名信息插入到数据库的 member_course 表中
        //int insert ：接收插入操作的返回结果，大于0表示插入成功
        int insert = this.baseMapper.insert(memberCourse);

        if(insert>0){
            RechargeParam parm = new RechargeParam();
            parm.setMemberId(memberCourse.getMemberId());
            parm.setMoney(course.getCoursePrice());
            //调用会员Mapper的扣费方法，从会员余额中扣除课程费用
            memberMapper.subMoney(parm);
        }
    }

}
