<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input v-model="listParam.lostName"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button type="danger" plain :icon="Close" @click="resetBtn"
        >重置</el-button
        >
        <el-button v-permission="['sys:lostList:add']" :icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData.list" border stripe>
      <el-table-column prop="lostName" label="物品名称"></el-table-column>
      <el-table-column prop="foundPerson" label="捡到人"></el-table-column>
      <el-table-column prop="foundAddres" label="捡到地址"></el-table-column>
      <el-table-column prop="foundTime" label="捡到时间"></el-table-column>
      <el-table-column prop="foundPhone" label="捡到人电话"></el-table-column>
      <el-table-column prop="lostPerson" label="认领人"></el-table-column>
      <el-table-column prop="status" label="认领状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status == '1'" type="success" size="default">已认领</el-tag>
          <el-tag v-if="scope.row.status == '0'" type="danger" size="default">未认领</el-tag>
        </template>
      </el-table-column>
      <el-table-column  label="操作" align="center" width="290">
        <template #default="scope">
          <el-button v-permission="['sys:lostList:edit']" type="primary" :icon="Edit" size="default" @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-permission="['sys:lostList:delete']" type="danger" :icon="Delete" size="default" @click="deleteBtn(scope.row)">删除</el-button>
          <el-button v-permission="['sys:lostList:get']" type="success" :icon="Edit" size="default" @click="lostBtn(scope.row)">认领</el-button>
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
    <AddLost ref="addRef" @reFresh="reFresh"></AddLost>
    <!-- 认领弹框 -->
    <LostPerson ref="lostPersonRef" @reFresh="reFresh"></LostPerson>
  </el-main>
</template>

<script setup lang="ts">
import AddLost from "./AddLost.vue";
import LostPerson from "./LostPerson.vue";
import useLost from "@/composables/lost/useLost";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useTable from "@/composables/lost/useTable";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
//表格
const { listParam, searchBtn, resetBtn,tableData,getList,sizeChange,currentChange ,tableHeight,reFresh} = useTable();
//新增 编辑
const { addBtn, editBtn, deleteBtn,addRef,lostBtn,lostPersonRef } = useLost(getList);
</script>

<style scoped></style>