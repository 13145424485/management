<template>
  <el-main style="padding-top: 10px">
    <!--新增 -->
    <div style="display: flex; align-items: center">
      <el-button
          v-permission="['sys:menu:add']"
          style="margin-bottom: 10px"
          type="primary"
          :icon="Plus"
          size="default"
          @click="addBtn"
      >新增</el-button
      >
    </div>
    <!-- 表格数据 -->
    <el-table
        :height="tableHeight"
        :data="tableList.list"
        row-key="menuId"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        border
        default-expand-all
        stripe
    >
      <el-table-column prop="title" label="菜单名称"></el-table-column>
      <el-table-column prop="type" label="类型">
        <template #default="scope">
          <el-tag v-if="scope.row.type == '0'" size="default">目录</el-tag>
          <el-tag v-if="scope.row.type == '1'" type="success" size="default"
          >菜单</el-tag
          >
          <el-tag v-if="scope.row.type == '2'" type="danger" size="default"
          >按钮</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="图标">
        <template #default="scope">
          <el-icon>
            <!-- 动态组件的方式显示图标 -->
            <component :is="scope.row.icon"></component>
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="路由名称"></el-table-column>
      <el-table-column prop="path" label="路由地址"></el-table-column>
      <el-table-column prop="url" label="组件路径"></el-table-column>
      <el-table-column prop="code" label="权限字段"></el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template #default="scope">
          <el-button
              v-permission="['sys:menu:edit']"
              type="success"
              :icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              v-permission="['sys:menu:delete']"
              type="danger"
              :icon="Delete"
              size="default"
              @click="deleteBtn(scope.row)"
          >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增、编辑 -->
    <add-menu ref="addRef" @refresh="refresh"></add-menu>
  </el-main>
</template>

<script setup lang="ts">
import AddMenu from "./AddMenu.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useMenu from "@/composables/menu/useMenu";
import useMenuTable from "@/composables/menu/useMenuTable";
//表格数据
const { tableList, tableHeight, refresh, getList } = useMenuTable();
//新增、编辑
const { addBtn, editBtn, deleteBtn, addRef } = useMenu(getList);
</script>

<style scoped></style>
