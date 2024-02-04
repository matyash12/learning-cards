<script setup>
import axios from 'axios';

import { API_ADDRESS } from '@/helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js'; 
const store = notificationStore();
const router = useRouter();
const route = useRoute();


// Data properties
const tridaName = ref('');
let tridaid = route.params.tridaid;

//is running?
const isCreateNewTridaRunning = ref(false);

// Methods
const createNewTrida = () => {
    if (isCreateNewTridaRunning.value = true){
        return;
    }
    isCreateNewTridaRunning.value = true;

    axios.post(API_ADDRESS + 'tridy/new', {
        'name': tridaName.value,
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            isCreateNewTridaRunning.value = false;
            console.log(response);
            store.newNotification("New class created",false,"is-success",3);
            moveToClasses();
        })
        .catch(function (error) {
            isCreateNewTridaRunning.value = false;
            router.push("/user/login")
            console.log(error);
        });

};


const moveToClasses = () => {
    router.push('/');
}
</script>


<template>
    <div class="m-4">
        <div>
            <h1 class="title">Create new class</h1>

            <div class="box">
                <div class="field">
                    <label class="label">Class name</label>
                    <div class="control">
                        <input v-model="tridaName" class="input" type="text" placeholder="Class name">
                    </div>
                </div>

                <div class="field is-grouped">
                    <div class="control">
                        <button class="button is-primary" @click="createNewTrida">
                            <div class="loader" v-if="isCreateNewTridaRunning == true"></div>
                            <p v-if="isCreateNewTridaRunning == false">Create</p>
                        </button>
                    </div>
                    <div class="control">
                        <button class="button is-link is-light" @click="moveToClasses">Cancel</button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
