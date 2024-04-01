<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js';
const store = notificationStore();
const router = useRouter();
let tridy = ref([]);
let selectedTrida = ref(null);
let selectedTridaDecks = ref(null);

//is running
const isDownloadHFRunning = ref(false);

//HF - Human Format
const downloadHF = () => {
    if (isDownloadHFRunning.value == true) {
        return;
    }
    isDownloadHFRunning.value = true;

    axios.get(API_ADDRESS + "export/allcardshumanformat").then(function (response) {

        const fileName = 'data.txt';

        const blob = new Blob([response.data.data.text], { type: 'text/plain' });
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;

        document.body.appendChild(link);
        link.click();

        document.body.removeChild(link);
        isDownloadHFRunning.value = false;
    }).catch(function (error) {
        isDownloadHFRunning.value = false;
        console.log(error);
        router.push("/user/login")
    });
}
const moveToClassView = () => {
    router.push('/')
}

</script>

<template>
    <div class="m-4">
        <div class="level">
            <div class="level-left">
                <div class="level-item">
                    <button @click="moveToClassView" class="button">
                        <img src="/src/icons/arrow_back_FILL0_wght400_GRAD0_opsz24.svg">
                        Back
                    </button>
                </div>
                <div class="level-item">
                    <h1 class="title">Export data</h1>
                </div>
            </div>
        </div>


        <p class="content has-text-centered-mobile">You can export data in csv format.</p>
        <div class="has-text-centered-mobile">
        <button class="button is-primary" @click="downloadHF">
            <div v-if="isDownloadHFRunning == false">
                <div class="level is-mobile">
                    <div class="level-item">
                        <img src="/src/icons/download_FILL0_wght400_GRAD0_opsz24.svg">
                    </div>
                    <div class="level-item">
                        <p>Download</p>
                    </div>
                </div>
               
              
            </div>
            <div class="loader" v-if="isDownloadHFRunning == true"></div>
        </button>
    </div>
    </div>
</template>