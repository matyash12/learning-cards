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

            <div class="level">
              <div class="level-left">


                <!--Send recovery email button-->
                <div class="level-item">
                  <button class="button is-primary" @click="sendRecoveryEmail">
                    <span class="icon">
                      <div class="loader" v-if="isSendRecoveryEmailRunning == true"></div>
                      <img v-if="isSendRecoveryEmailRunning == false"
                        src="/src/icons/lock_reset_FILL0_wght400_GRAD0_opsz24.svg">
                    </span>
                    <span>Send recovery email</span>
                  </button>
                </div>

                <!--Login instead button-->
                <div class="level-item">
                  <button @click="loginInstead" class="button">
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