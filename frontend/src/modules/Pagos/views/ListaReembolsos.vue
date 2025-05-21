<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import {PedidoServices} from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';
import {ReembolsoServices} from '@/services/ReembolsoServices'
import {CajaServices} from '@/services/CajaServices'
import Panel from 'primevue/panel';
import router from '@/router';
import Toast from 'primevue/toast';
import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';
import {formatearFechaHora} from '@/utils/utils';

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
const cajaAbierta = ref({});
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
 
  ReembolsoServices.obtenerReembolsos().then((data) => {
        reembolsos.value = data.data;
        console.log(reembolsos.value);
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
        return true;
    } else {
        return false;
    }
};



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

const getTipoAnticipo = (tipo) =>{

switch (tipo) {
    case 'V':
        return 'Cliente'
        break;
    case 'C':
        return 'Proveedor'
        break;
    default:
        return ''
        break;
}
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
    <Panel style=" position: relative; width: 90%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Reembolsos</h3>
        </div>
      </template>
         
      <template #icons>
        <div class="flex align-items-center">
                <InputGroup>
                    <InputText v-model="filters['global'].value" placeholder="Search..." />
                    <InputGroupAddon>
                    <i class="pi pi-search" />
                    </InputGroupAddon>
                </InputGroup>
                </div>
    
      </template>
      
  
      <div class="card">
        
        <DataTable  :value="reembolsos" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <template #empty> No hay registros para mostrar. </template>
          <template #loading> Cargando. </template>
          <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
                {{ formatearFechaHora(slotProps.data.fecha) }}
            </template>
        </Column>
        <Column field="total"  header="Cliente" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                        {{ slotProps.data.cliente?.persona?.nombre || ' ' }}
                                    </template>        
                                </Column>
          <Column field="anticipo"  header="N° Anticipo" aria-sort="ascending" sortable>  
            <template #body="slotProps">
                <div >
                    {{ slotProps.data.anticipo?.id }}
                </div>
               
                
            </template>          
        </Column>
          <Column field="monto"  header="Monto" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.monto) }}
            </template> 
        </Column>
        <Column field="motivo"  header="Motivo" aria-sort="ascending" sortable>            
        </Column>
        
            
              

           
        
            
        
        
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
                
                <Button :disabled="!registradoEnCajaActualAbierta(slotProps.data.fecha)"  icon="pi pi-trash" severity="danger" v-tooltip="'Eliminar'" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>