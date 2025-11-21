import { LostType } from "@/api/lost/LostModel"
import { EditType } from "@/type/BaseEnum"
import { ref } from "vue"
import { deleteApi } from "@/api/lost"
import useInstance from "@/hooks/useInstance"
import { ElMessage } from "element-plus"
import { FuncList } from "@/type/BaseType"
export default function useLost(getList:FuncList) {
    const { global } = useInstance()
    const addRef = ref<{ show: (type: string, row?: LostType) => void }>()
    const lostPersonRef = ref<{ show: (row: LostType) => void }>()
    //新增
    const addBtn = () => {
        addRef.value?.show(EditType.ADD)
    }
    //编辑
    const editBtn = (row: LostType) => {
        addRef.value?.show(EditType.EDIT, row)
    }
    //删除
    const deleteBtn = async(row: LostType) => {
        const cofirm = await global.$myconfirm('确定删除该数据吗?')
        if(cofirm){
            let res = await deleteApi(row.lostId)
            if(res && res.code == 200){
                //信息提示
                ElMessage.success(res.msg)
                //刷新表格
                getList()
            }
        }
    }
    //认领
    const lostBtn = (row: LostType) => {
        if(row.status == '1'){
            ElMessage.warning('该物品已经被认领!')
            return;
        }
        lostPersonRef.value?.show(row)
    }
    return {
        addBtn,
        editBtn,
        deleteBtn,
        addRef,
        lostBtn,
        lostPersonRef
    }
}