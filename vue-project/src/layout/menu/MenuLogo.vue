<template>
  <div class="logo" :class="{ collapsed: isCollapsed }">
    <img :src="MenuLogo" />
    <span v-show="show" class="logo-title">{{ title }}</span>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import MenuLogo from "@/assets/docter.png";
const title = ref("运动健康管理系统");
//定义组件属性
const props = defineProps({
  isCollapsed: {
    require: true,
    type: Boolean,
    default: false
  },
});
const show = ref(!props.isCollapsed);

//监听折叠状态变化,监听collapse状态
watch(
  () => props.isCollapsed,
  (collapsed: boolean) => {
    if (!collapsed) {
      setTimeout(() => {
        show.value = true;
      }, 100);
    } else {
      show.value = false;
    }
  },
  { immediate: true }
);

onMounted(() => {
  show.value = !props.isCollapsed;
});
</script>
<style scoped lang="scss">
.logo {
  display: flex;
  width: 100%;
  height: 60px;
  line-height: 60px;
  background: #2b2f3a;
  text-align: center;
  cursor: pointer;
  align-items: center;
  transition: all 0.3s;
  
  &.collapsed {
    padding-left: 10px;
    justify-content: center;
  }
  
  img {
    width: 36px;
    height: 36px;
    margin-right: 12px;
    margin-left: 20px;
    transition: all 0.3s;
  }
  
  .logo-title {
    color: #fff;
    font-weight: 800;
    line-height: 60px;
    font-size: 22px;
    font-family: FangSong;
    white-space: nowrap;
    transition: opacity 0.3s;
  }
  
  &.collapsed img {
    margin-left: 0;
    margin-right: 0;
  }
}
</style>