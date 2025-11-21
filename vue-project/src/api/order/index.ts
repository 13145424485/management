import http from '@/http'
import {OrderListParam} from '@/api/order/OrderModel'

//下单
export const downApi = (param:any)=>{
    return http.post("/api/goods_order/down",param)
}

//查询订单列表
export const listApi = (param:OrderListParam)=>{
    return http.get("/api/goods_order/list",param)
}