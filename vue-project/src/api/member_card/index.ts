import http from '@/http';
import {CardType,ListParam} from './MemberModel';

//新增会员
export const addApi = (param:CardType)=>{
    return http.post("/api/memberCard",param)
}

//查询列表
export const getListApi = (param:ListParam)=>{
    // 不发送 total 字段给后端，只发送必要的查询参数
    const { total, ...queryParam } = param
    return http.get("/api/memberCard/list", queryParam)
}

//编辑
export const editApi = (param:ListParam)=>{
    return http.put("/api/memberCard",param)
}

//删除
export const deleteApi = (cardId:string)=>{
    return http.delete(`/api/memberCard/${cardId}`)
}