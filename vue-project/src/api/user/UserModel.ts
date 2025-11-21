//定义角色类型
export type RoleType = {
    value:string,
    label:string
}

export type SelectRole = {
    list:RoleType[]
}

//列表查询参数类型
export type ListParam = {
    phone: string,
    nickName: string,
    currentPage: number,
    pageSize: number,
    total: number
}

//用户数据类型
export type AddUserModel = {
    userId: string,
    type: string,
    roleId: string,
    username: string,
    password: string,
    phone: string,
    email: string,
    sex: string,
    userType: string,
    status: string,
    salary: string,
    nickName: string
}