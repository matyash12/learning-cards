<script setup>
import axios from 'axios';

import { API_ADDRESS } from '../helpers.js';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const router = useRouter();
const route = useRoute();


// Data properties
const tridaName = ref('');

let tridaid = route.params.tridaid;


// Methods
const renameTrida = () => {
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
            console.log(response);
            moveToClasses();
        })
        .catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });

};

const loadTrida = () => {
    axios.get(API_ADDRESS + 'tridy/' + tridaid).then(function (response) {
        tridaName.value = response.data.data.name;
    }).catch(function (error) {
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
                        <input v-model="tridaName" class="input" type="text" placeholder="Class name">
                    </div>
                </div>

                <div class="field is-grouped">
                    <div class="control">
                        <button class="button is-primary" @click="renameTrida">Rename</button>
                    </div>
                    <div class="control">
                        <button class="button is-link is-light" @click="moveToClasses">Cancel</button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
