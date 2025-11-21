import { GoodsType } from "@/api/goods/GoodsModel"
import { EditType } from "@/type/BaseEnum"
import { ref } from "vue"
import { deleteApi } from "@/api/goods"
import useInstance from "@/hooks/useInstance"
import { ElMessage } from "element-plus"
import { FuncList } from "@/type/BaseType"

export default function useGoods(getList:FuncList){
    const { global } = useInstance()
    const addRef = ref<{show:(type:string,row?:GoodsType)=>void}>()

    //新增
    const addBtn = ()=>{
        addRef.value?.show(EditType.ADD) // 打开弹框
    }

    //编辑
    const editBtn = (row:GoodsType)=>{
        addRef.value?.show(EditType.EDIT,row)
    }

    //删除
    const deleteBtn = async (row:GoodsType)=>{
        const confirm = await global.$myconfirm("确定删除该数据吗?")
        if(confirm){
            let res = await deleteApi(row.goodsId)
            if(res && res.code == 200){
                ElMessage.success(res.msg)
                getList()
            }
        }
    }

    return {
        addBtn,
        editBtn,
        deleteBtn,
        addRef
    }
}