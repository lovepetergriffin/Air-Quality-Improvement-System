<template>
  <router-view />
</template>

<script setup>
import axios from 'axios';
import { provide } from 'vue';
import { ElMessage } from 'element-plus';

const apiHost = window.location.hostname || 'localhost';
axios.defaults.baseURL = `http://${apiHost}:8080/`;
const myElMessage = (message, type = 'success') => ElMessage({ message, type });
provide('axios', axios);
provide('myElMessage', myElMessage);
</script>

<style>
html, body, #app {
  width: 100%;
  height: 100%;
  margin: 0;
  font-family: "Microsoft YaHei", Arial, sans-serif;
}
* { box-sizing: border-box; }
.page-title {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.toolbar {
  margin: 14px 0;
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}
.chart-box {
  width: 100%;
  height: 360px;
}
</style>
