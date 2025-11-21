import http from '@/http'
import {AddUserModel} from "@/api/user/UserModel";
import {ListParam} from "@/api/role/RoleModel";
//查询角色列表
export const getSelectApi = () => {
    return http.get('/api/user/role/getSelect')
}
//新增;
export const addApi = (param:AddUserModel) => {
    return http.post('/api/user', param)
}
//查询用户列表
export const getListApi = (param: ListParam) => {
    // 不发送 total 字段给后端，只发送必要的查询参数
    const { total, ...queryParam } = param
    return http.get('/api/user/list', queryParam)
}
//编辑
export const editApi = (param:AddUserModel) => {
    return http.put('/api/user', param)
}
//删除
export const deleteApi = (userId:string) => {
    return http.delete(`/api/user/${userId}`)
}

//根据用户id查询角色
export const getRoleApi = (userId: string) => {
    return http.get("/api/user/role",{userId:userId})
}