
<script setup>
import axios from 'axios';

import { API_ADDRESS } from '../helpers.js';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
let tridy = ref([]);
let selectedTrida = ref(null);
let selectedTridaDecks = ref(null);

const getAllTrida = () => {
    //const axios = require('axios');

    let config = {
        method: 'get',
        maxBodyLength: Infinity,
        url: API_ADDRESS + 'tridy/all',
        headers: {}
    };

    axios.request(config)
        .then((response) => {
            tridy.value = response.data;
        })
        .catch((error) => {
            router.push("/user/login")
            console.log(error);
        });

}
const findDecks = (tridaid) => {
    axios.post(API_ADDRESS + 'deck/find',
        {
            tridaid: tridaid,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        selectedTridaDecks.value = result.data;

    }).catch(function (err) {
        router.push("/user/login")
        console.log(err);
    })
}

const clickTrida = (id) => {

    for (let trida of tridy.value) {
        if (trida.id == id) {
            selectedTrida.value = trida;
            findDecks(selectedTrida.value.id);
            break;
        }
    }

}

const clickDeck = (id) => {
    router.push('deck/' + id);
}

const createNewDeck = () => {
    router.push('/' + selectedTrida.value.id + '/new')
}
const createNewTrida = () => {
    router.push('/new')
}

const deleteTrida = () => {
    axios.post(API_ADDRESS + 'tridy/delete',
        {
            id: selectedTrida.value.id,
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        selectedTrida.value = null;
        selectedTridaDecks.value = [];
        getAllTrida();


    }).catch(function (err) {
        router.push("/user/login")
        console.log(err);
    })
}
const logOut = () => {
    axios.post(API_ADDRESS + 'user/logout',
        {
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {
        router.push("/user/login");
    }).catch(function (err) {
        router.push("/user/login")
        console.log(err);
    })
    
}




getAllTrida();




const isDeleteConfirmationModalActive = ref(false);

const showDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = true;
};

const hideDeleteConfirmationModal = () => {
    isDeleteConfirmationModalActive.value = false;
};

const isBurgerMenuOpen = ref(false);

const toggleBurgerMenu = () => {
  isBurgerMenuOpen.value = !isBurgerMenuOpen.value;
};

</script>


<template>
    <!--Navbar-->
    <header>
    <div class="wrapper">
      <nav class="navbar" role="navigation" aria-label="main navigation">
        <div class="navbar-brand">
          <a role="button" class="navbar-burger" aria-label="menu"
             :class="{ 'is-active': isBurgerMenuOpen }"
             @click="toggleBurgerMenu" aria-expanded="false" data-target="navbarBasicExample">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
          </a>
        </div>

        <div id="navbarBasicExample" :class="{ 'is-active': isBurgerMenuOpen }" class="navbar-menu">
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
                    <li v-for="(trida) in tridy">
                        <a :class="{ 'is-active': trida == selectedTrida }" @click="clickTrida(trida.id)">{{ trida.name
                        }}</a>
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
                    <button class="button is-danger" @click="showDeleteConfirmationModal">Delete</button>


                </div>

            </div>




            <aside class="menu">

                <ul class="menu-list">
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
                <button class="button is-danger" @click="deleteTrida">Delete</button>
                <button class="button" @click="hideDeleteConfirmationModal">Cancel</button>
            </footer>
        </div>
    </div>
</template>
