import http from '@/http'
import { MenuType } from './MenuModel'

//查询上级菜单
export const getParentApi = ()=>{
    return http.get("/api/menu/parent")
}

//新增菜单
export const addApi = (param:MenuType)=>{
    return http.post("/api/menu",param)
}

//菜单列表查询
export const getListApi = ()=>{
    return http.get("/api/menu/list")
}

//编辑
export const editApi = (param:MenuType)=>{
    return http.put("/api/menu",param)
}

//删除
export const deleteApi = (menuId:string | number)=>{
    return http.delete(`/api/menu/${menuId}`)
}