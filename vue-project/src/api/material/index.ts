import http from '@/http'
import { ListParam, MaterialType } from './MaterialModel'
//新增
export const addApi = (param:MaterialType)=>{
    return http.post("/api/material",param)
}
//列表
export const getListApi = (param:ListParam)=>{
    // 不发送 total 字段给后端，只发送必要的查询参数
    const { total, ...queryParam } = param
    return http.get("/api/material/list", queryParam)
}
//编辑
export const editApi = (param:MaterialType)=>{
    return http.put("/api/material",param)
}
//删除
export const deleteApi = (id:string)=>{
    return http.delete(`/api/material/${id}`)
}