import http from '@/http'
import { LostType,LostParam } from './LostModel'

//新增
export const addApi = (param:LostType)=>{
    return http.post('/api/lost',param)
}
//列表
export const getListApi = (param:LostParam)=>{
    // 不发送 total 字段给后端，只发送必要的查询参数
    const { total, ...queryParam } = param
    return http.get("/api/lost/list", queryParam)
}
//编辑
export const editApi = (param:LostType)=>{
    return http.put("/api/lost",param)
}
//删除
export const deleteApi = (lostId:string)=>{
    return http.delete(`/api/lost/${lostId}`)
}