<script setup>
import axios from 'axios';

import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();


// Data properties
const deckName = ref('');

let deckid = route.params.deckid;


// Methods
const renameDeck = () => {
    axios.post(API_ADDRESS + 'deck/update', {
        'id':deckid,
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
            console.log(response);
            moveToDeck();
        })
        .catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });

};

const loadDeck = () =>{
    axios.get(API_ADDRESS+'deck/'+deckid).then(function(response){
        deckName.value = response.data.name;
    }).catch(function(error){
        router.push("/user/login")
            console.log(error);
    });
}

loadDeck();

const moveToDeck = () => {
    router.push('/deck/'+deckid);
}
</script>


<template>
    <div>
        <h1 class="title">Rename deck</h1>

        <div class="box">
        <div class="field">
            <label class="label">New name for deck</label>
            <div class="control">
                <input v-model="deckName" class="input" type="text" placeholder="Deck name">
            </div>
        </div>

        <div class="field is-grouped">
            <div class="control">
                <button class="button is-primary" @click="renameDeck">Rename</button>
            </div>
            <div class="control">
                <button class="button is-link is-light" @click="moveToDeck">Cancel</button>
            </div>
        </div>
    </div>

    </div>
</template>
