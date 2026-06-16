<template>
  <el-container class="layout">
    <el-header>
      <div class="brand"><img src="../assets/logo.png" alt=""><h1>东软环保公众监督平台管理系统</h1></div>
      <div class="admin"><span>系统管理员：{{ admins.adminCode }}</span><el-button type="danger" size="small" @click="out">OUT</el-button></div>
    </el-header>
    <el-container>
      <el-aside width="220px">
        <el-menu router background-color="#16476c" text-color="#d8edf2" active-text-color="#7ce0b8" :default-active="$route.path">
          <el-sub-menu index="feedback">
            <template #title>公众监督数据管理</template>
            <el-menu-item index="/index/feedbackList">公众监督数据列表</el-menu-item>
            <el-menu-item index="/index/statisticsList">确认AQI数据列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="statistics">
            <template #title>统计数据管理</template>
            <el-menu-item index="/index/provinceItemTotalStatis">省分组检查统计</el-menu-item>
            <el-menu-item index="/index/aqiDistributeTotalStatis">AQI指数分布统计</el-menu-item>
            <el-menu-item index="/index/aqiTrendTotalStatis">AQI指数趋势统计</el-menu-item>
            <el-menu-item index="/index/otherTotalStatis">其他数据统计</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main><router-view /></el-main>
    </el-container>
    <el-footer>版权所有 东软睿道 www.neusoft.com</el-footer>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { getSessionStorage, removeSessionStorage } from '../common.js';

const router = useRouter();
const admins = getSessionStorage('admins') || {};
const out = () => {
  removeSessionStorage('admins');
  router.push('/login');
};
</script>

<style scoped>
.layout { width: 100%; height: 100%; }
.el-header { height: 62px; display: flex; align-items: center; justify-content: space-between; background: #102c50; color: #fff; }
.brand { display: flex; align-items: center; gap: 12px; }
.brand img { width: 34px; height: 34px; border-radius: 6px; object-fit: cover; }
.brand h1 { margin: 0; font-size: 22px; font-weight: 600; }
.admin { display: flex; align-items: center; gap: 18px; }
.el-aside { background: #16476c; }
.el-menu { border-right: 0; }
.el-main { background: #f3f7fa; padding: 20px; }
.el-footer { height: 42px; text-align: center; line-height: 42px; background: #102c50; color: #d8edf2; font-size: 13px; }
</style>
