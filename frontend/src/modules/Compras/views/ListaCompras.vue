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
import { CompraServices } from '@/services/CompraServices';
import {CajaServices} from '@/services/CajaServices'
import { AuthServices } from '@/services/AuthServices';
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

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { formatearNumero, formatearFecha, getEstadoFacturaCompra } from '@/utils/utils'; 

//Variables para control de diálogos y selecciones
const confirm = useConfirm();
const toast = useToast();
const selectedOpcion = ref();
const idPedidoSelected = ref();
const compras = ref();
const cajaAbierta = ref({});

//Función para mostrar diálogo de confirmación de eliminación de una compra
const confirm2 = (id) => {
  confirm.require({
    message: 'Eliminar compra #'+ id + '?',
    header: 'Confirmacion',
    icon: 'pi pi-info-circle',
    rejectLabel: 'Cancelar',
    acceptLabel: 'Eliminar',
    rejectClass: 'p-button-secondary p-button-outlined',
    acceptClass: 'p-button-danger',
      accept: () => {
        deleteCompra(id);    
      },   
  });
};


onMounted(() => {
    getCompras();
    getCajaAbierta();
});

//Función para obtener todas las compras
const getCompras= () => {
  CompraServices.obtenerCompras().then((data) => {
    compras.value = data.data;
    console.log(compras.value);
  });
};

//Función para verificar si hay caja abierta
const getCajaAbierta= () => {
  CajaServices.getCajaAbierta().then((data) => {
    cajaAbierta.value=data.data;
    console.log(cajaAbierta.value);
  });
};

//Función para eliminar una compra ojoooooooooooo
const deleteCompra = (id) =>{
  const cantidad= 1;
  const index = compras.value.findIndex((loopVariable) => loopVariable.id === id);
    CompraServices.deleteCompra(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      compras.value.splice(index,cantidad);
        toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
  })
};

//Función para verificar si un registro pertenece a la caja actual
const registradoEnCajaActualAbierta = (fechaRegistro) =>{
  console.log("registradoEnCajaActualAbierta");
    if (cajaAbierta.value != null && fechaRegistro >= cajaAbierta.value.fecha) {
      return false;
    } else {
      return true;
    }
};

//Función para verificar estado de pago
const isPagado = (estado) => {
  switch (estado) {
    case 'C':
      return true;
    default:
      return false;
  }
};

//Filtros para la tabla 
const filters = ref({
  'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

//Función para determinar el color según el estado
const getSeverity = (estado) => {
  switch (estado) {
    case 'C':
      return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66);';
    case 'P':
      return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';
    case 'F':
      return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';
    default:
      return null;
  }
};

//Función para agregar pago  
const addPago = () =>{
  let id = idPedidoSelected.value;
    if (selectedOpcion.value.id === 2 ) {
      router.push({name: 'AddPago', params: {id}});
    }   
}

//Función para el reembolso
const reembolsar = (id) =>{
    router.push({name: 'reembolsar', params: {id}});
}

//Función para cancelar 
const cancelar = ()=>{
  visible.value = false;
  selectedOpcion.value = null;
}

//Función para ver detalles de compra
const verCompra = (id) =>{
  router.push({name: 'ver_compra', params: {id}});   
}

//Función para ver anticipo
const verAnticipo = (id) =>{
  console.log("veranticipofuncion");
  router.push({name: 'verAnticipo', params: {id}});
}

//Función para eliminar pedido
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

//Función para nueva compra
const nuevoPedido = () =>{
  router.push({name: 'nueva_compra'});
}

</script>

<template>
  <div class="flex p-fluid justify-content-center " >
    <ConfirmDialog ></ConfirmDialog>
    <Toast />
    <!-- Panel principal que contiene la tabla de compras -->
    <Panel style=" position: relative; width: 90%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
          <h3 class="font-bold">Compras</h3>
        </div>
      </template>
      
      <template #icons>
        <div class="flex align-items-center">
          <Button  icon="pi pi-plus " @click="nuevoPedido" style="margin-right: 1% ;"  />
          <InputGroup>
            <InputText v-model="filters['global'].value" placeholder="Buscar..." />
            <InputGroupAddon>
              <i class="pi pi-search" />
            </InputGroupAddon>
        </InputGroup>
        </div>
      </template>
      
      <div >
        <DataTable  :value="compras" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <template #empty> No hay registros para mostrar. </template>
          <template #loading> Cargando. </template>
          <Column field="nroFactura" sortable header="N° Factura" aria-sort="ascending" ></Column>
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
          <Column field="fecha" sortable header="N° Recepción" aria-sort="ascending" >
            <template #body="slotProps">
              {{ slotProps.data.recepcion?.id }}
            </template>
          </Column>
          <Column field="proveedor.descripcion"  header="Proveedor" aria-sort="ascending" sortable>           
          </Column>
          <Column field="estado"  header="Estado" aria-sort="ascending" sortable>    
            <template #body="slotProps">
              <Tag :style="getSeverity(slotProps.data.estado)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ getEstadoFacturaCompra(slotProps.data.estado)}}</Tag>
            </template>          
          </Column>
          <Column field="total"  header="Total Gs." aria-sort="ascending" sortable> 
            <template #body="slotProps">
              {{ formatearNumero(slotProps.data.total) }}
            </template> 
          </Column>   
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <Button icon="pi pi-eye" v-tooltip="'Ver detalles'" text rounded aria-label="Search" @click="verCompra(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
              <Button  icon="pi pi-trash" v-tooltip="'Eliminar'" :disabled="isPagado(slotProps.data.estado)" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
            </template>
          </Column>
        </DataTable>
      </div>
    </Panel>
  </div> 
</template>