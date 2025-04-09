<script setup>
import { ref, onMounted,  computed } from 'vue';
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
import Checkbox from 'primevue/checkbox';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';


import SplitButton from 'primevue/splitbutton';

const pedidos = ref();
import { formatearNumero, formatearFecha, getEstadoPedidoCompra, getEstadoRecepcion } from '@/utils/utils'; 
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import NuevaFactura from './NuevaFactura.vue';
import {RecepcionServices} from '@/services/RecepcionServices';
const expandedRows = ref({});
const seleccionarFacturaDialog = ref(false);
const seleccionarRecepcionesDialog = ref(false);
const classe = ref(' color: #dd128a !important; weight: bold ' );
const confirm = useConfirm();
const facturasPedido = ref();
const selectedFactura = ref();
const selectedRecepciones = ref();
const recepcionesPedido = ref();
const pedidoRecepcionar =ref();
const pedidoFacturar =ref();
const toast = useToast();

const selectedOpcion = ref();
const idPedidoSelected = ref();
const rowClass = (data) => {
  console.log(data);
  if ( data.proveedor.tipo.descripcion === 'Extranjero') {
        return [{ '!bg-primary ': true }];
    }
    //return [{ '!bg-secondary !text-primary-contrast': data.proveedor.tipo.descripcion === 'Extranjero' }];
};
const rowStyle = (data) => {
  
  console.log(data);
    if ( data.proveedor.tipo.descripcion === 'Extranjero') {
      return classe.value;
    }
};

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



async function recepcionarPedidoCompra(id) {
   // let id = pedido.id;
   //pedidoRecepcionar.value = pedido;
   router.push({name: 'recepcionarPedido', params: {id}});
  /*if ((pedidoRecepcionar.value.proveedor.tipo.descripcion == 'Extranjero') ) {
    facturasPedido.value = (await CompraServices.obtenerComprasByPedido(id)).data;
    console.log(facturasPedido.value);
    seleccionarFacturaDialog.value = true;
    
  } else{
    router.push({name: 'recepcionarPedido', params: {id}});
  }*/
    
}

const recepcionarPedidoInternacional = () => {
  let id = pedidoRecepcionar.value.id;
  let facturaId = selectedFactura.value.compra.id;
  console.log(facturaId);
  router.push({name: 'recepcionarPedido', params: {id}, query: {facturaId: facturaId} });

}

async function facturarPedidoCompra(id) {
  pedidoFacturar.value = id;
  recepcionesPedido.value = (await RecepcionServices.getRecepcionesByPedido(id)).data;
    console.log(recepcionesPedido.value);
    seleccionarRecepcionesDialog.value = true;
   // let id = pedido.id;
  /*  pedidoFacturar.value = pedido;
  if ((pedidoFacturar.value.proveedor.tipo.descripcion == 'Nacional') ) {
    recepcionesPedido.value = (await RecepcionServices.getRecepcionesByPedido(id)).data;
    console.log(recepcionesPedido.value);
    seleccionarRecepcionesDialog.value = true;
    
  } else{
    sessionStorage.removeItem('recepcionesFacturar');
    nuevaFactura(id);
  }*/
    
}

const facturarPedidoNacional = () => {
  let id = pedidoFacturar.value.id;
  if (selectedRecepcionesIds.value!=null) {
    sessionStorage.setItem('recepcionesFacturar', JSON.stringify(selectedRecepcionesIds.value) );
    nuevaFactura(id);
  } 

}

const facturarRecepciones = () => {
  let id = pedidoFacturar.value;
  if (selectedRecepcionesIds.value!=null) {
    sessionStorage.setItem('recepcionesFacturar', JSON.stringify(selectedRecepcionesIds.value) );
    nuevaFactura(id);
  } 

}



