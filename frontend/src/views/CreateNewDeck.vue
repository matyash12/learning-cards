<script setup>
import { ref } from 'vue';


// Data properties
const deckName = ref('');


// Methods
const createNewDeckForm = () => {
    axios.post('http://localhost:8080/deck/new', {
        'name': deckName.value
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
        })
        .catch(function (error) {
            console.log(error);
        });

};
</script>


<template>
    <h1>Create new deck</h1>
    <div>
        <form @submit="createNewDeckForm">
            <label>Deck name:</label>
            <input v-model="deckName" required />
            

            <button type="submit">Submit</button>
        </form>

    </div>
</template>
