<script setup>
import { RouterLink, RouterView } from 'vue-router'
import { useRoute } from 'vue-router'
import Navbar from './components/Navbar.vue'
import { computed } from 'vue'

// Obtener la ruta actual
const route = useRoute()

// Computed para mostrar el navbar solo si la ruta no es "/"
const showNavbar = computed(() => route.path !== '/')
</script>

<template>
  <Navbar v-if="showNavbar"  />
  <RouterView />
</template>

<style scoped>
/* Alternativa si no usas Tailwind */
.mb-6 {
  margin-bottom: 1rem;
}
.mt-8 {
  margin-top: 3%;
}
</style>

