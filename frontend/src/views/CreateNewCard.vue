<script setup>
import { ref } from 'vue';


// Data properties
const hiddenPart = ref('');
const visiblePart = ref('');
const selectedDeckid = ref(-1);
const decks = ref([]);

// Methods
const createNewCardForm = () => {
    axios.post('http://localhost:8080/card/new', {
        hiddenPart: hiddenPart.value,
        visiblePart: visiblePart.value,
        deckid: selectedDeckid.value
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            //console.log(response);
        })
        .catch(function (error) {
            console.log(error);
        });

};
const update = () => {
    axios.get('http://localhost:8080/deck/all')
        .then(function (response) {
            decks.value = response.data;
        })
        .catch(function (error) {
            console.log(error);
        });
}

update();


</script>


<template>
    <h1>Create new card</h1>
    <div>
        <form @submit="createNewCardForm">
            <label>Hidden part:</label>
            <input v-model="hiddenPart" required />

            <label>Visible part:</label>
            <input v-model="visiblePart" required />

            <label>Deck:</label>
            <select v-model="selectedDeckid" class="form-select">
                <option selected>Select deck</option>
                <option v-for="(deck) in decks" :key="deck.id" :value="deck.id">
                    {{ deck.name }}
                </option>
            </select>

            <button type="submit">Submit</button>
        </form>

    </div>
</template>
