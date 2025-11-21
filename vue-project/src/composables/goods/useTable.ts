import { listApi } from '@/api/goods';
import {GoodsParam} from '@/api/goods/GoodsModel'
import {reactive,ref,onMounted,nextTick} from 'vue'

export default function useTable(){
    //表格的高度
    const tableHeight = ref(0)
    //表格数据
    const tableData = reactive({
        list:[]
    })
    //查询列表参数
    const listParam = reactive<GoodsParam>({
        pageSize:10,
        currentPage:1,
        name:'',
        total:0
    })

    const getList = async ()=>{
        let res = await listApi(listParam)
        if(res && res.code == 200){
            //设置表格的高度
            tableData.list = res.data.records
            //设置表格的数据
            listParam.total = res.data.total
        }
    }

    //搜索
    const searchBtn = ()=>{
        getList()
    }

    //重置
    const resetBtn = ()=>{
        listParam.name = ''
        listParam.currentPage = 1
        getList()
    }

    //页面容量改变时触发
    const sizeChange = (size:number)=>{
        listParam.pageSize = size;
        getList()
    }
    //页数改变时触发
    const currentChange = (page:number)=>{
        listParam.currentPage = page
        getList()
    }
    //刷新表格
    const reFresh = ()=>{
        getList()
    }

    onMounted(()=>{
        nextTick(()=>{
            tableHeight.value = window.innerHeight - 230
        })
        getList()
    })

    return {
        listParam,
        searchBtn,
        resetBtn,
        tableData,
        tableHeight,
        getList,
        sizeChange,
        reFresh,
        currentChange
    }
}