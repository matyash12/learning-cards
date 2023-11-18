<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

let deckid = route.params.deckid;
let cardid = route.params.cardid; //new == create new

let visiblePart = ref('');
let hiddenPart = ref('');


const createCard = () => {
    axios.post(API_ADDRESS+'card/new', {
        hiddenPart: hiddenPart.value,
        visiblePart: visiblePart.value,
        deckid: deckid
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
            moveToDeckView();
        })
        .catch(function (error) {
            console.log(error);
        });

};
const moveToDeckView = () =>{
    router.push('/deck/'+deckid)
}
</script>


<template>
    <p>Deck id:{{ deckid }}</p>
    <p>Card id:{{ cardid }}</p>


    <div>
        <div class="field">
            <label class="label">Hidden side</label>
            <div class="control">
                <input v-model="hiddenPart" class="input" type="text" placeholder="">
            </div>
        </div>
        <div class="field">
            <label class="label">Visible side</label>
            <div class="control">
                <input v-model="visiblePart" class="input" type="text" placeholder="">
            </div>
        </div>




        <div class="field is-grouped">
            <div class="control">
                <button @click="moveToDeckView" class="button">Cancel</button>
                <button @click="createCard" class="button">Create</button>
            </div>
        </div>
    </div>
</template>
