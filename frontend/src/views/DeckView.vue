<script setup>
import axios from 'axios';
import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js';



const store = notificationStore();
const router = useRouter();
const route = useRoute();

let id = ref(route.params.id);
let deck = ref(null);
let cards = ref([]);

//is loading?
const isGetDeckRunning = ref(false);
const isFindCardsRunning = ref(false);
const isDeleteThisDeckRunning = ref(false);
const isDeleteCardBoolRunning = ref(false);
const isDeleteCardIdRunning = ref(0);

const getDeck = async () => {
    isGetDeckRunning.value = true;
    try {
        const response = await axios.get(`${API_ADDRESS}deck/${id.value}`);
        deck.value = response.data.data;
        isGetDeckRunning.value = false;
    } catch (error) {
        isGetDeckRunning.value = false;
        handleApiError(error);
    }
};

const findCards = async () => {
    isFindCardsRunning.value = true;
    try {
        const result = await axios.post(
            `${API_ADDRESS}card/find`,
            { deckid: id.value },
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );
        cards.value = result.data.data;
        isFindCardsRunning.value = false;
    } catch (err) {
        isFindCardsRunning.value = false;
        handleApiError(err);
    }
};

const createNewCard = () => {
    router.push(`/deck/${id.value}/new`);
};

const moveToClassView = () => {
    router.push('/');
};

const movingToLearningIsRunning = ref(false);
const moveToLearning = () => {
    if (movingToLearningIsRunning.value) {
        return;
    }
    movingToLearningIsRunning.value = true;
    axios.post(API_ADDRESS + 'learnsession/new',
        {
            deckId: id.value,
            numberOfCardsInActiveLearning: 3
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        movingToLearningIsRunning.value = false;
        router.push(`/learn/${result.data.data.id}`);
    }).catch(function (err) {
        movingToLearningIsRunning.value = false;
        handleApiError(err);
    })
};

const refreshDataOnPage = () => {
    getDeck();
    findCards();
};

const deleteCard = async (cardid) => {
    if (isDeleteCardBoolRunning.value == true){
        return;
    }
    isDeleteCardIdRunning.value = cardid;
    isDeleteCardBoolRunning.value = true;
    try {
        await axios.post(
            `${API_ADDRESS}card/delete`,
            { id: cardid },
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );
        isDeleteCardBoolRunning.value = false;
        store.newNotification("Card was deleted", false, "is-info", 3);
        refreshDataOnPage();
    } catch (err) {
        isDeleteCardBoolRunning.value = false;
        handleApiError(err);
    }
};

const deleteThisDeck = async () => {
    if (isDeleteThisDeckRunning.value == true) {
        return;
    }
    isDeleteThisDeckRunning.value = true;
    try {
        await axios.post(
            `${API_ADDRESS}deck/delete`,
            { id: id.value },
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );
        isDeleteThisDeckRunning.value = false;
        store.newNotification("Deck was deleted", false, "is-info", 3);
        moveToClassView();
    } catch (err) {
        isDeleteThisDeckRunning.value = false;
        handleApiError(err);
    }
};

const editCard = (cardid) => {
    router.push(`/deck/${deck.value.id}/${cardid}/edit`);
};

refreshDataOnPage();

const isDeleteConfirmationModalActive = ref(false);

const showDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = true;
};

const hideDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = false;
};

const editDeck = () => {
    router.push(`/deck/${id.value}/edit`);
};

const isBurgerMenuOpen = ref(false);

const toggleBurgerMenu = () => {
    isBurgerMenuOpen.value = !isBurgerMenuOpen.value;
};
const handleApiError = (error) => {
    console.log(error)
    router.push("/user/login")
}
</script>



