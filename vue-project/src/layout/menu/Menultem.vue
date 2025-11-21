<template>
    <template v-for="menu in menuList" :key="menu.path">
        <el-sub-menu v-if="menu.children && menu.children.length > 0"
            :index="menu.path">
            <template #title>
                <el-icon>
                    <component :is="menu.meta.icon"></component>
                </el-icon>
                <span>{{ menu.meta.title }}</span>
            </template>
            <!-- 递归渲染子菜单 -->
            <template v-for="child in menu.children" :key="child.path">
                <el-menu-item :index="child.path">
                    <el-icon>
                        <component :is="child.meta.icon"></component>
                    </el-icon>
                    <template #title>{{ child.meta.title }}</template>
                </el-menu-item>
            </template>
        </el-sub-menu>
        <el-menu-item v-else :index="menu.path">
            <el-icon>
                <component :is="menu.meta.icon"></component>
            </el-icon>
            <template #title>{{ menu.meta.title }}</template>
        </el-menu-item>
    </template>
</template>
<script setup lang="ts">
// 递归组件需要自己引用自己
import MenuItem from './Menultem.vue';

defineProps(["menuList"]); //获取子组件传递过来的数据
</script>
<style scoped>
.el-menu-item {
  transition: background-color 0.3s, color 0.3s;
}
.el-menu-item:hover {
  background-color: #263445 !important;
}
.el-sub-menu :deep(.el-sub-menu__title) {
  transition: background-color 0.3s;
}
.el-sub-menu :deep(.el-sub-menu__title:hover) {
  background-color: #263445 !important;
}
</style>