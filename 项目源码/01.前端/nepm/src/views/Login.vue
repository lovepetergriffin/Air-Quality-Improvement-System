<template>
  <main class="login-page">
    <el-card class="box-card">
      <template #header><div class="card-header">NEPM后台管理登录</div></template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="登录编码"><el-input v-model.trim="form.adminCode" placeholder="登录编码" /></el-form-item>
        <el-form-item label="登录密码"><el-input v-model="form.password" show-password placeholder="登录密码" /></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
          <el-button @click="fillDemo">示例账号</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </main>
</template>

<script setup>
import { inject, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { setSessionStorage } from '../common.js';

const axios = inject('axios');
const myElMessage = inject('myElMessage');
const router = useRouter();
const form = reactive({ adminCode: '', password: '' });

const fillDemo = () => {
  form.adminCode = 'admin';
  form.password = '123';
};

const login = () => {
  if (!form.adminCode || !form.password) return myElMessage('账号和密码不能为空', 'warning');
  axios.post('admins/getAdminsByCodeByPass', form).then(res => {
    if (res.data.code === 200) {
      setSessionStorage('admins', res.data.data);
      router.push('/index');
    } else {
      myElMessage(res.data.message || res.data.msg, 'error');
    }
  }).catch(() => myElMessage('无法连接后端服务', 'error'));
};
</script>

<style scoped>
.login-page { min-height: 100vh; padding-top: 150px; background: linear-gradient(135deg, #0b2e52, #15966f); }
.box-card { width: 420px; margin: 0 auto; }
.card-header { font-size: 18px; font-weight: 700; color: #24465c; }
</style>
