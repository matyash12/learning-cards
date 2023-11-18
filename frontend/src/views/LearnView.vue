<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

const cards = ref([]);
const activeid = ref(-1);
const isHiddenVisible = ref(false)
const progressBarWidth = ref('45') //learning progress bar from 0-100
let deckid = route.params.deckid;
let mark_one_count = ref(null);
let mark_two_count = ref(null);
let mark_three_count = ref(null);
let mark_four_count = ref(null);
let mark_five_count = ref(null);
let mark_none_count = ref(null);

let deck = ref(null);

const getDeck = () => {
    let config = {
        method: 'get',
        maxBodyLength: Infinity,
        url: API_ADDRESS + 'deck/' + deckid,
        headers: {}
    };

    axios.request(config)
        .then((response) => {
            deck.value = response.data;
        })
        .catch((error) => {
            console.log(error);
        });
}


const numberOfMarksCountCounter = () => {
    mark_none_count.value = null;
    mark_one_count.value = null;
    mark_two_count.value = null;
    mark_three_count.value = null;
    mark_four_count.value = null;
    mark_five_count.value = null;

    for (let card of cards.value) {
        if (card.mark == 0) {
            if (mark_none_count.value == null) {
                mark_none_count.value = 1;
            } else {
                mark_none_count.value += 1
            }
        }
        if (card.mark == 1) {
            if (mark_one_count.value == null) {
                mark_one_count.value = 1;
            } else {
                mark_one_count.value += 1
            }
        }
        if (card.mark == 2) {
            if (mark_two_count.value == null) {
                mark_two_count.value = 1;
            } else {
                mark_two_count.value += 1
            }
        }
        if (card.mark == 3) {
            if (mark_three_count.value == null) {
                mark_three_count.value = 1;
            } else {
                mark_three_count.value += 1
            }
        }
        if (card.mark == 4) {
            if (mark_four_count.value == null) {
                mark_four_count.value = 1;
            } else {
                mark_four_count.value += 1
            }
        }
        if (card.mark == 5) {
            if (mark_five_count.value == null) {
                mark_five_count.value = 1;
            } else {
                mark_five_count.value += 1
            }
        }
    }
}

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
        updateUI();

        if (activeid.value == -1) {
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
const nextMark = (mark) => {
    axios.post(API_ADDRESS + 'card/update',
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
const moveToDeck = () => {
    router.push('/deck/' + deckid);
}

const updateUI = () => {
    updateProgressBar();
    numberOfMarksCountCounter();
}

const handleKeyDown = (event) => {
    
    if (event.key === ' '){
        if (!isHiddenVisible.value){
            isHiddenVisible.value = true;
        }
    }
    if (event.key === '1'){
        if (isHiddenVisible){
            nextMark(1);
        }
    }
    if (event.key === '2'){
        if (isHiddenVisible){
            nextMark(2);
        }
    }
    if (event.key === '3'){
        if (isHiddenVisible){
            nextMark(3);
        }
    }
    if (event.key === '4'){
        if (isHiddenVisible){
            nextMark(4);
        }
    }
    if (event.key === '5'){
        if (isHiddenVisible){
            nextMark(5);
        }
    }
    if (event.key === '0'){
        if (isHiddenVisible){
            next();
        }
    }
};

onMounted(() => {
    window.addEventListener('keydown', handleKeyDown);
});
onUnmounted(() => {
    window.removeEventListener('keydown', handleKeyDown);
});

findCards();
getDeck();

</script>

<template>
    <div class="columns">
        <div class="column is-one-quarter">
            <h1>{{ deck?.tridaEntity.name ?? 'loading..' }}</h1>
            <h2>{{deck?.name ?? 'loading..'}}</h2>
            <button @click="moveToDeck">Deck</button>
            <progress class="progress" :value="progressBarWidth" max="100">progressBarWidth</progress>
            <table class="table">
                <thead>
                    <tr>
                        <th>Mark</th>
                        <th>Count</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1</td>
                        <td>{{ mark_one_count ?? 0 }}</td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>{{ mark_two_count ?? 0 }}</td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>{{ mark_three_count ?? 0 }}</td>
                    </tr>
                    <tr>
                        <td>4</td>
                        <td>{{ mark_four_count ?? 0 }}</td>
                    </tr>
                    <tr>
                        <td>5</td>
                        <td>{{ mark_five_count ?? 0 }}</td>
                    </tr>
                    <tr>
                        <td>None</td>
                        <td>{{ mark_none_count ?? 0 }}</td>
                    </tr>
                </tbody>
            </table>


        </div>
        <div class="column">
            <h1>Learn</h1>
            



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



        </div>
    </div>
</template>