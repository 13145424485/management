<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input
            v-model="listParam.roleName"
            placeholder="请输入角色名称"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="searchBtn" :icon="Search">搜索</el-button>
        <el-button @click="resetBtn" :icon="Close" type="danger" plain>重置</el-button>
        <el-button v-permission="['sys:role:add']" type="primary" @click="addBtn" :icon="Plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableList.list" border stripe>
      <el-table-column prop="roleName" label="角色名称"></el-table-column>
      <el-table-column prop="remark" label="角色描述"></el-table-column>
      <el-table-column  label="操作" width="320" align="center">
        <template #default="scope">
          <el-button v-permission="['sys:role:edit']" type="primary" :icon="Edit" size="default" @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-permission="['sys:role:assign']" type="success" :icon="Edit" size="default" @click="assignBtn(scope.row)">分配权限</el-button>
          <el-button v-permission="['sys:role:delete']" type="danger" :icon="Delete" size="default" @click="deleteBtn(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.sync="listParam.currentPage"
        :page-sizes="[10,20, 40, 80, 100]"
        :page-size="listParam.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="listParam.total" background>
    </el-pagination>

    <!-- 新增、编辑弹框 -->
    <role-add ref="addRef" @refresh="refresh"></role-add>
    <!-- 分配权限 -->
    <AssignRole ref="assignRoleRef"></AssignRole>
  </el-main>
</template>

<script setup lang="ts">
import AssignRole from "./AssignRole.vue";
import RoleAdd from "./RoleAdd.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useTable from "@/composables/role/useTable";
import useRole from "@/composables/role/useRole";
//表格相关的业务
const { listParam, getList, searchBtn, resetBtn,tableList,sizeChange ,currentChange,tableHeight,refresh} = useTable();
//增删改查业务
const { addBtn, editBtn, deleteBtn,addRef,assignBtn,assignRoleRef } = useRole(getList);
</script>

<style scoped></style>
