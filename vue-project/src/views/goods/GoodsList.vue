<template>
  <el-main>
    <!--搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input v-model="listParam.name" label="请输入商品名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button :icon="Close" type="danger" plain @click="resetBtn"
        >重置</el-button
        >
        <el-button
            v-permission="['sys:goodsList:add']"
            :icon="Plus"
            type="primary"
            @click="addBtn"
        >新增</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData.list" border stripe>
      <el-table-column prop="image" width="90" label="商品图片" align="center">
        <template #default="scope">
          <el-image
              style="width: 60px; height: 60px; border-radius: 50%"
              :src="scope.row.image"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称"></el-table-column>
      <el-table-column prop="price" label="商品价格"></el-table-column>
      <el-table-column prop="store" label="商品库存"></el-table-column>
      <el-table-column prop="unit" label="单位"></el-table-column>
      <el-table-column prop="specs" label="规格"></el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template #default="scope">
          <el-button
              v-permission="['sys:goodsList:edit']"
              type="primary"
              :icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              v-permission="['sys:goodsList:delete']"
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
        :page-sizes="[10, 20, 40, 80, 100]"
        :page-size="listParam.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="listParam.total"
        background
    >
    </el-pagination>

    <!-- 新增弹框 -->
    <AddGoods ref="addRef" @reFresh="reFresh"></AddGoods>
  </el-main>
</template>

<script setup lang="ts">
import AddGoods from "./AddGoods.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useTable from "@/composables/goods/useTable";
import useGoods from "@/composables/goods/useGoods";

//表格
const {
  tableHeight,
  listParam,
  searchBtn,
  resetBtn,
  getList,
  tableData,
  sizeChange,
  currentChange,
  reFresh,
} = useTable();
//新增、编辑
const { addBtn, editBtn, deleteBtn, addRef } = useGoods(getList);
</script>

<style scoped></style>