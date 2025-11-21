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
      <el-form
          :model="addModel"
          ref="addRormRef"
          :rules="rules"
          label-width="80px"
          size="default"
      >
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="name" label="姓名">
              <el-input v-model="addModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="sex" label="性别">
              <el-radio-group v-model="addModel.sex">
                <el-radio :label="'0'">男</el-radio>
                <el-radio :label="'1'">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="phone" label="电话">
              <el-input type="number" v-model="addModel.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item type="number" prop="age" label="年龄">
              <el-input v-model="addModel.age"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="birthday" label="生日">
              <el-date-picker
                  style="width:100%"
                  v-model="addModel.birthday"
                  type="date"
                  placeholder="请选择生日"
                  size="default"
                  value-format="YYYY-MM-DD"
                  format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="joinTime" label="加入时间">
              <el-date-picker
                  style="width:100%"
                  v-model="addModel.joinTime"
                  type="date"
                  placeholder="请选择加入时间"
                  size="default"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item type="number" prop="height" label="身高">
              <el-input v-model="addModel.height"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item type="number" prop="weight" label="体重">
              <el-input v-model="addModel.weight"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="waist" label="腰围">
              <el-input v-model="addModel.waist"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="roleId" label="角色">
              <el-select
                  style="width:100%"
                  v-model="addModel.roleId"
                  class="m-2"
                  placeholder="请选择角色"
                  size="default"
              >
                <el-option
                    v-for="item in roleData.list"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="status" label="状态">
              <el-radio-group v-model="addModel.status">
                <el-radio :label="'0'">停用</el-radio>
                <el-radio :label="'1'">启用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="username" label="会员卡号">
              <el-input type="number" v-model="addModel.username"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="addModel.type == '0'">
          <el-col :span="12" :offset="0">
            <el-form-item prop="password" label="密码">
              <el-input v-model="addModel.password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { nextTick, reactive, ref } from "vue";
import { MemberType } from "@/api/member/MemberModel";
import { ElMessage, FormInstance } from "element-plus";
import { addApi,editApi } from "@/api/member/index";
import { EditType, Title,UserType } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";
import useSelectRole from "@/composables/user/useSelectRole";
const { global } = useInstance();
const addRormRef = ref<FormInstance>();
//角色
const { roleData, listRole, roleMemberId, getMemberRole } = useSelectRole();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//弹框显示
const show = async(type: string, row?: MemberType) => {
  await listRole(UserType.STUDENT)
  await getMemberRole(row!?.memberId)
  dialog.width = 720;
  dialog.height = 350;
  type == EditType.ADD
      ? (dialog.title = Title.ADD)
      : (dialog.title = Title.EDIT);
  if (EditType.EDIT == type) {
    nextTick(() => {
      global.$objCoppy(row, addModel);
      addModel.roleId = roleMemberId.value
      addModel.password = ''
    });
  }
  addModel.type = type;
  onShow();
  addRormRef.value?.resetFields()
};
//暴露出去，给父组件调用
defineExpose({
  show,
});
//表单绑定的数据对象
const addModel = reactive<MemberType>({
  type: "",
  memberId: "",
  name: "",
  sex: "",
  phone: "",
  age: "",
  birthday: "",
  height: "",
  weight: "",
  waist: "",
  joinTime: "",
  endTime: "",
  username: "",
  password: "",
  status: "",
  roleId:''
});
//表单验证规则
const rules = reactive({
  roleId:[
    {
      required: true,
      trigger: "change",
      message: "请选择角色",
    },
  ],
  name: [
    {
      required: true,
      trigger: "change",
      message: "请填写姓名",
    },
  ],
  sex: [
    {
      required: true,
      trigger: "change",
      message: "请选择性别",
    },
  ],
  phone: [
    {
      required: true,
      trigger: "change",
      message: "请填写电话",
    },
  ],
  joinTime: [
    {
      required: true,
      trigger: "change",
      message: "请填写加入时间",
    },
  ],
  username: [
    {
      required: true,
      trigger: "change",
      message: "请填写会员卡号",
    },
  ],
  password: [
    {
      required: true,
      trigger: "change",
      message: "请填写密码",
    },
  ],
  status: [
    {
      required: true,
      trigger: "change",
      message: "请选择状态",
    },
  ],
});
//注册事件
const emits = defineEmits(["refresh"]);
//表单提交
const commit = () => {
  addRormRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if(addModel.type == EditType.ADD){
        res = await addApi(addModel);
      }else{
        res = await editApi(addModel)
      }
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        emits("refresh");
        onClose();
      }
    }
  });
};
</script>

<style scoped></style>
