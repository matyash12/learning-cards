<script setup>
import { ref } from 'vue';

const cards = ref([]);
const activeid = ref(-1);
const isHiddenVisible = ref(false)



const update = () => {
    axios.get('http://localhost:8080/card/all').then(function (result) {
        if (result.status == 200) {
            cards.value = result.data;
            next();
        }
    }).catch(function (err) {
        console.log(err);
    })
}
const showHidden = () => {
    isHiddenVisible.value = true
}
const next = () => {
    if (cards.value.length > 0) {
        if (cards.value.length > activeid.value) {
            activeid.value = activeid.value + 1;
        }
        if (cards.value.length == activeid.value) {
            activeid.value = 0;
        }
    }
    isHiddenVisible.value = false;
}

update();



</script>

<template>
    <h1>Learn</h1>
    <div v-if="activeid != -1">
        <p>{{ cards[activeid].hiddenPart }}</p>


        <p v-if="isHiddenVisible">{{ cards[activeid].visiblePart }}</p>

        <button @click="showHidden">Show hidden</button>
        <button @click="next">Next</button>
    </div>
</template>