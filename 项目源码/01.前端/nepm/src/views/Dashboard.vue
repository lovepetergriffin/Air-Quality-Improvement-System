<template>
  <section>
    <div class="page-title">
      <el-breadcrumb>
        <el-breadcrumb-item>首页看板</el-breadcrumb-item>
        <el-breadcrumb-item>待办提醒</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button type="primary" @click="loadAll">刷新</el-button>
    </div>

    <div class="summary-grid">
      <el-card v-for="item in cards" :key="item.label" class="summary-card" shadow="hover" @click="goState(item.state)">
        <p>{{ item.label }}</p>
        <strong>{{ item.value }}</strong>
        <span>{{ item.tip }}</span>
      </el-card>
    </div>

    <el-alert
      class="priority-alert"
      type="warning"
      show-icon
      :closable="false"
      :title="`当前还有 ${dashboard.highPriorityPending || 0} 条重点污染待处理反馈，请优先安排网格员。`"
    />

    <div class="todo-grid">
      <el-card shadow="never">
        <template #header>
          <div class="card-head">
            <span>待指派反馈</span>
            <el-button link type="primary" @click="goState(0)">查看全部</el-button>
          </div>
        </template>
        <el-table :data="unassignedList" size="small" empty-text="暂无待指派反馈">
          <el-table-column prop="afId" label="编号" width="72" />
          <el-table-column label="地区">
            <template #default="{row}">{{ row.gridProvince?.provinceName }} {{ row.gridCity?.cityName }}</template>
          </el-table-column>
          <el-table-column prop="address" label="地址" min-width="140" />
          <el-table-column label="优先级" width="90">
            <template #default="{row}"><el-tag :type="priorityMeta(row).type">{{ priorityMeta(row).label }}</el-tag></template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-card shadow="never">
        <template #header>
          <div class="card-head">
            <span>待检测反馈</span>
            <el-button link type="primary" @click="goState(1)">查看全部</el-button>
          </div>
        </template>
        <el-table :data="assignedList" size="small" empty-text="暂无待检测反馈">
          <el-table-column prop="afId" label="编号" width="72" />
          <el-table-column label="网格员">
            <template #default="{row}">{{ row.gridMember?.gmName || '-' }}</template>
          </el-table-column>
          <el-table-column prop="address" label="地址" min-width="140" />
          <el-table-column label="优先级" width="90">
            <template #default="{row}"><el-tag :type="priorityMeta(row).type">{{ priorityMeta(row).label }}</el-tag></template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <el-card class="priority-card" shadow="never">
      <template #header>
        <div class="card-head">
          <span>最近重点污染反馈</span>
          <el-button link type="primary" @click="router.push('/index/feedbackList')">进入反馈列表</el-button>
        </div>
      </template>
      <el-table :data="highPriorityList" size="small" empty-text="暂无重点污染待处理反馈">
        <el-table-column prop="afId" label="编号" width="72" />
        <el-table-column label="状态" width="92">
          <template #default="{row}"><el-tag :type="row.state === 1 ? 'warning' : 'info'">{{ stateText[row.state] }}</el-tag></template>
        </el-table-column>
        <el-table-column label="地区">
          <template #default="{row}">{{ row.gridProvince?.provinceName }} {{ row.gridCity?.cityName }}</template>
        </el-table-column>
        <el-table-column prop="address" label="地址" min-width="180" />
        <el-table-column label="AQI" width="90">
          <template #default="{row}"><el-tag type="danger">{{ row.aqi?.chineseExplain || row.estimatedGrade }}</el-tag></template>
        </el-table-column>
      </el-table>
    </el-card>
  </section>
</template>

<script setup>
import { computed, inject, ref } from 'vue';
import { useRouter } from 'vue-router';

const axios = inject('axios');
const router = useRouter();
const dashboard = ref({});
const records = ref([]);
const stateText = ['未指派', '已指派', '已确认'];

const priorityMeta = row => {
  const grade = Number(row.estimatedGrade || row.aqiId || row.aqi?.aqiId || 0);
  if (grade >= 5) return { label: '重点', type: 'danger' };
  if (grade >= 3) return { label: '较急', type: 'warning' };
  return { label: '普通', type: 'success' };
};

const cards = computed(() => [
  { label: '反馈总数', value: dashboard.value.feedbackTotal || 0, tip: '公众提交反馈总量' },
  { label: '待指派', value: dashboard.value.unassigned || 0, tip: '需要管理员安排', state: 0 },
  { label: '待检测', value: dashboard.value.assigned || 0, tip: '网格员处理中', state: 1 },
  { label: '已完成', value: dashboard.value.confirmed || 0, tip: '已形成确认数据', state: 2 },
  { label: '重点污染', value: dashboard.value.highPriorityPending || 0, tip: 'AQI 五级及以上' },
  { label: '今日新增', value: dashboard.value.todayNewFeedback || 0, tip: '当天公众反馈' },
  { label: '网格员', value: dashboard.value.gridMemberTotal || 0, tip: '当前网格员数量' },
  { label: '确认数据', value: dashboard.value.statisticsTotal || 0, tip: '实测 AQI 记录数' }
]);

const unassignedList = computed(() => records.value.filter(item => item.state === 0).slice(0, 5));
const assignedList = computed(() => records.value.filter(item => item.state === 1).slice(0, 5));
const highPriorityList = computed(() => records.value
  .filter(item => item.state !== 2 && Number(item.estimatedGrade || item.aqiId || item.aqi?.aqiId || 0) >= 5)
  .slice(0, 6));

const loadAll = () => {
  axios.get('statistics/dashboard').then(res => dashboard.value = res.data.data || {});
  axios.post('aqiFeedback/listAqiFeedbackPage', { pageNum: 1, maxPageNum: 30 }).then(res => records.value = res.data.data?.records || []);
};
const goState = state => {
  if (state === 0 || state === 1 || state === 2) router.push({ path: '/index/feedbackList', query: { state } });
};

loadAll();
</script>

<style scoped>
.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(160px, 1fr));
  gap: 14px;
}
.summary-card {
  cursor: pointer;
}
.summary-card p {
  margin: 0;
  color: #607481;
  font-size: 14px;
}
.summary-card strong {
  display: block;
  margin: 8px 0;
  color: #16476c;
  font-size: 30px;
}
.summary-card span {
  color: #8a9aa5;
  font-size: 13px;
}
.priority-alert {
  margin: 16px 0;
}
.todo-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.priority-card {
  margin-top: 16px;
}
.card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
@media (max-width: 1100px) {
  .summary-grid { grid-template-columns: repeat(2, minmax(160px, 1fr)); }
  .todo-grid { grid-template-columns: 1fr; }
}
</style>
