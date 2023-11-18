<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

let id = route.params.id;
let deck = ref(null);
let cards = ref([]);

const getDeck = (deckid) => {

    let config = {
        method: 'get',
        maxBodyLength: Infinity,
        url: API_ADDRESS + 'deck/1',
        headers: {}
    };

    axios.request(config)
        .then((response) => {
            deck.value = response.data;
        })
        .catch((error) => {
            console.log(error);
        });

}
const findCards = (deckid) => {
    axios.post(API_ADDRESS + 'card/find',
        {
            deckid: deckid,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        cards.value = result.data;

    }).catch(function (err) {
        console.log(err);
    })
}

const createNewCard =() =>{
    router.push('/deck/'+id+'/'+'new');
}
const moveToClassView = () =>{
    router.push('/')
}
const moveToLearning = () =>{
    router.push('/learn/'+id);
}

getDeck(id);
findCards(id);

</script>


<template>
    <button @click="moveToLearning">Learn</button>
    <button @click="createNewCard">Add</button>
    <button @click="moveToClassView">Classes</button>
    <h1>{{ deck?.name ?? "loading..." }}</h1>
    <table class="table">
        <thead>
            <tr>
                <th>#</th>
                <th>Visible side</th>
                <th>Hidden side</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(card, index) in cards">
                <td>{{ index + 1 }}</td>
                <td>{{ card.visiblePart }}</td>
                <td>{{ card.hiddenPart }}</td>

            </tr>
        </tbody>
    </table>
</template>