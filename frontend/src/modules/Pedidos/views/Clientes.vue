<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Panel from 'primevue/panel';
import InputText from 'primevue/inputtext';
import { PersonaServices } from '@/services/PersonaServices';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import {DepartamentoServices } from '@/services/DepartamentoServices';
import {CiudadServices } from '@/services/CiudadServices';
import {DireccionServices} from '@/services/DireccionServices';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const clienteDialog = ref(false);
const personaCreationDTO = ref({});
const clientes= ref([]);
const direccion = ref({});
const direcciones = ref([]);
const direccionSubmitted = ref(false);
const direccionSelected = ref();
const ciudades = ref([]);
const departamentos = ref([]);
const direccionEnvio = ref({});

async function getClientes() {
  await PersonaServices.obtenerClientes().then((data) => {
       clientes.value = data.data;
   });
}
const updateClientes = (clientes) =>{
    PersonaServices.modificarPersona(clientes.id, clientes);
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
const hideDialog = () => {
    clienteDialog.value = false;
    direccion.value = {};
    submitted.value = false;
};



const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const cancelar = ()=>{
  visible.value = false;
  selectedOpcion.value = null;
};

const modificarCliente = (cli) => {
    PersonaServices.getClientes(clientes.id).then((data) => {
        console.log("data direccion");
        console.log(data.data.direccion);
       clientes.value = data.data.persona;
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
const verCliente = (id) =>{
    router.push({name: 'VisualizarCliente', params: {id}});
    
}
const saveCliente = () => {
    submitted.value = true;
    console.log(direccion.value.calle1);
    if (clientes?.value.nombre?.trim() && validarDireccionClientes(direccion.value) ) {
        direccion.value.tipo = 'P';
        generarDirecciones(direccion.value);
        personaCreationDTO.value = {personaEntity: cliente.value, direccion: direccion.value};
        if (clientes.value.id) {
            PersonaServices.modificarPersona(cliente.value.id, personaCreationDTO.value).then((response)=>{
            console.log("mod");
                eliminarClienteSelected();
                clientes.value[findIndexById(cliente.value.id)] = cliente.value;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
                selectedCliente.value = response.data;
                
                mostrarCliente();
                direccion.value.tipo = null;
            }).catch(
                (error)=>messageError("error")
            );
            
        }
        else {
         
            PersonaServices.registrarPersona(personaCreationDTO.value).then((response)=>{
            console.log("reg");
            console.log(personaCreationDTO.value);
                clientes.value.push(response.data);
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
                selectedCliente.value = response.data;
                mostrarCliente();
                direccion.value.tipo = null;
            }).catch(
                (error)=>messageError("error")
            );
        }

        clienteDialog.value = false;
        clientes.value = {};
    }
};

const getDireccionesCliente = () =>{
    DireccionServices.getDireccionesCliente(selectedCliente.value.id).then((data) => {
        console.log("data.data");
        console.log(data.data);
        direcciones.value=data.data;
        if (direcciones.value.length < 1) {
            console.log("direcciones.value.length");
            console.log(direcciones.value.length);
            nuevaDireccion.value = true;
        }

    });
};
const generarDirecciones = (dir) => {
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " +selectedOp.value + " "+ dir.calle2;
    }
    if (dir.calle3?.trim()) {
        d = d + " y " + dir.calle3;
    }

    dir.direccion = d;
};
const validarDireccionClientes = (dir) => {
    if (algunCampoTieneValor(dir) && (!dir.calle1 || !dir.ciudad)) {
        return false;
    }

    if (!algunCampoTieneValor(dir)) {
        return true;
    }
    
    
    return true;

};
const getCiudades = (id) => {
    CiudadServices.obtenerCiudadesByDepartamento(id).then((data) => {
        console.log("data ciudades");
        console.log(data.data);
       ciudades.value = data.data;
       
    });
    
};
const generarDireccion = (dir) => {
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " +selectedOp.value + " "+ dir.calle2;
    }
    if (dir.calle3?.trim()) {
        d = d + " y " + dir.calle3;
    }

    dir.direccion = d;
};





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
            <label for="description">Calle Secundaria</label>
            <InputText fluid id="description" v-model="direccion.calle2" required="true" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.calle1}" />
            <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.calle2">Ingrese Calle Secundaria</small>
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
                <DataTable  :value="clientes" scrollHeight="400px"  
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


