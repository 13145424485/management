<template>
  <menu-logo :isCollapsed="isCollapse"></menu-logo>
  <el-menu
      :collapse="isCollapse"
      :default-active="activeIndex"
      class="el-menu-vertical-demo"
      unique-opened
      background-color="#304156"
      router
  >
    <menu-item :menuList="menuList"></menu-item>
  </el-menu>
</template>
<script setup lang="ts">
import { ref, reactive, computed } from "vue";
import { useRoute } from "vue-router";
import MenuItem from "@/layout/menu/Menultem.vue";
import MenuLogo from "@/layout/menu/MenuLogo.vue";
import { collapseStore } from "@/store/collapse/index";
import { menuStore } from "@/store/menu";
const colstore = collapseStore()
const mstore = menuStore()
//collapse
const isCollapse = computed(()=>{
  return colstore.getCollapse
})
//获取当前的路由
const route = useRoute();
//当前激活的菜单index
const activeIndex = computed(() => {
  const { path } = route;
  return path;
});
//树形的菜单数据
const menuList = computed(()=>{
  return mstore.getMenuList
})
console.log(menuList)
//reactive:定义响应式的数据（复杂类型，对象）
//ref:定义响应式的数据 （基本类型） let count = ref(0)
// let menuList = reactive([
//   {
//         path: "/dashboard",
//         component: "Layout",
//         name: "dashboard",
//         meta: {
//           title: "首页",
//           icon: "HomeFilled",
//           roles: ["sys:dashboard"],
//         },
//       },
//   {
//     path: "/system",
//     component: "Layout",
//     name: "system",
//     meta: {
//       title: "系统管理",
//       icon: "Document",
//       roles: ["sys:manage"],
//     },
//     children: [
//       {
//         path: "/userList",
//         component: "/system/user/UserList",
//         name: "userList",
//         meta: {
//           title: "员工管理",
//           icon: "UserFilled",
//           roles: ["sys:user"],
//         },
//       },
//       {
//         path: "/roleList",
//         component: "/system/role/RoleList",
//         name: "roleList",
//         meta: {
//           title: "角色管理",
//           icon: "Wallet",
//           roles: ["sys:role"],
//         },
//       },
//       {
//         path: "/menuList",
//         component: "/system/menu/MenuList",
//         name: "menuList",
//         meta: {
//           title: "菜单管理",
//           icon: "Menu",
//           roles: ["sys:menu"],
//         },
//       },
//     ],
//   },
//   {
//     path: "/memberRoot",
//     component: "Layout",
//     name: "memberRoot",
//     meta: {
//       title: "会员管理",
//       icon: "Stamp",
//       roles: ["sys:memberRoot"],
//     },
//     children: [
//       {
//         path: "/cardType",
//         component: "/member/type/CardType",
//         name: "cardType",
//         meta: {
//           title: "会员卡类型",
//           icon: "UserFilled",
//           roles: ["sys:cardType"],
//         },
//       },
//       {
//         path: "/memberList",
//         component: "/member/list/MemberList",
//         name: "memberList",
//         meta: {
//           title: "会员管理",
//           icon: "Wallet",
//           roles: ["sys:memberList"],
//         },
//       },
//       {
//         path: "/myFee",
//         component: "/member/fee/FeeList",
//         name: "myFee",
//         meta: {
//           title: "我的充值",
//           icon: "Menu",
//           roles: ["sys:myFee"],
//         },
//       },
//     ],
//   },
//   {
//     path: "/courseRoot",
//     component: "Layout",
//     name: "courseRoot",
//     meta: {
//       title: "课程管理",
//       icon: "ScaleToOriginal",
//       roles: ["sys:courseRoot"],
//     },
//     children: [
//       {
//         path: "/courseList",
//         component: "/course/CourseList",
//         name: "courseList",
//         meta: {
//           title: "课程列表",
//           icon: "UserFilled",
//           roles: ["sys:courseList"],
//         },
//       },
//       {
//         path: "/mycourse",
//         component: "/mycourse/mycourse",
//         name: "mycourse",
//         meta: {
//           title: "我的课程",
//           icon: "Wallet",
//           roles: ["sys:mycourse"],
//         },
//       }
//     ],
//   },
//   {
//     path: "/materialRoot",
//     component: "Layout",
//     name: "materialRoot",
//     meta: {
//       title: "器材管理",
//       icon: "KnifeFork",
//       roles: ["sys:materialRoot"],
//     },
//     children: [
//       {
//         path: "/materialList",
//         component: "/material/MaterialList",
//         name: "materialList",
//         meta: {
//           title: "器材列表",
//           icon: "UserFilled",
//           roles: ["sys:materialList"],
//         },
//       }
//     ],
//   },
//   {
//     path: "/goodsRoot",
//     component: "Layout",
//     name: "goodsRoot",
//     meta: {
//       title: "商品管理",
//       icon: "Calendar",
//       roles: ["sys:goodsRoot"],
//     },
//     children: [
//       {
//         path: "/goodsList",
//         component: "/goods/GoodsList",
//         name: "goodsList",
//         meta: {
//           title: "商品列表",
//           icon: "UserFilled",
//           roles: ["sys:goodsList"],
//         },
//       },
//       {
//         path: "/orderList",
//         component: "/order/OrderList",
//         name: "orderList",
//         meta: {
//           title: "订单管理",
//           icon: "UserFilled",
//           roles: ["sys:orderList"],
//         },
//       }
//     ],
//   },
//   {
//     path: "/lostRoot",
//     component: "Layout",
//     name: "lostRoot",
//     meta: {
//       title: "失物招领",
//       icon: "Document",
//       roles: ["sys:lostRoot"],
//     },
//     children: [
//       {
//         path: "/lostList",
//         component: "/goods/LostList",
//         name: "lostList",
//         meta: {
//           title: "失物列表",
//           icon: "UserFilled",
//           roles: ["sys:lostList"],
//         },
//       }
//     ],
//   },
//   {
//     path: "/suggestRoot",
//     component: "Layout",
//     name: "suggestRoot",
//     meta: {
//       title: "反馈管理",
//       icon: "Document",
//       roles: ["sys:suggestRoot"],
//     },
//     children: [
//       {
//         path: "/suggestList",
//         component: "/suggest/SuggestList",
//         name: "suggestList",
//         meta: {
//           title: "反馈列表",
//           icon: "UserFilled",
//           roles: ["sys:suggestList"],
//         },
//       }
//     ],
//   }
// ]);
</script>
<style scoped lang="scss">
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 230px;
  min-height: 400px;
}
.el-menu {
  border-right: none;
}

:deep(.el-sub-menu .el-sub-menu__title) {
  color: #f4f4f5 !important;
}

:deep(.el-menu .el-menu-item) {
  color: #bfcbd9;
}
/* 菜单点中文字的颜色 */

:deep(.el-menu-item.is-active) {
  color: #409eff !important;
}
/* 当前打开菜单的所有子菜单颜色 */

:deep(.is-opened .el-menu-item) {
  background-color: #1f2d3d !important;
}
/* 鼠标移动菜单的颜色 */

:deep(.el-menu-item:hover) {
  background-color: #001528 !important;
}
</style>
