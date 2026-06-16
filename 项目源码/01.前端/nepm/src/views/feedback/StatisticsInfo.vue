<template>
  <el-breadcrumb><el-breadcrumb-item>确认AQI数据管理</el-breadcrumb-item><el-breadcrumb-item>确认AQI数据详情</el-breadcrumb-item></el-breadcrumb>
  <el-descriptions title="确认AQI数据详情" :column="1" border style="margin-top:16px">
    <template #extra><el-button type="primary" @click="router.go(-1)">返回</el-button></template>
    <el-descriptions-item label="确认编号">{{ item.id }}</el-descriptions-item>
    <el-descriptions-item label="确认地址">{{ item.gridProvince?.provinceName }} {{ item.gridCity?.cityName }} {{ item.address }}</el-descriptions-item>
    <el-descriptions-item label="污染物数据">SO2 {{ item.so2Value }} / CO {{ item.coValue }} / PM2.5 {{ item.spmValue }}</el-descriptions-item>
    <el-descriptions-item label="确认AQI"><el-tag :color="item.aqi?.color" effect="dark">{{ item.aqi?.chineseExplain }}级 {{ item.aqi?.aqiExplain }}</el-tag></el-descriptions-item>
    <el-descriptions-item label="确认时间">{{ item.confirmDate }} {{ item.confirmTime }}</el-descriptions-item>
    <el-descriptions-item label="网格员">{{ item.gridMember?.gmName }} {{ item.gridMember?.tel }}</el-descriptions-item>
    <el-descriptions-item label="公众监督员">{{ item.supervisor?.realName }} {{ item.supervisor?.telId }}</el-descriptions-item>
    <el-descriptions-item label="反馈描述">{{ item.information }}</el-descriptions-item>
  </el-descriptions>
</template>

<script setup>
import { inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const axios = inject('axios');
const route = useRoute();
const router = useRouter();
const item = ref({ gridProvince: {}, gridCity: {}, aqi: {}, gridMember: {}, supervisor: {} });
axios.post('statistics/getStatisticsById', { id: route.query.id }).then(res => item.value = res.data.data || item.value);
</script>
