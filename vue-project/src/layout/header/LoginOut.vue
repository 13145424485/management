<template>
  <el-dropdown>
    <span class="el-dropdown-link">
      <img class="userimg" src="@/assets/user.jpg" />
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="updateBtn">修改密码</el-dropdown-item>
        <el-dropdown-item @click="loginOut">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
  <!-- 修改密码弹框 -->
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
          :model="parm"
          ref="upForm"
          :rules="rules"
          label-width="80px"
          size="default"
      >
        <el-form-item prop="oldPassword" label="原密码">
          <el-input v-model="parm.oldPassword"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="新密码">
          <el-input v-model="parm.password"></el-input>
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { ResetPasswordParam } from "@/api/home/HomeModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import useInstance from "@/hooks/useInstance";
import { userStore } from "@/store/user";
import { ElMessage, FormInstance } from "element-plus";
import { reactive, ref } from "vue";
import { updatePasswordApi,loginOutApi } from "@/api/home";
const { dialog, onClose, onConfirm, onShow } = useDialog();
const { global } = useInstance();
const upForm = ref<FormInstance>();
const store = userStore();
const loginOut = async () => {
  const confirm = await global.$myconfirm("确定退出登录吗？");
  if (confirm) {
    try {
      await loginOutApi();
    } catch (error) {
      // 即使后端接口调用失败，也允许退出登录
      console.warn("退出登录接口调用失败，但继续执行退出操作", error);
    }
    //清空用户信息
    store.setToken("");
    store.setUserId("");
    store.setUserType("");
    localStorage.clear();
    window.location.href = "/login";
  }
};
const updateBtn = () => {
  dialog.height = 150;
  dialog.title = "修改密码";
  onShow();
};
const parm = reactive<ResetPasswordParam>({
  userId: store.getUserId,
  userType: store.getUserType,
  oldPassword: "",
  password: "",
});
const rules = reactive({
  oldPassword: [
    {
      required: true,
      trigger: "blur",
      message: "原密码不能为空!",
    },
  ],
  password: [
    {
      required: true,
      trigger: "blur",
      message: "新密码不能为空!",
    },
  ],
});
const commit = () => {
  upForm.value?.validate(async (valid) => {
    if (valid) {
      let res = await updatePasswordApi(parm);
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        //清空用户信息
        store.setToken("");
        store.setUserId("");
        store.setUserType("");
        localStorage.clear();
        window.location.href = "/login";
      }
    }
  });
};
</script>

<style scoped>
.userimg {
  height: 38px;
  width: 38px;
  border-radius: 50%;
}
</style>