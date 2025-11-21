import { SelectRole } from "@/api/user/UserModel";
import { reactive, ref } from "vue";
import { getSelectApi,getRoleApi } from "@/api/user";
import { getRoleByMemberIdApi } from '@/api/member/index';

export default function useSelectRole() {
    const roleId = ref('')
    const roleMemberId = ref('')
    //定义角色列表数据
    const roleData = reactive<SelectRole>({
        list: []
    })
    //获取数据
    const listRole = async()=>{
        let res = await getSelectApi()
        if(res && res.code == 200){
            roleData.list = res.data
        }
    }
    //根据用户id查询角色
    const getRole = async(userId:string)=>{
        roleId.value = ''
        let res = await getRoleApi(userId)
        if(res && res.code == 200 && res.data){
            roleId.value = res.data.roleId
        }
    }

    //根据用户id查询角色
    const getMemberRole = async (memberId: string) => {
        roleMemberId.value = ''
        let res = await getRoleByMemberIdApi(memberId)
        if (res && res.code == 200 && res.data) {
            roleMemberId.value = res.data.roleId
        }
    }
    return{
        roleData,
        listRole,
        getRole,
        roleId,
        getMemberRole,
        roleMemberId
    }
}