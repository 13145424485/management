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
        <el-button v-permission="['sys:orderList:down']" :icon="Plus" type="primary" @click="order">下单</el-button>
      </el-form-item>
    </el-form>
    <el-table :height="orderTableHeight" :data="tableData.list" border stripe>
      <el-table-column prop="image" width="90" label="商品图片" align="center">
        <template #default="scope">
          <el-image
              style="width: 60px; height: 60px; border-radius: 50%"
              :src="scope.row.image"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称"></el-table-column>
      <el-table-column prop="price" label="单价"></el-table-column>
      <el-table-column prop="num" label="数量"></el-table-column>
      <el-table-column prop="totalPrice" label="价格"></el-table-column>
      <el-table-column prop="controlUser" label="操作人"></el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
        @size-change="orderSizeChange"
        @current-change="orderCurrentChange"
        :current-page.sync="listParam.currentPage"
        :page-sizes="[20, 40, 80, 100]"
        :page-size="listParam.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="listParam.total" background>
    </el-pagination>

    <!-- 下单 -->
    <el-drawer
        :modal="false"
        custom-class="faDrawer"
        :append-to-body="true"
        :modal-append-to-body="false"
        size="50%"
        title="购物车"
        :close-on-click-modal="false"
        v-model="faDrawer"
        direction="rtl"
    >
      <template #title>
        <div class="car-title">
          <el-icon size="18px"><ShoppingCartFull /></el-icon>
          <span>购物车</span>
        </div>
        <el-button size="small" @click="innerDrawerShow">选择商品</el-button>
        <el-drawer
            custom-class="childDrawer"
            size="50%"
            v-model="innerDrawer"
            title="商品列表"
            :append-to-body="true"
            :before-close="innerCloseClick"
        >
          <el-form
              style="margin-top: 10px"
              :model="goodsParam"
              :inline="true"
              size="default"
          >
            <el-form-item>
              <el-input
                  v-model="goodsParam.name"
                  placeholder="请输入商品名称"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button :icon="Search" @click="searchGoodsBtn">搜索</el-button>
              <el-button
                  type="danger"
                  plain
                  :icon="Close"
                  @click="resetGoodsBtn"
              >重置</el-button
              >
            </el-form-item>
          </el-form>
          <el-table :height="goodsHeight" :data="tableList.list" border stripe>
            <el-table-column
                prop="image"
                width="90"
                label="商品图片"
                align="center"
            >
              <template #default="scope">
                <el-image
                    style="width: 40px; height: 40px; border-radius: 50%"
                    :src="scope.row.image"
                ></el-image>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="商品名称"></el-table-column>
            <el-table-column prop="price" label="商品价格"></el-table-column>
            <el-table-column prop="store" label="商品库存"></el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="scope">
                <el-button
                    @click="addCarBtn(scope.row)"
                    type="primary"
                    :icon="Plus"
                    size="default"
                >加入</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              @size-change="goodsSizeChange"
              @current-change="goodsCurrentChange"
              :current-page.sync="goodsParam.currentPage"
              :page-sizes="[20, 40, 80, 100]"
              :page-size="goodsParam.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="goodsParam.total"
              background
          >
          </el-pagination>
        </el-drawer>
      </template>
      <template #default>
        <el-table :data="selectGoods.list" border stripe>
          <el-table-column prop="name" label="商品名称"></el-table-column>
          <el-table-column prop="price" label="单价"></el-table-column>
          <el-table-column prop="num" label="数量" width="180">
            <template #default="scope">
              <el-input-number
                  @change="change"
                  size="default"
                  v-model="scope.row.num"
                  :min="1"
                  label="数量"
              ></el-input-number>
            </template>
          </el-table-column>
          <el-table-column label="金额">
            <template #default="scope">
              {{ (scope.row.price * scope.row.num).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template #default="scope">
              <el-button
                  @click="deleteCarBtn(scope.row)"
                  type="danger"
                  :icon="Delete"
                  size="default"
              >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </template>
      <template #footer>
        <div
            style="
            display: flex;
            align-items: center;
            justify-content: space-between;
          "
        >
          <div>
            <span
                style="
                font-size: 17px;
                font-weight: 600;
                padding-left: 50px;
                color: #009688;
              "
            >合计:</span
            >{{ totalPrice }}
          </div>
          <div style="flex: auto">
            <el-button type="danger" plain @click="cancelClick">取消</el-button>
            <el-button type="primary" @click="commit">确定</el-button>
          </div>
        </div>
      </template>
    </el-drawer>
  </el-main>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import useOrderTable from "@/composables/order/useOrderTable";
import useFaDrawer from "@/composables/order/useFaDrawer";
import useChildDrawer from "@/composables/order/useChildDrawer";
import { ElMessage } from "element-plus";
import { userStore } from "@/store/user";
import { downApi } from "@/api/order";
const store = userStore();
const { listParam, searchBtn, resetBtn, tableData, getList,orderSizeChange,orderCurrentChange,orderTableHeight } = useOrderTable();
const {
  innerCloseClick,
  innerDrawer,
  addCarBtn,
  goodsParam,
  tableList,
  innerDrawerShow,
  goodsSizeChange,
  goodsCurrentChange,
  searchGoodsBtn,
  resetGoodsBtn,
  goodsHeight,
  selectGoods,
} = useChildDrawer();
const { faDrawer, cancelClick, confirmClick, order, deleteCarBtn } =
    useFaDrawer(selectGoods);
//总价格
const totalPrice = ref<string | number>(0);
//购物车数量点击事件
const change = () => {
  if (selectGoods.list.length > 0) {
    let sumPrice = selectGoods.list
        .map((row) => (row.num as number) * row.price)
        .reduce((total, num) => total + num, 0)
        .toFixed(2);
    totalPrice.value = sumPrice;
  } else {
    totalPrice.value = 0;
  }
};
//监听表格数据的变化
watch(
    () => [...selectGoods.list],
    () => {
      change();
    }
);
//提交
const commit = async () => {
  if (selectGoods.list.length == 0) {
    ElMessage.error("请选择商品!");
    return;
  }
  //组装数据
  let list = selectGoods.list.map((item) => {
    return { goodsId: item.goodsId, num: item.num };
  });
  let parm = {
    userId: store.getUserId,
    orderList: list,
  };
  let res = await downApi(parm);
  if (res && res.code == 200) {
    getList()
    ElMessage.success(res.msg);
    faDrawer.value = false;
  }
};
</script>

<style lang="scss">
.faDrawer {
  .el-drawer__header {
    font-size: 16px;
    padding: 12.5px;
    margin-bottom: 0px !important;
    background-color: #009688 !important;
    .el-drawer__close {
      color: #fff !important;
    }
  }
  .car-title {
    color: #fff;
    font-size: 16px;
    display: flex;
    align-items: center;
  }
  .el-drawer__footer {
    border-top: 1px solid #e8eaec !important;
    padding: 10px;
  }
}
.childDrawer {
  .el-drawer__header {
    color: #fff;
    font-size: 16px;
    padding: 12.5px;
    margin-bottom: 0px !important;
    background-color: #009688 !important;
    .el-drawer__close {
      color: #fff !important;
    }
  }
  .el-drawer__body {
    padding: 5px;
  }
}
</style>  