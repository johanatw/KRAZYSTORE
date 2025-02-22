<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Navbar from './components/Navbar.vue';
import router from '@/router';


const showNavbar = () =>{
  // Ocultar el navbar si la ruta es /login
  console.log(router.currentRoute.value.path);
  return router.currentRoute.value.path !== '/';
}
</script>

<template>

<Navbar v-if="showNavbar()" style="margin-bottom: 2% !important"/>
  <RouterView style="margin-top: 3% !important;"/>
</template>



