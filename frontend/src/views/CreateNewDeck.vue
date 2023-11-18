<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();


// Data properties
const deckName = ref('');

let tridaid = route.params.tridaid;

let trida = ref(null);
const getTrida = () => {
    //const axios = require('axios');

    let config = {
        method: 'get',
        maxBodyLength: Infinity,
        url: API_ADDRESS + 'tridy/'+tridaid,
        headers: {}
    };

    axios.request(config)
        .then((response) => {
            trida.value = response.data;
        })
        .catch((error) => {
            console.log(error);
        });

}


// Methods
const createNewDeckForm = () => {
    axios.post(API_ADDRESS+'deck/new', {
        'name': deckName.value,
        tridaid:tridaid,
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
            //moveToClasses();
            
            router.push('/deck/'+response.data.id)
        })
        .catch(function (error) {
            console.log(error);
        });

};


const moveToClasses = () =>{
    router.push('/');
}
getTrida();
</script>


<template>
    <div class="m-0">
        <h1 class="title">Create new deck in {{ trida?.name ?? '' }}</h1>

        <div class="box">
        <div class="field">
            <label class="label">Deck name</label>
            <div class="control">
                <input v-model="deckName" class="input" type="text" placeholder="Deck name">
            </div>
        </div>

        <div class="field is-grouped">
            <div class="control">
                <button class="button is-primary" @click="createNewDeckForm">Create</button>
            </div>
            <div class="control">
                <button class="button is-link is-light" @click="moveToClasses">Cancel</button>
            </div>
        </div>
    </div>

    </div>
</template>