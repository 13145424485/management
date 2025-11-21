//订单列表查询参数类型
export type OrderListParam = {
    name:string,
    currentPage:number,
    pageSize:number,
    total:number,
    controlName:string  // 操作人
}