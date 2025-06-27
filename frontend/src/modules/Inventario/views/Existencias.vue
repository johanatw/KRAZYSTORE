<script setup>
//Importaciones
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

import { InputGroup } from 'primevue';
import {InputGroupAddon }from 'primevue';
import { ProductoServices } from '@/services/ProductoServices';
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


//Variables
const confirm = useConfirm();
const toast = useToast();
const selectedOpcion = ref();
const idPedidoSelected = ref();
const compras = ref([]);
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
    ProductoServices.obtenerProductos().then((data) => {
        compras.value = data.data;
        compras.value = compras.value.map(compra => ({
        ...compra,
                estadoSinStock: getEstadoSinStock(compra),
        estadoBajoDemanda: getEstadoBajoDemanda(compra),
        estadoTexto: [
                getEstadoSinStock(compra) || '',
                getEstadoBajoDemanda(compra) || ''
            ].filter(Boolean).join(' / ')

        }));
        console.log(compras.value);
    });
 getCajaAbierta();
    
});

//Obtener caja abierta
const getCajaAbierta= () => {
    CajaServices.getCajaAbierta().then((data) => {
        cajaAbierta.value=data.data;
        console.log(cajaAbierta.value);
    });
};

//Eliminar Reembolso
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


const getSeverity = (estado) => {  
  switch (estado) {
       case 'Sin Stock':
           return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';

       case 'Bajo Pedido':
           return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';

       default:
           return 'background-color: rgb(255, 255, 255);';
   }
};

const getColorDisponible = (cantidad) => {
  
  if (cantidad<1) {
    return 'color: red;';
  } else {
    return 'color: green;';
  }


};

const getColorReservado = (producto) => {
  let cantReservada = producto.cantReservada;
  let cantStock = producto.cantStock;
  if (cantStock <cantReservada) {
    return 'color: rgb(255, 102, 0);';
  } 
}


const getColorStock = (cantidad) => {
  if (cantidad<1) {
    return 'color: red;';
  } 
};



const getEstadoSinStock = (producto) => {
  let cantStock = producto.cantStock;  
  if(cantStock<1){
    return 'Sin Stock'
  }
};

const getEstadoBajoDemanda = (producto) => {
  let bajoDemanda = producto.bajoDemanda; 
  if (bajoDemanda) {
    return 'Bajo Pedido'
  }
};

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

const verCompra = (id) =>{
    router.push({name: 'ver_compra', params: {id}});    
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


const nuevoPedido = () =>{
    router.push({name: 'nueva_compra'});
}

</script>

<template>
    <div class="flex p-fluid justify-content-center " >
        <!-- Componentes de diálogo y notificación -->
        <ConfirmDialog ></ConfirmDialog>
        <Toast />
        
        <!-- Panel principal -->
        <Panel style=" position: relative; width: 90%;" >
            <!-- Encabezado del panel -->
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Existencias</h3>
                </div>
            </template>
            
            <!-- Barra de búsqueda -->
            <template #icons>
                <div class="flex align-items-center">
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
        
            <!-- Tabla de productos -->
            <div >
                <DataTable  :value="compras" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                    
                    <!-- Columnas de la tabla -->
                    <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
                    <Column field="nombre" sortable header="Producto" aria-sort="ascending"></Column>
                    
                    <!-- Columna de reservas con estilo condicional -->
                    <Column field="cantReservada" header="Reservas" aria-sort="ascending" sortable>  
                        <template #body="slotProps">
                            <div :style="getColorReservado(slotProps.data)" >
                                {{ slotProps.data.cantReservada }}
                            </div>
                        </template>         
                    </Column>
                    
                    <!-- Columna de disponibilidad con estilo condicional -->
                    <Column field="cantDisponible" header="Disponible" aria-sort="ascending" sortable>    
                        <template #body="slotProps">
                            <div :style="getColorDisponible(slotProps.data.cantDisponible)" >
                                {{ slotProps.data.cantDisponible }}
                            </div>
                        </template>
                    </Column>
                    
                    <!-- Columna de stock con estilo condicional -->
                    <Column field="cantStock" header="Stock" aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            <div :style="getColorStock(slotProps.data.cantStock)" >
                                {{ slotProps.data.cantStock }}
                            </div>
                        </template>
                    </Column>   
                    
                    <!-- Columna de estado con tags -->
                    <Column field="estadoTexto" header="Estado" aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            <Tag :style="getSeverity(slotProps.data.estadoSinStock)" style="font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;">
                                {{ slotProps.data.estadoSinStock }}
                            </Tag>
                            <Tag :style="getSeverity(slotProps.data.estadoBajoDemanda)" style="font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;">
                                {{ slotProps.data.estadoBajoDemanda }}
                            </Tag>
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
</template>

