import { ref,onMounted,reactive,nextTick } from 'vue'
import { listApi } from '@/api/goods'
import { AddCar,GoodsParam,GoodsType } from '@/api/goods/GoodsModel'
import { ElMessage } from 'element-plus'
export default function useChildDrawer(){
    const innerDrawer = ref(false)

    //关闭商品列表的内嵌的抽屉
    const innerCloseClick = ()=>{
        innerDrawer.value = false
    }

    //表格高度
    const goodsHeight = ref(0)
    //选择的商品数据
    const selectGoods =  reactive<AddCar>({
        list:[]
    })
    //商品列表参数
    const goodsParam = reactive<GoodsParam>({
        name:'',
        pageSize:10,
        currentPage:1,
        total:0
    })
    //商品表格中的数据
    const tableList = reactive({
        list:[]
    })

    //获取商品列表
    const getList = async ()=>{
        let res = await listApi(goodsParam)
        if(res && res.code == 200){
            tableList.list = res.data.records
            goodsParam.total = res.data.total
        }
    }

    //搜索
    const searchGoodsBtn = ()=>{
        getList()
    }
    //重置
    const resetGoodsBtn = ()=>{
        goodsParam.name = ''
        getList()
    }
    //页面容量改变时触发
    const goodsSizeChange = (size:number)=>{
        goodsParam.pageSize = size
        getList()
    }
    //页码改变时触发
    const goodsCurrentChange = (page:number)=>{
        goodsParam.currentPage = page
        getList()
    }

    const innerDrawerShow = ()=>{
        getList()
        innerDrawer.value = true;
    }

    onMounted(()=>{
        nextTick(()=>{
            goodsHeight.value = window.innerHeight - 180
        })
        getList()
    })

    //加入购物车
    const addCarBtn = (row:GoodsType)=>{
        row.num = 1;
        const flag = selectGoods.list.some(item => item.goodsId === row.goodsId)
        if(flag){
            ElMessage.warning('该商品已经添加到购物车')
            return
        }
        selectGoods.list.push(row)
        ElMessage.success("加入购物车成功")
    }

    return {
        innerDrawer,
        innerCloseClick,
        goodsParam,
        tableList,
        addCarBtn,
        selectGoods,
        searchGoodsBtn,
        resetGoodsBtn,
        goodsSizeChange,
        goodsCurrentChange,
        goodsHeight,
        innerDrawerShow,
        getList
    }
}