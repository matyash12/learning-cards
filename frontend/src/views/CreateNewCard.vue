<script setup>
import { ref } from 'vue';


// Data properties
const hiddenPart = ref('');
const visiblePart = ref('');


// Methods
const createNewCardForm = () => {
    axios.post('http://localhost:8080/card/new', {
        hiddenPart: hiddenPart.value,
        visiblePart: visiblePart.value
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
    <h1>Create new card</h1>
    <div>
        <form @submit="createNewCardForm">
            <label>Hidden part:</label>
            <input v-model="hiddenPart" required />

            <label>Visible part:</label>
            <input v-model="visiblePart" required />
            

            <button type="submit">Submit</button>
        </form>

    </div>
</template>
