<script setup>
import { ref } from 'vue';
const decks = ref([]);


const update = () => {
    axios.get('http://localhost:8080/deck/all')
        .then(function (response) {
            decks.value = response.data;
        })
        .catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });
}

const deleteDeck = (id) => {
    axios.post('http://localhost:8080/deck/delete', {
        id: id
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
            update();
        })
        .catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });
}

update();




</script>

<template>
    <table class="table">
        <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Name</th>
                <th scope="col">Delete</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(deck, index) in decks" :key="deck.id">
                <th scope="row">{{ index + 1 }}</th>
                <td>{{ deck.name }}</td>
                <td><button @click="deleteDeck(deck.id)">Delete</button></td>

            </tr>
        </tbody>
    </table>
</template>