<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js';
const store = notificationStore();
const router = useRouter();
const route = useRoute();

const email = ref('');
const showWarning = ref(false);
const warningMessage = ref('');

const isSendRecoveryEmailRunning = ref(false);

const sendRecoveryEmail = () => {
  if (isSendRecoveryEmailRunning.value == true) {
    return;
  }
  isSendRecoveryEmailRunning.value = true;

  axios.post(API_ADDRESS + 'recovery/request/password',
    {
      "email": email.value
    },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  ).then(function (result) {
    isSendRecoveryEmailRunning.value = false;
    store.newNotification("Request was succesful", false, "is-success", 3);
    router.push('/checkyouremail');
  }).catch(function (err) {
    isSendRecoveryEmailRunning.value = false;
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
          <h1 class="title">Recover password</h1>
          <form @submit.prevent>
            <div class="field">
              <label class="label">Email</label>
              <div class="control">
                <input v-model="email" class="input" type="email" placeholder="Email" autocomplete="email">
              </div>
            </div>
            <div class="notification is-danger" v-show="showWarning">
              <button class="delete" @click="closeWarning"></button>
              <p v-text="warningMessage"></p>
            </div>
            <div class="field is-grouped">
              <div class="control">
                <button class="button is-primary" @click="sendRecoveryEmail">

                  <div class="loader" v-if="isSendRecoveryEmailRunning == true"></div>
                  <p v-if="isSendRecoveryEmailRunning == false">Send recovery email</p>
                </button>
              </div>
              <div class="control">
                <button class="button is-link is-light" @click="loginInstead">Login instead</button>
              </div>

            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>