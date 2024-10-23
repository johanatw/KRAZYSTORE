<template>
    

    <div class="card flex p-fluid justify-content-center " >
     
    
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h4 class="font-bold">Factura N° {{ factura}}</h4>

                </div>
            </template>
            <template #icons>
            
            
            </template>
            <div class="card flex p-fluid justify-content-center " >
                        <object  :data="pdf" type="application/pdf" width="100%" height="594px"></object>
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

    const pdf = ref(null);
    const factura = ref("");

    onMounted(() => {
        console.log("mounted");
        console.log(router.currentRoute.value.params.id);
        document.title = "Factura";
        VentaServices.getVenta(router.currentRoute.value.params.id).then((data)=>{
        
        factura.value = data.data.nroFactura;
    })
        
        pdf.value = "http://localhost:7070/api/ventas/pdf?id="+router.currentRoute.value.params.id;
        /*AnticipoServices.obtenerAnticipoPdf(router.currentRoute.value.params.id).then((data)=>{
        console.log(data);
        pdf.value = data;
    })*/
 
        

   
});

</script>