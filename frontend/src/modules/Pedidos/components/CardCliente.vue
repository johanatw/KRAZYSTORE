<script setup>




import Button from 'primevue/button';

import Card from 'primevue/card';
import { ref, onMounted } from "vue";
import FormCliente from '@/modules/Pedidos/components/FormCliente.vue';
import FormSearchCliente from '@/modules/Pedidos/components/FormSearchCliente.vue';
import { TipoDocServices } from "@/services/TipoDocServices";
import Checkbox from 'primevue/checkbox';
import { defineEmits } from 'vue';
import Dialog from 'primevue/dialog';
import InputGroup from 'primevue/inputgroup';
import InputText from 'primevue/inputtext';
import { PersonaServices } from "@/services/PersonaServices";
const emit = defineEmits(['addDatoFactura']);
const searchOption = ref();
const searched = ref(false);
const filter = ref('Telefono');
const form = ref();
const searchValue = ref();

const searchOptions = ref([
    'Telefono',
    'N° Doc'
])




const buscarCliente = ref(false);
const show = ref(false);
const formFactura = ref(null);
const clienteSelected = ref();

const accion = ref('Buscar');

const selectedPersona = ref();
const inactivo = ref(true);
const visible = ref(false);
const cambioClienteContacto = ref(false);
const cambioClienteFactura= ref(false);
const formContact = ref(null);
const personaFactura = ref();
const updateContact = ref(false);
const updateFactura = ref(false);

const checked = ref(false);

const tiposDoc = ref([
               
            
]);

onMounted(() => {
    
  TipoDocServices.obtenerTipoDocs().then((response)=>{
        tiposDoc.value = response.data;

                  });
});

const searchClient = () => {
    searched.value = true;
    console.log("searched.value");
     accion.value = 'Buscar';
    if (filter.value==='Telefono') {
        searchOption.value = 'telefono';
    } else {
        searchOption.value =  'ci';
    }
    PersonaServices.obtenerPersona(searchValue.value, searchOption.value).then((data)=> {
   
        
        clienteSelected.value = data.data;
       
        
        if (!clienteSelected.value) {
            clienteSelected.value = {};
            accion.value='Registrar';
            form.value.tipoForm = 'R';
        } else{
            form.value.tipoForm = 'V';

        }
        
        form.value.showClient(clienteSelected.value);
        
         
    });
    clienteSelected.value = {};
 
   

}


const modificarCliente = () =>{
    accion.value = 'Modificar'
    form.value.tipoForm = 'M';
    form.value.showClient(clienteSelected.value);
}


const getClient = () =>{
    console.log("getclient");
    buscarCliente.value= false;

    if (!cambioClienteContacto.value && !cambioClienteFactura.value) {
        if (checked.value) {
        personaFactura.value = clienteSelected.value;    
        formFactura.value.showClient(personaFactura.value);
            
        } else {
        selectedPersona.value = clienteSelected.value;
        formContact.value.showClient(selectedPersona.value);
        }
        
        
    } else {
        if (cambioClienteContacto.value) {
            selectedPersona.value = clienteSelected.value;
            formContact.value.showClient(selectedPersona.value);
        } else {
            personaFactura.value = clienteSelected.value;    
            formFactura.value.showClient(personaFactura.value);
        }
        cambioClienteContacto.value = false;
        cambioClienteFactura.value = false;
        
    }

    
    visible.value = false;
    show.value= true;
    if (visible.value) {
        visible.value=false;
    }
    inactivo.value = false;
    restartSearchDialog();
   
}

const restartSearchDialog = () =>{
    clienteSelected.value = null;
    form.value.tipoForm = 'V';
    accion.value = 'Buscar'
    searchValue.value = null;

}

