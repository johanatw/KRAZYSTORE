<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import {PedidoServices} from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';
import { VentaServices } from '@/services/VentaServices';
import {ReembolsoServices} from '@/services/ReembolsoServices'
import { CajaServices } from '@/services/CajaServices';
import Panel from 'primevue/panel';
import router from '@/router';
import Toast from 'primevue/toast';
import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';


import SplitButton from 'primevue/splitbutton';

const pedidos = ref();

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";


const opciones = ref([{id: 1, descripcion: 'Facturar productos disponibles en stock.'},
                    {id: 2, descripcion: 'Registrar anticipo para productos no disponibles en stock.'}]);
const confirm = useConfirm();
const toast = useToast();
const montoReembolsar = ref(0);
const motivo = ref(null);
const selectedOpcion = ref();
const idPedidoSelected = ref();
const reembolsos = ref();
const movimientos = ref();
const ventas= ref();

const confirm2 = (id) => {
   
    confirm.require({
        message: 'Eliminar el reembolso #'+ id + '?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Eliminar',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: () => {
            deleteReembolso(id);
            
        },
        
    });
};
onMounted(() => {
 
 
getVentas();
    
 
    
});

const getVentas = (id) =>{
    VentaServices.getVentas().then((data) => {
        ventas.value = data.data;
        //console.log(reembolsos.value);
    });
   
}

const deleteReembolso = (id) =>{
    const cantidad= 1;
    const index = reembolsos.value.findIndex((loopVariable) => loopVariable.id === id);
    ReembolsoServices.deleteReembolso(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      
        reembolsos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
      
            
        })

   
}

const anular = (id) =>{
    console.log("anular");
    console.log(id);
    VentaServices.anularVenta(id).then((response)=>{
      
        getVentas();
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
      
            
        })

   
}




const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});



const addPago = () =>{
  let id = idPedidoSelected.value;
    if (selectedOpcion.value.id === 2 ) {
      router.push({name: 'AddPago', params: {id}});
    }
    
    
}

const reembolsar = (id) =>{
    router.push({name: 'reembolsar', params: {id}});
  

}

const nuevaFactura = () =>{
    router.push({name: 'nueva_factura'});
  

}

const cancelar = ()=>{
  visible.value = false;
  selectedOpcion.value = null;
}

const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
    
}

const verAnticipo = (id) =>{
    console.log("veranticipofuncion");
    router.push({name: 'verAnticipo', params: {id}});
}

const deletePedido = (id) =>{
    const cantidad= 1;
    const index = pedidos.value.findIndex((loopVariable) => loopVariable.id === id);
    
    
    PedidoServices.deleteDetallesPedido(id).then((response)=>{
        console.log(response);
        PedidoServices.deletePedido(id).then((response)=>{
            pedidos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 3000 });
        })
    });
}

const formatearNumero = (valor) =>{
    if(typeof(valor) == "number"){
        return valor.toLocaleString("de-DE");
    }

    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear()+' '+ fecha.getHours()+':'+fecha.getMinutes()+':'+fecha.getSeconds();
    return fechaFormateada;
}

const nuevoPedido = () =>{
    router.push({name: 'nuevo_pedido'});
}

</script>

<template>
    
  <div class="card flex p-fluid justify-content-center " >

    <ConfirmDialog ></ConfirmDialog>
    <Toast />
    <Panel style=" position: relative; width: 100%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Facturas de Venta</h3>
        </div>
      </template>
      <template #icons>
        
        <Button  label="Nueva Factura" @click="nuevaFactura()" />
    
    </template>
      
      
  
      <div class="card">
        
        <DataTable  :value="ventas" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.fecha) }}
            </template>
        </Column>
          <Column field="cliente.nombre"  header="Cliente" aria-sort="ascending" sortable>  
            <template #body="slotProps">
                
                <div v-if="slotProps.data.cliente.apellido">
                    {{ slotProps.data.cliente.nombre }} {{ slotProps.data.cliente.apellido }}
                </div>
                <div v-else>
                    {{ slotProps.data.cliente.nombre }}
                </div>
            </template>   
        </Column>
          <Column field="montoTotal"  header="Total" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.montoTotal) }}
            </template>
        </Column>
        <Column  header="Estado" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                <div v-if="slotProps.data.activo">
                    Activo
                </div>
                <div v-else>
                    Anulado
                </div>
            </template>           
        </Column>
        <Column field="nroFactura"  header="N° de Factura" aria-sort="ascending" sortable>            
        </Column>
      

        
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
                <div style="display: flex;" >
                    <Button  label="Revisar"  style="height: 2rem !important; width: 5rem !important; margin-right: 1%; font-size: 14px; " />
                    <div v-if="slotProps.data.activo">
                        <Button  severity="danger"  label="Anular" @click="anular(slotProps.data.id)"  style="height: 2rem !important; width: 5rem !important; font-size: 14px;" />
                </div>
                <div v-else>
                    <Button  severity="danger"  label="Anular" disabled  style="height: 2rem !important; width: 5rem !important; font-size: 14px;" />
                </div>
                
                </div>
                
                
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>