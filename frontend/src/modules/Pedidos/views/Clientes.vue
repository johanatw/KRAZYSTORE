<script setup>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Panel from 'primevue/panel';
import InputText from 'primevue/inputtext';
import { PersonaServices } from '@/services/PersonaServices';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { ref, onMounted } from 'vue';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import {DepartamentoServices } from '@/services/DepartamentoServices';
import {CiudadServices } from '@/services/CiudadServices';
import {DireccionServices} from '@/services/DireccionServices';

const clienteDialog = ref(false);

const clientes= ref([]);
const direccion = ref({});
const ciudades = ref([]);
const departamentos = ref([]);

async function getClientes() {
  await PersonaServices.obtenerClientes().then((data) => {
       clientes.value = data.data;
   });
}
//Otros

onMounted(() => {
    getClientes();
});
/*<!--Para abrir registro de cliente(funciona)-->*/
const registrarCliente = () =>{    
    clientes.value = {};
    clienteDialog.value = true;
}
/* ----------------------------------------------*/

const modificarCliente = (cli) => {
    PersonaServices.getPersona(cli.id).then((data) => {
        console.log("data direccion");
        console.log(data.data.direccion);
       cliente.value = data.data.persona;
       clienteDialog.value = true;
       if (data.data.direccion) {
        direccion.value = data.data.direccion;
        direccion.value.departamento = data.data.direccion.ciudad.departamento;
       } 
       if(data.data.direccion && data.data.direccion.ciudad != null){
        console.log("entra if ");
        getCiudades(data.data.direccion.ciudad.departamento.id);
       }
 
    });
    
};

const validarDireccionCliente = (dir) => {
    if (algunCampoTieneValor(dir) && (!dir.calle1 || !dir.ciudad)) {
        return false;
    }

    if (!algunCampoTieneValor(dir)) {
        return true;
    }
    
    
    return true;

};


const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});




</script>


<template>
    <div class="flex p-fluid justify-content-center " >
        <Dialog v-model:visible="clienteDialog" :closable="false" :style="{width: '450px'}" header="Cliente" :modal="true" class="p-fluid">
        <div class="formgrid">
        <div class="field">
            <label for="name">Nombre</label>
            <InputText fluid id="name" v-model.trim="clientes.nombre" required="true" autofocus :class="{'p-invalid': submitted && !clientes.nombre}" />
            <small class="p-error" v-if="submitted && !clientes.nombre">Ingrese un Nombre</small>
        </div>
        <div class="field">
            <label for="description">Apellido</label>
            <InputText fluid id="description" v-model="clientes.apellido" required="true"  />
        </div>
        <div class="field">
            <label for="inventoryStatus" class="mb-3">Tipo Documento</label>
            <Dropdown fluid id="inventoryStatus" v-model="clientes.tipoDoc" :options="documentos" optionLabel="descripcion" placeholder="Select a Status" />
        </div>
        <div class="field">
            <label for="description">Nro Documento</label>
            <InputText fluid id="description" v-model="clientes.nroDoc" required="true"  />
        </div>
        <div class="field">
            <label for="description">Telefono</label>
            <InputText fluid id="description" v-model="clientes.telefono" required="true"  />
        </div>
        <div class="field">
            <label for="description">Calle Principal</label>
            <InputText fluid id="description" v-model="direccion.calle1" required="true" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.calle1}" />
            <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.calle1">Ingrese Calle Principal</small>
        </div>
        
        <div class="field">
            <label for="description">Calle 2</label>
            <InputGroup fluid>
                <Dropdown v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
                <InputText id="description" v-model="direccion.calle2" required="true"  />
            </InputGroup>
        </div>
        <div class="field" v-if="selectedOp=='Entre'">
            <label for="description">Calle 3</label>
            <InputText fluid id="description" v-model="direccion.calle3" required="true"  />
        </div>
        <div class="field">
            <label for="description">N° Casa</label>
            <InputText fluid id="description" v-model="direccion.nroCasa" required="true"  />
        </div>
        <div class="field " >
            <label for="nombreu">Departamento</label>
            <Dropdown fluid v-model="direccion.departamento" :options="departamentos" optionLabel="descripcion" placeholder="Seleccione un departamento" @change="getCiudades(direccion.departamento.id)"  />
        </div>
        <div class="field " >
            <label for="nombreu">Ciudad</label>
            <Dropdown fluid v-model="direccion.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.ciudad}"  />
            <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.ciudad">Ingrese Ciudad</small>
        </div>
    </div>  

        <template #footer>
            <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
            <Button label="Save" icon="pi pi-check" text @click="saveCliente" />
        </template>
    </Dialog>


<!--Pantalla Principal Lista de Clientes-->
        <Panel style=" position: relative; width: 100%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Clientes</h3>
                </div>
            </template>
         
            <template #icons>
                <div class="flex align-items-center">
                    <Button  icon="pi pi-plus " @click="registrarCliente" style="margin-right: 1% ;"  />
                        <InputGroup>
                            <InputText v-model="filters['global'].value" placeholder="Search..." />
                            <InputGroupAddon>
                                <i class="pi pi-search" />
                            </InputGroupAddon>
                        </InputGroup>
                </div> 
            </template>        
            <div class="card">
                <DataTable  :value="clientes " scrollHeight="400px"  
                :paginator="true" :rows="7" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                    <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
                    <Column field="nombre" sortable header="Nombre y Apellido" aria-sort="ascending" >
                        <template #body="slotProps">
                            {{ slotProps.data.nombre }} &nbsp; {{ slotProps.data.apellido }}
                        </template>
                    </Column>
                    <Column field="nroDoc" header="Documento" aria-sort="ascending" sortable>
                        <template #body="slotProps">
                            <div v-if="slotProps.data.nroDoc != null " >
                                {{ slotProps.data.nroDoc }} &nbsp; ({{ slotProps.data.tipoDoc.descripcion }})
                            </div>
                        </template>
                    </Column>
                    <Column  field="telefono" header="Telefono" aria-sort="ascending" sortable ></Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-search" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-sync" severity="success" text rounded aria-label="Cancel" style="height: 2rem !important; width: 2rem !important;" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel> 
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
