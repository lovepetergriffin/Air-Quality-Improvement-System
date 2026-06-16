<template>
  <div class="wrapper login-wrap">
    <img src="../assets/bgtop.png" class="login-top" alt="">
    <h1>环保公众监督系统</h1>
    <div class="title"><i></i><span>公众监督员登录</span><i></i></div>
    <section>
      <label class="input-box"><span>手机号</span><input v-model.trim="form.telId" inputmode="numeric" placeholder="请输入11位手机号"></label>
      <label class="input-box"><span>密码</span><input v-model="form.password" type="password" placeholder="请输入密码"></label>
      <div class="reg-box"><button @click="router.push('/register')">注册账号</button><button @click="fillDemo">示例账号</button></div>
      <button class="button-box" @click="login">登 录</button>
    </section>
  </div>
</template>

<script setup>
import { inject, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { setSessionStorage } from '../common.js';

const router = useRouter();
const axios = inject('axios');
const form = reactive({ telId: '', password: '' });

const fillDemo = () => {
  form.telId = '13147859658';
  form.password = '123';
};

const login = () => {
  if (!/^\d{11}$/.test(form.telId)) return alert('请输入11位手机号码');
  if (!form.password) return alert('密码不能为空');
  axios.post('supervisor/login', { telId: form.telId, password: form.password })
    .then(res => {
      if (res.data.code === 200) {
        setSessionStorage('supervisor', res.data.data);
        router.push('/selectGrid');
      } else {
        alert(res.data.message || res.data.msg);
      }
    })
    .catch(() => alert('无法连接后端服务'));
};
</script>

<style scoped>
.login-wrap { padding-bottom: 8vw; }
.login-top { display: block; width: 100%; height: 38vw; object-fit: cover; }
h1 { margin: 5vw 0 2vw; text-align: center; color: #46a34f; font-size: 7vw; }
.title { width: 74%; margin: 0 auto 5vw; display: flex; align-items: center; gap: 3vw; color: #46a34f; font-size: 4vw; }
.title i { flex: 1; height: 1px; background: #68c56f; }
section { width: 86vw; margin: 0 auto; padding: 5vw; background: #fff; border-radius: 2.4vw; box-shadow: 0 6px 18px rgba(16,44,80,.14); }
.input-box { height: 12vw; margin-top: 4vw; padding: 0 3vw; display: flex; align-items: center; border: 1px solid #c7d3d6; border-radius: 2vw; }
.input-box span { width: 17vw; color: #2d727e; font-weight: 700; }
.input-box input { flex: 1; border: 0; outline: 0; }
.reg-box { display: flex; justify-content: space-between; margin: 4vw 0; }
.reg-box button { background: transparent; color: #2d727e; }
.button-box { width: 100%; height: 12vw; border-radius: 2vw; background: #70b0bc; color: #fff; font-size: 4.8vw; font-weight: 700; }
@media (min-width: 520px) {
  .login-top { height: 160px; }
  h1 { font-size: 28px; margin: 22px 0 8px; }
  .title { font-size: 16px; margin-bottom: 22px; }
  section { width: 370px; padding: 22px; border-radius: 10px; }
  .input-box { height: 48px; margin-top: 16px; border-radius: 8px; }
  .input-box span { width: 70px; }
  .button-box { height: 48px; border-radius: 8px; font-size: 18px; }
}
</style>
