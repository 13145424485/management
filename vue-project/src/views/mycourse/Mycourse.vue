<template>
  <el-main>
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
      <el-table-column  label="操作" align="center" width="290">
        <template #default="scope">
          <el-button
              type="primary"
              :icon="Edit"
              size="default"
              @click="exportBtn(scope.row)"
          >导出学生</el-button
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
  </el-main>
</template>

<script setup lang="ts">
import {  Edit } from "@element-plus/icons-vue";
import useMyCourseTable from "../../composables/mycourse/useMyCourseTable";
import { CourseType } from "@/api/course/CourseModel";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
//列表
const {
  listParam,
  tableData,
  sizeChange,
  currentChange,
  tableHeight,
} = useMyCourseTable();
//导出
const exportBtn = (row:CourseType)=>{

}
</script>
<style scoped></style>