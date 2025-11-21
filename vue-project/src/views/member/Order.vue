<template>
  <div class="order-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">订单管理</h2>
      <div class="header-actions">
        <el-button type="primary" :icon="Refresh" @click="refreshData">刷新</el-button>
        <el-button type="success" :icon="Plus" @click="handleAdd">新增订单</el-button>
      </div>
    </div>

    <!-- 数据筛选区域 -->
    <div class="filter-section">
      <el-card shadow="never" class="filter-card">
        <div class="filter-content">
          <div class="filter-row">
            <div class="filter-item">
              <label>订单编号：</label>
              <el-input
                v-model="searchForm.orderNo"
                placeholder="订单编号"
                clearable
                style="width: 200px;"
              />
            </div>
            <div class="filter-item">
              <label>会员姓名：</label>
              <el-input
                v-model="searchForm.memberName"
                placeholder="输入会员姓名"
                clearable
                style="width: 200px;"
              />
            </div>
            <div class="filter-item">
              <label>订单状态：</label>
              <el-select
                v-model="searchForm.status"
                placeholder="选择状态"
                clearable
                style="width: 200px;"
              >
                <el-option label="待支付" value="待支付" />
                <el-option label="已支付" value="已支付" />
                <el-option label="已完成" value="已完成" />
                <el-option label="已取消" value="已取消" />
                <el-option label="已退款" value="已退款" />
              </el-select>
            </div>
            <div class="filter-item">
              <label>下单时间：</label>
              <el-date-picker
                v-model="searchForm.orderTime"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 240px;"
              />
            </div>
            <div class="filter-actions">
              <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
              <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 数据表格区域 -->
    <div class="table-section">
      <el-card shadow="never" class="table-card">
        <div class="table-header">
          <span class="table-title">列表</span>
          <el-button type="text" :icon="Download" @click="handleExport">导出</el-button>
        </div>

        <el-table
          :data="tableData"
          style="width: 100%"
          stripe
          border
          v-loading="loading"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column prop="orderNo" label="订单编号" width="150" align="center" sortable />
          <el-table-column prop="memberName" label="会员姓名" width="100" align="center" />
          <el-table-column prop="coachName" label="教练" width="100" align="center" />
          <el-table-column prop="serviceType" label="服务类型" width="120" align="center" />
          <el-table-column prop="validPeriod" label="有效期" width="150" align="center" />
          <el-table-column prop="amount" label="金额(元)" width="100" align="center" sortable>
            <template #default="scope">
              <span class="amount">¥{{ scope.row.amount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-tag
                :type="getStatusType(scope.row.status)"
                size="small"
              >
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="orderTime" label="下单时间" width="150" align="center" />
          <el-table-column label="操作" width="180" align="center" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button type="success" size="small" :icon="View" @click="handleView(scope.row)">查看详情</el-button>
              <el-button
                v-if="scope.row.status === '已支付'"
                type="danger"
                size="small"
                :icon="RefreshLeft"
                @click="handleRefund(scope.row)"
              >
                退款
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="pagination.currentPage"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 新增/编辑订单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="700px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="formData.orderNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会员姓名" prop="memberName">
              <el-input v-model="formData.memberName" placeholder="请输入会员姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="服务类型" prop="serviceType">
              <el-select v-model="formData.serviceType" placeholder="请选择服务类型" style="width: 100%">
                <el-option label="月卡" value="月卡" />
                <el-option label="季卡" value="季卡" />
                <el-option label="年卡" value="年卡" />
                <el-option label="私教课" value="私教课" />
                <el-option label="体验卡" value="体验卡" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教练" prop="coachName">
              <el-select v-model="formData.coachName" placeholder="请选择教练" style="width: 100%">
                <el-option
                  v-for="coach in availableCoaches"
                  :key="coach.value"
                  :label="coach.label"
                  :value="coach.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="有效期" prop="validPeriod">
              <el-input v-model="formData.validPeriod" placeholder="请输入有效期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="金额" prop="amount">
              <el-input-number
                v-model="formData.amount"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="请输入金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="待支付">待支付</el-radio>
            <el-radio value="已支付">已支付</el-radio>
            <el-radio value="已完成">已完成</el-radio>
            <el-radio value="已取消">已取消</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 订单详情对话框 -->
    <el-dialog
      title="订单详情"
      v-model="detailDialogVisible"
      width="600px"
      :before-close="handleDetailDialogClose"
    >
      <div class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单编号">{{ selectedOrder.orderNo || '' }}</el-descriptions-item>
          <el-descriptions-item label="会员姓名">{{ selectedOrder.memberName || '' }}</el-descriptions-item>
          <el-descriptions-item label="教练">{{ selectedOrder.coachName || '' }}</el-descriptions-item>
          <el-descriptions-item label="服务类型">{{ selectedOrder.serviceType || '' }}</el-descriptions-item>
          <el-descriptions-item label="有效期">{{ selectedOrder.validPeriod || '' }}</el-descriptions-item>
          <el-descriptions-item label="金额">
            <span class="amount">¥{{ selectedOrder.amount || 0 }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(selectedOrder.status || '')" size="small">
              {{ selectedOrder.status || '' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ selectedOrder.orderTime || '' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ selectedOrder.phone || '' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ selectedOrder.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDetailDialogClose">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  RefreshRight,
  Plus,
  Edit,
  View,
  Download,
  Refresh,
  RefreshLeft
} from '@element-plus/icons-vue'

// 定义组件名称用于keep-alive
defineOptions({
  name: 'Order'
})

// 定义订单接口
interface OrderItem {
  id: number
  orderNo: string
  memberName: string
  coachName: string
  serviceType: string
  validPeriod: string
  amount: number
  status: string
  orderTime: string
  phone: string
  remark: string
}

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const dialogTitle = ref('新增订单')
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  orderNo: '',
  memberName: '',
  status: '',
  orderTime: []
})

// 表格数据
const tableData = ref([
  {
    id: 1,
    orderNo: 'GYM202401150001',
    memberName: '张三',
    coachName: '李教练',
    serviceType: '月卡',
    validPeriod: '2024-01-15 至 2024-02-15',
    amount: 299.00,
    status: '已支付',
    orderTime: '2024-01-15 14:30',
    phone: '138-1234-5678',
    remark: '新会员办卡'
  },
  {
    id: 2,
    orderNo: 'GYM202401150002',
    memberName: '李四',
    coachName: '王教练',
    serviceType: '季卡',
    validPeriod: '2024-01-15 至 2024-04-15',
    amount: 799.00,
    status: '待支付',
    orderTime: '2024-01-15 15:20',
    phone: '139-2345-6789',
    remark: '续费会员'
  },
  {
    id: 3,
    orderNo: 'GYM202401150003',
    memberName: '王五',
    coachName: '赵教练',
    serviceType: '年卡',
    validPeriod: '2024-01-15 至 2025-01-15',
    amount: 2999.00,
    status: '已完成',
    orderTime: '2024-01-15 16:45',
    phone: '137-3456-7890',
    remark: '年卡优惠活动'
  },
  {
    id: 4,
    orderNo: 'GYM202401150004',
    memberName: '赵六',
    coachName: '孙教练',
    serviceType: '私教课',
    validPeriod: '2024-01-15 至 2024-03-15',
    amount: 1200.00,
    status: '已取消',
    orderTime: '2024-01-15 17:10',
    phone: '136-4567-8901',
    remark: '10节私教课程'
  },
  {
    id: 5,
    orderNo: 'GYM202401150005',
    memberName: '孙七',
    coachName: '周教练',
    serviceType: '体验卡',
    validPeriod: '2024-01-15 至 2024-01-22',
    amount: 99.00,
    status: '已退款',
    orderTime: '2024-01-15 18:30',
    phone: '135-5678-9012',
    remark: '7天体验卡'
  }
])

// 分页数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 5
})

