<template>
  <router-view />
</template>

<script setup>
import axios from 'axios';
import { provide } from 'vue';

const apiHost = window.location.hostname || 'localhost';
axios.defaults.baseURL = `http://${apiHost}:8080/`;
provide('axios', axios);
</script>

<style>
html, body, #app {
  width: 100%;
  min-height: 100%;
  margin: 0;
  font-family: "Microsoft YaHei", Arial, sans-serif;
  background: #eef5f7;
}
* { box-sizing: border-box; }
button, input { font: inherit; }
button { border: 0; cursor: pointer; }
.wrapper {
  width: 100%;
  min-height: 100vh;
  position: relative;
  background: #eef5f7;
  color: #425762;
  padding-bottom: 120px;
}
.header-box header {
  position: fixed;
  left: 0;
  top: 0;
  z-index: 10;
  width: 100%;
  height: 15.7vw;
  min-height: 54px;
  max-height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 4vw;
  background: #fff;
  color: #425762;
  box-shadow: 0 1px 8px rgba(16, 44, 80, .12);
}
.header-box p { margin: 0; font-size: 4.4vw; font-weight: 700; }
.header-box span { min-width: 8vw; color: #409eff; font-size: 7vw; }
.top-ban { height: min(15.7vw, 64px); min-height: 54px; }
.header-img { display: block; width: 100%; height: 24vw; max-height: 112px; object-fit: cover; }
footer { position: fixed; left: 0; bottom: 0; width: 100%; z-index: 10; }
footer img { display: block; width: 100%; height: 92px; object-fit: cover; }
.panel {
  width: 90vw;
  margin: 4vw auto;
  padding: 4vw;
  background: #fff;
  border: 1px solid #d8e8ee;
  border-radius: 2vw;
}
.btn {
  width: 100%;
  height: 12vw;
  border-radius: 6vw;
  background: linear-gradient(90deg, #67c9ef, #7394f1);
  color: #fff;
  font-size: 4.6vw;
  font-weight: 700;
}
@media (min-width: 520px) {
  .wrapper { width: 430px; margin: 0 auto; box-shadow: 0 0 30px rgba(16, 44, 80, .12); }
  .header-box header, footer { left: 50%; width: 430px; transform: translateX(-50%); }
  .header-box p { font-size: 20px; }
  .header-box span { font-size: 30px; }
  .panel { width: 390px; padding: 20px; border-radius: 10px; }
  .btn { height: 48px; border-radius: 24px; font-size: 17px; }
}
</style>
