<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input
            v-model="listParam.title"
            placeholder="请输入反馈标题"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button :icon="Close" type="danger" plain @click="resetBtn">重置</el-button>
        <el-button v-permission="['sys:suggestList:add']" :icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData.list" border stripe>
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="content" label="内容"></el-table-column>
      <el-table-column  label="操作" align="center" width="210">
        <template #default="scope">
          <el-button v-permission="['sys:suggestList:edit']" type="primary" :icon="Edit" size="default" @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-permission="['sys:suggestList:delete']" type="danger" :icon="Delete" size="default" @click="deleteBtn(scope.row)">删除</el-button>
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

    <!-- 新增编辑 -->
    <AddSuggest ref="addRef" @reFresh="reFresh"></AddSuggest>
  </el-main>
</template>

<script setup lang="ts">
import AddSuggest from "./AddSuggest.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useTable from "@/composables/suggest/useTable";
import useSuggest from "@/composables/suggest/useSuggest";
//表格
const { listParam ,searchBtn,resetBtn,getList,tableData,sizeChange,currentChange,tableHeight,reFresh} = useTable();
//新增编辑
const { addBtn, editBtn, deleteBtn,addRef } = useSuggest(getList);
</script>

<style scoped></style>