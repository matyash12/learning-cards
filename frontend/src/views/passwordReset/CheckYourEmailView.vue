<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js';
const store = notificationStore();
const router = useRouter();
const route = useRoute();


const goToLogin = () =>{
    router.push('/user/login');
}
</script>

<template>
    <div class="m-4 has-text-centered">
        <div class="recovery-view">
            <h2 class="subtitle">
                Please check your email for further instructions on how to reset your password.
            </h2>
            <button class="button is-primary" @click="goToLogin">Go to login</button>
        </div>
    </div>
</template>
