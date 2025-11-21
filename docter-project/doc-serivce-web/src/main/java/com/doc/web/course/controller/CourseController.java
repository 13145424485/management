package com.doc.web.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doc.utils.ResultUtils;
import com.doc.utils.ResultVo;
import com.doc.web.course.Service.CourseService;
import com.doc.web.course.entity.Course;
import com.doc.web.course.entity.CourseList;
import com.doc.web.course.entity.PageParam;
import com.doc.web.member.entity.Member;
import com.doc.web.member.service.MemberService;
import com.doc.web.member_course.entity.MemberCourse;
import com.doc.web.member_course.service.MemberCourseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController{
    @Autowired
    private CourseService courseService;

    //新增
    @PostMapping
    public ResultVo add(@RequestBody Course course){
        if(courseService.save(course)){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    //编辑
    @PutMapping
    public ResultVo update(@RequestBody Course course){
        if(courseService.updateById(course)){
            return ResultUtils.success("编辑成功");
        }
        return ResultUtils.error("编辑失败");
    }

    //删除
    @DeleteMapping("/{courseid}")
    public ResultVo delete(@PathVariable("courseid") Integer courseid){
        if(courseService.removeById(courseid)) {
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }

    //列表
    @GetMapping("/list")
    public ResultVo list(CourseList courseList) {
        //构造分页对象
        IPage<Course> page = new Page<>(courseList.getCurrentPage(), courseList.getPageSize());
        //构造查询条件
        QueryWrapper<Course> query = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(courseList.getCourseName())) {
            query.lambda().like(Course::getCourseName, courseList.getCourseName());
        }
        if (StringUtils.isNotEmpty(courseList.getTeacherName())) {
            query.lambda().like(Course::getTeacherName, courseList.getTeacherName());

        }
        IPage<Course> list = courseService.page(page, query);
        return ResultUtils.success("查询成功", list);
    }
    @Autowired
    MemberCourseService memberCourseService;

    @Autowired
    MemberService memberService;
    //报名课程
    @PostMapping("/joinCourse")
    public ResultVo joinCourse(@RequestBody MemberCourse memberCourse) {
        //查询是否已报名
        QueryWrapper<MemberCourse> query = new QueryWrapper<>();
        query.lambda().eq(MemberCourse::getMemberId, memberCourse.getMemberId())
                .eq(MemberCourse::getCourseId, memberCourse.getCourseId());
        MemberCourse one = memberCourseService.getOne(query);
        if(one!=null){
            return ResultUtils.error("已报名");
        }
        //判断余额是否充足
        Course course = courseService.getById(memberCourse.getCourseId());
        Member member = memberService.getById(memberCourse.getMemberId());
        int flag =  member.getMoney().compareTo(course.getCoursePrice());
        if(flag==-1){
            return ResultUtils.error("余额不足");
        }
        memberCourseService.joinCourse(memberCourse);
        return ResultUtils.success("报名成功");
    }
    //我的课程列表
    @GetMapping("/getMyCourseList")
    public ResultVo getMyCourseList(PageParam pageParam){
        if(pageParam.getUserType().equals("1")){//会员
            IPage<MemberCourse> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
            QueryWrapper<MemberCourse> query = new QueryWrapper<>();
            query.lambda().eq(MemberCourse::getMemberId, pageParam.getUserId());
            IPage<MemberCourse> list = memberCourseService.page(page, query);
            return ResultUtils.success("查询成功",list);
        }else{//员工
            IPage<MemberCourse> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
            QueryWrapper<MemberCourse> query = new QueryWrapper<>();
            query.lambda().eq(MemberCourse::getTeacherId, pageParam.getUserId());
            IPage<MemberCourse> list = memberCourseService.page(page, query);
            return ResultUtils.success("查询成功",list);
        }
    }
}
