<script setup>
import axios from 'axios';
import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js';


const store = notificationStore();
const router = useRouter();
const tridy = ref([]);
const selectedTrida = ref(null);
const selectedTridaDecks = ref(null);
const isDeleteConfirmationModalActive = ref(false);
const isBurgerMenuOpen = ref(false);

//Loading statuses
const isTridaLoading = ref(false) //whether the api request is still running
const isDecksLoading = ref(false) //whenther the api request for decks of active class is still running
const isDeleteTridaRunning = ref(false);

onMounted(() => {
    fetchAllTrida();
});


//fetches all trida's from api
const fetchAllTrida = async () => {
    isTridaLoading.value = true;
    try {
        const response = await axios.get(API_ADDRESS + 'tridy/all');
        isTridaLoading.value = false;
        tridy.value = response.data.data;
    } catch (error) {
        isTridaLoading.value = false;
        handleApiError(error);
    }
};
//fetches all decks for the class
const fetchDecksForTrida = async (tridaid) => {
    try {
        isDecksLoading.value = true
        const result = await axios.post(
            `${API_ADDRESS}deck/find`,
            { tridaid },
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );
        isDecksLoading.value = false
        selectedTridaDecks.value = result.data.data;
    } catch (err) {
        isDecksLoading.value = false
        handleApiError(err);
    }
};

const selectTrida = (id) => {
    const foundTrida = tridy.value.find((trida) => trida.id === id);
    if (foundTrida) {
        selectedTrida.value = foundTrida;
        selectedTridaDecks.value = []
        fetchDecksForTrida(selectedTrida.value.id);
    }
};

const clickDeck = (id) => {
    router.push(`deck/${id}`);
};


const createNewDeck = () => {
    router.push(`/${selectedTrida.value.id}/new`);
};

const createNewTrida = () => {
    router.push('/new');
};

const deleteTrida = async () => {
    if (isDeleteTridaRunning.value == true) {
        return;
    }
    isDeleteTridaRunning.value = true;
    try {
        await axios.post(
            `${API_ADDRESS}tridy/delete`,
            { id: selectedTrida.value.id },
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );
        isDeleteTridaRunning.value = false;
        selectedTrida.value = null;
        selectedTridaDecks.value = [];
        store.newNotification("Class was deleted", false, "is-info", 3);
        fetchAllTrida();
        store.newNotification("Class was deleted", false, "is-info", 3);
        fetchAllTrida();
        isDeleteConfirmationModalActive.value = false;
    } catch (err) {
        isDeleteTridaRunning.value = false;
        handleApiError(err);
    }
};

const logOut = async () => {
    try {
        await axios.post(
            `${API_ADDRESS}user/logout`,
            {},
            { headers: { 'Content-Type': 'multipart/form-data' } }
        );
        router.push("/user/login");
    } catch (err) {
        handleApiError(err);
    }
};


const editTrida = () => {
    router.push(`/${selectedTrida.value.id}/edit`);
};

const showDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = true;
};

const hideDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = false;
};

const toggleBurgerMenu = () => {
    isBurgerMenuOpen.value = !isBurgerMenuOpen.value;
};

const exportData = () => {
    router.push("/export");
};


const handleApiError = (error) => {
    router.push("/user/login");
    console.error(error);
};

</script>




<template>
    <div class="m-4">
        <!--Navbar-->
        <header>
            <div class="wrapper">
                <nav class="navbar" role="navigation" aria-label="main navigation">
                    <div class="navbar-brand">
                        <a role="button" class="navbar-burger" aria-label="menu" :class="{ 'is-active': isBurgerMenuOpen }"
                            @click="toggleBurgerMenu" aria-expanded="false" data-target="navbarBasicExample">
                            <span aria-hidden="true"></span>
                            <span aria-hidden="true"></span>
                            <span aria-hidden="true"></span>
                        </a>
                    </div>

                    <div id="navbarBasicExample" :class="{ 'is-active': isBurgerMenuOpen }" class="navbar-menu">
                        <div class="navbar-start">
                            <a class="navbar-item" @click="exportData">
                                Export data
                            </a>
                        </div>
                        <div class="navbar-end">
                            <div class="navbar-item">
                                <div class="buttons">
                                    <a class="button is-light" @click="logOut">
                                        Log out
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
        </header>


        <div class="columns">
            <div class="column is-one-third">
                <aside class="menu">
                    <p class="menu-label">
                        Classes
                    </p>
                    <ul class="menu-list">
                        <li v-if="isTridaLoading">
                            <div class="loader"></div>
                        </li>

                        <li v-for="(trida) in tridy">
                            <a :class="{ 'is-active': trida == selectedTrida }" @click="selectTrida(trida.id)">{{ trida.name }}</a>
                        </li>

                        <li>
                            <a @click="createNewTrida" class="has-text-weight-bold">Create new class</a>
                        </li>

                    </ul>
                </aside>

            </div>
            <div class="column" v-show="selectedTrida != null">
                <div class="box">
                    <p class="title">
                        {{ selectedTrida?.name ?? '' }}
                    </p>

                    <div class="buttons">
                        <button @click="createNewDeck" class="button is-primary ">
                            Create new deck
                        </button>
                        <button @click="editTrida" class="button is-link">
                            Edit class
                        </button>
                        <button class="button is-danger" @click="showDeleteConfirmationModal">Delete</button>


                    </div>

                </div>




                <aside class="menu">
                    
                    <ul class="menu-list">
                        <li v-if="isDecksLoading">
                            <div class="loader"></div>
                        </li>

                        <li v-for="(deck) in selectedTridaDecks">
                            <a @click="clickDeck(deck.id)">{{ deck.name }}</a>
                        </li>
                        <li v-if="selectedTrida != null">
                            <a class="has-text-weight-bold" @click="createNewDeck">Create new deck</a>
                        </li>
                    </ul>
                </aside>

            </div>
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
                    <button class="button is-danger" @click="deleteTrida">
                        <div class="loader" v-if="isDeleteTridaRunning == true"></div>
                        <p v-if="isDeleteTridaRunning == false">Delete</p>
                    </button>
                    <button class="button" @click="hideDeleteConfirmationModal">Cancel</button>
                </footer>
            </div>
        </div>
    </div>
</template>
