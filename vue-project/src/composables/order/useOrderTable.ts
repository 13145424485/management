import { listApi } from '@/api/order'
import { OrderListParam } from '@/api/order/OrderModel'
import { reactive,ref,onMounted,nextTick } from 'vue'

export default function useOrderTable(){

    //表格的高度
    const orderTableHeight = ref(0)
    //表格数据
    const tableData = reactive({
        list:[]
    })
    //列表参数
    const listParam = reactive<OrderListParam>({
        name:'',
        currentPage:1,
        pageSize:10,
        total:0,
        controlName:''
    })

    //搜索
    const searchBtn = ()=>{
        getList()
    }

    //重置
    const resetBtn = ()=>{
        listParam.name = ''
        getList()
    }

    //页面容量改变时触发
    const orderSizeChange = (size:number)=>{
        listParam.pageSize = size
        getList()
    }

    //页码改变时触发
    const orderCurrentChange = (page:number)=>{
        listParam.currentPage = page
        getList()
    }

    //订单列表获取
    const getList = async ()=>{
        let res = await listApi(listParam)
        if(res && res.code == 200){
            tableData.list = res.data.records
            listParam.total = res.data.total
        }
    }

    onMounted(()=>{
        nextTick(()=>{
            orderTableHeight.value = window.innerHeight - 230
        })
        getList()
    })

    return {
        listParam,
        searchBtn,
        resetBtn,
        orderSizeChange,
        orderCurrentChange,
        orderTableHeight,
        tableData,
        getList
    }

}