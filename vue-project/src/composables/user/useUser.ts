import { AddUserModel } from "@/api/user/UserModel"
import { EditType } from "@/type/BaseEnum"
import { ref } from "vue"
import { deleteApi } from "@/api/user"
import { ElMessage } from "element-plus"
import { FuncList } from "@/type/BaseType"
import useInstance from "@/hooks/useInstance"
export default function useUser(getList: FuncList) {
    const { global } = useInstance()
    //新增组件的ref属性
    const addRef = ref<{ show: (type: string, row?: AddUserModel) => void }>()
    //新增
    const addBtn = () => {
        //父组件调用子组件的show方法
        addRef.value?.show(EditType.ADD)
    }
    //编辑
    const editBtn = (row: AddUserModel) => {
        //父组件调用子组件的show方法
        addRef.value?.show(EditType.EDIT, row)
    }
    //删除
    const deleteBtn = async (row: AddUserModel) => {
        const confrim = await global.$myconfirm('确定删除该数据吗?')
        if (confrim) {
            let res = await deleteApi(row.userId)
            if (res && res.code == 200) {
                ElMessage.success(res.message)
                //刷新表格
                getList()
            }
        }
    }

    //重置密码
    const resetPasBtn = async (row: AddUserModel) => {
        const confrim = await global.$myconfirm('确定重置该用户密码吗?')
        if (confrim) {
            // 这里可以调用重置密码的API
            ElMessage.success('密码重置成功')
            //刷新表格
            getList()
        }
    }

    return {
        addBtn,
        editBtn,
        deleteBtn,
        resetPasBtn,
        addRef
    }
}