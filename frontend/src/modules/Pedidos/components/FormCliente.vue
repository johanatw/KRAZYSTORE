<script setup>

import { ref, onMounted } from 'vue';
import Button from 'primevue/button';
import { defineComponent } from 'vue';
import { defineProps } from 'vue';
import { defineExpose } from 'vue';
import InputText from 'primevue/inputtext';
import { PersonaServices } from '@/services/PersonaServices';
import {TipoDocServices} from '@/services/TipoDocServices'

import { useToast } from 'primevue/usetoast';
import { defineEmits } from 'vue';
const emit = defineEmits(['clientSaved','changeAccion','changeClient','updateClient']);
const props = defineProps(['titulo','action']);
const submitted = ref(false);
const clienteSelected = ref({});
const campoTelefonoDisabled = ref(true);
const campoDocDisabled = ref(true);
const campoEmailDisabled = ref(true);
const campoApellidoDisabled = ref(true);
const error = ref(false);
const msg = ref();

const tiposDoc = ref([
               
            
]);

const validarCampo = (campo)=>{
    if (clienteSelected.value[campo] && clienteSelected.value[campo].trim()) {
        
        emit("updateClient");
    }
   
}


const showClient = (cliente) => {
  
    TipoDocServices.obtenerTipoDocs().then((response)=>{
        tiposDoc.value = response.data;
        if (!cliente.nroDoc) {
           
            console.log('updateClientemit');
            emit("updateClient");
            cliente.tipoDoc = response.data[0];
        } else {
            
        }

    });
  
  
  
console.log(cliente);
/*
    if (cliente.telefono) {
        campoTelefonoDisabled.value = true;
    } else {
        campoTelefonoDisabled.value = false;
    }
    if (cliente.email) {
        campoEmailDisabled.value = true;
    } else {
        campoEmailDisabled.value = false;
    }
    if (cliente.apellido) {
        campoApellidoDisabled.value = true;
    } else {
        campoApellidoDisabled.value = false;
    }*/
  console.log("showclientselected", cliente);
  clienteSelected.value = cliente;
  submitted.value = false;
  
   
   

}





const submit = () => {
  submitted.value=true;

  
  
  
      PersonaServices.modificarPersona(clienteSelected.value.id, clienteSelected.value).then((response)=>{
        clienteSelected.value = response.data;
        emit("changeAccion",'Buscar');
        showClient(clienteSelected.value);
                  
                  
                    
                    
        }).catch((e)=>{
            error.value = true;
            msg.value = e.response.data.mensaje;
        });
                  
               

}
defineExpose({
   showClient,
   submit,
});

</script>

<template>
    <div v-if="error" style="background-color: rgb(242, 222, 222); 
        border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
            <ul>
                <li v-for="msg in mensaje" style="list-style: none;">
                <a style="color: rgb(173, 89, 86);">{{ msg }}</a>
                </li>
            </ul>
        </div>
  <div class="card flex justify-content-center">
    
          <div  class="card " style="width: 100%;">
            <div class="formgrid grid">
            
            <div class="field col-12 md:col-4"  >
                <label for="nombre">Nombre</label>
                <input v-model="clienteSelected.nombre" disabled="true" id="nombre" type="text" required="true" class="input">
            </div>
            <div class="field col-12 md:col-4" >
                <label for="apellido">Apellido</label>
                <input :disabled="campoApellidoDisabled" v-model="clienteSelected.apellido" id="apellido" type="text"  class="input" @input="validarCampo('apellido')">
            </div>
            <div class="field col-12 md:col-4"  >
                <label for="telefono">Telefono</label>
                <input :disabled="campoTelefonoDisabled" v-model="clienteSelected.telefono" id="telefono" type="text" required="true" class="input" @input="validarCampo('telefono')">
        
            </div>
            <div class="field col-12 md:col-4"  >
                <label for="telefono">Email</label>
                <input :disabled="campoEmailDisabled" v-model="clienteSelected.email" id="email" type="text" required="true"  class="input" @input="validarCampo('email')">
            </div>
            <div class="field col-12 md:col-4">
            <label for="state">Tipo de documento</label>
            <select v-model="clienteSelected.tipoDoc" id="tipodoc"  class="input" :disabled="campoDocDisabled">
                <option v-for="tipo in tiposDoc" :value="tipo">{{tipo.descripcion}}</option>
            </select>
        </div>
                              
            <div class="field col-12 md:col-4"  >
                <label for="documento">N° de Documento</label>
                <input :disabled="campoDocDisabled"  v-model="clienteSelected.nroDoc" id="documento" type="text"  class="input" @input="validarCampo('nroDoc')">
            </div>
            <div class="field col-12 md:col-12">
            <Button type="button" class="btn"  label="Cambiar" @click="emit('changeClient')"/>
                        
        </div>
          </div>
        </div>
        
        

  </div>
</template>
<style>
.input {
    padding: 0.5rem!important;
    width: 100%;
    border-radius: var(--border-radius) !important;
    border-style: solid !important;
    border-width: 1px !important;
    font-size: 1rem !important;
    border-color: var(--surface-border) !important;
    background-color: var(--surface-overlay) !important;
    color: var(--text-color) !important;
    outline: none !important;
   
    appearance: auto !important;
}

.input:disabled{
    background-color: rgb(243, 237, 237) !important;
}
</style>