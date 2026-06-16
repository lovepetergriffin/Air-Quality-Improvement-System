<template>
  <section>
    <el-breadcrumb><el-breadcrumb-item>统计数据管理</el-breadcrumb-item><el-breadcrumb-item>AQI指数趋势统计</el-breadcrumb-item></el-breadcrumb>
    <div class="toolbar"><el-radio-group v-model="type" @change="load"><el-radio-button label="day">按日</el-radio-button><el-radio-button label="month">按月</el-radio-button></el-radio-group></div>
    <div ref="chartEl" class="chart-box"></div>
    <el-table :data="items" border stripe>
      <el-table-column prop="date" label="日期" />
      <el-table-column prop="avgAqiLevel" label="平均AQI等级" />
      <el-table-column prop="maxAqiLevel" label="最高AQI等级" />
      <el-table-column prop="recordCount" label="记录数" />
    </el-table>
  </section>
</template>

<script setup>
import * as echarts from 'echarts';
import { inject, nextTick, ref } from 'vue';
const axios = inject('axios');
const type = ref('day');
const items = ref([]);
const chartEl = ref(null);
let chart;
const load = () => axios.get('statistics/listAqiTrendTotalStatis?type=' + type.value).then(res => {
  items.value = res.data.data || [];
  nextTick(() => {
    chart = chart || echarts.init(chartEl.value);
    chart.setOption({
      tooltip: {},
      xAxis: { type: 'category', data: items.value.map(x => x.date) },
      yAxis: { type: 'value' },
      series: [{ name: '平均AQI等级', type: 'line', smooth: true, data: items.value.map(x => x.avgAqiLevel) }]
    });
  });
});
load();
</script>
