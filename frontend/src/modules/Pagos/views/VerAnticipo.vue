<template>
    <div class="card flex p-fluid justify-content-center " >
        <object  :data="pdf" type="application/pdf" width="100%" height="594px"></object>
</div>
            
        </template>
    <script setup>
    import { AnticipoServices } from '@/services/AnticipoServices';
    import { ref, onMounted } from 'vue';
    import router from '@/router';

    const pdf = ref(null);

    onMounted(() => {
        console.log("mounted");
        document.title = "Factura";
        
        pdf.value = "http://localhost:7070/api/anticipos/pdf?idAnticipo="+router.currentRoute.value.params.id;
        /*AnticipoServices.obtenerAnticipoPdf(router.currentRoute.value.params.id).then((data)=>{
        console.log(data);
        pdf.value = data;
    })*/
 
        

   
});
</script>