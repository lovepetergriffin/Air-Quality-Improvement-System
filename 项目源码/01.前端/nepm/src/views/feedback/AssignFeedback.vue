<template>
  <el-breadcrumb><el-breadcrumb-item>公众监督数据管理</el-breadcrumb-item><el-breadcrumb-item>指派网格员</el-breadcrumb-item></el-breadcrumb>
  <DataDetails />
  <el-card style="margin-top:16px">
    <div class="toolbar">
      <el-switch v-model="isRemote" active-text="异地指派" inactive-text="本地指派" @change="loadRemoteBase" />
      <template v-if="!isRemote">
        <el-select v-model="gmIdLocal" placeholder="选择本地网格员" style="width:220px">
          <el-option v-for="m in localMembers" :key="m.gmId" :label="m.gmName + ' / ' + m.gmCode" :value="m.gmId" />
        </el-select>
      </template>
      <template v-else>
        <el-select v-model="remote.provinceId" placeholder="省份" style="width:160px" @change="changeProvince">
          <el-option v-for="p in provinces" :key="p.provinceId" :label="p.provinceName" :value="p.provinceId" />
        </el-select>
        <el-select v-model="remote.cityId" placeholder="城市" style="width:160px" @change="loadRemoteMembers">
          <el-option v-for="c in cities" :key="c.cityId" :label="c.cityName" :value="c.cityId" />
        </el-select>
        <el-select v-model="gmIdNoLocal" placeholder="选择异地网格员" style="width:220px">
          <el-option v-for="m in remoteMembers" :key="m.gmId" :label="m.gmName + ' / ' + m.gmCode" :value="m.gmId" />
        </el-select>
      </template>
      <el-popconfirm title="确定要指派吗?" @confirm="assignEvent">
        <template #reference><el-button type="primary">确认指派</el-button></template>
      </el-popconfirm>
    </div>
  </el-card>
</template>

<script setup>
import { inject, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DataDetails from '../../components/DataDetails.vue';

const axios = inject('axios');
const myElMessage = inject('myElMessage');
const route = useRoute();
const router = useRouter();
const isRemote = ref(false);
const feedback = ref({});
const localMembers = ref([]);
const remoteMembers = ref([]);
const provinces = ref([]);
const cities = ref([]);
const gmIdLocal = ref(null);
const gmIdNoLocal = ref(null);
const remote = reactive({ provinceId: null, cityId: null });

const init = () => {
  axios.post('aqiFeedback/getAqiFeedbackById', { afId: route.query.afId }).then(res => {
    feedback.value = res.data.data || {};
    return axios.post('gridMember/listGridMemberByProvinceId', { provinceId: feedback.value.provinceId, cityId: feedback.value.cityId, state: 0 });
  }).then(res => localMembers.value = res.data.data || []);
};
const loadRemoteBase = () => axios.post('gridProvince/listGridProvinceAll').then(res => provinces.value = res.data.data || []);
const changeProvince = () => {
  remote.cityId = null; gmIdNoLocal.value = null; remoteMembers.value = [];
  axios.post('gridCity/listGridCityByProvinceId', { provinceId: remote.provinceId }).then(res => cities.value = res.data.data || []);
};
const loadRemoteMembers = () => axios.post('gridMember/listGridMemberByProvinceId', { provinceId: remote.provinceId, cityId: remote.cityId, state: 0 }).then(res => remoteMembers.value = res.data.data || []);
const assignEvent = () => {
  const gmId = isRemote.value ? gmIdNoLocal.value : gmIdLocal.value;
  if (!gmId) return myElMessage('请选择网格员', 'warning');
  axios.post('aqiFeedback/updateAqiFeedbackAssign', { afId: Number(route.query.afId), gmId }).then(res => {
    if (res.data.code === 200) { myElMessage('指派成功，网格员登录后将收到任务提醒'); router.push('/index/feedbackList'); }
    else myElMessage(res.data.message || res.data.msg, 'error');
  });
};
init();
</script>
