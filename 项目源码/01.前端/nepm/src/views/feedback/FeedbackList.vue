<template>
  <section>
    <div class="page-title"><el-breadcrumb><el-breadcrumb-item>公众监督数据管理</el-breadcrumb-item><el-breadcrumb-item>公众监督数据列表</el-breadcrumb-item></el-breadcrumb></div>
    <div class="toolbar">
      <el-select v-model="queryForm.state" clearable placeholder="反馈状态" style="width: 140px">
        <el-option label="未指派" :value="0" /><el-option label="已指派" :value="1" /><el-option label="已确认" :value="2" />
      </el-select>
      <el-select v-model="queryForm.provinceId" clearable placeholder="省份" style="width: 160px" @change="loadCities">
        <el-option v-for="p in provinces" :key="p.provinceId" :label="p.provinceName" :value="p.provinceId" />
      </el-select>
      <el-button type="primary" @click="query">查询</el-button>
      <el-button @click="clear">重置</el-button>
    </div>
    <el-table :data="page.records" border stripe>
      <el-table-column prop="afId" label="编号" width="80" />
      <el-table-column label="监督员"><template #default="{row}">{{ row.supervisor?.realName || row.telId }}</template></el-table-column>
      <el-table-column label="省市"><template #default="{row}">{{ row.gridProvince?.provinceName }} / {{ row.gridCity?.cityName }}</template></el-table-column>
      <el-table-column prop="address" label="地址" min-width="180" />
      <el-table-column label="AQI"><template #default="{row}"><el-tag :color="row.aqi?.color" effect="dark">{{ row.aqi?.chineseExplain }}</el-tag></template></el-table-column>
      <el-table-column label="优先级" width="110"><template #default="{row}"><el-tag :type="priorityMeta(row).type">{{ priorityMeta(row).label }}</el-tag></template></el-table-column>
      <el-table-column label="状态"><template #default="{row}"><el-tag :type="row.state === 2 ? 'success' : row.state === 1 ? 'warning' : 'info'">{{ stateText[row.state] }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{row}">
          <el-button link type="primary" @click="router.push({path:'/index/feedbackInfo', query:{afId:row.afId}})">查看</el-button>
          <el-button link type="success" v-if="row.state === 0" @click="router.push({path:'/index/assignFeedback', query:{afId:row.afId}})">指派</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next, total" :total="page.total" :page-size="page.pageSize" v-model:current-page="pageNum" @current-change="load" style="margin-top:16px;justify-content:center" />
  </section>
</template>

<script setup>
import { inject, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';

const axios = inject('axios');
const router = useRouter();
const route = useRoute();
const store = useStore();
const stateText = ['未指派', '已指派', '已确认'];
const parseState = value => (value === undefined || value === null || value === '' ? null : Number(value));
const queryForm = reactive({ state: parseState(route.query.state), provinceId: null });
const provinces = ref([]);
const cities = ref([]);
const pageNum = ref(1);
const page = ref({ records: [], total: 0, pageSize: store.state.maxPageNum });

const priorityMeta = row => {
  const grade = Number(row.estimatedGrade || row.aqiId || row.aqi?.aqiId || 0);
  if (grade >= 5) return { label: '重点', type: 'danger' };
  if (grade >= 3) return { label: '较急', type: 'warning' };
  return { label: '普通', type: 'success' };
};
const load = () => axios.post('aqiFeedback/listAqiFeedbackPage', { ...queryForm, pageNum: pageNum.value, maxPageNum: store.state.maxPageNum }).then(res => page.value = res.data.data || page.value);
const query = () => { pageNum.value = 1; load(); };
const clear = () => { queryForm.state = null; queryForm.provinceId = null; pageNum.value = 1; load(); };
const loadCities = () => queryForm.provinceId && axios.post('gridCity/listGridCityByProvinceId', { provinceId: queryForm.provinceId }).then(res => cities.value = res.data.data || []);
watch(() => route.query.state, value => {
  queryForm.state = parseState(value);
  pageNum.value = 1;
  load();
});

axios.post('gridProvince/listGridProvinceAll').then(res => provinces.value = res.data.data || []);
load();
</script>
