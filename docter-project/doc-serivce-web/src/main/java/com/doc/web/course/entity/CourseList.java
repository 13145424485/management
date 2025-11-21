package com.doc.web.course.entity;

import lombok.Data;

@Data
public class CourseList {

    private  Long currentPage;
    private  Long pageSize;
    private  String courseName;
    private  String teacherName;
    private  Long total;
}
