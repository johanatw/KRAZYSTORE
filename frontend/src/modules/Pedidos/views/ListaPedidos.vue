<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import {PedidoServices} from '@/services/PedidoServices';
import Panel from 'primevue/panel';

import Tooltip from 'primevue/tooltip';



import router from '@/router';
import Toast from 'primevue/toast';
import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';
import Card from 'primevue/card';
import ConfirmDialog from 'primevue/confirmdialog';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';


import SplitButton from 'primevue/splitbutton';
const visibleDeleteDialog = ref(false);
const pedidos = ref();

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import RegistrarPago from '../components/RegistrarPago.vue';

const opciones = ref([{id: 1, descripcion: 'Facturar productos disponibles en stock.'},
                    {id: 2, descripcion: 'Registrar anticipo para productos no disponibles en stock.'}]);
const confirm = useConfirm();
const confirm3 = useConfirm();
const toast = useToast();
const dialogAnticipo = ref(null);
const selectedOpcion = ref();
const idPedidoSelected = ref();
const idAnticipoGuardado = ref();
const productosStockInsuficiente = ref(false);
const productosParaFacturar = ref(false);
const visiblePedidoFacturadoDialog = ref(false);
const detallePedido = ref();
const formatearNumero = (valor) =>{
    if(typeof(valor) == "number"){
        return valor.toLocaleString("de-DE");
    }

    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear()+' '+ fecha.getHours()+':'+fecha.getMinutes()+':'+fecha.getSeconds();
    return fechaFormateada;
}
const getSeverity = (estado) => {
  
  
   switch (estado) {
        case 'Pagado':
            return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66);';

        case 'Parcial':
            return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';

        case 'Pendiente':
            return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';

        default:
            return null;
    }
};

const getDetallePedido = (id) => {
  idPedidoSelected.value = id;
  PedidoServices.getDetallePedido(id).then((data) => {
    detallePedido.value = data.data;
    
    verificarStockProductos();

        
    });
    
};

const verificarStockProductos = () => {
  console.log(detallePedido.value);
  console.log("length");
  console.log(detallePedido.value[0]);
  
  
  for (let i = 0; i < detallePedido.value.length; i++) {
      if (detallePedido.value[i].cantidad <= detallePedido.value[i].producto.cantStock && detallePedido.value[i].cantidad > detallePedido.value[i].cantidadFacturada) { 
        productosParaFacturar.value = true;
        
        break; }
      
    }

  for (let i = 0; i < detallePedido.value.length; i++) {
      if (detallePedido.value[i].cantidad > detallePedido.value[i].producto.cantStock && detallePedido.value[i].cantidad > detallePedido.value[i].cantidadFacturada) { 
        productosStockInsuficiente.value = true;
        break; }
      
    }

    
    if (productosParaFacturar.value && !productosStockInsuficiente.value) {
      facturarPedido();
    }

    if(!productosParaFacturar.value && !productosStockInsuficiente.value){
      visiblePedidoFacturadoDialog.value=true;
    }

   
};

