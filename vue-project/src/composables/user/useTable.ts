import { ListParam } from "@/api/user/UserModel";
import { nextTick, onMounted, reactive, ref } from "vue";
import { getListApi } from "@/api/user";

export default function useTable(){

    //表格的高度
    const tableHeight  = ref(0)
    //表格中的数据
    const tableList = reactive({
        list:[]
    })

    //查询参数
    const listParam = reactive<ListParam>({
        phone:'',
        pageSize:10,
        currentPage:1,
        nickName:'',
        total:0,
    })

    //定义查询列表的方法
    const getList = async()=>{
        let res = await getListApi(listParam)
        if(res && res.code == 200){
            //设置表格中的数据
            tableList.list = res.data.records
            //设置分页总条数
            listParam.total = res.data.total
        }
    }

    //搜索
    const searchBtn = ()=>{
        getList()
    }

    //重置
    const resetBtn = ()=>{
        listParam.nickName = ''
        listParam.phone = ''
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
    const refresh = ()=>{
        getList()
    }

    onMounted(()=>{
        getList()
        nextTick(()=>{
            tableHeight.value = window.innerHeight - 230
        })
    })

    return {
        tableList,
        listParam,
        getList,
        searchBtn,
        resetBtn,
        sizeChange,
        currentChange,
        tableHeight,
        refresh
    }
}
