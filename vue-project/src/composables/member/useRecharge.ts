import {ref} from 'vue'
import { MemberType } from '@/api/member/MemberModel'

export default function useRecharge(){
    //ref属性
    const rechargeRef = ref<{show:(row:MemberType)=>void}>()
    //点击充值按钮
    const rechargeBtn = (row:MemberType)=>{
        rechargeRef.value?.show(row)
    }

    return {
        rechargeRef,
        rechargeBtn
    }
}