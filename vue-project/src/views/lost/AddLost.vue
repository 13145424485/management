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
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="lostName" label="物品名称">
              <el-input v-model="addModel.lostName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="foundTime" label="捡到时间">
              <el-date-picker
                  style="width: 100%"
                  v-model="addModel.foundTime"
                  type="date"
                  placeholder="请选择捡到时间"
                  size="default"
                  value-format="YYYY-MM-DD"
                  format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="foundAddres" label="捡到地址">
              <el-input v-model="addModel.foundAddres"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="foundPerson" label="捡到人">
              <el-input v-model="addModel.foundPerson"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="foundPhone" label="联系电话">
              <el-input v-model="addModel.foundPhone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="status" label="状态">
              <el-radio-group v-model="addModel.status">
                <el-radio :label="'0'">未认领</el-radio>
                <el-radio :label="'1'">已认领</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { addApi, editApi } from "@/api/lost/index";
import { LostType } from "@/api/lost/LostModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { EditType, Title } from "@/type/BaseEnum";
import { ElMessage, FormInstance } from "element-plus";
import { nextTick, reactive, ref } from "vue";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
const addFormRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//定义外部使用的方法
const show = (type: string, row?: LostType) => {
  //设置弹框属性
  type == EditType.ADD
      ? (dialog.title = Title.ADD)
      : (dialog.title = Title.EDIT);
  dialog.height = 180;
  //如果是编辑，设置回显的数据
  if (type == EditType.EDIT) {
    nextTick(() => {
      global.$objCoppy(row, addModel);
    });
  }
  onShow();
  addModel.type = type;
  //清空表单
  addFormRef.value?.resetFields();
};
defineExpose({
  show,
});
//定义表单的数据对象
const addModel = reactive<LostType>({
  type: "",
  lostId: "",
  lostName: "",
  foundTime: "",
  foundAddres: "",
  foundPerson: "",
  foundPhone: "",
  status: "",
  lostPerson: "",
});
//表单验证规则
const rules = reactive({
  lostName: [
    {
      trigger: "blur",
      message: "请填写物品名称",
      required: true,
    },
  ],
  foundTime: [
    {
      trigger: "blur",
      message: "请填写捡到时间",
      required: true,
    },
  ],
  foundAddres: [
    {
      trigger: "blur",
      message: "请填写捡到地址",
      required: true,
    },
  ],
  foundPerson: [
    {
      trigger: "blur",
      message: "请填写捡到人",
      required: true,
    },
  ],
  foundPhone: [
    {
      trigger: "blur",
      message: "请填写捡到人电话",
      required: true,
    },
  ],
  status: [
    {
      trigger: "blur",
      message: "请选择认领状态",
      required: true,
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
        //信息提示
        ElMessage.success(res.msg);
        //刷新表格
        emits("reFresh");
        onClose();
      }
    }
  });
};
</script>

<style scoped></style>