<template>
  <el-main>
    <el-table :height="tableHeight" :data="tableData.list" border stripe>
      <el-table-column label="会员卡号" prop="username"></el-table-column>
      <el-table-column label="会员姓名" prop="name"></el-table-column>
      <el-table-column label="充值金额" prop="money"></el-table-column>
      <el-table-column label="充值时间" prop="createTime"></el-table-column>
      <el-table-column label="操作人" prop="createUser"></el-table-column>
    </el-table>
    <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.sync="listParm.currentPage"
        :page-sizes="[10, 20, 40, 80, 100]"
        :page-size="listParm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="listParm.total"
        background
    >
    </el-pagination>
  </el-main>
</template>

<script setup lang="ts">
import { MemberRecharge } from "@/api/member/MemberModel";
import { nextTick, onMounted, reactive, ref } from "vue";
import { getMyRechargeApi } from "@/api/member";
import { userStore } from "@/store/user";
//表格高度
const tableHeight = ref(0);
const store = userStore();
//表格查询的参数
const listParm = reactive<MemberRecharge>({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  memberId: "",
  userType: "",
});
//表格数据
const tableData = reactive({
  list: [],
});
//查询表格数据
const getList = async () => {
  listParm.memberId = store.userId;
  listParm.userType = store.userType;
  console.log("发送请求参数:", {
    memberId: listParm.memberId,
    userType: listParm.userType,
    storeUserId: store.userId,
    storeUserType: store.userType
  });
  
  // 检查参数是否有效
  if (!listParm.userType) {
    console.error("userType 为空,无法查询");
    return;
  }
  
  let res = await getMyRechargeApi(listParm);
  if (res && res.code == 200) {
    console.log(res);
    tableData.list = res.data.records;
    listParm.total = res.data.total;
  }
};
//页容量改变时触发
const sizeChange = (size: number) => {
  listParm.pageSize = size;
  getList();
};
//页数改变时触发
const currentChange = (page: number) => {
  listParm.currentPage = page;
  getList();
};
onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230;
  });
  getList();
});
</script>

<style scoped></style>