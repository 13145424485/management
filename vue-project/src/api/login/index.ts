import http from '@/http'
import {InfoParam, LoginParam} from './LoginModel'

//获取验证码
export const getImageApi = ()=>{
    return http.post("/api/login/image")
}

//登录
export const loginApi = (param:LoginParam)=>{
    return http.post("/api/login/login",param)
}

//动态获取菜单数据
export const getMenuListApi = (param:InfoParam)=>{
    return http.get("/api/login/getMenuList",param)
}

//查询用户信息
export const getInfoApi = (param:InfoParam)=>{
    return http.get("/api/login/getInfo",param)
}