<template>
    <!--Navbar-->


    <header>
        <nav class="navbar">

            <div class="navbar-brand">
                <a class="navbar-item" @click="moveToClassView">
                    <span class="icon">
                        <ion-icon name="chevron-back-outline" style=" font-size: 64px;"></ion-icon>
                    </span>
                </a>
                <a class="navbar-item">
                    <div class="loader" v-if="isGetDeckRunning == true && isFindCardsRunning == true"></div>
                    <h1 v-if="isGetDeckRunning == false && isFindCardsRunning == false" class="title">{{ deck?.name ??
                        "loading..." }}</h1>
                </a>
                <a class="navbar-item" @click="createNewCard" style="margin-right: 0; margin-left: auto;">
                    <span class="icon">
                        <ion-icon name="add-outline" style="font-size: 64px;"></ion-icon>
                    </span>
                </a>
            </div>


        </nav>
    </header>

    <div class="m-4">
        <div>
            <!-- <h1 class="title">{{ deck?.name ?? "loading..." }}</h1> -->

            <div class="buttons">
                <button @click="moveToLearning" class="button is-primary">
                    <div v-if="movingToLearningIsRunning == true" class="loader"></div>
                    <p v-if="movingToLearningIsRunning == false">Learn</p>

                </button>
                <button @click="editDeck" class="button is-info">Edit</button>
                <!-- <button @click="createNewCard" class="button is-success">Add card</button> -->
                <!-- <button @click="moveToClassView" class="button is-warning">Classes</button> -->
                <button @click="showDeleteConfirmationModal" class="button is-danger">Delete</button>
            </div>

            <table class="table is-fullwidth">
                <thead>
                    <tr>
                        <th class="is-hidden-touch">Mark</th>
                        <th>Visible side</th>
                        <th>Hidden side</th>
                        <th>Edit</th>
                        <th class="is-hidden-touch">Delete</th>
                    </tr>
                </thead>
                <tbody>
                    <!--loading card-->
                    <tr v-if="isGetDeckRunning == true && isFindCardsRunning == true">
                        <td class="is-hidden-touch">
                            <div class="loader"></div>
                        </td>
                        <td>
                            <div class="loader"></div>
                        </td>
                        <td>
                            <div class="loader"></div>
                        </td>
                        <td>
                            <a class="">
                                <div class="loader"></div>
                            </a>
                        </td>
                        <td class="is-hidden-touch">
                            <a class="has-text-danger">
                                <div class="loader"></div>
                            </a>
                        </td>
                    </tr>


                    <!--Actual cards-->
                    <tr v-if="isGetDeckRunning == false && isFindCardsRunning == false" v-for="(card, index) in cards"
                        :key="card.id">
                        <td class="is-hidden-touch">{{ card.mark }}</td>
                        <td>{{ card.visiblePart }}</td>
                        <td>{{ card.hiddenPart }}</td>
                        <td>
                            <a @click="editCard(card.id)" class="">Edit</a>
                        </td>
                        <td class="is-hidden-touch">
                            <a @click="deleteCard(card.id)" class="has-text-danger">
                                <div class="loader" v-if="isDeleteCardIdRunning == card.id && isDeleteCardBoolRunning == true"></div>
                                <p v-if="isDeleteCardIdRunning != card.id">Delete</p>
                            </a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Modal -->
        <div class="modal" :class="{ 'is-active': isDeleteConfirmationModalActive }">
            <div class="modal-background"></div>
            <div class="modal-card">
                <header class="modal-card-head">
                    <p class="modal-card-title">Confirmation</p>
                    <button class="delete" aria-label="close" @click="hideDeleteConfirmationModal()"></button>
                </header>
                <section class="modal-card-body">
                    Are you sure you want to delete this item?
                </section>
                <footer class="modal-card-foot">
                    <button class="button is-danger" @click="deleteThisDeck">
                        <div class="loader" v-if="isDeleteThisDeckRunning == true"></div>
                        <p v-if="isDeleteThisDeckRunning == false">Delete</p>
                    </button>
                    <button class="button" @click="hideDeleteConfirmationModal">Cancel</button>
                </footer>
            </div>
        </div>
    </div>
</template>

