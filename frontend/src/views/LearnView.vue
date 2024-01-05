<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
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


let hiddenImagePart = ref("")
let visibleImagePart = ref("")


//maximum number of cards user can have in "learning mode"
//These cards will repeat until learned
const maximumNumberOfCardsInLearning = ref(3);


//false = first will be shown visible side
//tru = first will be shown hidden side
const showReversedSides = ref(false)

const getDeck = () => {
    let config = {
        method: 'get',
        maxBodyLength: Infinity,
        url: API_ADDRESS + 'deck/' + deckid,
        headers: {}
    };

    axios.request(config)
        .then((response) => {
            deck.value = response.data.data;
        })
        .catch((error) => {

            console.log(error);
            somethingFailed();
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
        console.log("new cards from findCards")
        cards.value = result.data.data;
        updateUI();

        if (activeid.value == -1) {
            next();
        }

    }).catch(function (err) {

        console.log(err);
        somethingFailed();
    })
}


const showHidden = () => {
    isHiddenVisible.value = true
}


const cardsInLearning = ref([]);


//clears cardsInLearning from cards with mark 1
const dropCardsThatYouKnow = () => {

    cardsInLearning.value = cardsInLearning.value.filter(function (card) {
        return card.mark != 1;
    });

}
function getRandomElement(arr) {
    const randomIndex = Math.floor(Math.random() * arr.length);
    return arr[randomIndex];
}
//returns cardsInLearning with random card which is not cardsInLearning
const randomFillerCardItCantBeIncardsInLearning = () => {
    var okCards = []
    for (var card of cards.value) {
        var cardIdOkToBeAdded = true;
        for (var cardChecking of cardsInLearning.value) {
            if (cardChecking.id == card.id) {
                //ok and add the card

                cardIdOkToBeAdded = false;
            }
        }
        if (cardIdOkToBeAdded == true) {
            okCards.push(card);

        }
    }

    if (okCards.length == 0) {
        return null;
    } else {
        var randomCard = getRandomElement(okCards);
        return randomCard;
    }


}

const next = () => {

    dropCardsThatYouKnow();

    if (cards.value.length > 0) {


        //Add card in cardsInLearning
        while (cardsInLearning.value.length < maximumNumberOfCardsInLearning.value) {
            var cardAdded = false;

            for (var card of cards.value) {
                if (card.mark != 1) {
                    var cardIdOkToBeAdded = true;
                    for (var cardChecking of cardsInLearning.value) {
                        if (cardChecking.id == card.id) {
                            //ok and add the card

                            cardIdOkToBeAdded = false;
                        }
                    }
                    if (cardIdOkToBeAdded == true) {
                        cardsInLearning.value.push(card);
                        cardAdded = true;

                        break;
                    }
                }
            }
            if (cardAdded == false) {
                break;
            }
        }
        //This runs when cardmanager is unable to find enough cards with bad marks
        while (cardsInLearning.value.length < maximumNumberOfCardsInLearning.value) {
            var maybecard = randomFillerCardItCantBeIncardsInLearning();
            if (maybecard != null) {
                cardsInLearning.value.push(maybecard);
            } else {
                //there arent any more cards
                break;
            }

        }





        if (cardsInLearning.value.length > activeid.value) {
            activeid.value = activeid.value + 1;
        }
        if (cardsInLearning.value.length == activeid.value) {
            activeid.value = 0;
        }



    }




    loadImages();
    isHiddenVisible.value = false;
}
const nextMark = (mark) => {
    if (isHiddenVisible.value == false) {
        return;
    }

    axios.post(API_ADDRESS + 'card/update',
        {
            id: cardsInLearning.value[activeid.value].id,
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
        somethingFailed();
    })
    next();
}

