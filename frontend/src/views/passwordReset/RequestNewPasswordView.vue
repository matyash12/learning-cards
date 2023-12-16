<template>
    <div>
        <h1 class="title">Recover password</h1>
        <form @submit.prevent>
        <div class="box">
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
              <button class="button is-primary" @click="sendRecoveryEmail">Send recovery email</button>
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

import { API_ADDRESS } from '../../helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

const email = ref('');
const showWarning = ref(false);
const warningMessage = ref('');
const sendRecoveryEmail = () =>{
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
        router.push('/checkyouremail');
    }).catch(function (err) {
        warningMessage.value = err.response.data.message;
        showWarning.value = true;
        console.log(err);
    })
}

const loginInstead = () =>{
  router.push("/user/login");
}


</script>