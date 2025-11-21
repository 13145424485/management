import { MemberType } from "@/api/member/MemberModel"
import { EditType } from "@/type/BaseEnum"
import { ref } from "vue"
import { deleteApi } from "@/api/member"
import { ElMessage } from "element-plus"
import { FuncList } from "@/type/BaseType"
import useInstance from "@/hooks/useInstance"
import { resetPasswordApi } from '@/api/home'

export default function useMember(getList: FuncList){

    const { global } = useInstance()

    const addRef = ref<{ show: (type: string, row?: MemberType) => void }>()

    //新增
    const addBtn = ()=>{
        addRef.value?.show(EditType.ADD)
    }
    //编辑
    const editBtn = (row:MemberType)=>{
        addRef.value?.show(EditType.EDIT,row)
    }
    //删除
    const deleteBtn = async (row:MemberType)=>{
        const confirm = await global.$myconfirm('确定删除该数据吗?')
        if(confirm){
            let res = await deleteApi(row.memberId)
            if(res && res.code == 200){
                ElMessage.success(res.msg)
                getList()
            }
        }
    }

    //重置密码
    const resetPasBtn = async (row:MemberType)=>{
        const confirm = await global.$myconfirm('确定重置密码吗?重置后的密码是[666666]')
        if(confirm){
            let param = {
                userId:row.memberId,
                userType:'1'
            }
            let res = await resetPasswordApi(param)
            if(res && res.code == 200){
                ElMessage.success(res.msg)
            }
        }
    }
    return{
        addBtn,
        editBtn,
        deleteBtn,
        resetPasBtn,
        addRef
    }
}