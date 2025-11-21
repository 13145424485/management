//查询参数的类型
export type SuggestParam = {
    currentPage:number,
    pageSize:number,
    title:string,
    total:number
}
//Suggest数据类型
export type SuggestType = {
    type:string,
    id:string,
    title:string,
    content:string
}