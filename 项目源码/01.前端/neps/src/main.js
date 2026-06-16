import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register'];
  const supervisor = sessionStorage.getItem('supervisor');
  if (!publicPages.includes(to.path) && !supervisor) {
    next('/login');
    return;
  }
  next();
});

createApp(App).use(router).mount('#app');
