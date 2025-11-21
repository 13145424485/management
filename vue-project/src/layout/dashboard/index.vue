<template>
  <el-main :style="{ height: mianHeight + 'px' }">
    <!-- 数据统计 -->
    <el-row
        :gutter="20"
        type="flex"
        class="row-bg"
        justify="center"
        style="margin-bottom: 80px"
    >
      <el-col :span="6">
        <div class="show-header" style="background: rgb(45, 183, 245)">
          <div class="show-num">{{ total.memberCount }}</div>
          <div class="bottom-text">会员总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="show-header" style="background: rgb(237, 64, 20)">
          <div class="show-num">{{ total.userCount }}</div>
          <div class="bottom-text">员工总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="show-header">
          <div class="show-num">{{ total.materCount }}</div>
          <div class="bottom-text">器材总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="show-header" style="background: rgb(255, 153, 0)">
          <div class="show-num">{{ total.orderCount }}</div>
          <div class="bottom-text">昨日订单</div>
        </div>
      </el-col>
    </el-row>
    <div style="display: flex">
      <el-card style="flex: 1">
        <template #header>
          <div class="card-header">
            <span>热销商品</span>
          </div>
        </template>
        <div ref="myChart" :style="{ width: '400px', height: '250px' }"></div>
      </el-card>
      <el-card style="margin-left: 20px; flex: 1">
        <template #header>
          <div class="card-header">
            <span>热销卡</span>
          </div>
        </template>
        <div ref="myChart1" :style="{ width: '400px', height: '250px' }"></div>
      </el-card>
      <el-card style="margin-left: 20px; flex: 1">
        <template #header>
          <div class="card-header">
            <span>热销课程</span>
          </div>
        </template>
        <div ref="myChart2" :style="{ width: '400px', height: '250px' }"></div>
      </el-card>
    </div>
    <el-card class="box-card" style="margin-top: 30px">
      <div slot="header" class="clearfix">
        <span style="color: #000000; font-weight: 600; margin-bottom: 10px"
        >反馈列表</span
        >
        <el-divider></el-divider>
      </div>
      <div v-for="item in suggestList" class="text item">
        <span style="font-weight: 600; font-size: 14px">{{ item.title }}</span
        ><span style="margin-left: 30px; font-size: 14px">{{
          item.content
        }}</span>
        <span style="margin-left: 30px; font-size: 14px">{{
            item.dateTime
          }}</span>
        <el-divider></el-divider>
      </div>
    </el-card>
  </el-main>
</template>

<script setup lang="ts">
import { getTotalApi, getSuggestListApi,getHotCardsApi,getHotCourseApi,getHotGoodsApi } from "@/api/home";
import { onMounted, reactive, nextTick, ref } from "vue";
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
const myChart = ref<HTMLElement>();
const myChart1 = ref<HTMLElement>();
const myChart2 = ref<HTMLElement>();
const mianHeight = ref(0);
//柱状图
const charts1 = async() => {
  const echartInstance = global.$echarts.init(myChart.value);
  let option = reactive({
    xAxis: {
      type: "category",
      data: [],
      axisLabel: {
        //x轴文字的配置
        show: true,
        interval: 0, //使x轴文字显示全
      },
    },
    yAxis: {
      type: "value",
    },
    series: [
      {
        data: [],
        type: "bar",
      },
    ],
  });
  //获取数据
  const res = await getHotGoodsApi()
  if(res && res.code == 200){
    console.log(res.data)
    option.xAxis.data = res.data.names
    option.series[0].data = res.data.values
  }
  echartInstance.setOption(option);
};
//饼图
const charts2 = async() => {
  const myChart = global.$echarts.init(myChart1.value);
  let option = reactive({
    title: {
      left: "center",
    },
    tooltip: {
      trigger: "item",
    },
    legend: {
      orient: "vertical",
      left: "left",
    },
    series: [
      {
        name: "Access From",
        type: "pie",
        radius: "50%",
        data: [],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)",
          },
        },
      },
    ],
  });
  //获取数据
  const res = await getHotCardsApi()
  if(res && res.code == 200){
    console.log(res)
    option.series[0].data = res.data;
  }
  myChart.setOption(option);
};
//环图
const charts3 = async() => {
  const myChart = global.$echarts.init(myChart2.value);
  let option = reactive({
    tooltip: {
      trigger: "item",
    },
    legend: {
      top: "5%",
      left: "center",
    },
    series: [
      {
        name: "Access From",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: "center",
        },
        emphasis: {
          label: {
            show: true,
            fontSize: "40",
            fontWeight: "bold",
          },
        },
        labelLine: {
          show: false,
        },
        data: [],
      },
    ],
  });
  const res = await getHotCourseApi()
  if(res && res.code == 200){
    option.series[0].data = res.data;
  }
  myChart.setOption(option);
};
//总数
const total = reactive({
  memberCount: 0,
  userCount: 0,
  materCount: 0,
  orderCount: 0,
});

//获取总数
const getTotal = async () => {
  let res = await getTotalApi();
  if (res && res.code == 200) {
    total.memberCount = res.data.memberCount;
    total.userCount = res.data.userCount;
    total.materCount = res.data.materCount;
    total.orderCount = res.data.orderCount;
  }
};
type Suggest = {
  id: string;
  title: string;
  content: string;
  dateTime: string;
};
const suggestList = ref<Suggest[]>([]);
//反馈列表
const suggest = async () => {
  let res = await getSuggestListApi();
  if (res && res.code == 200) {
    suggestList.value = res.data;
  }
};
onMounted(() => {
  getTotal();
  suggest();
  charts1();
  charts2();
  charts3();
  nextTick(() => {
    mianHeight.value = window.innerHeight - 90;
  });
});
</script>

<style lang="scss" scoped>
.bottom-text {
  bottom: 0;
  width: 100%;
  background: rgba(0, 0, 0, 0.1);
  height: 25px;
  line-height: 25px;
  text-align: center;
  position: absolute;
  font-weight: 600;
}
.show-header {
  background: #00c0ef;
  color: #fff;
  height: 80px;
  border-radius: 5px;
  position: relative;
}
.show-num {
  font-size: 38px;
  font-weight: 600;
  padding: 5px;
}
</style>