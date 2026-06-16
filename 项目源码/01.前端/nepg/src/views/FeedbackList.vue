<template>
  <div class="wrapper">
    <div class="header-box">
      <header><span></span><p>反馈任务列表</p><span @click="logout">↪</span></header>
      <div class="top-ban"></div><img src="../assets/header.png" class="header-img" alt="">
    </div>
    <section v-if="showNotice" class="notice">
      <div>
        <b>新任务提醒</b>
        <p>你有 {{ newTasks.length }} 条新的检测任务，请及时处理。</p>
      </div>
      <button @click="openFirstTask">查看</button>
      <button class="plain" @click="showNotice = false">知道了</button>
    </section>
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
const newTasks = ref([]);
const showNotice = ref(false);

const storageKey = `nepg_seen_tasks_${gridMember?.gmId || 'guest'}`;
const readSeen = () => {
  try {
    const seen = JSON.parse(localStorage.getItem(storageKey) || '[]');
    return Array.isArray(seen) ? seen : [];
  } catch (e) {
    return [];
  }
};
const writeSeen = ids => localStorage.setItem(storageKey, JSON.stringify([...new Set(ids)]));
const load = () => axios.get('aqiFeedback/listAssignedTasks', { params: { gmId: gridMember.gmId } }).then(res => {
  tasks.value = res.data.data || [];
  const seen = readSeen();
  newTasks.value = tasks.value.filter(item => !seen.includes(item.afId));
  showNotice.value = newTasks.value.length > 0;
  if (newTasks.value.length > 0) {
    writeSeen([...seen, ...newTasks.value.map(item => item.afId)]);
  }
});
const openFirstTask = () => {
  showNotice.value = false;
  const first = newTasks.value[0];
  if (first) router.push({ path: '/feedbackInfo', query: { afId: first.afId } });
};
const logout = () => { removeSessionStorage('gridMember'); router.push('/login'); };
load();
</script>

<style scoped>
.notice { margin: 3vw; padding: 3vw; display: flex; align-items: center; gap: 2vw; border-radius: 2vw; background: #fff7e6; border: 1px solid #ffd591; box-shadow: 0 6px 18px rgba(164, 104, 0, .12); }
.notice div { flex: 1; min-width: 0; }
.notice b { color: #b76500; font-size: 4vw; }
.notice p { margin: 1vw 0 0; color: #7a5a24; font-size: 3.2vw; }
.notice button { padding: 1.5vw 2.8vw; border-radius: 1vw; background: #fa8c16; color: #fff; }
.notice .plain { background: #fff; border: 1px solid #ffd591; color: #b76500; }
ul { margin: 0; padding: 0; list-style: none; background: #fff; }
li { min-height: 18vw; padding: 3vw; display: flex; align-items: center; justify-content: space-between; border-bottom: 1px dashed #b9cad2; }
.left { display: flex; align-items: center; min-width: 0; }
i { flex: 0 0 8vw; height: 8vw; margin-right: 3vw; display: inline-flex; align-items: center; justify-content: center; color: #fff; border-radius: 1vw; font-style: normal; font-weight: 700; }
b { display: block; color: #3a4f5a; font-size: 4vw; }
p { margin: 1vw 0 0; color: #667984; font-size: 3.3vw; }
button { flex: 0 0 auto; padding: 1.5vw 2.5vw; border: 1px solid #c6e2ff; border-radius: 1vw; background: #ecf5ff; color: #409eff; }
.empty { text-align: center; color: #98aab1; font-size: 5vw; }
@media (min-width: 520px) { .notice { margin: 14px; padding: 14px; gap: 10px; border-radius: 8px; } .notice b { font-size: 16px; } .notice p { font-size: 13px; margin-top: 4px; } .notice button { padding: 7px 12px; border-radius: 4px; } li { min-height: 72px; padding: 14px; } i { flex-basis: 34px; height: 34px; margin-right: 14px; } b { font-size: 16px; } p { font-size: 14px; } button { padding: 7px 12px; } }
</style>
