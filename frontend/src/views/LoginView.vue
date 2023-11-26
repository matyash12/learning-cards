<template>
  <div>
        <h1 class="title">Login</h1>

        <div class="box">
        <div class="field">
            <label class="label">Username</label>
            <div class="control">
                <input v-model="username" class="input" type="text" placeholder="Username">
            </div>
        </div>
        <div class="field">
            <label class="label">Password</label>
            <div class="control">
                <input v-model="password" class="input" type="text" placeholder="Password">
            </div>
        </div>

        <div class="field is-grouped">
            <div class="control">
                <button class="button is-primary" @click="loginRequest">Login</button>
            </div>
            <div class="control">
                <button class="button is-link is-light" >Cancel</button>
            </div>
        </div>
    </div>

    </div>



</template>


<script setup>
import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();

const username = ref('');
const password = ref('');

const loginRequest =() => {
    axios.post(API_ADDRESS + 'user/login',
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
        
        router.push('/');
    }).catch(function (err) {
        console.log(err);
    })
}

</script>