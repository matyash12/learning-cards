<script setup>
import axios from 'axios';

import { API_ADDRESS, isValidField } from '@/helpers.js';
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { notificationStore } from '@/stores/notification.js'; 
const store = notificationStore();
const router = useRouter();
const route = useRoute();

let deckid = route.params.deckid;
let cardid = route.params.cardid; //new == create new

let visiblePart = ref('');
let hiddenPart = ref('');
let visiblePartInput = ref(null);
let wasLastCardCreated = ref(null);
let hiddenImagePart = ref("");
let visibleImagePart = ref("");

//is running
const isCreateCardRunning = ref(false);

const createCard = () => {
    if (isCreateCardRunning.value == true){
        return;
    }
    if (!isValidField(hiddenPart.value) || !isValidField(visiblePart.value)) {
        showWarning.value = true;
        warningMessage.value = "You can't have empty side."
        return;
    }
    isCreateCardRunning.value = true;

    let formData = {
        hiddenPart: hiddenPart.value,
        visiblePart: visiblePart.value,
        deckid: deckid,

    };

    if (hiddenImagePart.value != "") {
        formData["hiddenPartImageFile"] = imageHiddenFile.value
    }
    if (visibleImagePart.value != "") {
        formData["visiblePartImageFile"] = imageVisibleFile.value;
    }

    axios.post(API_ADDRESS + 'card/new', formData,
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            isCreateCardRunning.value = false;
            store.newNotification("Card was created",false,"is-success",1);

            visiblePart.value = '';
            hiddenPart.value = '';
            visiblePartInput.value.focus();
            wasLastCardCreated.value = true;

            //images
            imageHiddenFile.value = "";
            hiddenImagePart.value = "";
            imageVisibleFile.value = "";
            visibleImagePart.value = "";


            router.push('/deck/' + deckid + '/new')
            //moveToDeckView();
        })
        .catch(function (error) {
            isCreateCardRunning.value = false;
            router.push("/user/login")
            console.log(error);
            wasLastCardCreated.value = false;
        });

};
const moveToDeckView = () => {
    router.push('/deck/' + deckid)
}

const handleKeyDown = (event) => {
    if (event.key === 'Tab' && event.target.id === 'hiddenPart') {
        createCard();
    }

    const isInputField = event.target.tagName.toLowerCase() === 'input' ||
        event.target.tagName.toLowerCase() === 'textarea';


    // If it's an input field, do nothing
    if (isInputField) {
        return;
    }


    if (event.key === 'c') {
        createCard();
    }


};

onMounted(() => {
    visiblePartInput.value.focus();
    window.addEventListener('keydown', handleKeyDown);
});
onUnmounted(() => {
    window.removeEventListener('keydown', handleKeyDown);
});

const userChangesValue = () => {
    showWarning.value = false;
    wasLastCardCreated.value = false;
}

const showWarning = ref(false);
const warningMessage = ref('');

const closeWarning = () => {
    showWarning.value = false;
    warningMessage.value = '';
}

let imageHiddenFile = ref("");
let imageVisibleFile = ref("");

const imageHiddenFileChange = (event) => {
    imageHiddenFile.value = event.target.files[0];
    hiddenImagePart.value = (window.URL ? URL : webkitURL).createObjectURL(event.target.files[0])
}
const imageHiddenFileDelete = () => {
    imageHiddenFile.value = "";
    hiddenImagePart.value = ""
}

const imageVisibleFileChange = (event) => {
    imageVisibleFile.value = event.target.files[0];
    visibleImagePart.value = (window.URL ? URL : webkitURL).createObjectURL(event.target.files[0])
}
const imageVisibleFileDelete = () => {
    imageVisibleFile.value = "";
    visibleImagePart.value = ""
}
</script>


<template>
    <div class="m-4">
        <h1 class="title">Create new card</h1>
        <div>
            <!--VISIBLE SIDE-->
            <div>
                <div class="field">
                    <label class="label">Visible side image</label>
                    <div class="control">
                        <img v-if="visibleImagePart != ''" :src="visibleImagePart">
                    </div>
                </div>


                <div class="file has-name">
                    <label class="file-label">
                        <input @change="imageVisibleFileChange" class="file-input" type="file" name="resume">
                        <span class="file-cta">
                            <span class="file-icon">
                                <i class="fas fa-upload"></i>
                            </span>
                            <span class="file-label">
                                Choose a file…
                            </span>
                        </span>
                        <span class="file-name">
                        </span>
                    </label>
                    <button v-if="visibleImagePart != ''" @click="imageVisibleFileDelete" class="button">
                        Remove image
                    </button>
                </div>

                <div class="field">
                    <label class="label">Visible side text</label>
                    <div class="control">
                        <textarea ref="visiblePartInput" id="visiblePart" v-model="visiblePart" @input="userChangesValue"
                            class="textarea" type="text" placeholder=""></textarea>
                    </div>
                </div>

            </div>
            <!--HIDDEN SIDE-->
            <div>
                <div class="field">
                    <label class="label">Hidden side image</label>
                    <div class="control">
                        <img v-if="hiddenImagePart != ''" :src="hiddenImagePart">
                    </div>
                </div>


                <div class="file has-name">
                    <label class="file-label">
                        <input @change="imageHiddenFileChange" class="file-input" type="file" name="resume">
                        <span class="file-cta">
                            <span class="file-icon">
                                <i class="fas fa-upload"></i>
                            </span>
                            <span class="file-label">
                                Choose a file…
                            </span>
                        </span>
                        <span class="file-name">
                        </span>
                    </label>
                    <button v-if="hiddenImagePart != ''" @click="imageHiddenFileDelete" class="button">
                        Remove image
                    </button>
                </div>
                <div class="field">
                    <label class="label">Hidden side text</label>
                    <div class="control">
                        <textarea v-model="hiddenPart" id="hiddenPart" @input="userChangesValue" class="textarea"
                            type="text" placeholder=""></textarea>
                    </div>
                </div>
            </div>


            <!--
                Not used anymore
            <p v-if="wasLastCardCreated == true" class="is-italic">Card was created</p>
            -->
            <div class="notification is-danger" v-show="showWarning">
                <button class="delete" @click="closeWarning"></button>
                <p v-text="warningMessage"></p>
            </div>
            <div class="field is-grouped">
                <div class="control">
                    <button class="button is-primary" @click="createCard">
                        <p v-if="isCreateCardRunning == false">Create</p>
                        <div class="loader" v-if="isCreateCardRunning == true"></div>
                    
                    </button>
                </div>
                <div class="control">
                    <button class="button is-link is-light" @click="moveToDeckView">Done</button>
                </div>
            </div>
        </div>
    </div>
</template>