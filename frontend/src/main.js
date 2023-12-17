//import './assets/main.css'
// import 'bootstrap/dist/css/bootstrap.min.css';
// import 'bootstrap-icons/font/bootstrap-icons.css';
// import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bulma/css/bulma.css';
import axios from 'axios';

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './registerServiceWorker'

const app = createApp(App)

app.use(router)

app.mount('#app')
