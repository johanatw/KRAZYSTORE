<script setup>




import Button from 'primevue/button';
import { ref, onMounted } from "vue";
import { TipoDocServices } from "@/services/TipoDocServices";
import { defineEmits } from 'vue';
import Dialog from 'primevue/dialog';
import InputGroup from 'primevue/inputgroup';
import InputText from 'primevue/inputtext';
import { PersonaServices } from "@/services/PersonaServices";
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import Toast from 'primevue/toast';
const confirm = useConfirm();
const toast = useToast();
const messageError = (msg) => {
    confirm.require({
        group: 'headless',
        header: 'Ocurrio un error',
        message: msg.toUpperCase(),
    });
};

const emit = defineEmits(['addDatoFactura','getCliente']);
const searchOption = ref();
const filter = ref('Telefono');
const searchValue = ref();
const submitted = ref(false);
const disabled = ref(true);

const searchOptions = ref([
    'Telefono',
    'N° Doc'
])


const clienteSelected = ref({});
const clienteForm = ref({});

const accion = ref('Buscar');
const visible = ref(false);


const tiposDoc = ref([]);

onMounted(() => {
    
    TipoDocServices.obtenerTipoDocs().then((response)=>{
        tiposDoc.value = response.data;
    });
});

const searchClient = () => {
    changeTipoForm('Buscar');

    if (filter.value==='Telefono') {
        searchOption.value = 'telefono';
    } else {
        searchOption.value =  'ci';
    }

    PersonaServices.obtenerPersona(searchValue.value, searchOption.value).then((data)=> {
        clienteSelected.value = data.data;
        
        if (!clienteSelected.value) {
            messageError("No existe el cliente");
            //clienteSelected.value = {};
            //clienteForm.value = {};
            //clienteForm.value.tipoDoc = tiposDoc.value[0];
            //habilitarForm(false);
            //changeTipoForm('Registrar');
        } else{
            habilitarForm(true);
            showClient();
        }
 
    });
    clienteSelected.value = {};

}

const showClient = (cliente=null)=> {
    if (cliente) {
        clienteSelected.value = cliente;
    } 
    clienteForm.value.nombre = clienteSelected.value.nombre;
    clienteForm.value.apellido = clienteSelected.value.apellido;
    clienteForm.value.telefono = clienteSelected.value.telefono;
    clienteForm.value.email = clienteSelected.value.email;
    clienteForm.value.tipoDoc = clienteSelected.value.tipoDoc;
    clienteForm.value.nroDoc = clienteSelected.value.nroDoc; 

    if (!clienteForm.value.nroDoc) {
            clienteForm.value.tipoDoc = tiposDoc.value[0];}
    
}

const registrarCliente = () =>{
    clienteSelected.value = {};
    clienteForm.value = {};
    clienteForm.value.tipoDoc = tiposDoc.value[0];
    habilitarForm(false);
    changeTipoForm('Registrar');

}

const modificarCliente = () =>{
    showClient();
    changeTipoForm('Modificar');
    habilitarForm(false);
}


const getCliente = () =>{
    emit('getCliente', clienteSelected.value);
    
    showDialog(false);
    restartSearchDialog();
   
}

const restartSearchDialog = () =>{
    clienteSelected.value = {};
    clienteForm.value = {};
    changeTipoForm('Buscar');
    submitted.value = false;
    searchValue.value = null;
    
    habilitarForm(true);
}



 const cancel = () => {

    if (accion.value === 'Modificar') {
        clienteForm.value = clienteSelected.value;
        changeTipoForm('Buscar');
        habilitarForm(true);
    } else if(accion.value === 'Registrar') { 
        clienteForm.value = clienteSelected.value;
        changeTipoForm('Buscar');
        habilitarForm(true);
    } else {
        showDialog(false);
        restartSearchDialog();
    }
 }

const changeTipoForm = (tipo) => {
    accion.value = tipo;
}

const habilitarForm = (val) => {
    disabled.value = val;
}

const submit = () => {
    submitted.value=true;
  
    for (let campo in clienteForm.value){
  
        if (clienteForm.value[campo]== '') {
            clienteForm.value[campo]= null;
        }
    }

    if (clienteForm.value.nombre && (clienteForm.value.telefono || clienteForm.value.nroDoc)) {
        if (clienteSelected.value.id) {
            PersonaServices.modificarPersona(clienteSelected.value.id, clienteForm.value).then((response)=>{
        
                toast.add({ severity: 'success',  detail: 'Registro modificado', life: 3000 });
        
                clienteSelected.value = response.data;
                changeTipoForm('Buscar');
                habilitarForm(true);
                showClient();
            }).catch(
            (error)=>messageError(error.response.data.mensaje)
            );  
        } else {
    
            PersonaServices.registrarPersona(clienteForm.value).then((response)=>{
                
                clienteSelected.value = response.data;
                changeTipoForm('Buscar');
                habilitarForm(true);
                showClient();
            
            }).catch((error)=>{
                messageError(error.response.data.mensaje);
            });
                    
        }
    
    }
}


