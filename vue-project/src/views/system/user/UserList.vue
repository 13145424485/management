<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input v-model="listParam.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
            v-model="listParam.nickName"
            placeholder="请输入姓名"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="searchBtn" :icon="Search">搜索</el-button>
        <el-button @click="resetBtn" type="danger" plain :icon="Close">重置</el-button>
        <el-button v-permission="['sys:user:add']" type="primary" @click="addBtn" :icon="Plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableList.list" border stripe>
      <el-table-column prop="nickName" label="姓名"></el-table-column>
      <el-table-column prop="username" label="登录账户"></el-table-column>
      <el-table-column prop="sex" label="性别">
        <template #default="scope">
          <el-tag v-if="scope.row.sex == '0'" type="success" size="default">男</el-tag>
          <el-tag v-if="scope.row.sex == '1'" type="danger" size="default">女</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="userType" label="类型">
        <template #default="scope">
          <el-tag v-if="scope.row.userType == '1'" type="success" size="default">员工</el-tag>
          <el-tag v-if="scope.row.userType == '2'" type="danger" size="default">教练</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="salary" label="薪水"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status == '1'" type="success" size="default">启用</el-tag>
          <el-tag v-if="scope.row.status == '0'" type="danger" size="default">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column  label="操作" align="center" width="320">
        <template #default="scope">
          <el-button  type="primary" :icon="Edit" size="default" @click="editBtn(scope.row)">编辑</el-button>
          <el-button  type="danger" :icon="Delete" size="default" @click="deleteBtn(scope.row)">删除</el-button>
          <el-button  type="warning" :icon="Edit" size="default" @click="resetPasBtn(scope.row)">重置密码</el-button>
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
    <add-user ref="addRef" @refresh="refresh"></add-user>
  </el-main>
</template>

<script setup lang="ts">
import AddUser from "./AddUser.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useTable from "@/composables/user/useTable";
import useUser from "@/composables/user/useUser";
//表格
const { listParam, getList,searchBtn,resetBtn,tableList,sizeChange,currentChange,tableHeight,refresh } = useTable();
//新增、编辑
const { addBtn, editBtn, deleteBtn,resetPasBtn,addRef } = useUser(getList);
</script>

<style scoped></style>
