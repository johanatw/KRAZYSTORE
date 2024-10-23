import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import PrimeVue from 'primevue/config'
import 'primevue/resources/themes/lara-light-pink/theme.css'
import 'primeicons/primeicons.css'
import "/node_modules/primeflex/primeflex.css"
import ToastService from 'primevue/toastservice'
import ConfirmationService from 'primevue/confirmationservice';
import 'primeflex/primeflex.css';
import 'leaflet/dist/leaflet.css'; 
import Tooltip from 'primevue/tooltip';





const app = createApp(App)
app.use(ConfirmationService);
app.use(ToastService)
app.use(PrimeVue)
app.use(router)
app.directive('tooltip', Tooltip);
app.mount('#app')