const showDialog = (valor=true) =>{
    visible.value = valor;
}

 defineExpose({
    showDialog,
    showClient
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
    <div class="card flex justify-content-center" >
        <Dialog  v-model:visible="visible" modal header="Header" :closable="false" :draggable="false" :style="{ width: '40rem' }"  >
            <template #header>
            
            <div class="flex align-items-center " style="justify-content: space-between; width: 100%;" >
                <span class="font-bold">{{ accion }} Cliente</span>
                <div class="card flex" style="justify-content: flex-end; width: 60%; " >

                <InputGroup>
                    
                    
                <select v-model="filter" id="state" class="filtro" style="width: 6rem;">
                    <option v-for="option in searchOptions">{{option}}</option>
                </select>

                    <InputText v-model="searchValue" placeholder="Search..." />
                    <Button icon="pi pi-search" severity="danger"  @click="searchClient"/>

                </InputGroup>
                </div>
            </div>
            </template>
            

            <div class="card flex justify-content-center">
    
        <div class="card " >
            <div  class="formgrid grid">
                <div class="field col-12 md:col-4" >
                    <label for="nombreu">Nombre</label>
                    <input v-model.trim="clienteForm.nombre" :disabled="disabled" id="nombreu" type="text" required="true" class="input" :class="{'p-invalid': submitted && !clienteForm.nombre}" >
                    <small class="p-error" v-if="submitted && !clienteForm.nombre">Se requiere nombre.</small>
                </div>
                <div class="field col-12 md:col-4" >
                    <label for="apellidou">Apellido</label>
                    <input  v-model.trim="clienteForm.apellido" :disabled="disabled" id="apellidou" type="text"  class="input">
                </div>
                <div class="field col-12 md:col-4" >
                    <label for="telefonou">Telefono</label>
                    <input  v-model.trim="clienteForm.telefono" :disabled="disabled" id="telefonou" type="text" required="true" class="input" :class="{'p-invalid': submitted && !clienteForm.telefono && !clienteForm.nroDoc}" >
                    <small class="p-error" v-if="submitted && !clienteForm.telefono && !clienteForm.nroDoc">Ingrese Teléfono o Documento</small>
                </div>
                <div class="field col-12 md:col-4"  >
                    <label for="telefono">Email</label>
                    <input  v-model.trim="clienteForm.email"  :disabled="disabled" id="email" type="text" required="true" class="input" :class="{'p-invalid': submitted && !clienteForm.telefono}" >
                    
                </div>
                <div class="field col-12 md:col-4 ">
                <label for="state">Tipo de documento</label>
                <select v-model="clienteForm.tipoDoc" :disabled="disabled" id="tipodocu" class="input" >
                    <option v-for="tipo in tiposDoc" :value="tipo">{{tipo.descripcion}}</option>
                </select>
            </div>
                                
                <div class="field col-12 md:col-4"  >
                    <label for="documentou">N° de Documento</label>
                    <input v-model.trim="clienteForm.nroDoc" id="documentou" :disabled="disabled" type="text"  class="input" :class="{'p-invalid': submitted && !clienteForm.telefono && !clienteForm.nroDoc}">
                    <small class="p-error" v-if="submitted && !clienteForm.telefono && !clienteForm.nroDoc">Ingrese Teléfono o Documento</small>
                </div>
            </div>

            
            </div>

        </div>
        <template #footer>
            <div class="card flex" style="justify-content: end;">
                <Button  label="Cancelar"  style="margin-right: 1px;"  @click="cancel" />
                <div v-if="accion==='Buscar'" class="card flex" style="justify-content: end;">
                    <Button  label="Registrar"  style="margin-right: 1%;"  @click="registrarCliente" />
                            
                </div>
                
                <div v-if=" accion==='Buscar' && clienteForm.nombre" class="card flex" style="justify-content: end;">  
                    
                    <Button  label="Modificar"  style="margin-right: 1%;" @click="modificarCliente"/>
                    <Button  label="Aceptar"  style="margin-right: 1%;" @click="getCliente"/>
                </div>

                <div v-else-if="accion==='Modificar' || accion==='Registrar'" class="card flex" style="justify-content: end;">
                    <Button  label="Guardar"  @click="submit" />
                            
                </div>
                
                      
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