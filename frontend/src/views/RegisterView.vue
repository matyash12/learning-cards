<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js';
const store = notificationStore();
const router = useRouter();
const route = useRoute();

const username = ref('');
const email = ref('');
const password = ref('');
const password2 = ref('');

const showWarning = ref(false);
const warningMessage = ref('');

const isRegisterRequestRunning = ref(false);

const closeWarning = () => {
  showWarning.value = false;
  warningMessage.value = '';
}


const registerRequest = () => {
  if (isRegisterRequestRunning.value == true) {
    return;
  }
  isRegisterRequestRunning.value = true;

  if (password2.value !== password.value) {
    return;
  }

  axios.post(API_ADDRESS + 'user/register',
    {
      "email": email.value,
      "password": password.value
    },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  ).then(function (result) {
    isRegisterRequestRunning.value = false;
    store.newNotification("Account created", false, "is-success", 3);
    router.push('/user/login');
  }).catch(function (err) {
    isRegisterRequestRunning.value = false;
    warningMessage.value = err.response.data.message;
    showWarning.value = true;
    console.log(err);
  })
}

const loginInstead = () => {
  router.push("/user/login");
}

</script>


<template>
  <div class="m-4">
    <div class="container">
      <div class="columns is-centered">
        <div class="column is-half">
          <h1 class="title">Create account</h1>

          <form @submit.prevent @keyup.enter="registerRequest">
            <!-- <div class="field">
            <label class="label">Username</label>
            <div class="control">
              <input v-model="username" class="input" type="text" placeholder="Username" autocomplete="username">
            </div>
          </div> -->
            <div class="field">
              <label class="label">Email</label>
              <div class="control">
                <input v-model="email" class="input" type="email" placeholder="Email" autocomplete="email">
              </div>
            </div>
            <div class="field">
              <label class="label">Password</label>
              <div class="control">
                <input v-model="password" class="input" type="password" placeholder="Password"
                  autocomplete="new-password">
              </div>
            </div>
            <div class="field">
              <label class="label">Repeat password</label>
              <div class="control">
                <input v-model="password2" class="input" type="password" placeholder="Password"
                  autocomplete="new-password">
              </div>
            </div>
            <div class="notification is-danger" v-show="showWarning">
              <button class="delete" @click="closeWarning"></button>
              <p v-text="warningMessage"></p>
            </div>


            <div class="level">

              <div class="level-left">
                <div class="level-item">
                  <!--Create account button-->
                  <button class="button is-primary" @click="registerRequest">
                    <span class="icon">
                      <img v-if="isRegisterRequestRunning == false"
                        src="/src/icons/person_add_FILL0_wght400_GRAD0_opsz24.svg">
                      <div v-if="isRegisterRequestRunning == true" class="loader"></div>
                    </span>
                    <span>Create account</span>
                  </button>
                </div>
                <div class="level-item">
                  <button class="button" @click="loginInstead">
                    <span class="icon">
                      <img src="/src/icons/login_FILL0_wght400_GRAD0_opsz24.svg">
                    </span>
                    <span>Login instead</span>
                  </button>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>