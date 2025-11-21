<template>
  <el-tabs
    v-model="activeTab"
    @tab-click="clickBtn"
    type="card"
    closable
    @tab-remove="removeTab"
  >
    <el-tab-pane
      v-for="item in tabsList"
      :key="item.path"
      :label="item.title"
      :name="item.path"
      @contextmenu.prevent="openContextMenu($event, item)"
    ></el-tab-pane>
  </el-tabs>
  
  <!-- 右键菜单 -->
  <ul v-show="contextMenuVisible" :style="{ left: contextMenuLeft + 'px', top: contextMenuTop + 'px' }" class="contextmenu">
    <li @click="refreshSelectedTab">刷新当前</li>
    <li @click="closeSelectedTab">关闭当前</li>
    <li @click="closeOtherTabs">关闭其他</li>
    <li @click="closeAllTabs">关闭所有</li>
  </ul>
</template>
<script setup lang="ts">
import { ref, computed, watch, onMounted, onBeforeUnmount } from "vue";
import { tabStore } from "@/store/tabs";
import { useRoute, useRouter } from "vue-router";
import { Tab } from "@/store/tabs/index";
import { TabPaneName } from "element-plus";
const route = useRoute();
const router = useRouter();
const store = tabStore();
//获取tabs数据
const tabsList = computed(() => {
  // return store.getters['getTabs']
  return store.tabList;
});
//当前激活的选项卡，他跟当前激活的路由是一样的；
const activeTab = ref("");
const setActiveTab = () => {
  activeTab.value = route.path;
};
//删除选项卡
const removeTab = (targetName: TabPaneName) => {
  if (targetName === "/dashboard") return;
  //选项卡数据列表
  const tabs = tabsList.value;
  //当前激活的选项卡
  let activeName = activeTab.value;
  if (activeName === targetName) {
    tabs.forEach((tab: Tab, index: number) => {
      if (tab.path === targetName) {
        const nextTab = tabs[index + 1] || tabs[index - 1];
        if (nextTab) {
          activeName = nextTab.path;
        }
      }
    });
  }
  //重新设置当前激活的选项卡
  activeTab.value = activeName;
  //重新设置选项卡数据
  store.tabList = tabs.filter((tab: Tab) => tab.path !== targetName);
  //跳转路由
  router.push({ path: activeName });
};
//添加选项卡
const addTab = () => {
  //从当前路由获取path和title
  const { path, meta } = route;
  //通过vuex设置
  const tab: Tab = {
    path: path,
    title: meta.title as string,
  };
  // Check if tab already exists
  if (!store.tabList.some(item => item.path === tab.path)) {
    store.tabList.push(tab);
  }
};
//监听路由的变化
watch(
  () => route.path,
  () => {
    //设置激活的选项卡
    setActiveTab();
    //把当前路由添加到选项卡数据
    addTab();
  }
);
//解决刷新数据丢失的问题
const beforeRefresh = () => {
  if (route.path != "/login") {
    window.addEventListener("beforeunload", () => {
      sessionStorage.setItem("tabsView", JSON.stringify(tabsList.value));
    });
    let tabSesson = sessionStorage.getItem("tabsView");
    if (tabSesson) {
      let oldTabs = JSON.parse(tabSesson);
      if (oldTabs.length > 0) {
        store.tabList = oldTabs;
      }
    }
  }
};
onMounted(() => {
  //解决刷新选项卡数据丢失的问题
  beforeRefresh();
  //设置激活的选项卡
  setActiveTab();
  //把当前路由添加到选项卡数据
  addTab();
  document.addEventListener('click', closeContextMenu);
});
onBeforeUnmount(() => {
  document.removeEventListener('click', closeContextMenu);
});
//选项卡点击事件
const clickBtn = (tab: any) => {
  console.log(tab);
  const { props } = tab;
  console.log(props);
  //跳转路由
  router.push({ path: props.name });
};

// 右键菜单相关
const contextMenuVisible = ref(false);
const contextMenuLeft = ref(0);
const contextMenuTop = ref(0);
const selectedTab = ref<Tab | null>(null);

// 打开右键菜单
const openContextMenu = (e: MouseEvent, tab: Tab) => {
  e.preventDefault();
  contextMenuVisible.value = true;
  contextMenuLeft.value = e.clientX;
  contextMenuTop.value = e.clientY;
  selectedTab.value = tab;
};

// 刷新当前选中的标签页
const refreshSelectedTab = () => {
  if (selectedTab.value) {
    const { path } = selectedTab.value;
    router.replace({
      path: '/redirect',
      query: { path: path }
    });
  }
  contextMenuVisible.value = false;
};

// 关闭当前选中的标签页
const closeSelectedTab = () => {
  if (selectedTab.value && selectedTab.value.path !== '/dashboard') {
    removeTab(selectedTab.value.path);
  }
  contextMenuVisible.value = false;
};

// 关闭其他标签页
const closeOtherTabs = () => {
  if (selectedTab.value) {
    const { path } = selectedTab.value;
    // 保留当前选中的标签和首页标签
    store.tabList = tabsList.value.filter(
      (tab: Tab) => tab.path === path || tab.path === '/dashboard'
    );
    if (path !== activeTab.value) {
      router.push({ path });
    }
  }
  contextMenuVisible.value = false;
};

// 关闭所有标签页
const closeAllTabs = () => {
  // 只保留首页标签
  store.tabList = tabsList.value.filter((tab: Tab) => tab.path === '/dashboard');
  router.push({ path: '/dashboard' });
  contextMenuVisible.value = false;
};

// 点击其他区域关闭右键菜单
const closeContextMenu = () => {
  contextMenuVisible.value = false;
};
</script>
<style scoped lang="scss">
:deep(.el-tabs__header) {
  margin: 0px;
}
:deep(.el-tabs__item) {
  height: 26px !important;
  line-height: 26px !important;
  text-align: center !important;
  border: 1px solid #d8dce5 !important;
  margin: 0px 3px !important;
  color: #495060;
  font-size: 12px !important;
  padding: 0xp 10px !important;
}
:deep(.el-tabs__nav) {
  border: none !important;
}
:deep(.is-active) {
  border-bottom: 1px solid transparent !important;
  border: 1px solid #42b983 !important;
  background-color: #42b983 !important;
  color: #fff !important;
}
:deep(.el-tabs__item:hover) {
  color: #495060 !important;
}
:deep(.is-active:hover) {
  color: #fff !important;
}
:deep(.el-tabs__nav-next){
  line-height: 26px !important;
}
:deep(.el-tabs__nav-prev){
  line-height: 26px !important;
}

/* 右键菜单样式 */
.contextmenu {
  position: fixed;
  z-index: 3000;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 5px 0;
  min-width: 120px;
  list-style: none;
  margin: 0;
  
  li {
    padding: 8px 16px;
    cursor: pointer;
    font-size: 12px;
    color: #606266;
    
    &:hover {
      background-color: #f5f7fa;
      color: #42b983;
    }
  }
}
</style>