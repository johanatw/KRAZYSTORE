<template>
    

    <div class=" flex p-fluid justify-content-center " >
     
    
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h4 class="font-bold">Factura N° {{ factura}}</h4>

                </div>
            </template>
            <template #icons>
            
            
            </template>
            <div class="flex p-fluid justify-content-center " >
                        <object  v-if="url" :data="url" type="application/pdf" width="100%" height="594px"></object>
                </div>
   
        </Panel>
    </div>
    
    </template>
    <script setup>
    
    import { ref, onMounted } from 'vue';
    import { VentaServices } from '@/services/VentaServices';
    import router from '@/router';
    import Panel from 'primevue/panel';
    import Card from 'primevue/card';
    import axios from 'axios';

    const pdf = ref(null);
    const factura = ref("");
    const url = ref(null);

    onMounted(() => {
        console.log("mounted");
        console.log(router.currentRoute.value.params.id);
        document.title = "Factura";
        VentaServices.getVenta(router.currentRoute.value.params.id).then((data)=>{
        
        factura.value = data.data.nroFactura;
    })
    axios.get('http://localhost:7070/api/ventas/pdf', {
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
        params: { id: router.currentRoute.value.params.id },  // Pasa el ID del anticipo
        responseType: 'blob'  // 👈 Necesario para archivos
    })
    .then(response => {
        url.value = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
        //window.open(url); // Abre el PDF en una nueva pestaña
    })
    .catch(error => console.error('Error al obtener el PDF:', error));

    /*    pdf.value = "http://localhost:7070/api/ventas/pdf?id="+router.currentRoute.value.params.id,{
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    };*/
        /*AnticipoServices.obtenerAnticipoPdf(router.currentRoute.value.params.id).then((data)=>{
        console.log(data);
        pdf.value = data;
    })*/
 
        

   
});

</script>