const confirm2 = (id) => {
   
    confirm.require({
        message: 'Eliminar el pedido '+ id + '?',
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

const showTemplate = () => {
  confirm.require({
        group: 'headless',
        header: 'Anticipo Guardado',
        message: 'Please confirm to proceed.',
        
        accept: () => {
            //toast.add({ severity: 'info', summary: 'Confirmed', detail: 'You have accepted', life: 3000 });
            getPedidos();
            
        },
        reject: () => {
          router.push({name: 'anticipos'});
          
        }
    });
};
onMounted(() => {
 
  getPedidos();
 
    
});



 async function getPedidos() {
  

   await PedidoServices.getPedidos().then((data) => {
        pedidos.value = data.data;

        console.log(pedidos.value);
    });
  
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

const showDialogAnticipo = (idPedido) =>{
  dialogAnticipo.value.showDialog(idPedido);
}

const showDialogPagoGuardado = (idAnticipo) =>{
  idAnticipoGuardado.value = idAnticipo;
  showTemplate();
}


const cancelar = ()=>{
  visible.value = false;
  selectedOpcion.value = null;
}

const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
    
}

const facturarPedido = () =>{
   console.log(idPedidoSelected.value);
   let id = idPedidoSelected.value;
    router.push({name: 'facturar', params: {id}});
    
}

const deletePedido = (id) =>{
    const cantidad= 1;
    const index = pedidos.value.findIndex((loopVariable) => loopVariable.id === id);
    
    PedidoServices.deletePedido(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      if (response.data > 0) {
        visibleDeleteDialog.value = true;
      } else {
        pedidos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
      }
            
        })

   /* PedidoServices.deleteDetallesPedido(id).then((response)=>{
        console.log(response);
        PedidoServices.deletePedido(id).then((response)=>{
            pedidos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 3000 });
        })
    });*/
}

const nuevoPedido = () =>{
    router.push({name: 'nuevo_pedido'});
}

const prueba = (event) =>{
    if (event == false) {
        reiniciarDialog();
    }
}

const closeDialog = () =>{
    productosStockInsuficiente.value = false;
    reiniciarDialog();
}

const reiniciarDialog = () =>{
    productosParaFacturar.value = false;
    detallePedido.value = [];
    idPedidoSelected.value = null;
}



const irAnticipos = () =>{
  router.push({name: 'anticipos'});
}

const reload = () =>{
  window.location.reload();
}
</script>

<template>
  <div class="card flex p-fluid justify-content-center " >
    
    <RegistrarPago ref="dialogAnticipo" @anticipoGuardado="showDialogPagoGuardado()" />
    <ConfirmDialog group="headless">
    <template #container="{ message, acceptCallback, rejectCallback }">
      
        <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
          
            <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-4rem w-4rem ">
                <i class="pi pi-check text-4xl"></i>
            </div>
            <span class="font-bold text-2xl block mb-2 mt-4">{{ message.header }}</span>
            
            <div class="flex align-items-center gap-2 mt-4">
              <Button label="Ir a anticipos" outlined @click="rejectCallback" class="w-8rem" ></Button>
                <Button label="Cancelar" @click="acceptCallback" class="w-8rem"  ></Button>
                
            </div>
        </div>
    </template>
</ConfirmDialog>


    
  
    
  
    
    <Toast />
    <ConfirmDialog ></ConfirmDialog>

    <Dialog v-model:visible="productosStockInsuficiente" modal @update:visible="prueba($event)"  header="Edit Profile" :style="{ width: '40rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
     <template #header>
         <div class="flex align-items-center gap-2">
             <h3 class="font-bold">Productos Sin Stock Disponible</h3>
         </div>
     </template>

    
     <div > 
      <div class="flex align-items-center mb-3">
        Algunos productos en este pedido no tienen suficiente stock. 
        Solo se pueden facturar los productos que tienen stock disponible.
      </div> 
      <div class="flex align-items-center mb-1" style="justify-content: start; font-weight: bold; " >
        Detalles
      </div>
       
        <div>
          <DataTable :value="detallePedido" >
              <Column field="producto.nombre" header="Producto" aria-sort="ascending" >
              </Column>
              
              <Column field="producto.cantStock" header="Stock" aria-sort="ascending">
                <template #body="slotProps">
                      <div v-if="slotProps.data.cantidad > slotProps.data.producto.cantStock" style="color: red;">
                          {{ slotProps.data.producto.cantStock }} 
                      </div>
                      <div v-else style="color: green;">
                          {{ slotProps.data.producto.cantStock }} 
                      </div>
                 </template> 
              </Column>
              <Column field="cantidad" header="Cantidad Solicitada" aria-sort="ascending"  ></Column>
              <Column field="cantidadFacturada" header="Cantidad Facturada" aria-sort="ascending"  ></Column>
              
          </DataTable>
        </div>
        

        
                    
            </div>
 <template #footer>
     <div class="card flex" style="justify-content: end;">  
      <Button v-if="productosParaFacturar"  label="Facturar Productos en Stock" style="margin-right: 1%;" @click="facturarPedido()" />
                 <Button  label="Cancelar"  @click="closeDialog()"  />
                 
                 
             </div>
</template>
</Dialog>

<!--MODAL MENSAJE PAGOS ASOCIADOS -->
<Dialog v-model:visible="visibleDeleteDialog" modal  header="Edit Profile" :style="{ width: '50rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
     <template #header>
         <div class="flex align-items-center gap-2">
             <h3 class="font-bold">Este pedido tiene pagos asociados</h3>
         </div>
     </template>

    
     <div > 
      <div class="flex align-items-center mb-3">
        Por favor, elimina los pagos antes de proceder con la eliminación del pedido.
      </div> 
      
            </div>
 <template #footer>
     <div class="card flex" style="justify-content: end;">  
      <Button label="Ir a anticipos" style="margin-right: 1%;" @click="irAnticipos()" />
                 <Button  label="Cancelar"  @click="visibleDeleteDialog=false"  />
                 
                 
             </div>
</template>
</Dialog>

<!--MODAL SIN PRODUCTOS ´PARA FACTURAR -->
<Dialog v-model:visible="visiblePedidoFacturadoDialog" modal  header="Edit Profile" :style="{ width: '50rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
     <template #header>
         <div class="flex align-items-center gap-2">
             <h3 class="font-bold">Todos los productos de este pedido ya han sido facturados</h3>
         </div>
     </template>

    
     <div > 
      
      
            </div>
 <template #footer>
     <div class="card flex" style="justify-content: end;">  
      
                 <Button  label="Ok"  @click="visiblePedidoFacturadoDialog=false"  />
                 
                 
             </div>
</template>
</Dialog>




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
          <InputText style="padding: 12px !important; padding-left: 40px !important;" class="buscador p-fluid" v-model="filters['global'].value" placeholder="Buscar..."  />
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
            <template #body="slotProps">
            {{ formatearNumero(slotProps.data.fecha) }}   
          </template>        
        </Column>
          <Column field="cliente" header="Cliente" aria-sort="ascending" sortable>
            
              

            </Column>
            <Column  field="telefono" header="Telefono" aria-sort="ascending" sortable >
            </Column>
            <Column  field="estadoPedido" header="Estado Pedido" aria-sort="ascending" sortable >
              <template #body="slotProps">
                <Tag style="background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;">{{ slotProps.data.estadoPedido}}</Tag>
              </template>
              
            </Column>
            <Column  field="estadoPago" header="Estado Pago" aria-sort="ascending" sortable >
              <template #body="slotProps">
                <Tag :style="getSeverity(slotProps.data.estadoPago)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ slotProps.data.estadoPago}}</Tag>
              </template>
            </Column>
            
          <Column field="total"  header="Total" aria-sort="ascending" sortable>
            <template #body="slotProps">
            {{ formatearNumero(slotProps.data.total) }}   
          </template>
          </Column>
          <Column  class="col"  aria-sort="none">
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" v-if="slotProps.data.cantPreVenta > 0" style="max-width:10lvb  !important; font-size: 12px; ">
                   <Tag style="font-size: 10px;" value="SinStock" ></Tag>
                  {{ slotProps.data.cantPreVenta }}/{{ slotProps.data.totalItems }} item
                 
             </div>
               
           </template>
             
         </Column>
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
                <Button icon="pi pi-search" text rounded aria-label="Search" @click="verPedido(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-money-bill" severity="success" text rounded aria-label="Cancel" @click="showDialogAnticipo(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
        
                <Button  icon="pi pi-receipt" severity="info" text rounded aria-label="Cancel" @click="getDetallePedido(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>
<style>/*
.p-datatable .p-datatable-tbody > tr > td {
    text-align: left;
    border: 1px solid #e5e7eb;
    border-width: 0 0 1px 0;
    padding: 0.5rem 0.5rem;
}
.p-dialog .p-dialog-footer {
    border-top: 0 none;
    background: #ffffff;
    color: #334155;
    padding: 0 1.5rem 1.5rem 1.5rem;
    display: flex;
    justify-content: flex-end;
    gap: 0.5rem;
    border-bottom-right-radius: 6px;
    border-bottom-left-radius: 6px;
}
.p-dialog.p-confirm-dialog .p-confirm-dialog-message:not(:first-child) {
    margin-left: 1rem;
}
.p-dialog .p-dialog-header .p-dialog-title {
    font-weight: 600;
    font-size: 1.25rem;
}
.p-fluid .p-splitbutton {
    display: none;
}
.p-datatable .p-datatable-tbody > tr > td {
    font-size: 14px;
}

.p-input-icon-left > .p-inputtext {
    padding-left: 2.5rem !important;
}
.p-inputtext {
    padding: 12px !important;
}

*/
</style>