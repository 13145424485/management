//定义列表参数类型
export type MemberParam = {
    name:string,
    phone:string,
    username:string,
    currentPage:number,
    pageSize:number,
    total:number,
    userType:string,
    memberId:string
}
//会员数据类型
export type MemberType = {
    type:string,
    memberId:string,
    name:string,
    sex:string,
    phone:string,
    age:string,
    birthday:string,
    height:string,
    weight:string,
    waist:string,
    joinTime:string,
    endTime:string,
    username:string,
    password:string,
    status:string,
    roleId:string
}

//办卡数据类型
export type ApplyCard = {
    cardId:string,
    memberId:string
}
//充值数据类型
export type Recharge = {
    memberId:string,
    money:number,
    userId:string
}

//查询充值记录的数据模型
export type MemberRecharge = {
    currentPage:number,
    pageSize:number,
    total:number,
    memberId:string,
    userType:string
}