<script setup>
import axios from 'axios';

import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

let deckid = route.params.deckid;
let cardid = route.params.cardid; //new == create new

let visiblePart = ref('');
let hiddenPart = ref('');
let visiblePartInput = ref(null);
let wasLastCardCreated = ref(null);

const createCard = () => {
    axios.post(API_ADDRESS + 'card/new', {
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
            visiblePart.value = '';
            hiddenPart.value = '';
            visiblePartInput.value.focus();
            wasLastCardCreated.value = true;
            router.push('/deck/'+deckid+'/new')
            //moveToDeckView();
        })
        .catch(function (error) {
            router.push("/user/login")
            console.log(error);
            wasLastCardCreated.value = false;
        });

};
const moveToDeckView = () => {
    router.push('/deck/' + deckid)
}

const handleKeyDown = (event) => {
    if (event.key === 'Tab' && event.target.id === 'visiblePart') {
        createCard();
    }
    
    const isInputField = event.target.tagName.toLowerCase() === 'input' ||
        event.target.tagName.toLowerCase() === 'textarea';

    
    // If it's an input field, do nothing
    if (isInputField) {
        return;
    }


    if (event.key === 'c') {
        createCard();
    }
    
    
};

onMounted(() => {
    visiblePartInput.value.focus();
    window.addEventListener('keydown', handleKeyDown);
});
onUnmounted(() => {
    window.removeEventListener('keydown', handleKeyDown);
});

</script>


<template>
    <h1 class="title">Create new card</h1>
    <div>
        <div class="field">
            <label class="label">Hidden side</label>
            <div class="control">
                <input ref="visiblePartInput"  v-model="hiddenPart" class="input" type="text" placeholder="">
            </div>
        </div>
        <div class="field">
            <label class="label">Visible side</label>
            <div class="control">
                <input id="visiblePart" v-model="visiblePart" class="input" type="text" placeholder="">
            </div>
        </div>


        <p v-if="wasLastCardCreated == true" class="is-italic">Card was created</p>

        <div class="field is-grouped">
            <div class="control">
                <button class="button is-primary" @click="createCard">Create (c)</button>
            </div>
            <div class="control">
                <button class="button is-link is-light" @click="moveToDeckView">Cancel</button>
            </div>
        </div>
    </div>
</template>