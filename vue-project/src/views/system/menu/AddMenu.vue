<template>
  <SysDialog
      :title="dialog.title"
      :height="dialog.height"
      :width="dialog.width"
      :visible="dialog.visible"
      @onClose="onClose"
      @onConfirm="commit"
  >
    <template v-slot:content>
      <el-form
          :model="addModel"
          ref="addFormRef"
          :rules="rules"
          label-width="80px"
          size="default"
      >
        <el-form-item prop="type" label="菜单类型">
          <el-radio-group v-model="addModel.type">
            <el-radio :label="'0'">目录</el-radio>
            <el-radio :label="'1'">菜单</el-radio>
            <el-radio :label="'2'">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item label="上级菜单" prop="parentName">
              <el-input type="hidden" v-model="addModel.parentId"></el-input>
              <el-input
                  @click="selectOpen"
                  readonly
                  v-model="addModel.parentName"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item label="菜单名称" prop="title">
              <el-input v-model="addModel.title"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="addModel.type != '2'">
          <el-col :span="12" :offset="0">
            <el-form-item label="菜单图标" prop="icon">
              <el-input v-model="addModel.icon"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item label="路由名称" prop="name">
              <el-input v-model="addModel.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item label="菜单序号" prop="orderNum">
              <el-input v-model="addModel.orderNum"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item label="权限字段" prop="code">
              <el-input v-model="addModel.code"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col v-if="addModel.type != '2'" :span="12" :offset="0">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="addModel.path"></el-input>
            </el-form-item>
          </el-col>
          <el-col v-if="addModel.type == '1'" :span="12" :offset="0">
            <el-form-item label="组件路径" prop="url">
              <el-input v-model="addModel.url"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
  <!-- 上级菜单 -->
  <ParentMenu ref="parentRef" @selectParent="selectParent"></ParentMenu>
</template>

<script setup lang="ts">
import ParentMenu from "./ParentMenu.vue";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { MenuType, SelectNode } from "@/api/menu/MenuModel";
import { nextTick, reactive, ref } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { addApi,editApi } from "@/api/menu";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
const parentRef = ref<{ showParent: () => void }>();
const addFormRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//弹框显示
const show = (type: string, row?: MenuType) => {
  dialog.width = 680;
  type == EditType.ADD
      ? (dialog.title = Title.ADD)
      : (dialog.title = Title.EDIT);
  if (type == EditType.EDIT) {
    //把要编辑的数据设置到表单绑定的对象里面
    nextTick(() => {
      global.$objCoppy(row, addModel);
    });
  }
  onShow();
  //清空表单
  addFormRef.value?.resetFields();
  addModel.editType = type;
};
//暴露出去，给父组件调用
defineExpose({
  show,
});
//表单绑定的数据
const addModel = reactive<MenuType>({
  editType: "",
  menuId: "",
  parentId: "",
  title: "",
  code: "",
  name: "",
  path: "",
  url: "",
  type: "",
  icon: "",
  parentName: "",
  orderNum: "",
  open: true,
});
//表单验证规则
const rules = reactive({
  parentName: [
    {
      required: true,
      trigger: "change",
      message: "请选择上级菜单",
    },
  ],
  title: [
    {
      required: true,
      trigger: "change",
      message: "请填写菜单名称",
    },
  ],
  code: [
    {
      required: true,
      trigger: "change",
      message: "请填写权限字段",
    },
  ],
  name: [
    {
      required: true,
      trigger: "change",
      message: "请填写路由名称",
    },
  ],
  path: [
    {
      required: true,
      trigger: "change",
      message: "请填写路由地址",
    },
  ],
  url: [
    {
      required: true,
      trigger: "change",
      message: "请填写组件路径",
    },
  ],
  type: [
    {
      required: true,
      trigger: "change",
      message: "请选择菜单类型",
    },
  ],
});
//注册事件
const emits = defineEmits(["refresh"]);
//表单提交
const commit = () => {
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (addModel.editType == EditType.ADD) {
        res = await addApi(addModel);
      }else{
        res = await editApi(addModel)
      }
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        //刷新表格
        emits("refresh");
        onClose();
      }
    }
  });
};
//选择上级菜单事件
const selectOpen = () => {
  parentRef.value?.showParent();
};
//选择回调
const selectParent = (node: SelectNode) => {
  addModel.parentId = node.parentId;
  addModel.parentName = node.parentName;
};
</script>

<style scoped></style>
