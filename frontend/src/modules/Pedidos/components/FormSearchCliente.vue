<script setup>
import { ref, onMounted } from 'vue';

import { defineProps } from 'vue';
import { defineExpose } from 'vue';
import { PersonaServices } from '@/services/PersonaServices';
import {TipoDocServices} from '@/services/TipoDocServices';
import Button from 'primevue/button';
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { defineEmits } from 'vue';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();
const messageError = (msg) => {
    confirm.require({
        group: 'headless',
        header: 'Ocurrio un error',
        message: msg.toUpperCase(),
    });
};

const emit = defineEmits(['clientSaved','changeAccion',"changeClient"]);

const props = defineProps(['titulo','action','cliente']);
const submitted = ref(false);
const clienteSelected = ref({});
const tipoForm = ref('V');
const disabled = ref(true);
const updatedCliente = ref({});
const tiposDoc = ref([
               
            
]);

onMounted(() => {
    
  TipoDocServices.obtenerTipoDocs().then((response)=>{
        tiposDoc.value = response.data;

                  });
});


const showClient = (cliente) => {
  console.log("cliente",cliente);
  console.log("tipoform",tipoForm.value);
  clienteSelected.value = cliente;
  if (tipoForm.value !== 'V') {
    console.log("entraif");
    
    disabled.value = false;
  }else{
    console.log("entraelse");
    disabled.value = true;
    
  }
  updatedCliente.value.nombre = clienteSelected.value.nombre;
    updatedCliente.value.apellido = clienteSelected.value.apellido;
    updatedCliente.value.telefono = clienteSelected.value.telefono;
    updatedCliente.value.email = clienteSelected.value.email;
    updatedCliente.value.tipoDoc = clienteSelected.value.tipoDoc;
    updatedCliente.value.nroDoc = clienteSelected.value.nroDoc;
  if (!clienteSelected.value.nroDoc) {
    
    clienteSelected.value.tipoDoc = tiposDoc.value[0];
  }
  if (!updatedCliente.value.nroDoc) {
    
    updatedCliente.value.tipoDoc = tiposDoc.value[0];
  }
  submitted.value = false;

  console.log("clienteselected2", clienteSelected.value);

}
 
const onda = () => {
    console.log("tipoform",tipoForm.value);
    console.log("cliente",clienteSelected.value.apellido);
    console.log("updatecliente",updatedCliente.value.apellido);
  
}

const submit = () => {
  submitted.value=true;
  
  for (let campo in updatedCliente.value){
  console.log(updatedCliente.value[campo]);
  if (updatedCliente.value[campo]== '') {
    updatedCliente.value[campo]= null;
  }
  console.log(updatedCliente.value[campo]);
}
console.log(updatedCliente.value);
  if (updatedCliente.value.nombre && (updatedCliente.value.telefono || updatedCliente.value.nroDoc)) {
    if (clienteSelected.value.id) {
      PersonaServices.modificarPersona(clienteSelected.value.id, updatedCliente.value).then((response)=>{
        
        toast.add({ severity: 'success',  detail: 'Registro modificado', life: 3000 });
        
        clienteSelected.value = response.data;
        tipoForm.value = 'V';
        showClient(clienteSelected.value);          
        emit("changeClient",clienteSelected.value);  
                    
                  }).catch(
                    (error)=>messageError(error.response.data.mensaje)
                  );
                  
               
                
                

                
    } else {
 
      PersonaServices.registrarPersona(updatedCliente.value).then((response)=>{
        console.log(response.status);
        clienteSelected.value = response.data;
        tipoForm.value = 'V';
        showClient(clienteSelected.value);

        emit("changeClient",clienteSelected.value); 
      
     
        
        
                  }).catch((error)=>{
                    messageError(error.response.data.mensaje);
                    console.log(error.response.data.mensaje)
                  });
                  
                  
                   
      
    }
  }
   
   

}
defineExpose({
   showClient,
   submit,
   tipoForm
});

</script>

<template>
    <ConfirmDialog group="headless">
        <template #container="{ message, acceptCallback }">
            <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                    <i class="pi pi-times text-5xl"></i>
                </div>
                <span class="font-bold text-2xl block mb-2 mt-4">{{ message.header }}</span>
                <p class="mb-0">{{ message.message }}</p>
                <div class="flex align-items-center gap-2 mt-4">
                    <Button label="Ok" @click="acceptCallback"></Button>
                </div>
            </div>
        </template>
    </ConfirmDialog>
    
    <Toast />
  <div class="card flex justify-content-center">
    
        <div class="card " style="padding: 10%; padding-top: 0%; ">
          <div class="formgrid grid">
            <div class="field col-12 md:col-4" >
                <label for="nombreu">Nombre</label>
                <input v-model.trim="updatedCliente.nombre" :disabled="disabled" id="nombreu" type="text" required="true" class="input" :class="{'p-invalid': submitted && !updatedCliente.nombre}" >
                <small class="p-error" v-if="submitted && !updatedCliente.nombre">Name is required.</small>
            </div>
            <div class="field col-12 md:col-4" >
                <label for="apellidou">Apellido</label>
                <input  v-model.trim="updatedCliente.apellido" :disabled="disabled" @input="onda" id="apellidou" type="text"  class="input">
            </div>
            <div class="field col-12 md:col-4" >
                <label for="telefonou">Telefono</label>
                <input  v-model.trim="updatedCliente.telefono" :disabled="disabled" id="telefonou" type="text" required="true" class="input" :class="{'p-invalid': submitted && !updatedCliente.telefono && !updatedCliente.nroDoc}" >
                <small class="p-error" v-if="submitted && !updatedCliente.telefono ">Debe ingresar N° Telefono.</small>
            </div>
            <div class="field col-12 md:col-4"  >
                <label for="telefono">Email</label>
                <input  v-model.trim="updatedCliente.email"  :disabled="disabled" id="email" type="text" required="true" class="input" :class="{'p-invalid': submitted && !updatedCliente.telefono}" >
                
            </div>
            <div class="field col-12 md:col-4 ">
            <label for="state">Tipo de documento</label>
            <select v-model="updatedCliente.tipoDoc" :disabled="disabled" id="tipodocu" class="input" >
                <option v-for="tipo in tiposDoc" :value="tipo">{{tipo.descripcion}}</option>
            </select>
        </div>
                              
            <div class="field col-12 md:col-4"  >
                <label for="documentou">N° de Documento</label>
                <input v-model.trim="updatedCliente.nroDoc" id="documentou" :disabled="disabled" type="text"  class="input" >
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