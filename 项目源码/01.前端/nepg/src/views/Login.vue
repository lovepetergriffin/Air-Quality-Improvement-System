<template>
  <div class="wrapper login-page">
    <header><img src="../assets/header.png" alt=""></header>
    <h1>网格员工作端</h1>
    <section class="login">
      <label><span>编码</span><input v-model.trim="form.gmCode" placeholder="请输入登录编码"></label>
      <label><span>密码</span><input v-model="form.password" type="password" placeholder="请输入密码"></label>
      <button class="btn" @click="login">登 录</button>
      <button class="demo" @click="fillDemo">使用示例账号</button>
    </section>
    <footer><img src="../assets/bottom.png" alt=""></footer>
  </div>
</template>

<script setup>
import { inject, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { setSessionStorage } from '../common.js';

const router = useRouter();
const axios = inject('axios');
const form = reactive({ gmCode: '', password: '' });

const fillDemo = () => {
  form.gmCode = 'caocao';
  form.password = '123';
};

const login = () => {
  if (!form.gmCode) return alert('登录编码不能为空');
  if (!form.password) return alert('密码不能为空');
  axios.post('gridMember/login', form).then(res => {
    if (res.data.code === 200) {
      setSessionStorage('gridMember', res.data.data);
      router.push('/feedbackList');
    } else {
      alert(res.data.message || res.data.msg);
    }
  }).catch(() => alert('无法连接后端服务'));
};
</script>

<style scoped>
.login-page header img { display: block; width: 100%; height: 200px; object-fit: cover; }
h1 { width: 78vw; margin: 8vw auto 0; padding-bottom: 2vw; text-align: center; color: #409eff; border-bottom: 2px solid #6ab9f2; font-size: 5.4vw; }
.login { width: 80vw; margin: 6vw auto; }
label { height: 12vw; margin-top: 6vw; padding: 0 5vw; display: flex; align-items: center; border-radius: 6vw; background: linear-gradient(90deg,#f7f7f7,#e6edf0); }
label span { width: 16vw; color: #409eff; font-weight: 700; }
label input { flex: 1; border: 0; outline: 0; background: transparent; }
.btn { margin-top: 8vw; }
.demo { width: 100%; margin-top: 4vw; background: transparent; color: #409eff; }
@media (min-width: 520px) { h1 { width: 336px; font-size: 24px; margin-top: 30px; } .login { width: 344px; } label { height: 48px; margin-top: 24px; border-radius: 24px; } label span { width: 64px; } .btn { margin-top: 32px; } }
</style>
