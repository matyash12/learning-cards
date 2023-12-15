<template>
    <div>
      <h1 class="title">Create account</h1>
  
      <form @submit.prevent @keyup.enter="registerRequest">
        <div class="box">
          <div class="field">
            <label class="label">Username</label>
            <div class="control">
              <input v-model="username" class="input" type="text" placeholder="Username" autocomplete="username">
            </div>
          </div>
          <div class="field">
            <label class="label">Password</label>
            <div class="control">
              <input v-model="password" class="input" type="password" placeholder="Password" autocomplete="new-password">
            </div>
          </div>
          <div class="field">
            <label class="label">Repeat password</label>
            <div class="control">
              <input v-model="password2" class="input" type="password" placeholder="Password" autocomplete="new-password">
            </div>
          </div>
          <div class="notification is-danger" v-show="showWarning">
            <button class="delete" @click="closeWarning"></button>
            <p v-text="warningMessage"></p>
          </div>
          <div class="field is-grouped">
            <div class="control">
              <button class="button is-primary" @click="registerRequest">Create</button>
            </div>
            <div class="control">
              <button class="button is-link is-light" @click="loginInstead">Login instead</button>
            </div>
          </div>
        </div>
      </form>
  
    </div>
  </template>
  
  
  
<script setup>
import axios from 'axios';

import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

const username = ref('');
const password = ref('');
const password2 = ref('');

const showWarning = ref(false);
const warningMessage = ref('');

const closeWarning = () =>{
    showWarning.value = false;
    warningMessage.value = '';
}


const registerRequest = () => {
    if (password2.value !== password.value) {
        return;
    }

    axios.post(API_ADDRESS + 'user/register',
        {
            "username": username.value,
            "password": password.value
        },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    ).then(function (result) {

        router.push('/user/login');
    }).catch(function (err) {
        warningMessage.value = err.response.data.message;
        showWarning.value = true;
        console.log(err);
    })
}

const loginInstead = () => {
    router.push("/user/login");
}

</script>