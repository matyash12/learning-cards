

<script setup>
import axios from 'axios';

import { API_ADDRESS } from '../helpers.js';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
let tridy = ref([]);
let selectedTrida = ref(null);
let selectedTridaDecks = ref(null);

//HF - Human Format
const downloadHF = () => {
    axios.get(API_ADDRESS + "export/allcardshumanformat").then(function (response) {

        const fileName = 'data.txt';

        const blob = new Blob([response.data.data.text], { type: 'text/plain' });
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;

        document.body.appendChild(link);
        link.click();

        document.body.removeChild(link);

    }).catch(function (error) {
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
        <div class="buttons">
            <button @click="moveToClassView" class="button is-info">Classes</button>
        </div>
        <h1 class="title">Export data</h1>
        <p class="content">You can export data in csv format.</p>
        <button class="button" @click="downloadHF">Download</button>
    </div>
</template>