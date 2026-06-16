<template>
  <section>
    <el-breadcrumb><el-breadcrumb-item>确认AQI数据管理</el-breadcrumb-item><el-breadcrumb-item>确认AQI数据列表</el-breadcrumb-item></el-breadcrumb>
    <div class="toolbar">
      <el-select v-model="queryForm.provinceId" clearable placeholder="省份" style="width:160px" @change="changeProvince">
        <el-option v-for="p in provinces" :key="p.provinceId" :label="p.provinceName" :value="p.provinceId" />
      </el-select>
      <el-select v-model="queryForm.cityId" clearable placeholder="城市" style="width:160px">
        <el-option v-for="c in cities" :key="c.cityId" :label="c.cityName" :value="c.cityId" />
      </el-select>
      <el-date-picker v-model="queryForm.confirmDate" value-format="YYYY-MM-DD" type="date" placeholder="确认日期" />
      <el-button type="primary" @click="query">查询</el-button>
      <el-button @click="clear">重置</el-button>
    </div>
    <el-table :data="page.records" border stripe>
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column label="区域"><template #default="{row}">{{ row.gridProvince?.provinceName }} / {{ row.gridCity?.cityName }}</template></el-table-column>
      <el-table-column prop="address" label="地址" min-width="180" />
      <el-table-column prop="so2Value" label="SO2" />
      <el-table-column prop="coValue" label="CO" />
      <el-table-column prop="spmValue" label="PM2.5" />
      <el-table-column label="AQI"><template #default="{row}"><el-tag :color="row.aqi?.color" effect="dark">{{ row.aqi?.chineseExplain }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="100"><template #default="{row}"><el-button link type="primary" @click="router.push({path:'/index/statisticsInfo', query:{id:row.id}})">查看</el-button></template></el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next, total" :total="page.total" :page-size="page.pageSize" v-model:current-page="pageNum" @current-change="load" style="margin-top:16px;justify-content:center" />
  </section>
</template>

<script setup>
import { inject, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

const axios = inject('axios');
const router = useRouter();
const store = useStore();
const queryForm = reactive({ provinceId: null, cityId: null, confirmDate: '' });
const provinces = ref([]);
const cities = ref([]);
const pageNum = ref(1);
const page = ref({ records: [], total: 0, pageSize: store.state.maxPageNum });

const load = () => axios.post('statistics/listStatisticsPage', { ...queryForm, pageNum: pageNum.value, maxPageNum: store.state.maxPageNum }).then(res => page.value = res.data.data || page.value);
const query = () => { pageNum.value = 1; load(); };
const clear = () => { queryForm.provinceId = null; queryForm.cityId = null; queryForm.confirmDate = ''; cities.value = []; query(); };
const changeProvince = () => {
  queryForm.cityId = null;
  if (queryForm.provinceId) axios.post('gridCity/listGridCityByProvinceId', { provinceId: queryForm.provinceId }).then(res => cities.value = res.data.data || []);
};
axios.post('gridProvince/listGridProvinceAll').then(res => provinces.value = res.data.data || []);
load();
</script>
