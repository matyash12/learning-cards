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

let isDeleteThisDeckRunning = ref(false)

const getDeck = (deckid) => {

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
            router.push("/user/login")
            console.log(error);
        });

}
const findCards = (deckid) => {
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
        cards.value = result.data.data;

    }).catch(function (err) {
        router.push("/user/login")
        console.log(err);
    })
}

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
}
const deleteThisDeck = () => {
    isDeleteThisDeckRunning.value = true;

    axios.post(API_ADDRESS + 'deck/delete',
        {
            id: id.value,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        isDeleteThisDeckRunning.value = false;
        store.newNotification("Deck was deleted", false, "is-info", 3);
        moveToClassView();

    }).catch(function (err) {
        isDeleteThisDeckRunning.value = false;
        router.push("/user/login")
        console.log(err);
    })
}

const deleteCard = async (cardid) => {
    try {
        await axios.post(
            `${API_ADDRESS}card/delete`,
            { id: cardid },
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );
        store.newNotification("Card was deleted", false, "is-info", 3);
        refreshDataOnPage();
    } catch (err) {
        handleApiError(err);
    }
};

const editCard = (cardid) => {
    router.push(`/deck/${deck.value.id}/${cardid}/edit`);
};

const refreshDataOnPage = () => {
    getDeck(id.value);
    findCards(id.value);
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
    <div class="m-4">
        <div>
            <div class="block">
                <div class="level">
                    <div class="level-left">
                        <div class="level-item">

                            <button @click="moveToClassView" class="button">
                                <span class="icon">
                                    <img src="/src/icons/arrow_back_FILL0_wght400_GRAD0_opsz24.svg">
                                </span>
                                <span>
                                    Back
                                </span>
                            </button>


                        </div>
                        <div class="level-item">
                            <h1 class="title">
                                {{ deck?.name ?? "loading..." }}
                            </h1>
                        </div>

                    </div>
                    <div class="level-right">
                        <div class="level-item">
                            <button @click="moveToLearning" class="button">
                                <span class="icon">
                                    <img v-if="movingToLearningIsRunning == false"
                                        src="/src/icons/school_FILL0_wght400_GRAD0_opsz24.svg">
                                    <div v-if="movingToLearningIsRunning == true" class="loader"></div>
                                </span>
                                <span>Learn</span>
                            </button>
                        </div>
                        <div class="level-item">
                            <button @click="editDeck" class="button">
                                <span class="icon">
                                    <img src="/src/icons/edit_FILL0_wght400_GRAD0_opsz24.svg">
                                </span>
                                <span>
                                    Edit
                                </span>
                            </button>
                        </div>
                        <div class="level-item">
                            <button @click="showDeleteConfirmationModal" class="button">
                                <span class="icon">
                                    <img src="/src/icons/delete_FILL0_wght400_GRAD0_opsz24.svg">
                                </span>
                                <span>
                                    Delete
                                </span>
                            </button>
                        </div>
                        <div class="level-item">
                            <button @click="createNewCard" class="button">
                                <span class="icon">
                                    <img src="/src/icons/add_FILL0_wght400_GRAD0_opsz24.svg">
                                </span>
                                <span>
                                    New card
                                </span>
                            </button>
                        </div>
                    </div>
                </div>
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
                    <tr v-for="(card, index) in cards" :key="card.id">
                        <td class="is-hidden-touch">{{ card.mark }}</td>
                        <td>{{ card.visiblePart }}</td>
                        <td>{{ card.hiddenPart }}</td>
                        <td>
                            <a @click="editCard(card.id)">
                                Edit
                            </a>
                        </td>
                        <td>
                            <a @click="deleteCard(card.id)" class="has-text-danger is-hidden-touch">Delete</a>
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
