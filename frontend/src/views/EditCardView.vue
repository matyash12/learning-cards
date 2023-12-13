<script setup>
import axios from 'axios';

import { API_ADDRESS, isValidField } from '../helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

let deckid = route.params.deckid;
let cardid = route.params.cardid;


let visiblePart = ref('');
let hiddenPart = ref('');
let markValue = ref(0);
let visiblePartInput = ref(null);
let wasLastCardCreated = ref(null);

const loadCardDetails = () => {
    axios.get(API_ADDRESS + 'card/' + cardid)
        .then(function (response) {
            visiblePart.value = response.data.visiblePart;
            hiddenPart.value = response.data.hiddenPart;
            markValue.value = response.data.mark;

        }).catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });
}

loadCardDetails();



const updateCard = () => {
    if (!isValidField(hiddenPart.value) || !isValidField(visiblePart.value)) {
        showWarning.value = true;
        warningMessage.value = "You can't have empty side."
        return;
    }



    axios.post(API_ADDRESS + 'card/update', {
        hiddenPart: hiddenPart.value,
        visiblePart: visiblePart.value,
        mark: markValue.value,
        id: cardid
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            visiblePart.value = '';
            hiddenPart.value = '';
            visiblePartInput.value.focus();
            wasLastCardCreated.value = true;
            router.push('/deck/' + deckid)
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
        updateCard();
    }

    const isInputField = event.target.tagName.toLowerCase() === 'input' ||
        event.target.tagName.toLowerCase() === 'textarea';


    // If it's an input field, do nothing
    if (isInputField) {
        return;
    }


    if (event.key === 'c') {
        updateCard();
    }


};

const setMark = (mark) =>{

    markValue.value = mark;


}


onMounted(() => {
    visiblePartInput.value.focus();
    window.addEventListener('keydown', handleKeyDown);
});
onUnmounted(() => {
    window.removeEventListener('keydown', handleKeyDown);
});

const userChangesValue = () => {
    showWarning.value = false;
    wasLastCardCreated.value = false;
}

const showWarning = ref(false);
const warningMessage = ref('');

const closeWarning = () => {
    showWarning.value = false;
    warningMessage.value = '';
}

</script>


<template>
    <h1 class="title">Edit card</h1>
    <div>
        <div class="field">
            <label class="label">Hidden side</label>
            <div class="control">
                <textarea ref="visiblePartInput" v-model="hiddenPart" @input="userChangesValue" class="textarea" type="text"
                    placeholder=""></textarea>
            </div>
        </div>
        <div class="field">
            <label class="label">Visible side</label>
            <div class="control">
                <textarea id="visiblePart" v-model="visiblePart" @input="userChangesValue" class="textarea" type="text"
                    placeholder=""></textarea>
            </div>
        </div>
        <div class="field">
            <label class="label">Mark</label>
            <div class="buttons">
                <button @click="setMark(0)" :class="{ 'button': true, 'is-success': markValue === 0 }">None</button>
                <button @click="setMark(1)" :class="{ 'button': true, 'is-success': markValue === 1 }">1</button>
                <button @click="setMark(2)" :class="{ 'button': true, 'is-success': markValue === 2 }">2</button>
                <button @click="setMark(3)" :class="{ 'button': true, 'is-success': markValue === 3 }">3</button>
                <button @click="setMark(4)" :class="{ 'button': true, 'is-success': markValue === 4 }">4</button>
                <button @click="setMark(5)" :class="{ 'button': true, 'is-success': markValue === 5 }">5</button>
            </div>
        </div>


        <p v-if="wasLastCardCreated == true" class="is-italic">Card was edited</p>
        <div class="notification is-danger" v-show="showWarning">
            <button class="delete" @click="closeWarning"></button>
            <p v-text="warningMessage"></p>
        </div>
        <div class="field is-grouped">
            <div class="control">
                <button class="button is-primary" @click="updateCard">Save changes (c)</button>
            </div>
            <div class="control">
                <button class="button is-link is-light" @click="moveToDeckView">Cancel</button>
            </div>
        </div>
    </div>
</template>