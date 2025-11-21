<template>
  <SysDialog
      :title="dialog.title"
      :width="dialog.width"
      :height="dialog.height"
      :visible="dialog.visible"
      @onClose="onClose"
      @onConfirm="commit"
  >
    <template v-slot:content>
      <el-tree
          ref="assignTree"
          :data="assignTreeData.list"
          node-key="menuId"
          :props="defaultProps"
          empty-text="暂无数据"
          :show-checkbox="true"
          default-expand-all
          :highlight-current="true"
          :default-checked-keys="assignTreeData.assignTreeChecked"
      ></el-tree>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import useAssign from "@/composables/role/useAssign";
import { userStore } from "@/store/user";
import { reactive, ref } from "vue";
import { ElMessage, ElTree } from "element-plus";
import { saveRoleMenuApi } from "@/api/role/index";
import { SaveAssignParm } from "@/api/role/RoleModel";
//树的ref属性
const assignTree = ref<InstanceType<typeof ElTree>>();
const store = userStore();
//提交参数
const saveParm = reactive<SaveAssignParm>({
  roleId: "",
  list: [],
});
//权限树
const { assignTreeData, getMenuTree, defaultProps } = useAssign();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//弹框显示
const show = (roleId: string, name: string) => {
  saveParm.roleId = roleId;
  let parm = {
    roleId: roleId,
    userId: store.getUserId,
  };
  getMenuTree(parm);
  //设置弹框属性
  dialog.title = "为【" + name + "】分配权限";
  dialog.width = 300;
  dialog.height = 450;
  onShow();
};
defineExpose({
  show,
});
//提交保存
const commit = async () => {
  //获取选择的菜单数据id
  // console.log(assignTree.value)
  let checkIds = assignTree.value?.getCheckedKeys() as string[];
  let hlfIds = assignTree.value?.getHalfCheckedKeys() as string[];
  let list = checkIds?.concat(hlfIds);
  if (list.length == 0) {
    ElMessage.warning("请勾选权限信息!");
    return;
  }
  saveParm.list = list;
  console.log(checkIds);
  console.log(hlfIds);
  console.log(list);
  //提交保存
  let res = await saveRoleMenuApi(saveParm);
  if (res && res.code == 200) {
    ElMessage.success(res.msg);
    onClose();
  }
};
</script>

<style scoped></style>
