<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input v-model="listParam.title" placeholder="请输入标题"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button :icon="Close" type="danger" plain @click="resetBtn"
        >重置</el-button
        >
        <el-button v-permission="['sys:memberRoot:add']" :icon="Plus" type="primary" @click="addBtn"
        >新增</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableList.list" border stripe>
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="cardType" label="类型">
        <template #default="scope">
          <el-tag v-if="scope.row.cardType == '1'" size="default">天卡</el-tag>
          <el-tag v-if="scope.row.cardType == '2'" type="success" size="default"
          >周卡</el-tag
          >
          <el-tag v-if="scope.row.cardType == '3'" type="danger" size="default"
          >月卡</el-tag
          >
          <el-tag v-if="scope.row.cardType == '4'" type="danger" size="default"
          >年卡</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="cardDay" label="天数"></el-table-column>
      <el-table-column prop="price" label="价格(元)"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status == '1'" type="success" size="default">启用</el-tag>
          <el-tag v-if="scope.row.status == '0'" type="danger" size="default">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column  label="操作" align="center" width="220">
        <template #default="scope">
          <el-button v-permission="['sys:memberRoot:edit']" type="primary" :icon="Edit" size="default" @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-permission="['sys:memberRoot:delete']" type="danger" :icon="Delete" size="default" @click="deleteBtn(scope.row)">删除</el-button>
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
    <AddCard ref="addRef" @refresh="refresh"></AddCard>
  </el-main>
</template>

<script setup lang="ts">
import AddCard from "./AddCard.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useMemberTable from "@/composables/member_card/useMemberTable";
import useMember from "@/composables/member_card/useMember";
//表格操作
const { listParam, getList, resetBtn, searchBtn,tableList,sizeChange,currentChange,tableHeight,refresh } = useMemberTable();
//新增、编辑
const { addBtn, editBtn, deleteBtn,addRef } = useMember(getList);
</script>

<style scoped></style>
