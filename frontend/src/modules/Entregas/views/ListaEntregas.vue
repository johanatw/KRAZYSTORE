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
import { EntregaServices } from '@/services/EntregaServices';
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


const confirm = useConfirm();
const toast = useToast();
const selectedOpcion = ref();
const idPedidoSelected = ref();
const entregas = ref();
const cajaAbierta = ref({});
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
    getEntregas();
    
    
});

const getCompras= () => {
  CompraServices.obtenerCompras().then((data) => {
        entregas.value = data.data;
        console.log(entregas.value);
    });
};

const getEntregas = async () => {
    try {
      const response = await EntregaServices.obtenerEntregas();
      entregas.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

const getCajaAbierta= () => {
    CajaServices.getCajaAbierta().then((data) => {
        cajaAbierta.value=data.data;
        console.log(cajaAbierta.value);
    });
};

const deleteCompra = (id) =>{
    const cantidad= 1;
    const index = entregas.value.findIndex((loopVariable) => loopVariable.id === id);
    CompraServices.deleteCompra(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      
        entregas.value.splice(index,cantidad);
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

const isPagado = (estado) => {
  
  switch (estado) {
       case 'C':
           return true;
       default:
           return false;
   }
};

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});


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

    <ConfirmDialog ></ConfirmDialog>
    <Toast />
    <Panel style=" position: relative; width: 100%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Entregas</h3>
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
      
  
      <div >
        
        <DataTable  :value="entregas" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <template #empty> No hay registros para mostrar. </template>
          <template #loading> Cargando. </template>
          <Column field="fecha" sortable header="N° Pedido" aria-sort="ascending" >
            <template #body="slotProps">
                {{ slotProps.data.pedido?.id }}
            </template>
        </Column>
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
                {{ formatearFecha(slotProps.data.fecha) }}
            </template>
        </Column>
       
          <Column field="proveedor.descripcion"  header="Cliente" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                <div>
                    {{ slotProps.data.direccionEnvio?.persona?.nombre }}
                    <label v-if="slotProps.data.direccionEnvio?.persona?.apellido" for="apellido">
                        {{ slotProps.data.direccionEnvio?.persona?.apellido }}
                    </label>
                </div>
            </template>           
        </Column>
        <Column field="proveedor.descripcion"  header="Modalidad" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                <div>
                    {{ slotProps.data.modoEntrega?.descripcion }}
                </div>
            </template>          
        </Column>
        <Column field="proveedor.descripcion"  header="Delivery/Punto de Retiro" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                <div v-if="slotProps.data.modoEntrega?.descripcion == 'Retiro'">
                    {{ slotProps.data.puntoEntrega?.descripcion }}
                </div>
                <div v-else>
                    {{ slotProps.data.empresaTransporte?.descripcion }}
                </div>
                
            </template>          
        </Column>
        <Column field="estado"  header="Estado" aria-sort="ascending" sortable>    
          <template #body="slotProps">
                <Tag :style="getSeverity(slotProps.data.estado)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ getEstadoFacturaCompra(slotProps.data.estado)}}</Tag>
              </template>          
        </Column>
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <Button icon="pi pi-search" text rounded aria-label="Search" @click="verCompra(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
              <Button  icon="pi pi-times" :disabled="isPagado(slotProps.data.estado)" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>