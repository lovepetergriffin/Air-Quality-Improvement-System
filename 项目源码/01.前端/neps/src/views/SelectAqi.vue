<template>
  <div class="wrapper">
    <div class="header-box">
      <header><span @click="router.back()">‹</span><p>选择空气质量</p><span @click="router.push('/feedbackList')">☰</span></header>
      <div class="top-ban"></div><img src="../assets/header.png" class="header-img" alt="">
    </div>
    <table border="1">
      <tr><td>AQI</td><td>等级</td><td>健康影响</td></tr>
      <tr v-for="aqi in levels" :key="aqi.aqiId">
        <td :style="{backgroundColor: aqi.color, color: '#fff'}">{{ aqi.chineseExplain }}</td>
        <td>{{ aqi.aqiExplain }}</td>
        <td>{{ aqi.healthImpact }}</td>
      </tr>
    </table>
    <section class="panel">
      <h3>请选择空气质量等级</h3>
      <div class="levels">
        <label v-for="aqi in levels" :key="aqi.aqiId" :style="{borderColor: aqi.color}">
          <input type="radio" v-model.number="aqiId" :value="aqi.aqiId">
          <b :style="{backgroundColor: aqi.color}">{{ aqi.chineseExplain }}</b>
          <span>{{ aqi.aqiExplain }}</span>
        </label>
      </div>
      <label class="field"><span>反馈信息</span><textarea v-model.trim="information" placeholder="请输入污染情况描述"></textarea></label>
      <button class="btn" @click="submit">提 交</button>
    </section>
  </div>
</template>

<script setup>
import { inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getSessionStorage } from '../common.js';

const route = useRoute();
const router = useRouter();
const axios = inject('axios');
const supervisor = getSessionStorage('supervisor');
const levels = ref([]);
const aqiId = ref(0);
const information = ref('');

axios.get('common/aqi-list').then(res => levels.value = res.data.data || []);

const submit = () => {
  if (!aqiId.value) return alert('请选择AQI等级');
  if (!information.value) return alert('反馈信息不能为空');
  axios.post('supervisor/submitFeedback', {
    telId: supervisor.telId,
    provinceId: Number(route.query.provinceId),
    cityId: Number(route.query.cityId),
    address: route.query.address,
    aqiId: aqiId.value,
    information: information.value
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
table { width: 90%; margin: 5vw auto 0; border-collapse: collapse; border-color: #d6e5e8; background: #fff; font-size: 3.4vw; }
td { padding: 1.5vw; }
h3 { margin-top: 0; color: #2d727e; }
.levels { display: grid; grid-template-columns: repeat(2, 1fr); gap: 2.6vw; margin-bottom: 4vw; }
.levels label { min-height: 12vw; display: flex; align-items: center; gap: 2vw; padding: 2vw; border: 1px solid; border-radius: 2vw; }
.levels b { display: inline-flex; min-width: 8vw; height: 8vw; align-items: center; justify-content: center; color: #fff; border-radius: 1.2vw; }
.levels span { font-size: 3.4vw; }
@media (min-width: 520px) { table { font-size: 14px; } .levels label { min-height: 52px; border-radius: 8px; } .levels b { min-width: 34px; height: 34px; } .levels span { font-size: 14px; } }
</style>
