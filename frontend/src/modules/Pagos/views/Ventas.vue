<script setup>
import { ref, onMounted } from 'vue';
import { version } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import {PedidoServices} from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';
import { VentaServices } from '@/services/VentaServices';
import { TimbradoServices } from '@/services/TimbradoServices';
import {ReembolsoServices} from '@/services/ReembolsoServices'
import { CajaServices } from '@/services/CajaServices';
import Panel from 'primevue/panel';
import { InputGroup, InputGroupAddon } from 'primevue';
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
import { formatearNumero, formatearFecha, getEstadoFacturaVenta } from '@/utils/utils'; 

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
const existeTimbradoVigente = ref(false);
const ventas= ref();

const confirm2 = (id, nro) => {
   
    confirm.require({
        message: 'Anular la factura #'+ nro+ '?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Anular',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: () => {
            anular(id);
            
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

const getTimbrado = () =>{
    TimbradoServices.obtenerTimbradoVigente().then((data) => {
        if (data.data.timbrado!=null) {
            existeTimbradoVigente.value = true;
        }else{
            existeTimbradoVigente.value = false;
        }

    });
   
}

const getSeverity = (estado) => {
  
  
  switch (estado) {
       case 'C':
           return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66);';

       case 'A':
           return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';

       case 'P':
           return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';

       default:
           return null;
   }
};

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


const anular = async (id) =>{
    console.log("anular");
    console.log(id);
    let pagosAsociados = 1;
    
    try {
      const { data } = await VentaServices.anularVenta(id);
      getVentas();
        toast.add({ severity: 'success', detail: 'Factura anulada', life: 5000 });
    } catch (error) {
      showError(error.response.data.mensaje);
    }
   
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

const nuevaFactura = async () =>{
    const response = await TimbradoServices.obtenerTimbradoVigente();
    console.log(response.data);
    if (response.data.timbrado != null) {
        router.push({name: 'nueva_factura'});
    } else {
        showError('No existe timbrado vigente para la factura');
    }
    
  

}

const showError = (message) => {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: message,
      life: 3000
    });
  };

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

const verFactura = (id) =>{
    
    router.push({name: 'verFactura', params: {id}});
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

const nuevoPedido = () =>{
    router.push({name: 'nuevo_pedido'});
}


</script>

<template>
  <div class=" flex p-fluid justify-content-center " >
    <ConfirmDialog ></ConfirmDialog>
    <Toast />
    
    <Panel style=" position: relative; width: 90%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
          <h3 class="font-bold">Facturas de Venta</h3>
        </div>
      </template>
      
      <template #icons>
        <div class="flex align-items-center">
          <Button icon="pi pi-plus " @click="nuevaFactura()" style="margin-right: 1% ;"/>
          <InputGroup>
            <InputText fluid v-model="filters['global'].value" placeholder="Buscar..." />
            <InputGroupAddon>
              <i class="pi pi-search" />
            </InputGroupAddon>
          </InputGroup>
        </div>
      </template>
      
      <div >
        <DataTable :value="ventas" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          
          <template #empty> No hay registros para mostrar. </template>
          <template #loading> Cargando. </template>
          
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
              {{ formatearFecha(slotProps.data.fecha) }}
            </template>
          </Column>
          
          <Column field="fecha" sortable header="N° Pedido" aria-sort="ascending" >
            <template #body="slotProps">
              {{ slotProps.data.pedido?.id }}
            </template>
          </Column>
          
          <Column field="cliente.nombre" header="Cliente" aria-sort="ascending" sortable>  
            <template #body="slotProps">
              <div v-if="slotProps.data.cliente.apellido">
                {{ slotProps.data.cliente.persona.nombre }} {{ slotProps.data.cliente.persona.apellido }}
              </div>
              <div v-else>
                {{ slotProps.data.cliente.persona.nombre }}
              </div>
            </template>   
          </Column>
          
          <Column field="montoTotal" header="Total Gs." aria-sort="ascending" sortable> 
            <template #body="slotProps">
              {{ formatearNumero(slotProps.data.montoTotal) }}
            </template>
          </Column>
          
          <Column field="estado" header="Estado" aria-sort="ascending" sortable>    
            <template #body="slotProps">
              <Tag :style="getSeverity(slotProps.data.estado)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >
                {{ getEstadoFacturaVenta(slotProps.data.estado)}}
              </Tag>
            </template>          
          </Column>

          <Column field="nroFactura" header="N° de Factura" aria-sort="ascending" sortable>            
          </Column>
        
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <div style="display: flex;" >
                <Button icon="pi pi-eye" v-tooltip="'Ver Factura'" text rounded aria-label="Search" 
                  @click="verFactura(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-times" :disabled="slotProps.data.estado !== 'P'" v-tooltip="'Anular'" 
                  text rounded aria-label="Search" severity="warn" 
                  @click="confirm2(slotProps.data.id,slotProps.data.nroFactura )" 
                  style="height: 2rem !important; width: 2rem !important;" />
              </div>
            </template>
          </Column>
        </DataTable>
      </div>
    </Panel>
  </div>
</template>