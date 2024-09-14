<script setup>

import Card from 'primevue/card';
import { ref, onMounted } from "vue";

import Dropdown from 'primevue/dropdown';

import {FormasPagoServices} from '@/services/FormasPagoServices';


const selectedFormaPago = ref({});


const formasPago = ref([
]);

const setPago = (pago) =>{
    selectedFormaPago.value = pago;
}

onMounted(() => {


    FormasPagoServices.obtenerFormasPago().then((data) => {
        formasPago.value=data.data;
        selectedFormaPago.value=data.data[1];

    });
});

defineExpose({
    selectedFormaPago,
    setPago
});

</script>
<template>
    
    
    <div class="grid">
        
        <div class="col-12 md:col-12" >
            <Card style="height: 100%;">
                <template #title> Forma de pago </template>
                <template #content>
                    <div class="formgrid grid">
                        <div class="field col-12 md:col-12">
                            <Dropdown v-model="selectedFormaPago" :options="formasPago" optionLabel="descripcion" placeholder="Select a City" class="w-full md:w-14rem" />
    
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