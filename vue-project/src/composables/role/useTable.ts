import {reactive, ref,nextTick,onMounted} from "vue";
import {getListApi} from "@/api/role/index";
import {ListParam} from "@/api/role/RoleModel";

export  default function useTable(){

    //定义表格的高度
    const tableHeight = ref(0)
    //定义表格数据
    const tableList = reactive({
        list: []
    })
    //列表参数
    const listParam = reactive<ListParam>({
        roleName: "",
        currentPage: 1,
        pageSize: 10,
        total: 0
    })
    //分页
    //列表查询
    const getList = async() => {
        try {
            let res = await getListApi(listParam)
            if(res && res.code === 200) {
                console.log(res)
                //设置表格数据
                tableList.list = res.data.records
                //设置总条数
                listParam.total = res.data.total
            }
        } catch (error) {
            console.error('获取角色列表失败:', error)
            // 错误已经在 HTTP 拦截器中处理了，这里只需要记录日志
        }
    }
    //搜索
    const searchBtn = () => {

    }
    //重置
    const resetBtn = () => {
        listParam.roleName = ""
        getList()
    }
    //页面容量发生改变时触发
    const sizeChange = (val: number) => {
        listParam.pageSize = val
        getList()
    }
    //当前页码发生改变时触发
    const currentChange = (val: number) => {
        listParam.currentPage = val
        getList()
    }
    //刷新列表
    const refreshList = () => {
        getList()
    }
    //页面一打开时候触发的操作
    onMounted(() => {
        //获取列表数据
        getList()
        nextTick(()=> {
            //获取表格的高度
            tableHeight.value = document.documentElement.clientHeight - 200
        })
    })

    return {
        listParam,
        getList,
        searchBtn,
        resetBtn,
        sizeChange,
        currentChange,
        tableHeight,
        tableList,
        refreshList
    }
}