import http from '@/http'
import { ResetPasswordParam } from './HomeModel'
//总数统计
export const getTotalApi = ()=>{
    return http.get("/api/home/getTotal")
}
//反馈列表
export const getSuggestListApi = ()=>{
    return http.get("/api/home/getSuggestList")
}
//热销商品
export const getHotGoodsApi = ()=>{
    return http.get("/api/home/getHotGoods")
}
//热销卡
export const getHotCardsApi = ()=>{
    return http.get("/api/home/getHotCards")
}
//热销课程
export const getHotCourseApi = ()=>{
    return http.get("/api/home/getHotCourse")
}
//重置密码
export const resetPasswordApi = (param:ResetPasswordParam)=>{
    return http.post("/api/home/resetPassword",param)
}
//修改密码
export const updatePasswordApi = (param:ResetPasswordParam)=>{
    return http.post("/api/home/updatePassword",param)
}
//退出登录
export const loginOutApi = ()=>{
    return http.post("/api/home/loginOut")
}