<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

const isHiddenVisible = ref(false)
const progressBarWidth = ref('45') //learning progress bar from 0-100
var sessionId = route.params.sessionid;
var mark_one_count = ref(null);
var mark_two_count = ref(null);
var mark_three_count = ref(null);
var mark_four_count = ref(null);
var mark_five_count = ref(null);
var mark_none_count = ref(null);

var deck = ref(null);
var card = ref(null);

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

var hiddenImagePart = ref("")
var visibleImagePart = ref("")

const isBurgerMenuOpen = ref(false);

const isSettingsOpened = ref(false);

//maximum number of cards user can have in "learning mode"
//These cards will repeat until learned
const maximumNumberOfCardsInLearning = ref(3);

//false = first will be shown visible side
//true= first will be shown hidden side
const showReversedSides = ref(false)


watch(maximumNumberOfCardsInLearning, (newValue, oldValue) => {
    axios.post(API_ADDRESS + 'learnsession/update',
        {
            sessionId: sessionId,
            numberOfCardsInActiveLearning: newValue
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
    }).catch(function (err) {
        somethingFailed(err);
    })
});

const toggleBurgerMenu = () => {
    isBurgerMenuOpen.value = !isBurgerMenuOpen.value;
};

const somethingFailed = (error) => {
    console.log(error)
    router.push("/user/login")
}

const showHidden = () => {
    isHiddenVisible.value = true
}

const moveToDeck = () => {
    router.push('/deck/' + deck.value.id);
}

