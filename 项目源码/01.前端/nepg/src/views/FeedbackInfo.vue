<template>
  <div class="wrapper">
    <div class="header-box">
      <header><span @click="router.back()">‹</span><p>反馈任务详细信息</p><span @click="router.push('/feedbackList')">☰</span></header>
      <div class="top-ban"></div><img src="../assets/header.png" class="header-img" alt="">
    </div>
    <table class="info">
      <tr><td>监督员</td><td>{{ feedback.supervisor?.realName || feedback.telId }}</td></tr>
      <tr><td>省份</td><td>{{ feedback.gridProvince?.provinceName }}</td></tr>
      <tr><td>城市</td><td>{{ feedback.gridCity?.cityName }}</td></tr>
      <tr><td>地址</td><td>{{ feedback.address }}</td></tr>
      <tr><td>反馈信息</td><td>{{ feedback.information }}</td></tr>
      <tr><td>预估AQI</td><td :style="{backgroundColor: feedback.aqi?.color || '#ccc', color:'#fff'}">{{ feedback.aqi?.chineseExplain }}级 {{ feedback.aqi?.aqiExplain }}</td></tr>
    </table>
    <section class="actual">
      <h4>实测数据录入</h4>
      <table>
        <tr><td>SO2浓度</td><td><input v-model="so2Value" placeholder="请输入SO2浓度"></td><td><p :style="{backgroundColor: so2Level.color}">{{ so2Level.chineseExplain }}</p></td></tr>
        <tr><td>CO浓度</td><td><input v-model="coValue" placeholder="请输入CO浓度"></td><td><p :style="{backgroundColor: coLevel.color}">{{ coLevel.chineseExplain }}</p></td></tr>
        <tr><td>PM2.5浓度</td><td><input v-model="spmValue" placeholder="请输入PM2.5浓度"></td><td><p :style="{backgroundColor: spmLevel.color}">{{ spmLevel.chineseExplain }}</p></td></tr>
      </table>
      <h3 :style="{backgroundColor: aqiObj.color}">综合AQI等级：{{ aqiObj.chineseExplain || '-' }}</h3>
      <button class="btn" @click="submitData">提交数据</button>
    </section>
    <footer><img src="../assets/bottom.png" alt=""></footer>
  </div>
</template>

<script setup>
import { computed, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getSessionStorage } from '../common.js';

const route = useRoute();
const router = useRouter();
const axios = inject('axios');
const gridMember = getSessionStorage('gridMember');
const feedback = ref({ supervisor: {}, gridProvince: {}, gridCity: {}, aqi: {} });
const aqiArr = ref([]);
const so2Value = ref('');
const coValue = ref('');
const spmValue = ref('');
const emptyLevel = { aqiId: 0, chineseExplain: '', color: '#cccccc' };

const findLevel = (value, minKey, maxKey) => {
  const val = Number(value);
  if (value === '' || Number.isNaN(val)) return emptyLevel;
  return aqiArr.value.find(item => val >= item[minKey] && val <= item[maxKey]) || aqiArr.value[aqiArr.value.length - 1] || emptyLevel;
};

const so2Level = computed(() => findLevel(so2Value.value, 'so2Min', 'so2Max'));
const coLevel = computed(() => findLevel(coValue.value, 'coMin', 'coMax'));
const spmLevel = computed(() => findLevel(spmValue.value, 'spmMin', 'spmMax'));
const aqiObj = computed(() => {
  const max = Math.max(so2Level.value.aqiId || 0, coLevel.value.aqiId || 0, spmLevel.value.aqiId || 0);
  return aqiArr.value.find(item => item.aqiId === max) || emptyLevel;
});

axios.get('aqiFeedback/detail/' + route.query.afId).then(res => feedback.value = res.data.data || feedback.value);
axios.get('common/aqi-list').then(res => aqiArr.value = res.data.data || []);

const submitData = () => {
  const reg = /^[0-9]+(\.[0-9]+)?$/;
  if (!so2Value.value || !coValue.value || !spmValue.value) return alert('数据值不能为空');
  if (!reg.test(so2Value.value) || !reg.test(coValue.value) || !reg.test(spmValue.value)) return alert('必须为数字');
  if (!confirm('确认提交实测数据？')) return;
  axios.post('statistics/save', {
    afId: feedback.value.afId,
    gmId: gridMember.gmId,
    so2Value: Number(so2Value.value),
    coValue: Number(coValue.value),
    spmValue: Number(spmValue.value)
  }).then(res => {
    if (res.data.code === 200) {
      alert('提交成功');
      router.push('/feedbackList');
    } else {
      alert(res.data.message || res.data.msg);
    }
  });
};
</script>

<style scoped>
.info { width: 90%; margin: 3vw auto; border-collapse: collapse; background: #fff; color: #526772; }
.info td { border: 1px solid #d8e8ee; padding: 1.8vw; font-size: 3.6vw; }
.info td:first-child { width: 24%; background: #f5f7fa; font-weight: 700; }
.actual { width: 90%; margin: 4vw auto; color: #526772; }
.actual h4 { margin: 0 0 2vw; }
.actual table { width: 100%; border-collapse: collapse; font-size: 3.5vw; }
.actual td { padding: 1.4vw 0; }
.actual input { width: 100%; height: 8vw; padding: 0 3vw; border: 1px solid #aab8bd; border-radius: 4vw; outline: none; }
.actual p { width: 8vw; height: 8vw; margin: 0 0 0 2vw; display: flex; align-items: center; justify-content: center; border-radius: 1.3vw; color: #fff; font-weight: 700; }
h3 { height: 9vw; margin: 4vw 0; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 4vw; }
.btn { width: 42vw; display: block; margin: 0 auto; }
@media (min-width: 520px) { .info td, .actual table { font-size: 14px; } .actual input { height: 34px; border-radius: 17px; } .actual p { width: 34px; height: 34px; } h3 { height: 38px; font-size: 16px; } .btn { width: 180px; } }
</style>
