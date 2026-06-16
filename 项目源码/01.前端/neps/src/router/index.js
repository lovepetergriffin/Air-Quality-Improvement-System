import { createRouter, createWebHashHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import SelectGrid from '../views/SelectGrid.vue';
import SelectAqi from '../views/SelectAqi.vue';
import FeedbackList from '../views/FeedbackList.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/selectGrid', component: SelectGrid },
  { path: '/selectAqi', component: SelectAqi },
  { path: '/feedbackList', component: FeedbackList }
];

export default createRouter({
  history: createWebHashHistory(),
  routes
});