const somethingFailed = () => {
    console.log("Something failed!")
    router.push("/user/login")
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

    if (event.key === ' ') {
        if (!isHiddenVisible.value) {
            isHiddenVisible.value = true;
        }
    }
    if (event.key === '1') {
        if (isHiddenVisible) {
            nextMark(1);
        }
    }
    if (event.key === '2') {
        if (isHiddenVisible) {
            nextMark(2);
        }
    }
    if (event.key === '3') {
        if (isHiddenVisible) {
            nextMark(3);
        }
    }
    if (event.key === '4') {
        if (isHiddenVisible) {
            nextMark(4);
        }
    }
    if (event.key === '5') {
        if (isHiddenVisible) {
            nextMark(5);
        }
    }
    if (event.key === '0') {
        if (isHiddenVisible) {
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
const loadImages = () => {
    if (activeid.value == -1) {
        return;
    }
    hiddenImagePart.value = "";
    visibleImagePart.value = "";
    axios.post(API_ADDRESS + 'image/find', {
        "cardid": cardsInLearning.value[activeid.value].id
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    )
        .then(function (response) {
            hiddenImagePart.value = "";
            visibleImagePart.value = "";
            for (const imageR of response.data.data) {
                if (imageR.position == 0) {
                    hiddenImagePart.value = API_ADDRESS + 'image/show/' + imageR.id;
                }
                if (imageR.position == 1) {
                    visibleImagePart.value = API_ADDRESS + 'image/show/' + imageR.id;
                }
            }

        }).catch(function (error) {
            //router.push("/user/login")
            console.log(error);
        });
}

const isSettingsOpened = ref(false);
const openLearningSettings = () => {
    isSettingsOpened.value = !isSettingsOpened.value
}

const optionForNumberOfcards = ref([
    { text: '3 cards', value: 3 },
    { text: '4 cards', value: 4 },
    { text: '5 cards', value: 5 },
    { text: '6 cards', value: 6 },
    { text: '7 cards', value: 7 },
    { text: '8 cards', value: 8 },
    { text: '9 cards', value: 9 },
    { text: '10 cards', value: 10 },
    { text: '11 cards', value: 11 },
    { text: '12 cards', value: 12 },
    { text: '13 cards', value: 13 },
    { text: '14 cards', value: 14 },
    { text: '15 cards', value: 15 },
    { text: '16 cards', value: 16 },
    { text: '17 cards', value: 17 },
    { text: '18 cards', value: 18 },
    { text: '19 cards', value: 19 },
    { text: '20 cards', value: 20 },
    { text: '21 cards', value: 21 },
    { text: '22 cards', value: 22 }
]);

findCards();
getDeck();
loadImages();
</script>

<template>
    <!--Settings START-->
    <div v-if="isSettingsOpened">
        <nav class="navbar">
            <div class="navbar-menu is-active">
                <!-- shown on mobile -->
                <div class="navbar-end">
                    <div class="navbar-start">
                        <a class="navbar-item">

                        </a>
                        <a class="navbar-item">
                            <button @click="openLearningSettings" class="button">Done and save</button>
                        </a>
                    </div>
                </div>
            </div>
        </nav>
        <div class="m-4">
            <!--Cards to learn at once-->
                <div>
                <p class="">Number of cards to learn at once.</p>
                <div class="select">
                    <select v-model="maximumNumberOfCardsInLearning">
                        <option v-for="option in optionForNumberOfcards" :value="option.value">
                            {{ option.text }}
                        </option>
                    </select>
                </div>
            </div>


                <!--settings for showReversedSides-->
                <div>
                <label class="checkbox">
                    <input type="checkbox" v-model="showReversedSides">
                    Show hidden side first (reverse)
                </label>
            </div>
        </div>


    </div>
    <!--Settings END-->





    <div v-if="!isSettingsOpened">
        <nav class="navbar">
            <div class="navbar-menu is-active">
                <!-- shown on mobile -->
                <div class="navbar-end">
                    <div class="navbar-start">
                        <a class="navbar-item">
                            <button @click="moveToDeck" class="button">
                                Exit
                            </button>

                        </a>
                        <a class="navbar-item">
                            <button @click="openLearningSettings" class="button">Setup</button>
                        </a>
                    </div>
                </div>
            </div>
        </nav>
        <div class='container hero is-fullheight-with-navbar '>


            <div class="columns">
                <div class="column is-one-quarter is-hidden-mobile">
                    <div class="box">
                        <h1 class="title">{{ deck?.tridaEntity.name ?? 'Loading...' }}</h1>
                        <h2 class="subtitle">{{ deck?.name ?? 'Loading...' }}</h2>
                        <progress class="progress is-primary" :value="progressBarWidth" max="100">{{ progressBarWidth
                        }}%</progress>
                        <table class="table is-bordered is-striped is-narrow is-hoverable">
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
                    <!--
                <button @click="moveToDeck" class="button is-link">Go to Deck</button>
                    -->
                </div>

                <div class="column">

                    <div v-if="activeid !== -1">
                        <div class="box">

                            <div v-if="!showReversedSides">
                                <img v-if="visibleImagePart != ''" :src="visibleImagePart">
                                <textarea class="textarea" readonly>{{ cardsInLearning[activeid].visiblePart }}</textarea>
                            </div>

                            <div v-if="showReversedSides">
                                <img v-if="hiddenImagePart != ''" :src="hiddenImagePart">
                                <textarea class="textarea" readonly>{{ cardsInLearning[activeid].hiddenPart }}</textarea>
                            </div>
                        </div>
                        <div class="box" v-if="isHiddenVisible">
                            <div v-if="showReversedSides">
                                <img v-if="visibleImagePart != ''" :src="visibleImagePart">
                                <textarea class="textarea" readonly>{{ cardsInLearning[activeid].visiblePart }}</textarea>
                            </div>

                            <div v-if="!showReversedSides">
                                <img v-if="hiddenImagePart != ''" :src="hiddenImagePart">
                                <textarea class="textarea" readonly>{{ cardsInLearning[activeid].hiddenPart }}</textarea>
                            </div>
                        </div>



                    </div>
                </div>

            </div>
            <!--
        <footer class="has-text-centered m-4">
            <button v-if="!isHiddenVisible" @click="showHidden" class="button is-info is-fullwidth is-fullheight">Show hidden</button>
            <div v-if="isHiddenVisible">
                <div class="buttons">
                    
                    
                    
                   
                    
                </div>
                
                <div class="columns is-mobile is-gapless m-4">
                    <div class="column">
                        <button @click="nextMark(1)" class="button is-success is-responsible is-large">1</button>
                    </div>
                    <div class="column">
                        <button @click="nextMark(2)" class="button is-success is-responsible is-large ">2</button>
                    </div>
                    <div class="column">
                        <button @click="nextMark(3)" class="button is-success is-responsible is-large">3</button>
                    </div>
                    <div class="column">
                        <button @click="nextMark(4)" class="button is-success is-responsible is-large">4</button>
                    </div>
                    <div class="column">
                        <button @click="nextMark(5)" class="button is-success is-responsible is-large">5</button>
                    </div>
                </div>
               
                    
                <button v-if="isHiddenVisible" @click="next" class="button is-primary is-light">Skip</button>

            </div>
        </footer>
    -->


            <footer class="m-2">
                <button v-if="!isHiddenVisible" @click="showHidden" class="button is-link"
                    style="width: 100%; padding: 15px; font-size: 18px; min-height: 100px;">Reveal answer</button>

                <div v-if="isHiddenVisible" style="width: 100%;">
                    <button @click="nextMark(1)" class="button mark-1 markedbutton">1</button>
                    <button @click="nextMark(2)" class="button mark-2 markedbutton">2</button>
                    <button @click="nextMark(3)" class="button mark-3 markedbutton">3</button>
                    <button @click="nextMark(4)" class="button mark-4 markedbutton">4</button>
                    <button @click="nextMark(5)" class="button mark-5 markedbutton">5</button>
                </div>
            </footer>

        </div>
    </div>
</template>

<style scoped>
.markedbutton {
    width: 20%;
    padding: 15px;
    font-size: 18px;
    min-height: 100px;
}

/* Define a gradient from red to green */
/* Define a gradient from green to red */
.gradient {
    background: linear-gradient(to right, green, yellow, red);
}

/* Apply colors to marks from 1 to 5 using gradient stops */
.mark-1 {
    background-color: green;
    /* or any other color you prefer for 1 */
}

.mark-2 {
    background-color: #99cc00;
    /* or any other color you prefer for 2 using hexadecimal value */
}

.mark-3 {
    background-color: yellow;
    /* or any other color you prefer for 3 */
}

.mark-4 {
    background-color: #ff6600;
    /* or any other color you prefer for 4 using hexadecimal value */
}

.mark-5 {
    background-color: red;
    /* or any other color you prefer for 5 */
}
</style>
  