const openLearningSettings = () => {
    isSettingsOpened.value = !isSettingsOpened.value
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

const fetchSession = () => {
    //fetchSession
    axios.get(API_ADDRESS + 'learnsession/' + sessionId
    ).then(function (result) {
        maximumNumberOfCardsInLearning.value = result.data.data.numberOfCardsInActiveLearning;
        progressBarWidth.value = result.data.data.progress;
    }).catch(function (err) {
        somethingFailed(err);
    })
}

const next = (mark = null) => {
    if (isHiddenVisible.value == false && mark != null) {
        return;
    }

    const fetchNextCard = () => {
        axios.post(API_ADDRESS + 'learn/next',
            {
                id: sessionId
            },
            {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
        ).then(function (result) {
            card.value = result.data.data.theCard;
            deck.value = card.value.deckEntity

            mark_none_count.value = result.data.data.markCount.mark_zero;
            mark_one_count.value = result.data.data.markCount.mark_one;
            mark_two_count.value = result.data.data.markCount.mark_two;
            mark_three_count.value = result.data.data.markCount.mark_three;
            mark_four_count.value = result.data.data.markCount.mark_four;
            mark_five_count.value = result.data.data.markCount.mark_five;

            isHiddenVisible.value = false;
            fetchSession();
            loadImages();
        }).catch(function (err) {
            somethingFailed(err);
        })

    }


    if (mark != null) {
        axios.post(API_ADDRESS + 'card/update',
            {
                id: card.value.id,
                mark: mark
            },
            {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
        ).then(function (result) {
            if (result.status == 200) {
                fetchNextCard();
            }
        }).catch(function (err) {
            somethingFailed(err);
        })
    } else {
        fetchNextCard();
    }
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

const handleKeyDown = (event) => {
    if (event.key === ' ') {
        if (!isHiddenVisible.value) {
            isHiddenVisible.value = true;
        }
    }
    if (event.key === '1') {
        if (isHiddenVisible) {
            next(1);
        }
    }
    if (event.key === '2') {
        if (isHiddenVisible) {
            next(2);
        }
    }
    if (event.key === '3') {
        if (isHiddenVisible) {
            next(3);
        }
    }
    if (event.key === '4') {
        if (isHiddenVisible) {
            next(4);
        }
    }
    if (event.key === '5') {
        if (isHiddenVisible) {
            next(5);
        }
    }
    if (event.key === '0') {
        if (isHiddenVisible) {
            next(0);
        }
    }
};

const loadImages = () => {
    if (card.value == null) {
        return;
    }
    hiddenImagePart.value = "";
    visibleImagePart.value = "";
    axios.post(API_ADDRESS + 'image/find', {
        "cardid": card.value.id
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
            somethingFailed(error)
        });
}

onMounted(() => {
    window.addEventListener('keydown', handleKeyDown);
    fetchSession();
    next();

});
onUnmounted(() => {
    window.removeEventListener('keydown', handleKeyDown);
});


watch(card, async () => {
    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
    await sleep(1);
    try {
        const firstTextArea = document.getElementById('firstTextArea')
        firstTextArea.style.height = 'auto';
        firstTextArea.style.height = `${firstTextArea.scrollHeight}px`;
    } catch (e) { }
    try {
        const secondTextArea = document.getElementById('secondTextArea')
        secondTextArea.style.height = 'auto';
        secondTextArea.style.height = `${secondTextArea.scrollHeight}px`;
    } catch (e) { }
    try {
        const thirdTextArea = document.getElementById('thirdTextArea')
        thirdTextArea.style.height = 'auto';
        thirdTextArea.style.height = `${thirdTextArea.scrollHeight}px`;
    } catch (e) { }
    try {
        const fourthTextArea = document.getElementById('fourthTextArea')
        fourthTextArea.style.height = 'auto';
        fourthTextArea.style.height = `${fourthTextArea.scrollHeight}px`;
    } catch (e) { }
})

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





    <div v-if="!isSettingsOpened" style="overflow-x: hidden;">
        <!--Navbar-->

        <header>
            <div class="container" style="height: 52px;">
                <div class="buttons">
                    <button @click="moveToDeck" class="button">
                        <img src="/src/icons/arrow_back_FILL0_wght400_GRAD0_opsz24.svg">
                        Exit
                    </button>
                    <button class="button" @click="openLearningSettings">
                        <img src="/src/icons/settings_FILL0_wght400_GRAD0_opsz24.svg">
                        Setup
                    </button>

                </div>
            </div>
        </header>


        <div class='container hero is-fullheight-with-navbar '>


            <div class="columns">

                <!--Left side stats-->
                <div class="column is-one-quarter is-hidden-touch">
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
                </div>


                <div class="m-4 is-hidden-desktop">
                    <progress class="progress is-primary " :value="progressBarWidth" max="100">{{ progressBarWidth
                        }}%</progress>
                </div>
                <div class="column">

                    <div v-if="card != null">
                        <div class="box">

                            <div v-if="!showReversedSides">
                                <img v-if="visibleImagePart != ''" :src="visibleImagePart">
                                <textarea id="firstTextArea" class="textarea" readonly>{{ card.visiblePart }}</textarea>
                            </div>

                            <div v-if="showReversedSides">
                                <img v-if="hiddenImagePart != ''" :src="hiddenImagePart">
                                <textarea id="secondTextArea" class="textarea" readonly>{{ card.hiddenPart }}</textarea>
                            </div>
                        </div>
                        <div class="box" v-if="isHiddenVisible">
                            <div v-if="showReversedSides">
                                <img v-if="visibleImagePart != ''" :src="visibleImagePart">
                                <textarea id="thirdTextArea" class="textarea" readonly>{{ card.visiblePart }}</textarea>
                            </div>

                            <div v-if="!showReversedSides">
                                <img v-if="hiddenImagePart != ''" :src="hiddenImagePart">
                                <textarea id="fourthTextArea" class="textarea" readonly>{{ card.hiddenPart }}</textarea>
                            </div>
                        </div>



                    </div>
                </div>

            </div>


            <footer class="m-2">
                <button v-if="!isHiddenVisible" @click="showHidden" class="button is-link"
                    style="width: 100%; padding: 15px; font-size: 18px; min-height: 100px;">Reveal answer</button>

                <div v-if="isHiddenVisible" style="width: 100%;">
                    <button @click="next(1)" class="button mark-1 markedbutton">1</button>
                    <button @click="next(2)" class="button mark-2 markedbutton">2</button>
                    <button @click="next(3)" class="button mark-3 markedbutton">3</button>
                    <button @click="next(4)" class="button mark-4 markedbutton">4</button>
                    <button @click="next(5)" class="button mark-5 markedbutton">5</button>
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