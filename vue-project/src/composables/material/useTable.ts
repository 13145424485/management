import { getListApi } from "@/api/material";
import { ListParam } from "@/api/material/MaterialModel";
import { reactive,ref,nextTick,onMounted } from "vue";

export default function useTable(){
    //定义表格的高度
    const tableHeight = ref(0)
    //定义表格的数据
    const tableData = reactive({
        list:[]
    })
    //定义列表查询参数
    const listParam = reactive<ListParam>({
        name:'',
        currentPage:1,
        pageSize:10,
        total:0
    })

    //获取表格数据
    const getList = async ()=>{
        let res = await getListApi(listParam)
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
        listParam.name = ''
        listParam.currentPage = 1
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

    //刷新列表
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
        sizeChange,
        currentChange,
        tableHeight,
        reFresh
    }
}