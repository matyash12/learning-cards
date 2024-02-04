<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js'; 
const store = notificationStore();
const router = useRouter();
const route = useRoute();


// Data properties
const deckName = ref('');
let deckid = route.params.deckid;

//is running
const isRenameDeckRunning = ref(false);
const isLoadDeckRunning = ref(true);

// Methods
const renameDeck = () => {
    if (isRenameDeckRunning.value == true){
        return;
    }
    isRenameDeckRunning.value = true;
    axios.post(API_ADDRESS + 'deck/update', {
        'id': deckid,
        'name': deckName.value,
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            store.newNotification("Deck was renamed",false,"is-info",3);
            isRenameDeckRunning.value = false;
            console.log(response);
            moveToDeck();
        })
        .catch(function (error) {
            isRenameDeckRunning.value = false;
            router.push("/user/login")
            console.log(error);
        });

};

const loadDeck = () => {
    axios.get(API_ADDRESS + 'deck/' + deckid).then(function (response) {
        isLoadDeckRunning.value = false;
        deckName.value = response.data.data.name;
    }).catch(function (error) {
        isLoadDeckRunning.value = false;
        router.push("/user/login")
        console.log(error);
    });
}

loadDeck();

const moveToDeck = () => {
    router.push('/deck/' + deckid);
}
</script>


<template>
    <div class="m-4">
        <div>
            <h1 class="title">Rename deck</h1>

            <div class="box">
                <div class="field">
                    <label class="label">New name for deck</label>
                    <div class="control">
                        <div class="loader" v-if="isLoadDeckRunning == true"></div> 
                        <input v-if="isLoadDeckRunning == false" v-model="deckName" class="input" type="text" placeholder="Deck name">
                    </div>
                </div>

                <div class="field is-grouped">
                    <div class="control">
                        <button class="button is-primary" @click="renameDeck">
                        <div class="loader" v-if="isRenameDeckRunning == true"></div>
                        <p v-if="isRenameDeckRunning == false">Rename</p>
                        </button>
                    </div>
                    <div class="control">
                        <button class="button is-link is-light" @click="moveToDeck">Cancel</button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
