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
  background: #cfe8e9;
}
* { box-sizing: border-box; }
button, input, select, textarea { font: inherit; }
button { border: none; cursor: pointer; }
.wrapper {
  width: 100%;
  min-height: 100vh;
  background: #cfe8e9;
  color: #38464f;
}
.header-box header {
  position: fixed;
  left: 0;
  top: 0;
  z-index: 20;
  width: 100%;
  height: 15.7vw;
  max-height: 64px;
  min-height: 54px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 4vw;
  background: #fff;
  color: #425762;
  box-shadow: 0 1px 8px rgba(16, 44, 80, .12);
}
.header-box header p {
  margin: 0;
  font-size: 4.4vw;
  font-weight: 700;
}
.header-box header span {
  min-width: 8vw;
  font-size: 7vw;
  color: #2d727e;
}
.top-ban { height: min(15.7vw, 64px); min-height: 54px; }
.header-img {
  display: block;
  width: 100%;
  height: 22vw;
  max-height: 110px;
  object-fit: cover;
}
.panel {
  width: 90vw;
  margin: 4vw auto;
  padding: 4vw;
  background: #fff;
  border: 1px solid #c6e2ff;
  border-radius: 2vw;
}
.field {
  display: block;
  margin-bottom: 4vw;
}
.field span {
  display: block;
  margin-bottom: 1.6vw;
  color: #526772;
  font-size: 3.7vw;
  font-weight: 700;
}
.field input, .field select, .field textarea {
  width: 100%;
  min-height: 11vw;
  padding: 0 3vw;
  border: 1px solid #b8d4d7;
  border-radius: 2vw;
  outline: none;
  background: #eff8f8;
}
.field textarea {
  min-height: 22vw;
  padding: 3vw;
  resize: vertical;
}
.btn {
  width: 100%;
  height: 12vw;
  border-radius: 2vw;
  background: #70b0bc;
  color: #fff;
  font-size: 4.6vw;
  font-weight: 700;
  letter-spacing: .5vw;
}
.ghost-btn {
  width: 100%;
  height: 11vw;
  margin-top: 3vw;
  border: 1px solid #70b0bc;
  border-radius: 2vw;
  background: #fff;
  color: #2d727e;
  font-size: 4vw;
}
@media (min-width: 520px) {
  .wrapper { width: 430px; margin: 0 auto; box-shadow: 0 0 30px rgba(16, 44, 80, .12); }
  .header-box header { left: 50%; width: 430px; transform: translateX(-50%); }
  .header-box header p { font-size: 20px; }
  .header-box header span { font-size: 30px; }
  .panel { width: 390px; padding: 20px; }
  .field span { font-size: 16px; }
  .field input, .field select, .field textarea, .btn, .ghost-btn { min-height: 46px; height: 46px; border-radius: 8px; font-size: 16px; }
  .field textarea { height: 92px; }
}
</style>
