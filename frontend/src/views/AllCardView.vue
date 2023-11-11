<script setup>
import { ref } from 'vue';
let cards = ref();

const update = () => {
    axios.get('http://localhost:8080/card/all').then(function (result) {
        if (result.status == 200) {
            cards.value = result.data;
        }
    }).catch(function (err) {
        console.log(err);
    })
}
const deleteCard = (id) => {
    axios.post('http://localhost:8080/card/delete',{
        id:id
    },

    {
        headers:{
            'Content-Type': 'multipart/form-data'
        }
    }
    
    ).then(function (result) {
        update();
    }).catch(function (err) {
        console.log(err);
    });
}

update()

</script>

<template>
    <h1>See all cards</h1>
    <button @click="update">Update</button>
    <table class="table">
        <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Mark</th>
                <th scope="col">Hidden part</th>
                <th scope="col">Visible part</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(card, index) in cards" :key="card.id">
                <th scope="row">{{ index + 1 }}</th>
                <td>{{ card.mark }}</td>
                <td>{{ card.hiddenPart }}</td>
                <td>{{ card.visiblePart }}</td>
                <td><button @click="deleteCard(card.id)">Delete</button></td>

            </tr>
        </tbody>
    </table>
</template>