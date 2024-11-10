<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
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
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';


import SplitButton from 'primevue/splitbutton';

const pedidos = ref();

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const entregas = ref([{id: 1, idPedido: 1, cliente: 'María Amarilla', descripcion: 'Inventario 1', fecha: '15/06/2023',modalidad:'Envío', estado: 'Finalizado', retiro: 'UPS', guia: '12345678' },
{id: 2, idPedido: 2, cliente: 'Johana Pineda', descripcion: 'Inventario 2', fecha: '20/06/2023', modalidad:'Retiro', estado: 'En curso', retiro: 'Shopping del Sol', guia: '' }
]);
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
            <h3 class="font-bold">Inventarios</h3>
        </div>
         
      </template>
         
      <template #icons>
        <div class="flex align-items-center">
            <Button  icon="pi pi-plus " @click="registrarAnticipo()" style=" width: 3rem !important; height: 2.9rem;" />
                    
        <span class="p-input-icon-left" style="margin-left: 1%;">
          <i class="pi pi-search" style="top: 35%;"/>
          <InputText style="padding: 12px !important; padding-left: 40px !important;" class="buscador p-fluid" v-model="filters['global'].value" placeholder="Buscar..."  />
        </span>
        
        </div>
        
        
    
      </template>
      
  
      <div class="card">
        
        <DataTable  :value="entregas" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
        </Column>
          <Column field="descripcion"  header="Descripcion" aria-sort="ascending" sortable>         
        </Column>
          <Column field="estado"  header="Estado" aria-sort="ascending" sortable> 
        </Column>
            
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
                
                <Button  icon="pi pi-search" severity="primary" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" />
                <Button  icon="pi pi-pencil" severity="secondary" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" />
                <Button  icon="pi pi-times" severity="danger" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>