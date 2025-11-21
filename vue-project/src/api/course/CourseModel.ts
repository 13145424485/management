//分页查询的数据类型
export type CourseListParam = {
    teacherName:string,
    courseName:string,
    currentPage:number,
    pageSize:number,
    total:number
}
//课程数据类型
export type CourseType = {
    type:string,
    courseId:string,
    courseName:string,
    image:string,
    teacherName:string,
    courseHour:number,
    courseDetails:string,
    coursePrice:number,
    teacherId:string
}
//选课参数类型
export type SelectCourse = {
    courseId:string,
    memberId:string
}
//选课查询参数类型
export type MyCourseListParam = {
    userType:string,
    userId:string,
    currentPage:number,
    pageSize:number,
    total:number
}