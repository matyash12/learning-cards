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


//is running properties
const isRenameTridaRunning = ref(false);
const isLoadTridaRunning = ref(true);

// Methods
const renameTrida = () => {
    if (isRenameTridaRunning.value == true) {
        return;
    }
    isRenameTridaRunning.value = true;

    axios.post(API_ADDRESS + 'tridy/update', {
        'id': tridaid,
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
            isRenameTridaRunning.value = false;
            console.log(response);
            store.newNotification("Class renamed", false, "is-success", 3);

            moveToClasses();
        })
        .catch(function (error) {
            isRenameTridaRunning.value = false;
            router.push("/user/login")
            console.log(error);
        });

};

const loadTrida = () => {
    axios.get(API_ADDRESS + 'tridy/' + tridaid).then(function (response) {
        isLoadTridaRunning.value = false;
        tridaName.value = response.data.data.name;
    }).catch(function (error) {
        isLoadTridaRunning.value = false;
        router.push("/user/login")
        console.log(error);
    });
}

loadTrida();

const moveToClasses = () => {
    router.push('/');
}
</script>


<template>
    <div class="m-4">
        <div>
            <h1 class="title">Rename class</h1>

            <div class="box">
                <div class="field">
                    <label class="label">New name for class</label>
                    <div class="control">
                        <div class="loader" v-if="isLoadTridaRunning == true"></div>
                        <input v-model="tridaName" class="input" type="text" placeholder="Class name" v-if="isLoadTridaRunning == false">
                    </div>
                </div>

                <div class="field is-grouped">
                    <div class="control">
                        <button class="button is-primary" @click="renameTrida">
                            <div class="loader" v-if="isRenameTridaRunning == true"></div>
                            <p v-if="isRenameTridaRunning == false">Rename</p>
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
