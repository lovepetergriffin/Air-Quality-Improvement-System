<template>
  <div class="wrapper">
    <div class="header-box">
      <header><span></span><p>选择所在网格</p><span @click="router.push('/feedbackList')">☰</span></header>
      <div class="top-ban"></div><img src="../assets/header.png" class="header-img" alt="">
    </div>
    <section class="panel">
      <label class="field"><span>省份</span><select v-model.number="provinceId" @change="changeProvince"><option :value="0">请选择省份</option><option v-for="p in provinces" :key="p.provinceId" :value="p.provinceId">{{ p.provinceName }}</option></select></label>
      <label class="field"><span>城市</span><select v-model.number="cityId"><option :value="0">请选择城市</option><option v-for="c in cities" :key="c.cityId" :value="c.cityId">{{ c.cityName }}</option></select></label>
      <label class="field"><span>详细地址</span><textarea v-model.trim="address" placeholder="请输入街道、社区或污染发生位置"></textarea></label>
      <button class="btn" @click="next">下一步</button>
      <button class="ghost-btn" @click="router.push('/feedbackList')">查看历史反馈</button>
    </section>
  </div>
</template>

<script setup>
import { inject, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const axios = inject('axios');
const provinces = ref([]);
const cities = ref([]);
const provinceId = ref(0);
const cityId = ref(0);
const address = ref('');

axios.get('common/province-list').then(res => provinces.value = res.data.data || []);

const changeProvince = () => {
  cityId.value = 0;
  cities.value = [];
  if (provinceId.value) axios.get('common/city-list?provinceId=' + provinceId.value).then(res => cities.value = res.data.data || []);
};

const next = () => {
  if (!provinceId.value || !cityId.value) return alert('请选择省份和城市');
  if (!address.value) return alert('具体地址不能为空');
  router.push({ path: '/selectAqi', query: { provinceId: provinceId.value, cityId: cityId.value, address: address.value } });
};
</script>
