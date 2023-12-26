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
let cardid = route.params.cardid;


let visiblePart = ref('');
let hiddenPart = ref('');
let markValue = ref(0);
let visiblePartInput = ref(null);
let wasLastCardCreated = ref(null);
let hiddenImagePart = ref();
let visibleImagePart = ref();


const loadImages = () => {
    axios.post(API_ADDRESS + 'image/find', {
        "cardid": cardid
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
    )
        .then(function (response) {
            hiddenImagePart.value = "";
            visibleImagePart.value = "";
            for (const imageR of response.data.data) {
                if (imageR.position == 0) {
                    hiddenImagePart.value = API_ADDRESS + 'image/show/' + imageR.id;
                }
                if (imageR.position == 1) {
                    visibleImagePart.value = API_ADDRESS + 'image/show/' + imageR.id;
                }
            }

        }).catch(function (error) {
            //router.push("/user/login")
            console.log(error);
        });
}

const loadCardDetails = (onlyImages) => {
    axios.get(API_ADDRESS + 'card/' + cardid)
        .then(function (response) {
            if (!onlyImages) {
                console.log("Update images")
                visiblePart.value = response.data.data.visiblePart;
                hiddenPart.value = response.data.data.hiddenPart;
                markValue.value = response.data.data.mark;
            }

            loadImages();

        }).catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });
}

loadCardDetails();



const updateCard = () => {
    if (!isValidField(hiddenPart.value) || !isValidField(visiblePart.value)) {
        showWarning.value = true;
        warningMessage.value = "You can't have empty side."
        return;
    }



    axios.post(API_ADDRESS + 'card/update', {
        hiddenPart: hiddenPart.value,
        visiblePart: visiblePart.value,
        mark: markValue.value,
        id: cardid
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            visiblePart.value = '';
            hiddenPart.value = '';
            visiblePartInput.value.focus();
            wasLastCardCreated.value = true;
            store.newNotification("Card was successfuly updated",false,"is-success",3);
            router.push('/deck/' + deckid)
            //moveToDeckView();
        })
        .catch(function (error) {
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
        updateCard();
    }

    const isInputField = event.target.tagName.toLowerCase() === 'input' ||
        event.target.tagName.toLowerCase() === 'textarea';


    // If it's an input field, do nothing
    if (isInputField) {
        return;
    }


    if (event.key === 'c') {
        updateCard();
    }


};

const setMark = (mark) => {

    markValue.value = mark;


}


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
const imageHiddenFileChange = (event) => {
    axios.post(API_ADDRESS + 'card/update', {
        'id': cardid,
        'hiddenPartImageFile': event.target.files[0],

    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            loadCardDetails(true);
            console.log(response);
        })
        .catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });
}
const imageHiddenFileDelete = () => {
    axios.post(API_ADDRESS + 'card/update', {
        'id': cardid,
        'hiddenPartImageFileDelete': 'True'
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            loadImages();
            console.log(response);
        })
        .catch(function (error) {
            loadCardDetails(true);
            //router.push("/user/login")
            console.log(error);
        });
}


const imageVisibleFileChange = (event) => {
    axios.post(API_ADDRESS + 'card/update', {
        'id': cardid,
        'visiblePartImageFile': event.target.files[0],

    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            loadCardDetails(true);
        })
        .catch(function (error) {
            router.push("/user/login")
            console.log(error);
        });
}
const imageVisibleFileDelete = () => {
    axios.post(API_ADDRESS + 'card/update', {
        'id': cardid,
        'visiblePartImageFileDelete': 'True'
    },
        {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
        ,
    )
        .then(function (response) {
            loadImages();
        })
        .catch(function (error) {
            loadCardDetails(true);
            //router.push("/user/login")
            console.log(error);
        });
}

</script>


<template>
    <div class="m-4">
        <h1 class="title">Edit card</h1>
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



            <div class="field">
                <label class="label">Mark</label>
                <div class="buttons">
                    <button @click="setMark(0)" :class="{ 'button': true, 'is-success': markValue === 0 }">None</button>
                    <button @click="setMark(1)" :class="{ 'button': true, 'is-success': markValue === 1 }">1</button>
                    <button @click="setMark(2)" :class="{ 'button': true, 'is-success': markValue === 2 }">2</button>
                    <button @click="setMark(3)" :class="{ 'button': true, 'is-success': markValue === 3 }">3</button>
                    <button @click="setMark(4)" :class="{ 'button': true, 'is-success': markValue === 4 }">4</button>
                    <button @click="setMark(5)" :class="{ 'button': true, 'is-success': markValue === 5 }">5</button>
                </div>
            </div>


            <p v-if="wasLastCardCreated == true" class="is-italic">Card was edited</p>
            <div class="notification is-danger" v-show="showWarning">
                <button class="delete" @click="closeWarning"></button>
                <p v-text="warningMessage"></p>
            </div>
            <div class="field is-grouped">
                <div class="control">
                    <button class="button is-primary" @click="updateCard">Save changes</button>
                </div>
                <div class="control">
                    <button class="button is-link is-light" @click="moveToDeckView">Cancel</button>
                </div>
            </div>
        </div>
    </div>
</template>