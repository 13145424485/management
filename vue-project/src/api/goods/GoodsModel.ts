//商品数据类型
export type GoodsType = {
    type:string,
    goodsId:string,
    name:string,
    image:string,
    details:string,
    unit:string,
    specs:string,
    price:number,
    store:number,
    num?:number
}
//商品查询的数据类型
export type GoodsParam = {
    currentPage:number,
    pageSize:number,
    name:string,
    total:number
}
//定义购物车模型
export type AddCar = {
    list:GoodsType[]
}