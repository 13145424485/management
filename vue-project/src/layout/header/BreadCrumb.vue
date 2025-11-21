<template>
    <el-breadcrumb class="bred" separator="/">>
        <el-breadcrumb-item v-for="item in tabs">{{ item.meta.title }}</el-breadcrumb-item>
    </el-breadcrumb>
</template>
<script setup lang="ts">
import { ref, watch, Ref } from "vue";
import { useRoute, RouteLocationMatched } from "vue-router";
//定义一个数组 存放面包屑数据
const tabs: Ref<RouteLocationMatched[]> = ref([])
//获取当前路由信息
const route = useRoute()
const getBredcrumb = () => {
    //从路由里面获取所有有meta和title
    let mached = route.matched.filter((item) => item.meta && item.meta.title);
    console.log(mached)
    //判断第一个是否是首页,不是，构造一个
    //构造，，拼接
    // 确保首页始终在首位
    const first = mached[0];
    if (first.path !== "/dashboard") {
      mached = [{ path: "/dashboard", meta: { title: "首页" } } as any].concat(mached);
    }
    // 更新面包屑/标签页数据
    tabs.value = mached;
  };
  //  初始化执行
  getBredcrumb();
  //  监听路由变化自动更新
  watch(
    () => route.path,
    () => getBredcrumb()
  );
</script>
<style scoped lang="scss">
  //修改字体颜色
  :deep(.el-breadcrumb__inner) {
    color: #fff !important;
  }
  .bred {
    margin-left: 20px;
  }
  // 修改字体大小
  // :deep(.el-breadcrumb__item) {
  //   font-size: 16px !important;
  // }
  </style>