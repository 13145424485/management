import { SuggestType } from "@/api/suggest/SuggestParam"
import { EditType } from "@/type/BaseEnum"
import { ref } from "vue"
import { deleteApi } from "@/api/suggest"
import useInstance from "@/hooks/useInstance"
import { ElMessage } from "element-plus"
import { FuncList } from "@/type/BaseType"
export default function useSuggest(getList:FuncList){

    const {global} = useInstance()
    const addRef = ref<{show:(type:string,row?:SuggestType)=>void}>()

    //新增
    const addBtn = ()=>{
        addRef.value?.show(EditType.ADD)
    }

    //编辑
    const editBtn = (row:SuggestType)=>{
        addRef.value?.show(EditType.EDIT,row)
    }

    //删除
    const deleteBtn = async (row:SuggestType)=>{
        const confirm = await global.$myconfirm("确定删除该记录吗?")
        if(confirm){
            let res = await deleteApi(row.id)
            if(res && res.code == 200){
                ElMessage.success(res.msg)
                getList()
            }
        }
    }

    return {
        addRef,
        addBtn,
        editBtn,
        deleteBtn
    }

}