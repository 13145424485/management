//失物数据类型
export type LostType = {
    type:string,
    lostId:string,
    lostName:string,
    foundTime:string,
    foundAddres:string,
    foundPerson:string,
    foundPhone:string,
    status:string,
    lostPerson:string
}
//分页查询参数类型
export type LostParam = {
    currentPage:number,
    pageSize:number,
    lostName:string,
    total:number
}