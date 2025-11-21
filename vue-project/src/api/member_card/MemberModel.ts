//查询参数类型
export type ListParam = {
    title:string,
    currentPage:number,
    pageSize:number,
    total:number
}
// 表单提交的会员卡数据类型
export type CardType = {
    type:string,
    title:string,
    cardType:string,
    cardId:string,
    price:string,
    cardDay:number,
    status:string
}