<template>
    <div id="map"  ></div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import L from 'leaflet';
  const emit = defineEmits(['getUbicacion']);
  const props = defineProps(['lat','lng']); 
 
  const map = ref(null);
  
    const markers = ref([]); // Para almacenar los marcadores
  
      onMounted(() => {
        let lat;
        let lng;
        console.log("mounted");
        console.log(typeof props.lat != "undefined");
        
        if (typeof props.lat != "undefined" && props.lat ) {
          lat = props.lat;
          lng = props.lng;
        } else {
          lat = -25.3245;
          lng = -57.5478
        }
        //Coordenada inicial
        const userLatLng = [lat,lng];

        // Crear el mapa centrado en una ubicación predeterminada
        map.value = L.map('map').setView(userLatLng, 13); // Coordenadas iniciales (Londres)
        

  
        // Añadir capa de mapa de OpenStreetMap  
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }).addTo(map.value);

        // Crear un nuevo marcador
        const marker = L.marker(userLatLng)
            .addTo(map.value)
            .openPopup();

        // Guardar el nuevo marcador en el array
        markers.value.push(marker);

        // Evento para detectar el clic en el mapa
      map.value.on('click', marcar);

       
      });

      const marcar = (event) =>{
        console.log(event);
        const { lat, lng } = event.latlng;
        
        // Remover todos los marcadores anteriores
        markers.value.forEach(marker => map.value.removeLayer(marker));
        markers.value = [];

        // Crear un nuevo marcador
        const marker = L.marker([lat, lng])
            .addTo(map.value)
            .bindPopup(`Latitud: ${lat.toFixed(4)}, Longitud: ${lng.toFixed(4)}`)
            .openPopup();

        // Guardar el nuevo marcador en el array
        markers.value.push(marker);
        emit('getUbicacion', lat, lng);
        }

        

        
  </script>
  
  <style>
  
  
  
  #map {
    height: 30vh; /* Ajusta el tamaño del mapa */
    width: 60vh;
  }
  </style>
  