<template>
  <div class="wrapper">
    <div class="header-box">
      <header><span @click="router.back()">‹</span><p>公众监督员注册</p><span></span></header>
      <div class="top-ban"></div><img src="../assets/header.png" class="header-img" alt="">
    </div>
    <section class="panel">
      <label class="field"><span>手机号</span><input v-model.trim="form.telId" inputmode="numeric" @blur="checkExist"></label>
      <label class="field"><span>真实姓名</span><input v-model.trim="form.realName"></label>
      <label class="field"><span>出生日期</span><input v-model="form.birthday" type="date"></label>
      <label class="field"><span>性别</span><select v-model.number="form.sex"><option :value="1">男</option><option :value="0">女</option></select></label>
      <label class="field"><span>密码</span><input v-model="form.password" type="password"></label>
      <label class="field"><span>确认密码</span><input v-model="confirmPassword" type="password"></label>
      <button class="btn" @click="submit">注册并返回登录</button>
    </section>
  </div>
</template>

<script setup>
import { inject, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const axios = inject('axios');
const confirmPassword = ref('');
const form = reactive({ telId: '', realName: '', birthday: '', sex: 1, password: '' });

const checkExist = () => {
  if (/^\d{11}$/.test(form.telId)) {
    axios.get('supervisor/exist?telId=' + form.telId).then(res => {
      if (res.data.data) alert('此手机号码已被注册');
    });
  }
};

const submit = () => {
  if (!/^\d{11}$/.test(form.telId)) return alert('请输入11位手机号码');
  if (!form.realName || !form.birthday || !form.password) return alert('请完整填写注册信息');
  if (form.password !== confirmPassword.value) return alert('两次密码输入不一致');
  axios.post('supervisor/register', form).then(res => {
    if (res.data.code === 200) {
      alert('注册成功');
      router.push('/login');
    } else {
      alert(res.data.message || res.data.msg);
    }
  });
};
</script>
