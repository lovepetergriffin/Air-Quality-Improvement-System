<template>
  <div class="wrapper">
    <div class="header-box">
      <header><span @click="router.back()">‹</span><p>历史反馈</p><span @click="router.push('/selectGrid')">＋</span></header>
      <div class="top-ban"></div><img src="../assets/header.png" class="header-img" alt="">
    </div>
    <ul>
      <li v-for="item in items" :key="item.afId">
        <div class="main"><i :style="{backgroundColor: item.aqi?.color || '#70b0bc'}">{{ item.aqi?.chineseExplain || '-' }}</i><p>{{ item.feedbackTime }}</p></div>
        <div>{{ item.gridProvince?.provinceName || item.provinceId }}</div>
        <div>{{ item.gridCity?.cityName || item.cityId }}</div>
        <div>{{ stateText[item.state] }}</div>
      </li>
    </ul>
    <p v-if="items.length === 0" class="empty">暂无反馈记录</p>
  </div>
</template>

<script setup>
import { inject, ref } from 'vue';
import { useRouter } from 'vue-router';
import { getSessionStorage } from '../common.js';

const router = useRouter();
const axios = inject('axios');
const supervisor = getSessionStorage('supervisor');
const items = ref([]);
const stateText = ['未指派', '已指派', '已确认'];

axios.get('supervisor/feedbackList?telId=' + supervisor.telId).then(res => items.value = res.data.data || []);
</script>

<style scoped>
ul { width: 100%; margin: 0; padding: 0; list-style: none; background: #fff; }
li { width: 100%; min-height: 16vw; display: grid; grid-template-columns: 2fr 1fr 1fr 1fr; align-items: center; gap: 2vw; padding: 2vw; border-bottom: 1px solid #d7edf0; font-size: 3.5vw; color: #526772; }
.main { display: flex; align-items: center; gap: 2vw; }
.main i { min-width: 8vw; height: 8vw; display: inline-flex; align-items: center; justify-content: center; color: #fff; border-radius: 1vw; font-style: normal; font-weight: 700; }
.main p { margin: 0; }
.empty { text-align: center; color: #799199; font-size: 4vw; }
@media (min-width: 520px) { li { min-height: 64px; font-size: 14px; } .main i { min-width: 34px; height: 34px; } }
</style>
