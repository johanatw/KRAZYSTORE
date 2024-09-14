<script setup>
import { ref, onMounted } from 'vue';
import { PersonaServices } from '../services/PersonaServices';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import InputGroup from 'primevue/inputgroup';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import RegisPru from '../components/RegisPru.vue';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import Panel from 'primevue/panel';
import ToastService from 'primevue/toastservice';
import Toast from 'primevue/toast';

import { useToast } from 'primevue/usetoast';

const dialog = ref(null);
const titulo = ref('');
const toast = useToast();
const modForm = ref(false);
const selectedPersona = ref();
const deleteClientDialog = ref(false);
onMounted(() => {
  PersonaServices.obtenerClientes().then((data) => ( clientes.value = data.data));
});

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});


const clientes = ref();
const cliente = ref();

const showForm = (valor) =>{
    if (valor==="Reg") {
        titulo.value = 'Registrar';
    } else {
        titulo.value = 'Modificar';
        modForm.value = true;
    }
    
    dialog.value.showDialog(selectedPersona);
}

const modificarCliente = (cliente) =>{
    titulo.value = 'Modificar';
    
    dialog.value.showDialog(cliente);
}

const deleteCliente= () => {
    try {
            PersonaServices.eliminar(cliente.value.id).then((respuesta)=>{
                clientes.value = clientes.value.filter(val => val.id !== cliente.value.id);
                 deleteClientDialog.value = false;
                cliente.value = {};
                toast.add({severity:'success', summary: 'Successful', detail: 'Cliente eliminado', life: 3000});
              });
            } catch (error) {
                console.log(error);
                
            }
   
    deleteClientDialog.value= true;

}

const confirmDeleteClient= (client) => {
  
   cliente.value = client;
  deleteClientDialog.value= true;


}

const showMsg= (cli,msg) => {
  console.log(cli);
  let index = clientes.value.findIndex((loopVariable) => loopVariable.id === cli.id);
  clientes.value[index].nombreCompleto= cli.nombre + ' ' +cli.apellido;
  toast.add({severity:'success', summary: 'Successful', detail: msg, life: 3000});
  
}
</script>

<template>
  <div class="card flex  justify-content-center" >
    <RegisPru :titulo="titulo"  ref="dialog" @clientSaved="showMsg" />
    <Toast />

    <Panel>
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Clientes</h3>
        </div>
      </template>
         
      <template #icons>
        
        
        <Button class="plus" icon="pi pi-plus" @click="showForm('Reg')" style="margin-right: 0.3rem;" />
        <span class="p-input-icon-left">
          <i class="pi pi-search"/>
          <InputText  class="buscador p-fluid"  v-model="filters['global'].value" placeholder="Buscar..." />
        </span>
    
      </template>
  
      <div class="card">
        <DataTable  :value="clientes " scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >

          <Column field="id" sortable header="ID" aria-sort="ascending" ></Column>
          <Column field="nombreCompleto" header="Nombre" aria-sort="none" sortable></Column>
          <Column field="telefono"  header="Telefono" aria-sort="none" sortable></Column>
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <Button icon="pi pi-pencil" class="mod_icono" @click="modificarCliente(slotProps.data)" style="margin-right: 0.3rem;"/>
              <Button icon="pi pi-trash" severity="danger"  @click="confirmDeleteClient(slotProps.data)"/>
            </template>
          </Column>
        </DataTable>
      </div>
      <Dialog v-model:visible="deleteClientDialog" :style="{width: '450px'}" header="Confirm" :modal="true">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span v-if="cliente">Are you sure you want to delete <b>{{cliente.nombreCompleto}}</b>?</span>
            </div>
            <template #footer>
                <Button label="No" icon="pi pi-times" text @click="deleteClientDialog = false"/>
                <Button label="Yes" icon="pi pi-check" text @click="deleteCliente" />
            </template>
        </Dialog>
    </Panel>
  </div>
</template>
