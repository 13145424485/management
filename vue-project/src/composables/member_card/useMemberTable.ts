import { getListApi } from '@/api/member_card'
import {ListParam} from '@/api/member_card/MemberModel'
import {reactive,ref,nextTick,onMounted} from 'vue'

export default function useMemberTable(){

    //表格的高度
    const tableHeight = ref(0)
    //表格数据
    const tableList = reactive({
        list:[]
    })

    //定义查询参数
    const listParam = reactive<ListParam>(
        {
            title:'',
            pageSize:10,
            currentPage:1,
            total:0
        }
    )

    //查询列表
    const getList = async ()=>{
        let res = await getListApi(listParam)
        if(res && res.code == 200){
            tableList.list = res.data.records
            listParam.total = res.data.total
        }
    }

    //搜索
    const searchBtn = ()=>{
        getList()
    }

    //重置
    const resetBtn = ()=>{
        getList()
    }

    //页面容量改变时触发
    const sizeChange = (size:number)=>{
        listParam.pageSize = size
        getList()
    }

    //页数改变时触发
    const currentChange = (page:number)=>{
        listParam.currentPage = page
        getList()
    }

    //刷新
    const refresh = ()=>{
        getList
    }

    onMounted(()=>{
        getList()
        nextTick(()=>{
            tableHeight.value = window.innerHeight - 230
        })
    })

    return {
        listParam,
        getList,
        searchBtn,
        resetBtn,
        sizeChange,
        currentChange,
        refresh,
        tableList,
        tableHeight
    }
}