<template>
  <div class="wrapper">
    <div class="header-box">
      <header><span></span><p>反馈任务列表</p><span @click="logout">↪</span></header>
      <div class="top-ban"></div><img src="../assets/header.png" class="header-img" alt="">
    </div>
    <ul>
      <li v-for="item in tasks" :key="item.afId">
        <div class="left">
          <i :style="{backgroundColor: item.aqi?.color || '#fe0000'}">{{ item.aqi?.chineseExplain || '-' }}</i>
          <div><b>{{ item.gridProvince?.provinceName }} {{ item.gridCity?.cityName }}</b><p>{{ item.address }}</p></div>
        </div>
        <button @click="router.push({ path: '/feedbackInfo', query: { afId: item.afId } })">去检测</button>
      </li>
    </ul>
    <p v-if="tasks.length === 0" class="empty">No Data</p>
    <footer><img src="../assets/bottom.png" alt=""></footer>
  </div>
</template>

<script setup>
import { inject, ref } from 'vue';
import { useRouter } from 'vue-router';
import { getSessionStorage, removeSessionStorage } from '../common.js';

const router = useRouter();
const axios = inject('axios');
const gridMember = getSessionStorage('gridMember');
const tasks = ref([]);

const load = () => axios.get('aqiFeedback/listAssignedTasks', { params: { gmId: gridMember.gmId } }).then(res => tasks.value = res.data.data || []);
const logout = () => { removeSessionStorage('gridMember'); router.push('/login'); };
load();
</script>

<style scoped>
ul { margin: 0; padding: 0; list-style: none; background: #fff; }
li { min-height: 18vw; padding: 3vw; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px dashed #b9cad2; }
.left { display: flex; align-items: center; min-width: 0; }
i { flex: 0 0 8vw; height: 8vw; margin-right: 3vw; display: inline-flex; align-items: center; justify-content: center; color: #fff; border-radius: 1vw; font-style: normal; font-weight: 700; }
b { display: block; color: #3a4f5a; font-size: 4vw; }
p { margin: 1vw 0 0; color: #667984; font-size: 3.3vw; }
button { flex: 0 0 auto; padding: 1.5vw 2.5vw; border: 1px solid #c6e2ff; border-radius: 1vw; background: #ecf5ff; color: #409eff; }
.empty { text-align: center; color: #98aab1; font-size: 5vw; }
@media (min-width: 520px) { li { min-height: 72px; padding: 14px; } i { flex-basis: 34px; height: 34px; margin-right: 14px; } b { font-size: 16px; } p { font-size: 14px; } button { padding: 7px 12px; } }
</style>
