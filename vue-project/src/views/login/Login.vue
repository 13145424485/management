<template>
  <div class="logincontainer">
    <el-form
        :model="loginModel"
        class="loginform"
        ref="loginRef"
        :rules="rules"
        size="default"
    >
      <el-form-item>
        <div class="logintitle">系统登录</div>
      </el-form-item>
      <el-form-item prop="username">
        <el-input
            size="large"
            v-model="loginModel.username"
            placeholder="请输入账户"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            type="password"
            size="large"
            v-model="loginModel.password"
            placeholder="请输入密码"
        ></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-row :gutter="20">
          <el-col :span="16" :offset="0">
            <el-input
                size="large"
                v-model="loginModel.code"
                placeholder="请输入验证码"
            ></el-input>
          </el-col>
          <el-col :span="8" :offset="0">
            <img :src="imgSrc" class="image" @click="getImage" />
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="用户类型" prop="userType">
        <el-radio-group v-model="loginModel.userType">
          <el-radio :label="1">会员</el-radio>
          <el-radio :label="2">员工</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-row style="width: 100%">
          <el-col
              :span="12"
              :offset="0"
              style="padding-right: 10px; padding-left: 0px"
          >
            <el-button
                size="large"
                style="width: 100%"
                type="primary"
                @click="onSubmit"
            >登录</el-button
            >
          </el-col>
          <el-col
              :span="12"
              :offset="0"
              style="padding-right: 0px; padding-left: 10px"
          >
            <el-button size="large" style="width: 100%" type="danger" plain
            >重置</el-button
            >
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import useImage from "@/composables/login/useImage";
import { reactive, ref } from "vue";
import { loginApi } from "@/api/login/index";
import { FormInstance } from "element-plus";
import { userStore } from "@/store/user";
import { useRouter } from "vue-router";
const store = userStore();
const router = useRouter();
const loginRef = ref<FormInstance>();
//验证码
const { imgSrc, getImage } = useImage();
//登录表单对象
const loginModel = reactive({
  username: "admin",
  password: "123456",
  code: "",
  userType: "",
});
//表单验证规则
const rules = reactive({
  username: [
    {
      required: true,
      trigger: "blur",
      message: "请填写账户",
    },
  ],
  password: [
    {
      required: true,
      trigger: "blur",
      message: "请填写密码",
    },
  ],
  code: [
    {
      required: true,
      trigger: "blur",
      message: "请填写验证码",
    },
  ],
  userType: [
    {
      required: true,
      trigger: "blur",
      message: "请选择用户类型",
    },
  ],
});
//登录提交
const onSubmit = () => {
  loginRef.value?.validate(async (valid) => {
    if (valid) {
      // 将 userType 转换为字符串，确保与后端一致
      const loginParam = {
        ...loginModel,
        userType: String(loginModel.userType)
      };
      let res = await loginApi(loginParam);
      if (res && res.code == 200) {
        //存储userid和token
        store.setToken(res.data.token);
        store.setUserId(String(res.data.userId));
        store.setUserType(String(res.data.userType));
        //跳转到首页
        router.push({ path: "/" });
      }
    }
  });
};
</script>

<style scoped lang="scss">
.logincontainer {
  background-color: #fff;
  height: 100%;
  background-image: url("../../assets/bg.png");
  background-size: 100% 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  .loginform {
    border-radius: 10px;
    background-color: #fff;
    height: 340px;
    width: 420px;
    padding: 20px 35px;
    .logintitle {
      width: 100%;
      font-size: 24px;
      font-weight: 600;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .image {
      height: 40px;
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
    }
  }
}
</style>