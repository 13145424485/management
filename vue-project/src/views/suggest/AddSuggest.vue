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
        <el-form-item prop="title" label="标题">
          <el-input v-model="addModel.title"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="内容">
          <el-input type="textarea" v-model="addModel.content"></el-input>
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { SuggestType } from "@/api/suggest/SuggestParam";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage, FormInstance } from "element-plus";
import { nextTick, reactive, ref } from "vue";
import { addApi, editApi } from "@/api/suggest/index";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
const addFormRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//显示弹框
const show = (type: string, row?: SuggestType) => {
  console.log(type)
  console.log(row)
  dialog.height = 160;
  type == EditType.ADD
      ? (dialog.title = Title.ADD)
      : (dialog.title = Title.EDIT);
  //编辑数据回显
  if (type == EditType.EDIT) {
    nextTick(()=>{
      global.$objCoppy(row, addModel);
    })
  }
  onShow();
  addFormRef.value?.resetFields();
  addModel.type = type;
};
defineExpose({
  show,
});
//表单对象
const addModel = reactive<SuggestType>({
  type: "",
  id: "",
  title: "",
  content: "",
});
//表单验证规则
const rules = reactive({
  title: [
    {
      required: true,
      trigger: "blur",
      message: "请输入标题",
    },
  ],
  content: [
    {
      required: true,
      trigger: "blur",
      message: "请输入内容",
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
        ElMessage.success(res.msg);
        emits("reFresh");
        onClose();
      }
    }
  });
};
</script>

<style scoped></style>