const nuevaFactura = (id) => {
  router.push({name: 'nueva_factura_compra', params: {id}});

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

const isRecepcionPermitida = (pedido) => {
  let tipo = pedido.proveedor.tipo.descripcion;
  let estado = pedido.estadoPedido;
  switch (tipo) {
        case 'Nacional':
           return true;
       case 'Extranjero':
            return (estado == 'N' || estado == 'A') ?false:true;
           /*if (estado == 'N' || estado == 'A') {
            return false;
           }
            return true;*/
       default:
           return false;
   }
  
  
};

const isFacturacionPermitida = (pedido) => {
  let tipo = pedido.proveedor.tipo.descripcion;
  let estado = pedido.estadoPedido;
  switch (tipo) {
        case 'Extranjero':
           return true;
       case 'Nacional':
            return (estado == 'N' || estado == 'A') ?false:true;
           /*if (estado == 'N' || estado == 'A') {
            return false;
           }
            return true;*/
       default:
           return false;
   }
  
};

const isTotalFacturado = (estado) => {
  switch (estado) {
       case 'F':
           return true;
       default:
           return false;
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

const ver = (e) =>{
    console.log(e);
    console.log(selectedRecepciones.value);
}

const selectedRecepcionesIds = computed(() => 
  selectedRecepciones.value.map(r => r.recepcion.id) // Solo guardamos los IDs
);


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
        
        <DataTable  :value="pedidos"  scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <template #empty> No hay registros para mostrar. </template>
          <template #loading> Cargando. </template>
          <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
          <Column   field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
                {{ formatearFecha(slotProps.data.fecha) }}
            </template>
        </Column>
          <Column field="proveedor.descripcion"  header="Proveedor" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                
                  {{ slotProps.data.proveedor.descripcion }}                
            </template>           
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
                <Button icon="pi pi-truck" severity="success" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="recepcionarPedidoCompra(slotProps.data.id)" />
                <Button  icon="pi pi-receipt" severity="info" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="facturarPedidoCompra(slotProps.data.id)" />

                <Button :disabled="!isNuevo(slotProps.data.estadoPedido)" icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>
        </DataTable>
      </div>

      <div>
        <!--Modal Seleccionar Factura-->
        <Dialog v-model:visible="seleccionarFacturaDialog"  modal header="Edit Profile" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Seleccionar Factura</h3>
                </div>
            </template>
    
            <div class="contenedor" > 
              <div>     
              <DataTable v-model:selection="selectedFactura"  v-model:expandedRows="expandedRows" :value="facturasPedido" dataKey="compra.id" tableStyle="min-width: 50rem">
                <Column selectionMode="single" headerStyle="width: 3rem"></Column>
                <Column expander style="width: 5rem" />
                <Column field="compra.id" header="Factura#">
                  
                </Column>
                <template #expansion="slotProps">
                  <div >
                  <table width="100%" align="center" border="1" style="border-collapse: collapse;" >
                    <thead>
                      <tr>
                        <th style="font-weight: bold;" >Producto</th>
                        <th style="font-weight: bold;">Cantidad Facturada</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="d in slotProps.data.detalle" :key="d.id">
                        <td>{{ d.producto.nombre  }}</td>
                        <td>{{ d.cantidad  }}</td>
                       
                      </tr>
                    </tbody>
                  </table>
                </div>

                
            </template>
            </DataTable>
          </div>
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;" />
                    <Button  label="Aceptar" @click="recepcionarPedidoInternacional()" />
                </div>
            </template>
        </Dialog>
      </div>

      <div>
        <!--Modal Seleccionar Recepciones-->
        <Dialog v-model:visible="seleccionarRecepcionesDialog"  modal header="Edit Profile" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Seleccionar Recepciones</h3>
                </div>
            </template>
    
            <div class="contenedor" > 
              <div>     
              <DataTable   v-model:expandedRows="expandedRows" :value="recepcionesPedido"  dataKey="recepcion.id" v-model:selection="selectedRecepciones" tableStyle="min-width: 50rem">
                <Column selectionMode="multiple" headerStyle="width: 3rem">
                  <template #body="slotProps">
                  <div class="flex-auto p-fluid" style="max-width:20lvb  !important; " >
                      <Checkbox :disabled="slotProps.data.recepcion.compra !== null" v-model="selectedRecepciones" :value="slotProps.data"  />
                  </div> 
              </template>
                </Column>
                <Column expander style="width: 5rem" />
                <Column field="recepcion.id" header="Recepcion#"></Column>
                <Column field="recepcion.fecha" header="Fecha">
                  <template #body="slotProps">
                      {{ formatearFecha(slotProps.data.recepcion.fecha) }}
                  </template>
                </Column>
                <Column field="estado"  > 
                    <template #body="slotProps">
                        <Tag style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ getEstadoRecepcion(slotProps.data.recepcion.estado)}}</Tag>
                      </template> 
                </Column> 
                <template #expansion="slotProps">
                <div >
                  <table width="100%" align="center" border="1" style="border-collapse: collapse;" >
                    <thead>
                      <tr>
                        <th style="font-weight: bold;" >Producto</th>
                        <th style="font-weight: bold;">Cantidad Recepcionada</th>
                        <th style="font-weight: bold;">Cantidad Aceptada</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="d in slotProps.data.detalle" :key="d.id">
                        <td>{{ d.detallePedido.producto.nombre  }}</td>
                        <td>{{ d.cantRecepcionado  }}</td>
                        <td>{{ d.cantAceptada }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                
            </template>
            </DataTable>
          </div>
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;" />
                    <Button  label="Aceptar" @click="facturarRecepciones()" />
                </div>
            </template>
        </Dialog>
      </div>
      
    </Panel>
    
  </div>
  
</template>
<style>

</style>