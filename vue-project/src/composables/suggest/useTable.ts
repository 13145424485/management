import { SuggestParam } from "@/api/suggest/SuggestParam";
import { reactive,ref,onMounted,nextTick } from "vue";
import { listApi } from "@/api/suggest";

export default function useTable(){

    //表格高度
    const tableHeight = ref(0)
    //表格数据
    const tableData = reactive({
        list:[]
    })
    //列表查询参数
    const listParam = reactive<SuggestParam>({
        currentPage:1,
        pageSize:10,
        title:'',
        total:0
    })

    //查询列表的方法
    const getList = async ()=>{
        let res = await listApi(listParam)
        if(res && res.code == 200){
            tableData.list = res.data.records
            listParam.total = res.data.total
        }
    }

    //搜索
    const searchBtn = ()=>{
        getList()
    }
    //重置
    const resetBtn = ()=>{
        listParam.title = ''
        getList()
    }

    //页面容量改变时触发
    const sizeChange = (size:number)=>{
        listParam.pageSize = size
        getList()
    }

    //页码改变时触发
    const currentChange = (page:number)=>{
        listParam.currentPage = page
        getList()
    }

    //刷新
    const reFresh = ()=>{
        getList()
    }

    onMounted(()=>{
        nextTick(() => {
            tableHeight.value = window.innerHeight - 230
        })
        getList()
    })

    return {
        listParam,
        searchBtn,
        resetBtn,
        getList,
        tableData,
        tableHeight,
        reFresh,
        currentChange,
        sizeChange
    }
}