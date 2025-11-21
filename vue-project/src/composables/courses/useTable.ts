import { listApi } from "@/api/course"
import { CourseListParam } from "@/api/course/CourseModel"
import { nextTick, onMounted, reactive,ref } from "vue"

export default function useTable(){

    //表格高度
    const tableHeight = ref(0)
    //定义表格数据
    const tableData = reactive({
        list:[]
    })

    //列表查询参数
    const listParam = reactive<CourseListParam>({
        courseName:'',
        teacherName:'',
        currentPage:1,
        pageSize:10,
        total:0
    })

    //查询列表
    const getList = async ()=>{
        let res = await listApi(listParam)
        if(res && res.code == 200){
            tableData.list = res.data.records;
            listParam.total = res.data.total;
        }
    }
    //页面容量改变时触发
    const sizeChange = (size:number)=>{
        listParam.pageSize = size;
        getList()
    }
    //页码改变时触发
    const currentChange = (page:number)=>{
        listParam.currentPage = page;
        getList()
    }
    //点击搜索框
    const searchBtn = ()=>{
        getList()
    }
    //重置
    const resetBtn = ()=>{
        listParam.courseName = ''
        listParam.teacherName = ''
        listParam.currentPage = 1
        listParam.pageSize = 10
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
        tableData,
        getList,
        sizeChange,
        currentChange,
        tableHeight,
        searchBtn,
        resetBtn,
        reFresh
    }
}