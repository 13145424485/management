// 角色数据类型
export type AddRoleModel={
    type:string,
    roleId:string,
    roleName:string,
    remark:string
}

//分页查询参数类型
export type ListParam = {
    roleName:string,
    currentPage:number,
    pageSize:number,
    total:number
}

export type AssignParam = {
    roleId:string,
    userId:string
}

export type SaveAssignParam = {
    roleId:string,
    list:Array<string>
}
