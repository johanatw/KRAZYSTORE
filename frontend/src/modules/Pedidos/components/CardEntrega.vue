<script setup>



import RadioButton from 'primevue/radiobutton';
import Dialog from 'primevue/dialog';

import Card from 'primevue/card';
import { ref, onMounted } from "vue";
import { ModosEntregaServices } from "@/services/ModosEntregaServices";
import FormEnvio from "@/modules/Pedidos/components/FormEnvio.vue";
const emit = defineEmits(['getEntrega']);
import Button from 'primevue/button';
import { CiudadServices } from '@/services/CiudadServices';
import {EnvioServices} from '@/services/EnvioServices';
import { defineEmits } from 'vue';
import Dropdown from 'primevue/dropdown';


const selectedCiudad = ref({});
const selectedEnvio = ref({});
const medios = ref();

const ciudades = ref([]);
const selectedFormaEntrega = ref({});
const visible = ref(false);

const formasEntrega = ref([
]);


onMounted(() => {
    

    ModosEntregaServices.obtenerModosEntrega().then((data) => {
        formasEntrega.value=data.data;
        selectedFormaEntrega.value = data.data[0];

    });
    CiudadServices.obtenerCiudades().then((data) => {
        ciudades.value=data.data;

    });


});

const getEntrega = () =>{

    emit('getEntrega', selectedFormaEntrega.value,selectedEnvio.value );
    
    visible.value = false;

}

const showDialog = () =>{
    visible.value = true;
}


const searchEnvio= () => {
    EnvioServices.obtenerCostosEnvioByCiudad(selectedCiudad.value.id).then((data) => {
 
        medios.value=data.data;
        selectedEnvio.value = { };
        

    });
}

const setEntrega = (entrega, envioValue) => {
    selectedFormaEntrega.value = entrega;
    if (entrega.descripcion === 'Envío') {
        selectedEnvio.value = envioValue;
        //envio.value.setEnvio(envioValue);
        selectedCiudad.value = envioValue.ciudad;
    }
    
 }


const changeEntrega = () => {
    if (selectedFormaEntrega.value && selectedFormaEntrega.value.descripcion === "Retiro") {
        selectedEnvio.value = null
    }else{
        selectedCiudad.value = {};
        medios.value = null;
    }
}



 defineExpose({
    selectedFormaEntrega,
    selectedEnvio,
    setEntrega,
    showDialog
});
</script>
<template>
    <div class="card flex justify-content-center" >
        <Dialog  v-model:visible="visible" modal header="Metodo de entrega" :closable="false" :draggable="false" :style="{ width: '40rem' }"  >
            <div class="card flex justify-content-start">
                <div class="flex flex-column gap-3">
                    <div v-for="entrega in formasEntrega" :key="entrega.id" class="flex align-items-center">
                        <RadioButton v-model="selectedFormaEntrega" :inputId="entrega.id.toString()" name="dynamic" :value="entrega" @change="changeEntrega"/>
                        <label :for="entrega.id" class="ml-2">{{ entrega.descripcion }}</label>
                    </div>
                    <div v-show="selectedFormaEntrega.descripcion === 'Envío'">
                        <div class="formgrid grid">

                            <div class="field col-12 ">
                                <label for="state">Ciudad: &nbsp;</label>
                                <Dropdown v-model="selectedCiudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione un elemento" class="w-full md:w-14rem" @change="searchEnvio"/>
                            
                            </div>
                            <div class="field col-12 ">
                            
                                <label for="state">Envío con:&nbsp;</label>
                                <Dropdown v-model="selectedEnvio" :options="medios" optionLabel="envio" placeholder="Seleccione un elemento" class="w-full md:w-14rem" >
                                    <template #value="slotProps">
                                        <div v-if="slotProps.value && slotProps.value.envio" class="flex align-items-center">
                                            <div>{{ slotProps.value.envio.descripcion }} - {{ slotProps.value.costo }} Gs</div>
                                        </div>
                                        <span v-else>
                                            {{ slotProps.placeholder }}
                                        </span>
                                    </template>
                                    <template #option="slotProps">
                                        <div class="flex align-items-center">
                                            <div>{{ slotProps.option.envio.descripcion}} - {{ slotProps.option.costo }} Gs</div>
                                        </div>
                                    </template>
                                </Dropdown>
                            </div>    
                        </div>
                    </div>

                </div>
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">
                    <Button  label="Cancelar"  style="margin-right: 1%;" @click="visible=false"/>
                    <Button  label="Aceptar"   @click="getEntrega"/>
    
                </div>

            </template>
                
        </Dialog>
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