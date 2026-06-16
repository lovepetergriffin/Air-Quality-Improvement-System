<template>
  <section>
    <el-breadcrumb><el-breadcrumb-item>统计数据管理</el-breadcrumb-item><el-breadcrumb-item>省分组检查统计</el-breadcrumb-item></el-breadcrumb>
    <el-alert title="省分组分项检查统计" type="success" effect="dark" :closable="false" style="margin:16px 0" />
    <el-table :data="items" border stripe>
      <el-table-column prop="provinceName" label="省份" />
      <el-table-column prop="cityName" label="城市" />
      <el-table-column prop="statCount" label="记录数" />
      <el-table-column prop="avgSo2Value" label="SO2平均值" />
      <el-table-column prop="avgCoValue" label="CO平均值" />
      <el-table-column prop="avgSpmValue" label="PM2.5平均值" />
      <el-table-column prop="maxAqiLevel" label="最高AQI等级" />
    </el-table>
  </section>
</template>

<script setup>
import { inject, ref } from 'vue';
const axios = inject('axios');
const items = ref([]);
axios.get('statistics/listProvinceItemTotalStatis').then(res => items.value = res.data.data || []);
</script>
