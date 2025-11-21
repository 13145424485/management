import { reactive,ref,nextTick,onMounted } from 'vue'
import {LostParam } from '@/api/lost/LostModel'
import { getListApi } from '@/api/lost'

export default function useTable(){
    //表格高度
    const tableHeight = ref(0)
    //表格数据
    const tableData = reactive({
        list:[]
    })
    //列表参数
    const listParam = reactive<LostParam>({
        currentPage:1,
        pageSize:10,
        lostName:'',
        total:0
    })

    //查询列表的的方法
    const getList = async ()=>{
        let res = await getListApi(listParam)
        if(res && res.code == 200){
            //设置表格的高度
            tableData.list = res.data.records
            //设置表格的数据
            listParam.total = res.data.total
        }
    }

    //重置
    const resetBtn = ()=>{
        listParam.lostName = '',
            listParam.currentPage = 1,
            getList()
    }
    //搜索
    const searchBtn = ()=>{
        getList()
    }

    //页面容量改变时需要触发
    const sizeChange = (size:number)=>{
        listParam.pageSize =  size
        getList()
    }
    //页码改变时需要触发
    const currentChange = (page:number)=>{
        listParam.currentPage = page
        getList()
    }
    //刷新表格需要触发
    const reFresh = ()=>{
        getList()
    }
    //页面打开的时候需要触发
    onMounted(()=>{
        tableHeight.value = window.innerHeight - 230
        getList()
    })

    return {
        listParam,
        resetBtn,
        searchBtn,
        getList,
        tableData,
        tableHeight,
        sizeChange,
        currentChange,
        reFresh
    }
}
