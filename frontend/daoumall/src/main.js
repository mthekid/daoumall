import router from './router'
import { createApp } from 'vue'
import App from './App.vue'
import { store } from "@/store/store";

import axios from 'axios'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"

const app = createApp(App).use(router)

// axios 전역 설정
app.config.globalProperties.axios = axios;
app.use(router)
app.use(store)
app.mount('#app')
