import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '../views/Login.vue';
import FeedbackList from '../views/FeedbackList.vue';
import FeedbackInfo from '../views/FeedbackInfo.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/feedbackList', component: FeedbackList },
  { path: '/feedbackInfo', component: FeedbackInfo }
];

export default createRouter({
  history: createWebHashHistory(),
  routes
});
