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
          :model="lostPerson"
          ref="lostRef"
          :rules="rules"
          label-width="80px"
          :inline="false"
          size="default"
      >
        <el-form-item prop="lostPerson" label="认领人">
          <el-input
              v-model="lostPerson.lostPerson"
              placeholder="请输入认领人"
          />
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { LostType } from "@/api/lost/LostModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage, FormInstance } from "element-plus";
import { reactive, ref } from "vue";
import { editApi } from "@/api/lost";
const lostRef = ref<FormInstance>();
const lostPerson = reactive<LostType>({
  type: "",
  lostId: "",
  lostName: "",
  foundTime: "",
  foundAddres: "",
  foundPerson: "",
  foundPhone: "",
  status: "1",
  lostPerson: "",
});
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//显示弹框
const show = (row:LostType) => {
  dialog.title = "认领";
  dialog.height = 150;
  lostPerson.lostId = row.lostId
  onShow();
  lostRef.value?.resetFields()
};
defineExpose({
  show,
});
//表单验证规则
const rules = reactive({
  lostPerson: [
    {
      required: true,
      message: "请录入认领人",
      trigger: "blur",
    },
  ],
});
//注册事件
const emiets = defineEmits(['reFresh'])
//表单提交
const commit = () => {
  lostRef.value?.validate(async(valid) => {
    if(valid){
      let res = await editApi(lostPerson)
      if(res && res.code == 200){
        ElMessage.success('认领成功')
        emiets('reFresh')
        onClose()
      }
    }
  });
};
</script>

<style scoped></style>