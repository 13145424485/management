<template>
    <el-icon class="icons" @click="toggleCollapse">
         <component :is="status ? Fold : Expand"></component>
    </el-icon> 
</template>
<script setup lang="ts">
    import { computed } from "vue";
    import { Fold, Expand } from "@element-plus/icons-vue";
    import { collapseStore } from "@/store/collapse/index";
    //获取store
    const store = collapseStore();
    //获取collapse
    const status = computed(() => {
        return !store.getCollapse;
    });
    //改变collapse
    const toggleCollapse = ()=>{
        store.toggleCollapse()
    }
</script>
<style scoped lang="scss">
    .icons {
    color: #fff;
    font-size: 24px;
    }
</style>