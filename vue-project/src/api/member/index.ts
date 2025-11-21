import http from "@/http"
import { MemberParam, MemberType,ApplyCard,Recharge,MemberRecharge } from "./MemberModel"
//新增
export const addApi = (param:MemberType)=>{
    return http.post("/api/member",param)
}
//查询列表
export const getListApi = (param: MemberParam)=>{
    // 不发送 total 字段给后端，只发送必要的查询参数
    const { total, ...queryParam } = param
    return http.get("/api/member/list", queryParam)
}

//查询会员卡列表
export const getCardListApi = ()=>{
    return http.get("/api/member/getCardList")
}
//编辑
export const editApi = (param: MemberParam)=>{
    return http.put("/api/member",param)
}
//删除
export const deleteApi = (memberId:string)=>{
    return http.delete(`/api/member/${memberId}`)
}
//根据会员id查询角色信息
export const getRoleByMemberIdApi = (memberId:string)=>{
    return http.get("/api/member/getRoleByMemberId",{memberId:memberId})
}
//办卡
export const applySaveApi = (param:ApplyCard)=>{
    return http.post("/api/member/joinApply",param)
}
//充值
export const rechargeApi = (param:Recharge)=>{
    return http.post("/api/member/recharge",param)
}
//我都重置查询
export const getMyRechargeApi = (param:MemberRecharge)=>{
    // 不发送 total 字段给后端，只发送必要的查询参数
    const { total, ...queryParam } = param
    return http.get("/api/member/getMyRecharge", queryParam)
}