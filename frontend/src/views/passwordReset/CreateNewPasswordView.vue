<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js';
const store = notificationStore();
const router = useRouter();
const route = useRoute();

const { token, userId } = defineProps(['token', 'userId']);

const password1 = ref("");
const password2 = ref("");
const showWarning = ref(false);
const warningMessage = ref('');

const isCreateNewPasswordRequestRunning = ref(false);

const createNewPasswordRequest = () => {
  if (isCreateNewPasswordRequestRunning.value == true) {
    return;
  }
  if (password1.value !== password2.value) {
    return;
  }
  isCreateNewPasswordRequestRunning.value = true;

  axios.post(API_ADDRESS + 'recovery/change/password',
    {
      "token": token,
      "userid": userId,
      "password": password1.value
    },
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  ).then(function (result) {
    isCreateNewPasswordRequestRunning.value = false;
    store.newNotification("New password created", false, "is-success", 3);
    router.push('/user/login');
  }).catch(function (err) {
    isCreateNewPasswordRequestRunning.value = false;
    warningMessage.value = err.response.data.message;
    showWarning.value = true;
    console.log(err);
  })
}

</script>

<template>
  <div class="m-4">
    <div>
      <h1 class="title">Create new password</h1>
      <form @submit.prevent>
        <div class="box">
          <div class="field">
            <label class="label">New password</label>
            <div class="control">
              <input v-model="password1" class="input" type="password" placeholder="New password" autocomplete="password">
            </div>
          </div>
          <div class="field">
            <label class="label">New password again</label>
            <div class="control">
              <input v-model="password2" class="input" type="password" placeholder="New password" autocomplete="password">
            </div>
          </div>
          <div class="notification is-danger" v-show="showWarning">
            <button class="delete" @click="closeWarning"></button>
            <p v-text="warningMessage"></p>
          </div>
          <div class="field is-grouped">
            <div class="control">
              <button class="button is-primary" @click="createNewPasswordRequest">
                <div class="loader" v-if="isCreateNewPasswordRequestRunning == true"></div>
                <p v-if="isCreateNewPasswordRequestRunning == false">Save new password</p>
              </button>
            </div>

          </div>
        </div>
      </form>
    </div>

  </div>
</template>