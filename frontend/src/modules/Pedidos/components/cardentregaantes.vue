<script setup>



import RadioButton from 'primevue/radiobutton';


import Card from 'primevue/card';
import { ref, onMounted } from "vue";
import { ModosEntregaServices } from "@/services/ModosEntregaServices";
import FormEnvio from "@/modules/Pedidos/components/FormEnvio.vue";

const selectedFormaEntrega = ref({});

const formasEntrega = ref([
]);


onMounted(() => {
    

    ModosEntregaServices.obtenerModosEntrega().then((data) => {
        formasEntrega.value=data.data;
        selectedFormaEntrega.value = data.data[0];

    });


});

const envio = ref(null);

const costoEnvio = ref(null);










 const setCostoEnvio = (costo) => {
    
    costoEnvio.value=costo;
    console.log("costo", costoEnvio.value);
 }

 const setEntrega = (entrega, envioValue) => {
    console.log('setentrega', entrega);
    selectedFormaEntrega.value = entrega;
    if (entrega.descripcion === 'Envío') {
        envio.value.setEnvio(envioValue);
        console.log('enviocalue', envioValue);
    }
    
 }

 defineExpose({
    selectedFormaEntrega,
    costoEnvio,
    setEntrega
});
</script>
<template>
        

    
    
    <div class="grid">
        <div class="col-12 md:col-12" >
            <Card style="height: 100%;">
                <template #title> Metodo de entrega </template>
                <template #content>
                    <div class="card flex justify-content-start">
                        <div class="flex flex-column gap-3">
                            <div v-for="entrega in formasEntrega" :key="entrega.id" class="flex align-items-center">
                                <RadioButton v-model="selectedFormaEntrega" :inputId="entrega.id.toString()" name="dynamic" :value="entrega" @change="console.log(selectedFormaEntrega)"/>
                                <label :for="entrega.id" class="ml-2">{{ entrega.descripcion }}</label>
                            </div>
                            <div v-show="selectedFormaEntrega.descripcion === 'Envío'">
                                <FormEnvio ref="envio" @setCostoEnvio="setCostoEnvio"/>
                                
                            </div>

                        </div>
                    </div>
                </template>
            </Card>
        </div>
        </div>

        

</template>
<style>

.p-accordion-tab{
    margin: 2%;
    
    
}
.p-icon{
    color: pink;
    margin-right: 1%;
}

.p-accordion-header-link{
    height: 7vh !important;
}
.p-accordion-header-text{
    color: black;
}


.p-card-title{
    font-size:medium;
}
.p-card .p-card-body {
    padding: 1rem;
}
.p-card .p-card-content {
    padding: 0.5rem 0;
}

.principal{
    display: flex;
    border: solid palevioletred 2px;
    justify-content: center;
    border-radius: 1vh;
    margin-left: 4%;
    margin-right: 4%;
    padding: 1%;
}

h3 {
    display: flex;
    font-size: 1.17em;
    margin-block-start: 0px;
    margin-block-end: 0px;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    font-weight: bold;
    justify-content: center;
}
</style>