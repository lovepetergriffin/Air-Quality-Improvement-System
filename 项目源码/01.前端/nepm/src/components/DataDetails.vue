<template>
  <el-descriptions title="公众监督数据详情" :column="1" border style="margin-top: 16px">
    <template #extra><el-button type="primary" @click="router.go(-1)">返回</el-button></template>
    <el-descriptions-item label="反馈编号">{{ item.afId }}</el-descriptions-item>
    <el-descriptions-item label="监督员">{{ item.supervisor?.realName }} {{ item.telId }}</el-descriptions-item>
    <el-descriptions-item label="反馈地址">
      <el-tag>{{ item.gridProvince?.provinceName }}</el-tag>
      <el-tag style="margin-left: 6px">{{ item.gridCity?.cityName }}</el-tag>
      <span style="margin-left: 8px">{{ item.address }}</span>
    </el-descriptions-item>
    <el-descriptions-item label="预估AQI">
      <el-tag :color="item.aqi?.color" effect="dark">{{ item.aqi?.chineseExplain }}级 {{ item.aqi?.aqiExplain }}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="反馈时间">{{ item.feedbackTime }}</el-descriptions-item>
    <el-descriptions-item label="指派网格员">{{ item.gridMember?.gmName || '未指派' }}</el-descriptions-item>
    <el-descriptions-item label="反馈状态">{{ stateText[item.state] }}</el-descriptions-item>
    <el-descriptions-item label="反馈描述">{{ item.information }}</el-descriptions-item>
  </el-descriptions>
</template>

<script setup>
import { inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const axios = inject('axios');
const route = useRoute();
const router = useRouter();
const item = ref({ supervisor: {}, gridProvince: {}, gridCity: {}, aqi: {}, gridMember: {} });
const stateText = ['未指派', '已指派', '已确认'];

axios.post('aqiFeedback/getAqiFeedbackById', { afId: route.query.afId }).then(res => item.value = res.data.data || item.value);
</script>
