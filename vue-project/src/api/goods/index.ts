import http from '@/http'
import {GoodsType,GoodsParam} from './GoodsModel'
//新增
export const addApi = (param:GoodsType)=>{
    return http.post("/api/goods",param)
}
//查询商品列表
export const listApi = (param:GoodsParam)=>{
    // 不发送 total 字段给后端，只发送必要的查询参数
    const { total, ...queryParam } = param
    return http.get("/api/goods/list", queryParam)
}
//编辑
export const editApi = (param:GoodsType)=>{
    return http.put("/api/goods",param)
}
//删除
export const deleteApi = (goodsId:string)=>{
    return http.delete(`/api/goods/${goodsId}`)
}
