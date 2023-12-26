<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js'; 
const store = notificationStore();
const router = useRouter();
const route = useRoute();

let id = route.params.id;
let deck = ref(null);
let cards = ref([]);

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
    router.push('/deck/' + id + '/' + 'new');
}
const moveToClassView = () => {
    router.push('/')
}
const moveToLearning = () => {
    router.push('/learn/' + id);
}
const refreshDataOnPage = () => {
    getDeck(id);
    findCards(id);
}
const deleteCard = (cardid) => {
    axios.post(API_ADDRESS + 'card/delete',
        {
            id: cardid,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        store.newNotification("Card was deleted",false,"is-info",3);
        refreshDataOnPage();

    }).catch(function (err) {
        router.push("/user/login")
        console.log(err);
    })
}
const deleteThisDeck = () => {
    axios.post(API_ADDRESS + 'deck/delete',
        {
            id: id,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        store.newNotification("Deck was deleted",false,"is-info",3);
        moveToClassView();

    }).catch(function (err) {
        router.push("/user/login")
        console.log(err);
    })
}
const editCard = (cardid) => {
    router.push("/deck/" + deck.value.id + "/" + cardid + "/edit");
}

refreshDataOnPage();




const isDeleteConfirmationModalActive = ref(false);

const showDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = true;
};

const hideDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = false;
};
const editDeck = () => {
    router.push("/deck/" + id + "/edit")
};
</script>


<template>
    <div class="m-4">
        <div>
            <div class="buttons">
                <button @click="moveToLearning" class="button is-primary">Learn</button>
                <button @click="editDeck" class="button is-info">Edit deck</button>
                <button @click="createNewCard" class="button is-success">Add card</button>
                <button @click="moveToClassView" class="button is-warning">Classes</button>
                <button @click="showDeleteConfirmationModal" class="button is-danger">Delete</button>
            </div>
            <h1 class="title">{{ deck?.name ?? "loading..." }}</h1>
            <table class="table is-fullwidth">
                <thead>
                    <tr>
                        <th>Mark</th>
                        <th>Visible side</th>
                        <th>Hidden side</th>
                        <th>Edit</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(card, index) in cards" :key="card.id">
                        <td>{{ card.mark }}</td>
                        <td>{{ card.visiblePart }}</td>
                        <td>{{ card.hiddenPart }}</td>
                        <td>
                            <a @click="editCard(card.id)" class="">Edit</a>
                        </td>
                        <td>
                            <a @click="deleteCard(card.id)" class="has-text-danger">Delete</a>
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
                    <button class="button is-danger" @click="deleteThisDeck">Delete</button>
                    <button class="button" @click="hideDeleteConfirmationModal">Cancel</button>
                </footer>
            </div>
        </div>
    </div>
</template>

