import { CardType } from "@/api/member_card/MemberModel"
import { EditType } from "@/type/BaseEnum"
import { ref } from "vue"
import useInstance from "@/hooks/useInstance";
import { deleteApi } from "@/api/member_card/index";
import { ElMessage } from "element-plus";
import { FuncList } from "@/type/BaseType";

export default function useMember(getList:FuncList){

    const { global} = useInstance();

    //定义弹框属性
    const addRef = ref<{show:(type:string,row?:CardType)=>void}>()

    //新增
    const addBtn = ()=>{
        addRef.value?.show(EditType.ADD) //弹框显示
    }

    //编辑
    const editBtn = (row:CardType)=>{
        addRef.value?.show(EditType.EDIT,row)
    }

    //删除
    const deleteBtn = async (row:CardType)=>{
        let confirm = await global.$myconfirm('确定删除该数据吗?')
        if(confirm){
            let res = await deleteApi(row.cardId)
            if(res && res.code == 200){
                ElMessage.success(res.msg)
                //刷新
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