// 表单数据
const formData = reactive({
  id: null,
  orderNo: '',
  memberName: '',
  coachName: '',
  serviceType: '',
  validPeriod: '',
  amount: 0,
  phone: '',
  remark: '',
  status: '待支付'
})

// 选中的订单
const selectedOrder = ref<Partial<OrderItem>>({})

// 可选教练列表
const availableCoaches = ref([
  { label: '李教练', value: '李教练' },
  { label: '王教练', value: '王教练' },
  { label: '赵教练', value: '赵教练' },
  { label: '孙教练', value: '孙教练' },
  { label: '周教练', value: '周教练' }
])

// 表单验证规则
const formRules = {
  memberName: [
    { required: true, message: '请输入会员姓名', trigger: 'blur' }
  ],
  coachName: [
    { required: true, message: '请选择教练', trigger: 'change' }
  ],
  serviceType: [
    { required: true, message: '请选择服务类型', trigger: 'change' }
  ],
  validPeriod: [
    { required: true, message: '请输入有效期', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入金额', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 方法定义
const getStatusType = (status: string) => {
  const statusMap: { [key: string]: string } = {
    '待支付': 'warning',
    '已支付': 'success',
    '已完成': 'success',
    '已取消': 'info',
    '已退款': 'danger'
  }
  return statusMap[status] || 'info'
}

const handleSearch = () => {
  loading.value = true
  // 模拟搜索请求
  setTimeout(() => {
    loading.value = false
    ElMessage.success('查询完成')
  }, 1000)
}

const handleReset = () => {
  Object.assign(searchForm, {
    orderNo: '',
    memberName: '',
    status: '',
    orderTime: []
  })
}

const refreshData = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('数据已刷新')
  }, 1000)
}

