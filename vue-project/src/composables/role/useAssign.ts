import { getMenuTreeApi } from "@/api/role";
import { AssignParam } from "@/api/role/RoleModel";
import { reactive } from "vue";
export default function useAssign() {
    //树的属性配置
    const defaultProps = reactive({
        children: 'children',
        label: 'title',
    })
    //权限树数据
    const assignTreeData = reactive({
        list: [],
        assignTreeChecked: [] //原来分配的权限id的集合
    })
    //获取权限树数据
    const getMenuTree = async (parm: AssignParam) => {
        let res = await getMenuTreeApi(parm)
        if (res && res.code == 200) {
            //设置权限树数据
            assignTreeData.list = res.data.listmenu
            //设置角色原来的权限Id
            assignTreeData.assignTreeChecked = res.data.checkList
            //数据回显，判断角色原来是否已经分配过权限，如果有，回显
            if (assignTreeData.assignTreeChecked.length > 0) {
                let newArr: any = [];
                assignTreeData.assignTreeChecked.forEach((item => {
                    checked(item, assignTreeData.list, newArr)
                }))
                assignTreeData.assignTreeChecked = newArr;
            }
        }
    }
    const checked = (id: number, data: any, newArr: any) => {
        data.forEach((item: any) => {
            if (item.menuId == id) {
                if (item.children && item.children.length == 0) {
                    newArr.push(item.menuId)
                }
            } else {
                if (item.children && item.children.length != 0) {
                    //递归调用
                    checked(id, item.children, newArr)
                }
            }
        })
    }
    return{
        defaultProps,
        assignTreeData,
        getMenuTree
    }
}