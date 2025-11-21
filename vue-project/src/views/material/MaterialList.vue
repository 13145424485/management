<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input
            v-model="listParam.name"
            placeholder="请输入器材名称"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button :icon="Close" type="danger" plain @click="resetBtn">重置</el-button>
        <el-button :icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData.list" border stripe>
      <el-table-column prop="name" label="器材名称"></el-table-column>
      <el-table-column prop="numTotal" label="器材数量"></el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template #default="scope">
          <el-button
              type="primary"
              :icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              type="danger"
              :icon="Delete"
              size="default"
              @click="deleteBtn(scope.row)"
          >删除</el-button
          >
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

    <!-- 新增、编辑 -->
    <AddMaterial ref="addRef" @reFresh="reFresh"></AddMaterial>
  </el-main>
</template>

<script setup lang="ts">
import AddMaterial from "./AddMaterial.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useTable from "@/composables/material/useTable";
import useMaterial from "@/composables/material/useMaterial";
//表格
const { listParam ,searchBtn,resetBtn,tableData,sizeChange,currentChange,tableHeight,reFresh} = useTable();
//新增、编辑
const {addBtn,editBtn,deleteBtn,addRef} = useMaterial()
</script>

<style scoped></style>
