<script setup>
import axios from 'axios';

import { RouterLink, RouterView } from 'vue-router'
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
import MainNotificationView from '@/views/modal/MainNotificationView.vue';

</script>

<template>
  <MainNotificationView/>
  <RouterView />
</template>

