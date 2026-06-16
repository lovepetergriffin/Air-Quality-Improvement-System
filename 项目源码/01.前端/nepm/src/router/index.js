import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Index from '../views/Index.vue';
import Dashboard from '../views/Dashboard.vue';
import FeedbackList from '../views/feedback/FeedbackList.vue';
import FeedbackInfo from '../views/feedback/FeedbackInfo.vue';
import AssignFeedback from '../views/feedback/AssignFeedback.vue';
import StatisticsList from '../views/feedback/StatisticsList.vue';
import StatisticsInfo from '../views/feedback/StatisticsInfo.vue';
import ProvinceItemTotalStatis from '../views/statistics/ProvinceItemTotalStatis.vue';
import AqiDistributeTotalStatis from '../views/statistics/AqiDistributeTotalStatis.vue';
import AqiTrendTotalStatis from '../views/statistics/AqiTrendTotalStatis.vue';
import OtherTotalStatis from '../views/statistics/OtherTotalStatis.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  {
    path: '/index',
    component: Index,
    redirect: '/index/dashboard',
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'feedbackList', component: FeedbackList },
      { path: 'feedbackInfo', component: FeedbackInfo },
      { path: 'assignFeedback', component: AssignFeedback },
      { path: 'statisticsList', component: StatisticsList },
      { path: 'statisticsInfo', component: StatisticsInfo },
      { path: 'provinceItemTotalStatis', component: ProvinceItemTotalStatis },
      { path: 'aqiDistributeTotalStatis', component: AqiDistributeTotalStatis },
      { path: 'aqiTrendTotalStatis', component: AqiTrendTotalStatis },
      { path: 'otherTotalStatis', component: OtherTotalStatis }
    ]
  }
];

export default createRouter({
  history: createWebHashHistory(),
  routes
});
