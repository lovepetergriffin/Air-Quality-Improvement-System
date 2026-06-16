<template>
  <section>
    <el-breadcrumb><el-breadcrumb-item>统计数据管理</el-breadcrumb-item><el-breadcrumb-item>其他数据统计</el-breadcrumb-item></el-breadcrumb>
    <el-descriptions title="其他数据统计" :column="1" border style="margin-top:16px;width:520px">
      <el-descriptions-item label="空气质量检测总数量">{{ data.aqiCount }}</el-descriptions-item>
      <el-descriptions-item label="空气质量检测良好数量">{{ data.aqiGoodCount }}</el-descriptions-item>
      <el-descriptions-item label="省网格覆盖率">{{ data.provinceCoverage }}%</el-descriptions-item>
      <el-descriptions-item label="大城市网格覆盖率">{{ data.cityCoverage }}%</el-descriptions-item>
    </el-descriptions>
  </section>
</template>

<script setup>
import { inject, reactive } from 'vue';
const axios = inject('axios');
const data = reactive({ aqiCount: 0, aqiGoodCount: 0, provinceCoverage: 0, cityCoverage: 0 });
axios.get('statistics/getAqiCount').then(res => data.aqiCount = res.data.data || 0);
axios.get('statistics/getAqiGoodCount').then(res => data.aqiGoodCount = res.data.data || 0);
axios.get('statistics/getProvinceCoverage').then(res => data.provinceCoverage = res.data.data || 0);
axios.get('statistics/getCityCoverage').then(res => data.cityCoverage = res.data.data || 0);
</script>
