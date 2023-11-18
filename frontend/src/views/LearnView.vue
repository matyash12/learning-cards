<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

const cards = ref([]);
const activeid = ref(-1);
const isHiddenVisible = ref(false)
const progressBarWidth = ref('45') //learning progress bar from 0-100
let deckid = route.params.deckid;


const findCards = () => {
    axios.post(API_ADDRESS + 'card/find',
        {
            deckid: deckid,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        cards.value = result.data;
        if (activeid.value == -1) {
            next();
            updateProgressBar();
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
    updateProgressBar();
}
const nextMark = (mark) => {
    axios.post('http://localhost:8080/card/update',
        {
            id: cards.value[activeid.value].id,
            mark: mark
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        if (result.status == 200) {
            findCards();
            findCards();
        }
    }).catch(function (err) {
        console.log(err);
    })
    next();
}

const updateProgressBar = () => {
    var maximumValueOfMarks = cards.value.length * 4;
    var actualValueOfMarks = 0;
    cards.value.forEach((element) => {
        var mark = element.mark;
        switch (mark) {
            case 0:
                mark = 4;
                break;
            case 1:
                mark = 0;
                break;
            case 2:
                mark = 1;
                break;
            case 3:
                mark = 2;
                break;
            case 4:
                mark = 3;
                break;
            case 5:
                mark = 4;
                break;
            default:
                break;
        }

        actualValueOfMarks += mark;

    });
    progressBarWidth.value = (1 - (actualValueOfMarks / maximumValueOfMarks)) * 100;
}
const moveToDeck = () =>{
    router.push('/deck/'+deckid);
}


findCards();



</script>

<template>
    <h1>Learn</h1>
    <button @click="moveToDeck">Deck</button>
    <progress class="progress" :value="progressBarWidth" max="100">progressBarWidth</progress>



    <div v-if="activeid != -1">
        <p>{{ cards[activeid].hiddenPart }}</p>


        <p v-if="isHiddenVisible">{{ cards[activeid].visiblePart }}</p>

        <button v-if="!isHiddenVisible" @click="showHidden">Show hidden</button>
        <div v-if="isHiddenVisible">

            <button @click="nextMark(1)">1</button>
            <button @click="nextMark(2)">2</button>
            <button @click="nextMark(3)">3</button>
            <button @click="nextMark(4)">4</button>
            <button @click="nextMark(5)">5</button>
            <button v-if="isHiddenVisible" @click="next">Skip</button>
        </div>

    </div>
</template>