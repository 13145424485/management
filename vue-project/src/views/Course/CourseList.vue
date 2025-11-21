<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input
            v-model="listParam.courseName"
            placeholder="课程名称"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="listParam.teacherName" placeholder="教练"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button :icon="Close" @click="resetBtn">重置</el-button>
        <el-button
            :icon="Plus"
            type="primary"
            @click="addBtn"
        >新增</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData.list" border stripe>
      <el-table-column prop="image" width="90" label="课程封面" align="center">
        <template #default="scope">
          <el-image
              style="width: 60px; height: 60px; border-radius: 50%"
              :src="scope.row.image"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="courseName" label="课程名称"></el-table-column>
      <el-table-column prop="courseHour" label="课时"></el-table-column>
      <el-table-column prop="coursePrice" label="价格"></el-table-column>
      <el-table-column prop="teacherName" label="授课教师"></el-table-column>
      <el-table-column label="操作" align="center" width="290">
        <template #default="scope">
          <el-button
              type="primary"
              :icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              @click="joinBtn(scope.row)"
              type="success"
              :icon="Edit"
              size="default"
          >报名</el-button
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
        :page-sizes="[10, 20, 40, 80, 100]"
        :page-size="listParam.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="listParam.total"
        background
    >
    </el-pagination>

    <!-- 新增、编辑 -->
    <AddCourse ref="addRef" @reFresh="reFresh"></AddCourse>
  </el-main>
</template>

<script setup lang="ts">
import AddCourse from "./AddCourse.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useTable from "@/composables/courses/useTable";
import useCourse from "@/composables/courses/useCourse";
//列表
const {
  listParam,
  tableData,
  getList,
  sizeChange,
  currentChange,
  tableHeight,
  searchBtn,
  resetBtn,
  reFresh,
} = useTable();
//新增、编辑
const { addBtn, editBtn, deleteBtn, addRef,joinBtn } = useCourse(getList);
</script>

<style scoped></style>