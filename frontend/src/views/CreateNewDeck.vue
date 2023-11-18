<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();


// Data properties
const deckName = ref('');

let tridaid = route.params.tridaid;


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
            moveToClasses();
        })
        .catch(function (error) {
            console.log(error);
        });

};


const moveToClasses = () =>{
    router.push('/');
}
</script>


<template>
    <h1>Create new deck</h1>
    <div>
            <label>Deck name:</label>
            <input v-model="deckName"  />
            
            <button @click="moveToClasses">Cancel</button>
            <button @click="createNewDeckForm">Submit</button>

    </div>
</template>
