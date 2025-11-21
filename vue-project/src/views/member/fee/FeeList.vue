<template>
  <div class="fee-list-container">
    <div class="search-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="会员姓名">
          <el-input
            v-model="searchForm.memberName"
            placeholder="请输入会员姓名"
            clearable
          />
        </el-form-item>
        <el-form-item label="充值时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="充值状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="成功" value="成功" />
            <el-option label="失败" value="失败" />
            <el-option label="处理中" value="处理中" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增充值
      </el-button>
      <el-button type="success" @click="handleExport">
        <el-icon><Download /></el-icon>
        导出记录
      </el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="memberName" label="会员姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="cardType" label="卡类型" />
      <el-table-column prop="amount" label="充值金额(元)">
        <template #default="scope">
          <span style="color: #f56c6c; font-weight: bold;">
            ¥{{ scope.row.amount }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="payMethod" label="支付方式" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag 
            :type="scope.row.status === '成功' ? 'success' : 
                   scope.row.status === '失败' ? 'danger' : 'warning'"
          >
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="chargeTime" label="充值时间" />
      <el-table-column prop="operator" label="操作员" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleView(scope.row)">
            查看详情
          </el-button>
          <el-button 
            v-if="scope.row.status === '失败'" 
            type="warning" 
            size="small" 
            @click="handleRetry(scope.row)"
          >
            重试
          </el-button>
          <el-button 
            v-if="scope.row.status === '成功'" 
            type="danger" 
            size="small" 
            @click="handleRefund(scope.row)"
          >
            退款
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 统计信息 -->
    <div class="statistics">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">¥{{ totalAmount }}</div>
              <div class="stat-label">总充值金额</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">{{ successCount }}</div>
              <div class="stat-label">成功笔数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">{{ failCount }}</div>
              <div class="stat-label">失败笔数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat-item">
              <div class="stat-value">{{ pendingCount }}</div>
              <div class="stat-label">处理中笔数</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  memberName: '',
  dateRange: [],
  status: ''
})

// 表格数据
const tableData = ref([
  {
    id: 1,
    memberName: '张三',
    phone: '13800138001',
    cardType: '年卡',
    amount: 1800,
    payMethod: '微信支付',
    status: '成功',
    chargeTime: '2024-01-01 10:00:00',
    operator: '管理员'
  },
  {
    id: 2,
    memberName: '李四',
    phone: '13800138002',
    cardType: '月卡',
    amount: 200,
    payMethod: '支付宝',
    status: '成功',
    chargeTime: '2024-01-02 11:00:00',
    operator: '管理员'
  },
  {
    id: 3,
    memberName: '王五',
    phone: '13800138003',
    cardType: '季卡',
    amount: 500,
    payMethod: '现金',
    status: '失败',
    chargeTime: '2024-01-03 12:00:00',
    operator: '管理员'
  }
])

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

// 统计数据
const totalAmount = computed(() => {
  return tableData.value
    .filter(item => item.status === '成功')
    .reduce((sum, item) => sum + item.amount, 0)
})

const successCount = computed(() => {
  return tableData.value.filter(item => item.status === '成功').length
})

const failCount = computed(() => {
  return tableData.value.filter(item => item.status === '失败').length
})

const pendingCount = computed(() => {
  return tableData.value.filter(item => item.status === '处理中').length
})

// 搜索
const handleSearch = () => {
  console.log('搜索', searchForm)
  // TODO: 实现搜索逻辑
}

// 重置
const handleReset = () => {
  searchForm.memberName = ''
  searchForm.dateRange = []
  searchForm.status = ''
  handleSearch()
}

// 新增充值
const handleAdd = () => {
  ElMessage.info('新增充值功能开发中...')
}

// 查看详情
const handleView = (row: any) => {
  ElMessage.info('查看详情功能开发中...')
}

// 重试
const handleRetry = (row: any) => {
  ElMessage.info('重试功能开发中...')
}

// 退款
const handleRefund = (row: any) => {
  ElMessageBox.confirm(
    `确认退款 "${row.memberName}" 的充值金额 ¥${row.amount} 吗？`,
    '退款确认',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    ElMessage.success('退款成功')
  }).catch(() => {
    ElMessage.info('已取消退款')
  })
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 分页
const handleSizeChange = (val: number) => {
  pageSize.value = val
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  handleSearch()
}

onMounted(() => {
  total.value = tableData.value.length
})
</script>

<style scoped lang="scss">
.fee-list-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.statistics {
  margin-top: 20px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}
</style>
