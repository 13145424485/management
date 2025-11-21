<template>
  <SysDialog
      :title="dialog.title"
      :height="dialog.height"
      :width="dialog.width"
      :visible="dialog.visible"
      @onConfirm="commit"
      @onClose="onClose"
  >
    <template v-slot:content>
      <el-form
          :model="addModel"
          ref="addFormRef"
          :rules="rules"
          label-width="80px"
          size="default"
      >
        <el-form-item prop="name" label="器材名称">
          <el-input v-model="addModel.name"></el-input>
        </el-form-item>
        <el-form-item prop="numTotal" label="器材数量">
          <el-input v-model="addModel.numTotal"></el-input>
        </el-form-item>
        <el-form-item prop="details" label="简介">
          <el-input type="textarea" v-model="addModel.details" />
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { MaterialType } from "@/api/material/MaterialModel";
import { nextTick, reactive, ref } from "vue";
import { ElMessage, FormInstance } from "element-plus";
import { addApi, editApi } from "@/api/material/index";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
//表单的ref属性
const addFormRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//定义方法给外部使用
const show = (type: string, row?: MaterialType) => {
  //设置弹框属性
  type == EditType.ADD
      ? (dialog.title = Title.ADD)
      : (dialog.title = Title.EDIT);
  //如果是编辑，设置数据回显
  if (type == EditType.EDIT) {
    //把当前编辑的数据复制到表单对象
    nextTick(() => {
      global.$objCoppy(row, addModel);
    });
  }
  onShow();
  //清空表单
  addFormRef.value?.resetFields();
  addModel.type = type;
};
//暴露出去给外地调用
defineExpose({
  show,
});
//弹框绑定的对象
const addModel = reactive<MaterialType>({
  type: "",
  name: "",
  numTotal: 0,
  details: "",
  id: "",
});
const validateNum = (rule: any, value: any, callback: any) => {
  if (value === 0 || value < 0) {
    callback(new Error("请填写数量"));
  } else {
    callback();
  }
};
//表单验证规则
const rules = reactive({
  name: [
    {
      required: true,
      message: "请输入器材名称",
      trigger: "blur",
    },
  ],
  numTotal: [
    {
      required: true,
      validator: validateNum,
      trigger: "blur",
    },
  ],
});
//注册事件
const emits = defineEmits(["reFresh"]);
//表单提交
const commit = () => {
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (addModel.type == EditType.ADD) {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      if (res && res.code == 200) {
        emits("reFresh");
        //信息提示
        ElMessage.success(res.msg);
        //刷新表格
        onClose();
      }
    }
  });
};
</script>

<style scoped></style>