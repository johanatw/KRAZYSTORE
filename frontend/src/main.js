import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import PrimeVue from 'primevue/config'
import Lara from '@primevue/themes/lara';
//import 'primevue/resources/themes/lara-light-pink/theme.css';
import 'primeicons/primeicons.css'
import "/node_modules/primeflex/primeflex.css"
import ToastService from 'primevue/toastservice'
import { definePreset } from '@primevue/themes';
import ConfirmationService from 'primevue/confirmationservice';
import 'primeflex/primeflex.css';
import 'leaflet/dist/leaflet.css'; 
import Tooltip from 'primevue/tooltip';
import BadgeDirective from 'primevue/badgedirective';



const MyPreset = definePreset(Lara, {
    semantic: {
        primary: {
            50: '{pink.50}',
            100: '{pink.100}',
            200: '{pink.200}',
            300: '{pink.300}',
            400: '{pink.400}',
            500: '{pink.500}',
            600: '{pink.600}',
            700: '{pink.700}',
            800: '{pink.800}',
            900: '{pink.900}',
            950: '{pink.950}'
        }
    }
});

const app = createApp(App)
app.use(PrimeVue, {
    // Default theme configuration
    theme: {
        preset: MyPreset,
        options: {
            prefix: 'p',
           
        }
    }
 });
app.use(ConfirmationService);
app.use(ToastService)
app.use(PrimeVue)
app.use(router)
app.directive('tooltip', Tooltip);
app.directive('badge', BadgeDirective);
app.mount('#app')
