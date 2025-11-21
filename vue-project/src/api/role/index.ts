import http from '@/http'
import { AddRoleModel, AssignParam, ListParam, SaveAssignParam } from './RoleModel'

//新增
export const addApi = (param:AddRoleModel)=>{
    return http.post("/api/role",param)
}

//查询角色列表
export const getListApi = (param : ListParam)=>{
    return http.get("/api/role/list",param)
}

//删除
export const deleteApi = (roleId : string)=>{
    return http.delete(`/api/role/${roleId}`)
}

//编辑
export const editApi = (param : AddRoleModel)=>{
    return http.put("/api/role",param)
}

//查询权限树数据
export const getMenuTreeApi = (param:AssignParam)=>{
    return http.get("/api/role/getMenuTree",param)
}

//保存权限
export const saveRoleMenuApi = (param:SaveAssignParam)=>{
    return http.post("/api/role/saveRoleMenu",param)
}