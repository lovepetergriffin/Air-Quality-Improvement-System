<template>
  <section>
    <el-breadcrumb><el-breadcrumb-item>统计数据管理</el-breadcrumb-item><el-breadcrumb-item>AQI指数分布统计</el-breadcrumb-item></el-breadcrumb>
    <el-alert title="AQI空气质量指数级别分布统计" type="success" effect="dark" :closable="false" style="margin:16px 0" />
    <div ref="chartEl" class="chart-box"></div>
    <el-table :data="items" border stripe>
      <el-table-column prop="chineseExplain" label="等级" />
      <el-table-column prop="count" label="数量" />
      <el-table-column prop="percentage" label="占比%" />
    </el-table>
  </section>
</template>

<script setup>
import * as echarts from 'echarts';
import { inject, nextTick, ref } from 'vue';
const axios = inject('axios');
const items = ref([]);
const chartEl = ref(null);
axios.get('statistics/listAqiDistributeTotalStatis').then(res => {
  items.value = res.data.data || [];
  nextTick(() => {
    echarts.init(chartEl.value).setOption({
      tooltip: {},
      xAxis: { type: 'category', data: items.value.map(x => x.chineseExplain + '级') },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: items.value.map(x => ({ value: x.count, itemStyle: { color: x.color } })) }]
    });
  });
});
</script>
