
<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
let tridy = ref([]);
let selectedTrida = ref(null);
let selectedTridaDecks = ref(null);

const getAllTrida = () => {
    //const axios = require('axios');

    let config = {
        method: 'get',
        maxBodyLength: Infinity,
        url: API_ADDRESS + 'tridy/all',
        headers: {}
    };

    axios.request(config)
        .then((response) => {
            tridy.value = response.data;
        })
        .catch((error) => {
            console.log(error);
        });

}
const findDecks = (tridaid) => {
    axios.post(API_ADDRESS + 'deck/find',
        {
            tridaid: tridaid,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        selectedTridaDecks.value = result.data;

    }).catch(function (err) {
        console.log(err);
    })
}

const clickTrida = (id) => {

    for (let trida of tridy.value) {
        if (trida.id == id) {
            selectedTrida.value = trida;
            findDecks(selectedTrida.value.id);
            break;
        }
    }

}

const clickDeck = (id) => {
    router.push('deck/' + id);
}

const createNewDeck = () => {
    router.push('/'+selectedTrida.value.id+'/new')
}
const createNewTrida = () => {
    router.push('/new')
}



getAllTrida();


</script>


<template>
    <div class="columns">
        <div class="column is-one-third">
            <aside class="menu">
                <p class="menu-label">
                    Classes
                </p>
                <ul class="menu-list">
                    <li v-for="(trida) in tridy">
                        <a :class="{ 'is-active': trida == selectedTrida }" @click="clickTrida(trida.id)">{{ trida.name
                        }}</a>
                    </li>

                    <li>
                        <a @click="createNewTrida">Create new class</a>
                    </li>

                </ul>
            </aside>

        </div>
        <div class="column">
            <aside class="menu">
                <p class="menu-label">
                    {{ selectedTrida?.name ?? 'None selected' }}
                </p>
                <ul class="menu-list">
                    <li v-for="(deck) in selectedTridaDecks">
                        <a @click="clickDeck(deck.id)">{{ deck.name }}</a>
                    </li>
                    <li v-if="selectedTrida != null">
                        <a @click="createNewDeck">Create new deck</a>
                    </li>
                </ul>
            </aside>

        </div>
    </div>
</template>