const addPersonaFactura = () =>{

    if (checked.value) {
        visible.value=true;
    }else{
        personaFactura.value=null;
       

    }
    
}


 const cancel = () => {
    if (form.value.tipoForm === 'M') {
        accion.value = 'Buscar'
        form.value.tipoForm = 'V';
        console.log("cancelmod", clienteSelected.value);
        form.value.showClient(clienteSelected.value);
    } else {
        if (checked.value && !personaFactura.value) {
            checked.value = false;
        }
        visible.value = false;
        cambioClienteContacto.value = false;
        cambioClienteFactura.value = false;
        restartSearchDialog();
    }

    
 }

 const updateCliente = (clienteUpdated) => {
    accion.value = 'Buscar';
    clienteSelected.value = clienteUpdated;
    
 }

 

 const changeClient= (cliente) => {
    if (cliente == 'Contacto') {
        cambioClienteFactura.value=false;
        cambioClienteContacto.value=true;
    } else if (cliente == 'Factura') {
        cambioClienteFactura.value=true;
        cambioClienteContacto.value=false;
    }
    visible.value = true;
 }


 const updateClient= (tipo) => {
    if (tipo == 'C') {
        updateContact.value = true;
    } else if (tipo == 'F'){
        updateFactura.value = true;
    }
   
 }

 const submit = () => {
  form.value.submit(true);
   
   

}

const setCliente = (c) =>{
    show.value = true;
    inactivo.value = false;
    selectedPersona.value = c;
    formContact.value.showClient(c);
}

 defineExpose({
    selectedPersona,
    personaFactura,
    updateContact,
    updateFactura,
    setCliente
});

</script>
<template>
    <div class="card flex justify-content-center" >
        <Dialog v-if="visible" v-model:visible="visible" modal header="Header" :closable="false" :draggable="false" :style="{ width: '40rem' }"  >
            <template #header>
            
            <div class="flex align-items-center " style="justify-content: space-between; width: 100%;" >
                <span class="font-bold">{{ accion }} Cliente</span>
                <div class="card flex" style="justify-content: flex-end; width: 60%; " >

                <InputGroup>
                    
                    
                <select v-model="filter" id="state" class="filtro" style="width: 6rem;">
                    <option v-for="option in searchOptions">{{option}}</option>
                </select>

                    <InputText v-model="searchValue" placeholder="Search..." />
                    <Button icon="pi pi-search" severity="danger" @click="searchClient"/>

                </InputGroup>
                </div>
            </div>
            </template>
            

            <FormSearchCliente :action="accion" ref="form"  @updateCliente="updateCliente" />
        <template #footer>
            <div class="card flex" style="justify-content: end;">
                <Button  label="Cancelar"  style="margin-right: 1%;"  @click="cancel" />
                <div v-if=" accion==='Buscar' && clienteSelected" class="card flex" style="justify-content: end;">  
                    <Button  label="Modificar"  style="margin-right: 1%;" @click="modificarCliente"/>
                    <Button  label="Aceptar"   @click="getClient"/>
                </div>
                <div v-else-if="accion==='Modificar' || accion==='Registrar'" class="card flex" style="justify-content: end;">
                    <Button  label="Guardar"  @click="submit" />
                            
                </div>
                      
            </div>

        </template>

        </Dialog>
    </div>
    <div class="grid">
        <div class="col-12 md:col-12" >

            <Card style="height: 100%;" >
                <template #title> Datos del Cliente </template>
                <template #content>
                    <div  class="card" >
                        <div v-show="show">
                            <FormCliente ref="formContact" @updateClient="updateClient('C')" @changeClient="changeClient('Contacto')"/>
                            
        
                        </div>
                    <div>
                        <Button  v-if="inactivo" type="button" class="btn pi pi-user"  label="  Cliente" @click="visible=true"/>
                       
                                    
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

.btn{
    padding: 0.5rem!important;
    width: 10rem;
    height: max-content;
    border-radius: var(--border-radius) !important;
    border-style: solid !important;
    border-width: 1px !important;
    font-size: 1rem !important;
    border-color: var(--surface-border) !important;
    background-color: var(--surface-overlay) !important;
    color: gray !important;
    outline: none !important;
   

}
.filtro{
   
   border-top-left-radius: 6px;
   border-bottom-left-radius: 6px;
   width: 3rem;
   padding: 0.75rem 0;
   border: 1px solid #d1d5db;
   font-family: var(--font-family);
   font-feature-settings: var(--font-feature-settings, normal);
   font-size: 1rem;
   color: #4b5563;
   border: 1px solid #d1d5db;
   outline: none !important;
   appearance: auto !important;
}
</style>