import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

router.beforeEach((to, from, next) => {
  const gridMember = sessionStorage.getItem('gridMember');
  if (to.path !== '/login' && !gridMember) {
    next('/login');
    return;
  }
  next();
});

createApp(App).use(router).mount('#app');
