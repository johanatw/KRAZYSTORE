<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import {PedidoServices} from '@/services/PedidoServices';
import { PedidoCompraServices } from '@/services/PedidoCompraServices';
import { AnticipoServices } from '@/services/AnticipoServices';
import {ReembolsoServices} from '@/services/ReembolsoServices'
import { CompraServices } from '@/services/CompraServices';
import {CajaServices} from '@/services/CajaServices'
import Panel from 'primevue/panel';
import router from '@/router';
import Toast from 'primevue/toast';
import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';


import SplitButton from 'primevue/splitbutton';

const pedidos = ref();
import { formatearNumero, formatearFecha, getEstadoPedidoCompra } from '@/utils/utils'; 
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";



const confirm = useConfirm();
const toast = useToast();
const selectedOpcion = ref();
const idPedidoSelected = ref();

const cajaAbierta = ref({});
const confirm2 = (id) => {
   
    confirm.require({
        message: 'Eliminar el pedido #'+ id + '?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Eliminar',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: () => {
            deletePedido(id);
            
        },
        
    });
};
onMounted(() => {
  
    PedidoCompraServices.obtenerPedidos().then((data) => {
        pedidos.value = data.data;
        console.log(pedidos.value);
    });
 getCajaAbierta();
    
});

const getCajaAbierta= () => {
    CajaServices.getCajaAbierta().then((data) => {
        cajaAbierta.value=data.data;
        console.log(cajaAbierta.value);
    });
};

const deleteReembolso = (id) =>{
    const cantidad= 1;
    const index = reembolsos.value.findIndex((loopVariable) => loopVariable.id === id);
    CajaServices.deleteReembolso(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      
        reembolsos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
      
            
        })

   
}

const registradoEnCajaActualAbierta = (fechaRegistro) =>{
    console.log("registradoEnCajaActualAbierta");
    if (cajaAbierta.value != null && fechaRegistro >= cajaAbierta.value.fecha) {
        return false;
    } else {
        return true;
    }
};



const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});



const recepcionarPedidoCompra = (id) =>{
    router.push({name: 'recepcionarPedido', params: {id}});
    
}

const verAnticipo = (id) =>{
    console.log("veranticipofuncion");
    router.push({name: 'verAnticipo', params: {id}});
}

const verPedidoCompra = (id) => {
    router.push({name: 'ver_pedido_compra', params: {id}});
  
  }

const deletePedido = (id) =>{
    const cantidad= 1;
    const index = pedidos.value.findIndex((loopVariable) => loopVariable.id === id);
    console.log("Deletepedddo");
    PedidoCompraServices.deletePedido(id).then((response)=>{
        pedidos.value.splice(index,cantidad);
        toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 3000 });
        
    });
}

const getSeverity = (estado) => {
  
  
  switch (estado) {
       case 'R':
           return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66);';

       case 'M':
           return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';

       case 'N':
           return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';

       default:
           return null;
   }
};

const isRecepcionCompleta = (estado) => {
  console.log(estado);
  switch (estado) {
       case 'R':
           return true;
       default:
           return false;
   }
};

const isNuevo = (estado) => {
  switch (estado) {
       case 'N':
           return true;
       default:
           return false;
   }
};

const nuevoPedido = () =>{
    router.push({name: 'nuevo_pedido_compra'});
}

</script>

<template>
    
  <div class="flex p-fluid justify-content-center " >

    <ConfirmDialog ></ConfirmDialog>
    <Toast />
    <Panel style=" position: relative; width: 100%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Pedidos</h3>
        </div>
      </template>
         
      <template #icons>
        <div class="flex align-items-center">
          <Button  icon="pi pi-plus " @click="nuevoPedido" style="margin-right: 1% ;"  />
          <InputGroup>
            <InputText v-model="filters['global'].value" placeholder="Search..." />
            <InputGroupAddon>
              <i class="pi pi-search" />
            </InputGroupAddon>
        </InputGroup>
        </div>
    
      </template>
      
  
      <div >
        
        <DataTable  :value="pedidos" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <template #empty> No hay registros para mostrar. </template>
          <template #loading> Cargando. </template>
          <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
                {{ formatearFecha(slotProps.data.fecha) }}
            </template>
        </Column>
          <Column field="proveedor.descripcion"  header="Proveedor" aria-sort="ascending" sortable>           
        </Column>
          <Column field="total"  header="Total" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.total) }}
            </template> 
        </Column>   
        <Column field="estado"  header="Estado" aria-sort="ascending" sortable>  
          <template #body="slotProps">
                <Tag :style="getSeverity(slotProps.data.estadoPedido)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ getEstadoPedidoCompra(slotProps.data.estadoPedido)}}</Tag>
              </template>         
        </Column>
        
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
                <Button icon="pi pi-search" text rounded aria-label="Search" @click="verPedidoCompra(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                <Button :disabled="isRecepcionCompleta(slotProps.data.estadoPedido)" icon="pi pi-truck" severity="success" text rounded aria-label="Search" @click="recepcionarPedidoCompra(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                
                <Button :disabled="!isNuevo(slotProps.data.estadoPedido)" icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>
<style>

</style>