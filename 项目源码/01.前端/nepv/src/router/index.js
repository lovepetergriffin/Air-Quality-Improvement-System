import { createRouter, createWebHashHistory } from 'vue-router';
import Index from '../views/Index.vue';

const routes = [
  { path: '/', component: Index },
  { path: '/index', component: Index }
];

export default createRouter({
  history: createWebHashHistory(),
  routes
});
