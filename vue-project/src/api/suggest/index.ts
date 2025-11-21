import http from '@/http'
import { SuggestParam, SuggestType } from './SuggestParam'

//新增
export const addApi = (param:SuggestType)=>{
    return http.post("/api/suggest",param)
}

//查询
export const listApi = (param:SuggestParam)=>{
    return http.get("/api/suggest/list",param)
}

//编辑
export const editApi = (param:SuggestType)=>{
    return http.put("/api/suggest",param)
}
//删除
export const deleteApi = (id:string)=>{
    return http.delete(`/api/suggest/${id}`)
}