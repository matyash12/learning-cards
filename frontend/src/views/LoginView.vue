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
const password = ref('');
const showWarning = ref(false);
const warningMessage = ref('');

const isLoginRequestLoading = ref(false) //whether there is an api request running for login

const closeWarning = () => {
  showWarning.value = false;
  warningMessage.value = '';
};

const loginRequest = async () => {
  if (isLoginRequestLoading.value == true){
    return
  }
  try {
    isLoginRequestLoading.value = true
    const result = await axios.post(
      `${API_ADDRESS}user/login`,
      { email: email.value, password: password.value },
      { headers: { 'Content-Type': 'multipart/form-data' } }
    );
    isLoginRequestLoading.value = false
    store.newNotification("Login was successful", false, "is-success", 3);
    router.push('/');
  } catch (err) {
    isLoginRequestLoading.value = false
    warningMessage.value = err.response.data.message;
    showWarning.value = true;
    console.error(err);
  }
};

const createAccount = () => {
  router.push("/user/register");
};

const recoverPassword = () => {
  router.push("/requestnewpassword");
};

</script>


<template>
  <div class="m-4">
    <div class="container">
      <div class="columns is-centered">
        <div class="column is-half">
          <h1 class="title">Login</h1>

          <form @submit.prevent @keyup.enter="loginRequest">
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
                  autocomplete="current-password">
              </div>
            </div>
            <div class="notification is-danger" v-show="showWarning">
              <button class="delete" @click="closeWarning"></button>
              <p v-text="warningMessage"></p>
            </div>
            <div class="buttons">
              <button class="button is-primary" @click="loginRequest">
                <div v-if="isLoginRequestLoading">
                  <div class="loader"></div>
                </div>
                <div v-if="isLoginRequestLoading == false">
                  Login
                </div> 
                
              </button>
              <button class="button is-link is-light" @click="recoverPassword">Recover password</button>
              <button class="button is-link is-light" @click="createAccount">Create account</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>