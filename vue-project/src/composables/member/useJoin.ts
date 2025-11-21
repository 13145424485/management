import {ref,reactive,onMounted} from 'vue';
import { MemberType} from '@/api/member/MemberModel'
import { getCardListApi } from '@/api/member'
import { CardType} from '@/api/member_card/MemberModel'

export default function useJoin(){
    //会员卡列表数据
    const cardList = ref<CardType[]>([])
    //弹框属性
    const joinRef = ref<{show:(row:MemberType)=>void}>()
    //点击办卡按钮进行弹框
    const joinBtn = (row:MemberType)=>{
        joinRef.value?.show(row)
    }
    //获取会员卡列表
    const getCardList = async()=>{
        let res = await getCardListApi()
        if(res && res.code == 200){
            cardList.value = res.data
        }
    }

    onMounted(()=>{
        getCardList()
    })

    return {
        joinRef,
        joinBtn,
        getCardList,
        cardList
    }
}