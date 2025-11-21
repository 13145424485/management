import { AddRoleModel } from '@/api/role/RoleModel'
import { EditType } from '@/type/BaseEnum'
import { FuncList } from '@/type/BaseType'
import { ref } from 'vue'
import { deleteApi } from '@/api/role'
import { ElMessage } from 'element-plus'
import useInstance from '@/hooks/useInstance'
export default function useRole(getList: FuncList) {
    const { global } = useInstance()
    //分配权限弹框ref属性
    const assignRoleRef = ref<{ show: (roleId: string, name: string) => void }>()
    //新增、编辑组件ref属性
    const addRef = ref<{ show: (type: string, row?: AddRoleModel) => void }>()
    //新增
    const addBtn = () => {
        addRef.value?.show(EditType.ADD)
    }
    //编辑
    const editBtn = (row: AddRoleModel) => {
        addRef.value?.show(EditType.EDIT, row);
    }
    //删除
    const deleteBtn = async (row: AddRoleModel) => {
        let confirm = await global.$myconfirm('确定删除该数据吗?')
        if (confirm) {
            let res = await deleteApi(row.roleId)
            if (res && res.code == 200) {
                ElMessage.success(res.msg)
                getList()
            }
        }
    }

    //分配权限按钮的方法
    const assignBtn = (row:AddRoleModel)=>{
        assignRoleRef.value?.show(row.roleId,row.roleName)
    }

    return {
        addBtn,
        editBtn,
        deleteBtn,
        assignBtn,
        assignRoleRef,
        addRef
    }
}