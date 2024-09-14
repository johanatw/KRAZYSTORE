

<template>
                
                
                          
    <div class="formgrid grid">
        
        <div class="field col-12 ">
            <label for="state">Ciudad</label>
            <Dropdown v-model="selectedCiudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione un elemento" class="w-full md:w-14rem" @change="searchEnvio"/>
           
        </div>
        <div class="field col-12 ">
           
           <label for="state">Envío con:</label>
           <Dropdown v-model="selectedEnvio" :options="medios" optionLabel="envio" placeholder="Seleccione un elemento" class="w-full md:w-14rem" @change="setCostoEnvio">
            <template #value="slotProps">
                <div v-if="slotProps.value.envio" class="flex align-items-center">
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

</template>

<script setup>
import { ref, onMounted } from 'vue';
import { CiudadServices } from '@/services/CiudadServices';
import {EnvioServices} from '@/services/EnvioServices';
import { defineEmits } from 'vue';
import Dropdown from 'primevue/dropdown';


const selectedCiudad = ref({});
const selectedEnvio = ref({});
const medios = ref();

const ciudades = ref([]);


onMounted(() => {
    
    CiudadServices.obtenerCiudades().then((data) => {
        ciudades.value=data.data;

    });
    
});

const setEnvio= (e) => {
    selectedEnvio.value = e;
    selectedCiudad.value = e.ciudad;
    emit("setCostoEnvio",selectedEnvio.value);
}

const searchEnvio= () => {
    EnvioServices.obtenerCostosEnvioByCiudad(selectedCiudad.value.id).then((data) => {
 
        medios.value=data.data;
        selectedEnvio.value = { };
        

    });
}

const visible = ref(true);
const setCostoEnvio= () => {
    emit("setCostoEnvio",selectedEnvio.value);
}

const showDialog= () => {
    visible.value=true;
}
const emit = defineEmits(['setCostoEnvio']);


defineExpose({
    showDialog,
    setEnvio,
});


</script>
<style>


</style>
