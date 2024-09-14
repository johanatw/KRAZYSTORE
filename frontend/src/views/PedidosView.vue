<script setup>
import { ref, onMounted } from 'vue';
import { PersonaServices } from '../services/PersonaServices';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import RegisPru from '../components/RegisPru.vue';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import {PedidoServices} from '@/services/PedidoServices';
import Panel from 'primevue/panel';
import ToastService from 'primevue/toastservice';
import Toast from 'primevue/toast';
import router from '@/router';
import InputGroup from 'primevue/inputgroup';
import prueba from "@/components/pedidos/prueba.vue";
import { useToast } from 'primevue/usetoast';



const pedidos = ref();
onMounted(() => {
  PedidoServices.getPedidos().then((data) => {
        pedidos.value = data.data;

        console.log(pedidos.value);
     

    });
  
    
  
});

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});



const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
}

const nuevoPedido = () =>{
    router.push({name: 'nuevo_pedido'});
}

</script>

<template>
    
  <div class="card flex p-fluid justify-content-center " >
 
    <Panel style=" position: relative; width: 100%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Pedidos</h3>
        </div>
      </template>
         
      <template #icons>
        <div class="flex align-items-center">
          <Button  icon="pi pi-plus " @click="nuevoPedido" style=" width: 3rem !important; height: 2.9rem;" />
        <span class="p-input-icon-left" style="margin-left: 1%;">
          <i class="pi pi-search" style="top: 35%;"/>
          <InputText  class="buscador p-fluid" v-model="filters['global'].value" placeholder="Buscar..."  />
        </span>

        </div>
        
                    
                    
          
    
                   
        
        
    
      </template>
  
      <div class="card">
        <DataTable  :value="pedidos " scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >

          <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
          <Column field="fecha"  header="Fecha" aria-sort="ascending" sortable>            
        </Column>
          <Column field="cliente" header="Cliente" aria-sort="ascending" sortable>
            
              

            </Column>
            <Column  field="telefono" header="Telefono" aria-sort="ascending" sortable >
            </Column>
            
          <Column field="total"  header="Total" aria-sort="ascending" sortable></Column>
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <Button icon="pi pi-pencil" class="mod_icono" @click="verPedido(slotProps.data.id)" style="margin-right: 0.3rem !important; width: 3rem !important;"/>
            </template>
          </Column>
        </DataTable>
      </div>
    </Panel>
  </div>
</template>
<style>
.p-datatable .p-datatable-tbody > tr > td {
    text-align: left;
    border: 1px solid #e5e7eb;
    border-width: 0 0 1px 0;
    padding: 0.1rem 0.1rem;
}
</style>