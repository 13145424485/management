<template>
  <div class="full-screen">
    <el-container class="layout">
      <el-aside width="auto" class="asside">
        <menu-bar></menu-bar>
      </el-aside>
      <el-container>
        <el-header height="50px" class="header">
          <Header></Header>
        </el-header>
        <el-main class="main">
          <Tabs style="padding: 0px 20px"></Tabs>
          <keep-alive :include="cachedViews">
            <router-view v-slot="{ Component }">
              <component :is="Component" />
            </router-view>
          </keep-alive>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup lang="ts">
//vue3里面，组件引入，可以直接使用。不需要注册
import Header from "@/layout/header/Header.vue";
import MenuBar from "@/layout/menu/MenuBar.vue";
import Tabs from "./tabs/Tabs.vue";
import { computed } from "vue";
import { tabStore } from "@/store/tabs";

// 获取需要缓存的组件名列表
const store = tabStore();
const cachedViews = computed(() => {
  // 从路由路径中提取组件名，用于keep-alive的include属性
  return store.tabList
    .map(tab => {
      // 从路径中提取组件名，例如/dashboard -> Dashboard
      const pathSegments = tab.path.split('/').filter(Boolean);
      if (pathSegments.length > 0) {
        // 将组件名首字母大写以匹配组件定义
        const componentName = pathSegments[pathSegments.length - 1];
        return componentName.charAt(0).toUpperCase() + componentName.slice(1);
      }
      return '';
    })
    .filter(Boolean); // 过滤掉空字符串
});
</script>
<style scoped lang="scss">
.full-screen {
  height: 100vh; /* 视口高度 */
  width: 100vw; /* 视口宽度 */
}
.layout {
  height: 100%;
  .asside {
    // background-color: blueviolet;
    background-color: #304156;
  }
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #009688;
  }
  .main {
    padding-top: 1px;
    padding-left: 0px !important;
    padding-right: 0px !important;
  }
}
</style>