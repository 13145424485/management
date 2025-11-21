import { AddCar,GoodsType } from '@/api/goods/GoodsModel'
import {ref} from 'vue'
export default function useFaDrawer(selectGoods:AddCar){
    const faDrawer = ref(false)

    //点击取消按钮,关闭抽屉效果
    const cancelClick = ()=>{
        faDrawer.value = false
    }

    //点击确定，关闭抽屉效果
    const confirmClick = ()=>{
        faDrawer.value = false
    }

    //点击下单，打开抽屉
    const order = ()=>{
        selectGoods.list = []
        faDrawer.value = true
    }

    //购物车删除
    const deleteCarBtn = (row:GoodsType)=>{
        selectGoods.list = selectGoods.list.filter(item => item.goodsId != row.goodsId)
    }

    return {
        faDrawer,
        cancelClick,
        confirmClick,
        order,
        deleteCarBtn
    }
}