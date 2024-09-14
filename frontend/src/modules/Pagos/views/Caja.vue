<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import {FormasPagoServices} from '@/services/FormasPagoServices';
import {PedidoServices} from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';
import { VentaServices } from '@/services/VentaServices';
import {ReembolsoServices} from '@/services/ReembolsoServices'
import { CajaServices } from '@/services/CajaServices';
import { ConceptoServices } from '@/services/ConceptoServices';
import Panel from 'primevue/panel';
import router from '@/router';
import Toast from 'primevue/toast';
import Card from 'primevue/card';
import SelectButton from 'primevue/selectbutton';

import InputNumber from 'primevue/inputnumber';

import Textarea from 'primevue/textarea';

import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';


import Dropdown from 'primevue/dropdown';

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
const formasPagos = ref();
const idPedidoSelected = ref();
const reembolsos = ref();
const movimientos = ref();
const movimiento = ref({});
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
 
  ReembolsoServices.obtenerReembolsos().then((data) => {
        reembolsos.value = data.data;
        console.log(reembolsos.value);
    });

    CajaServices.obtenerMovimientos().then((data) => {
        movimientos.value = data.data;
        //console.log(reembolsos.value);
    });
    
 
    
});

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




const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const value = ref('Ingreso');
const options = ref(['Ingreso', 'Egreso']);
const conceptos = ref();

const addPago = () =>{
  let id = idPedidoSelected.value;
    if (selectedOpcion.value.id === 2 ) {
      router.push({name: 'AddPago', params: {id}});
    }
    
    
}

const reembolsar = (id) =>{
    router.push({name: 'reembolsar', params: {id}});
  

}

const getConceptos = (tipo) =>{
  let buscarTipo;
  switch (tipo) {
    case 'Ingreso':
      buscarTipo = 'I';
      
      break;

    case 'Egreso':
    buscarTipo = 'E';
    break;
    default:
    buscarTipo = 'I';
      break;
  }
  ConceptoServices.obtenerConceptosByTipo(buscarTipo).then((data) => {
        conceptos.value = data.data;
        //console.log(reembolsos.value);
    });
  

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

const nuevoMovimiento = () =>{
  FormasPagoServices.obtenerFormasPagoSinAnticipo().then((data) => {
        formasPagos.value = data.data;
        //console.log(reembolsos.value);
    });

    ConceptoServices.obtenerConceptosByTipo('I').then((data) => {
        conceptos.value = data.data;
        //console.log(reembolsos.value);
    });

    visible.value = true;
}

</script>

<template>
    
  <div class="card flex p-fluid justify-content-center " >

    <ConfirmDialog ></ConfirmDialog>
    <Toast />

    <Dialog v-model:visible="visible" modal header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
     <template #header>
         <div class="flex align-items-center gap-2">
             <h3 class="font-bold">Reembolsar Anticipo</h3>
         </div>
     </template>
    
     <div >      
      
                        
                      
      <div>
        <div class="flex align-items-center gap-3 mb-2">
        <label for="username" class="font-semibold w-9rem">Tipo transacción</label>
        <Dropdown v-model:model-value="movimiento.tipo" :options="options" @update:model-value="getConceptos(movimiento.tipo)" placeholder="Select a City" checkmark :highlightOnSelect="false" class="w-full md:w-14rem"  />
    </div>
                                <div class="flex align-items-center gap-3 mb-2">
        <label for="username" class="font-semibold w-9rem">Concepto</label>
        <Dropdown v-model:model-value="movimiento.concepto" :options="conceptos" optionLabel="descripcion" placeholder="Select a City" checkmark :highlightOnSelect="false" class="w-full md:w-14rem"  />
    </div>

    <div class="flex align-items-center gap-3 mb-2">
        <label for="email" class="font-semibold w-9rem">Monto</label>
        <InputNumber v-model:model-value="movimiento.monto" inputId="integeronly" />
    </div>
    <div class="flex align-items-center gap-3 mb-2">
        <label for="email" class="font-semibold w-9rem">Medio de pago</label>
        <Dropdown v-model:model-value="movimiento.formaPago" :options="formasPagos" optionLabel="descripcion"  placeholder="Select a City" checkmark :highlightOnSelect="false" class="w-full md:w-14rem" />
    </div>
    <div class="flex align-items-center gap-3 mb-2">
        <label for="email" class="font-semibold w-9rem">Documento</label>
        <InputText v-model:model-value="movimiento.documento" id="email" class="flex-auto" autocomplete="off"  />
    </div>
    <div class="flex align-items-center gap-3 mb-2">
        <label for="email" class="font-semibold w-9rem">Observación</label>
        <Textarea v-model:model-value="movimiento.observacion" rows="5" cols="30"  />
    </div>

                             
                            </div>
                       
                  
            </div>
 <template #footer>
     <div class="card flex" style="justify-content: end;">  
                 <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                 <Button  label="Guardar" :disabled="disabledSubmit" @click="guardarReembolso()" />
             </div>
</template>
</Dialog>

    <Panel style=" position: relative; width: 100%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Caja</h3>
        </div>
      </template>
      <template #icons>
        <div class="flex align-items-center">
          <Button  icon="pi pi-plus " @click="nuevoMovimiento" style=" width: 3rem !important; height: 2.9rem;" />

        </div>
        
    
      </template>
      
      
      
  
      <div class="card">
        
        <DataTable  :value="movimientos" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.fecha) }}
            </template>
        </Column>
          <Column field="concepto.descripcion"  header="Concepto" aria-sort="ascending" sortable>  
                     
        </Column>
          <Column field="formaPago.descripcion"  header="Forma" aria-sort="ascending" sortable> 
            
        </Column>
        <Column field="monto"  header="Monto" aria-sort="ascending" sortable>    
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.monto) }}
            </template>        
        </Column>
        <Column field="ingreso"  header="Ingreso" aria-sort="ascending" sortable>   
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.ingreso) }}
            </template>         
        </Column>
        <Column field="egreso"  header="Egreso" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.egreso) }}
            </template>           
        </Column>
        <Column field="venta.nroFactura"  header="N° Factura" aria-sort="ascending" sortable>            
        </Column>

        
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
                
                <Button icon="pi pi-times" severity="danger" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>