const handleAdd = () => {
  dialogTitle.value = '新增订单'
  resetForm()
  // 生成订单编号
  formData.orderNo = 'GYM' + new Date().toISOString().slice(0, 10).replace(/-/g, '') + String(Date.now()).slice(-4)
  dialogVisible.value = true
}

const handleEdit = (row: any) => {
  dialogTitle.value = '编辑订单'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleView = (row: any) => {
  selectedOrder.value = row
  detailDialogVisible.value = true
}


const handleRefund = (row: any) => {
  ElMessageBox.confirm(
    `确认退款订单 ${row.orderNo}，金额 ¥${row.amount}？`,
    '退款确认',
    {
      confirmButtonText: '确认退款',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // 模拟退款处理
    row.status = '已退款'
    ElMessage.success('退款成功')
  }).catch(() => {
    ElMessage.info('已取消退款')
  })
}

const handleExport = () => {
  ElMessage.success('导出功能开发中...')
}

const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handleDetailDialogClose = () => {
  detailDialogVisible.value = false
}

const handleServiceTypeChange = () => {
  // 根据服务类型筛选教练
  formData.coachName = ''
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    orderNo: '',
    memberName: '',
    coachName: '',
    serviceType: '',
    validPeriod: '',
    amount: 0,
    phone: '',
    remark: '',
    status: '待支付'
  })
  formRef.value?.clearValidate()
}

const handleSubmit = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      loading.value = true
      setTimeout(() => {
        loading.value = false
        ElMessage.success(dialogTitle.value.includes('新增') ? '新增成功' : '编辑成功')
        dialogVisible.value = false
        resetForm()
      }, 1000)
    }
  })
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  // 重新加载数据
}

const handleCurrentChange = (page: number) => {
  pagination.currentPage = page
  // 重新加载数据
}

onMounted(() => {
  // 初始化数据
})
</script>

<style lang="scss" scoped>
.order-management {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 120px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .page-title {
      margin: 0;
      color: #409eff;
      font-size: 20px;
      font-weight: 600;
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .filter-section {
    margin-bottom: 20px;

    .filter-card {
      border: none;

      :deep(.el-card__body) {
        padding: 20px;
      }
    }

    .filter-content {
      .filter-row {
        display: flex;
        align-items: center;
        gap: 20px;
        flex-wrap: wrap;

        .filter-item {
          display: flex;
          align-items: center;
          gap: 8px;

          label {
            white-space: nowrap;
            color: #606266;
            font-size: 14px;
          }
        }

        .filter-actions {
          display: flex;
          gap: 10px;
          margin-left: auto;
        }
      }
    }
  }

  .table-section {
    .table-card {
      border: none;

      :deep(.el-card__body) {
        padding: 0;
      }
    }

    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      border-bottom: 1px solid #ebeef5;

      .table-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    :deep(.el-table) {
      .el-table__header {
        th {
          background-color: #fafafa;
          color: #606266;
          font-weight: 600;
        }
      }
    }

    .pagination-wrapper {
      display: flex;
      justify-content: flex-end;
      padding: 20px;
      border-top: 1px solid #ebeef5;
    }
  }

  .amount {
    color: #f56c6c;
    font-weight: 600;
  }
}

.order-detail {
  .amount {
    color: #f56c6c;
    font-weight: 600;
  }
}

:deep(.el-dialog__header) {
  padding: 20px 20px 10px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

:deep(.el-dialog__footer) {
  padding: 10px 20px 20px;
  border-top: 1px solid #ebeef5;
}
</style>