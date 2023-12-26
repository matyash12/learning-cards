<script setup>
import axios from 'axios';

import { API_ADDRESS } from '../../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js'; 

const store = notificationStore();




console.log(store.visible);

const router = useRouter();
const route = useRoute();


const hideNotification = () =>{
    store.hideNotification();
}


</script>


<script>

/*
document.addEventListener('DOMContentLoaded', () => {
    (document.querySelectorAll('.notification .delete') || []).forEach(($delete) => {
        const $notification = $delete.parentNode;

        $delete.addEventListener('click', () => {
            $notification.parentNode.removeChild($notification);
        });
    });
});
*/
</script>
<template>
    <div class="notification" :class="store.bulmaClass" v-if="store.visible">
        <button v-if="store.showCloseButton" @click="hideNotification" class="delete"></button>
        <p v-text="store.message"></p>
    </div>
</template>