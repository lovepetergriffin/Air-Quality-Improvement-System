<template>
  <nav class="bottom-nav">
    <button :class="{active: route.path === '/selectGrid' || route.path === '/selectAqi'}" @click="go('/selectGrid')"><b>＋</b><span>上报</span></button>
    <button :class="{active: route.path === '/feedbackList'}" @click="go('/feedbackList')"><b>▤</b><span>我的反馈</span></button>
    <button :class="{active: route.path === '/profile'}" @click="go('/profile')"><b>●</b><span>个人</span></button>
  </nav>
</template>
<script setup>
import { useRoute, useRouter } from 'vue-router'
const route = useRoute(); const router = useRouter(); const go = path => router.push(path)
</script>
<style scoped>
.bottom-nav { position:fixed; z-index:10; left:50%; bottom:0; transform:translateX(-50%); width:100%; max-width:480px; height:70px; display:grid; grid-template-columns:repeat(3,1fr); border-top:1px solid #d8e2ee; background:#fff; }
button { border:0; background:#fff; color:#7b8798; display:flex; flex-direction:column; align-items:center; justify-content:center; gap:3px; }
button b { font-size:20px; line-height:20px; } button span { font-size:12px; } button.active { color:#18a774; }
</style>
