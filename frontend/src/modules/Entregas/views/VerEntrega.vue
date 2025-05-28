<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Card from "primevue/card";
import { ProveedorServices } from '@/services/ProveedorServices';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { ProductoServices } from '@/services/ProductoServices';
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import { getEstadoPedidoCompra, getEstadoEntrega, formatearFechaHora } from "@/utils/utils";
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import { EntregaServices } from "@/services/EntregaServices";
import DatePicker from 'primevue/datepicker';
const fecha = ref(new Date());
const map = ref();
const direccion = ref({});
const selectedCliente = ref();
const clienteDialog = ref(false);
const personaCreationDTO = ref({});
const entrega = ref({});
const submitted = ref(false);
const clienteSeleccionado = ref(false);
const mensaje = ref([]);
const ciudades = ref([]);
const departamentos = ref([]);
const documentos = ref([]);
const visible = ref(false);
const cliente = ref({});
const selectedOp = ref('Casi');
const productos= ref();
const clientes=ref();
const filteredClientes = ref();
const error = ref(false);
const opciones = ref(['Casi','Entre']);
const infoProveedor= ref([{
    
}])

//router.currentRoute.value.params.id

import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";
import {formatearNumero, formatearFecha} from '@/utils/utils';

const confirm = useConfirm();
const toast = useToast();

const message = (m) => {
    let id = m.id;
    let nro = m.nroFactura;
    console.log('messageError invocado');
    confirm.require({
        group: 'headless',
        header: '#'+ nro,
        message: 'Se ha generado la Factura',

        accept: () => {
            router.push({name: 'verFactura', params: {id}});
            //verPedido(router.currentRoute.value.params.id);
            
        },
        reject: () => {
            router.push({name: 'ventas'});
            //verPedido(router.currentRoute.value.params.id);
            
        },
    });
};

const detalleFacturar = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});

onMounted(() => {
    EntregaServices.getEntrega(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        entrega.value = data.data.entrega
        detalle.value = data.data.detalle;
   });

});

const vistaEntregas= () =>{
    router.push({name: 'entregas'});
}

const isRetiro = (modalidad) => {
    let descripcion = modalidad?.descripcion;
  switch (descripcion) {
       case 'Retiro':
           return true;
       default:
           return false;
   }
};

const isEnvio = (modalidad) => {
    let descripcion = modalidad?.descripcion;
  switch (descripcion) {
       case 'Envio':
           return true;
       default:
           return false;
   }
};

const mostrarCliente = (proveedor) =>{
    let valor;
    if(proveedor.descripcion!=null){
        valor={valor: proveedor.descripcion};
        infoProveedor.value.push(valor);
    }

    if(proveedor.ruc!=null){
        valor={valor: 'RUC: '+ proveedor.ruc};
        infoProveedor.value.push(valor);
    }

    if(proveedor.telefono!=null){
        valor={valor: 'Telefono: '+ proveedor.telefono};
        infoProveedor.value.push(valor);
    }
    
    clienteSeleccionado.value = true;
}

const modificarPedido = (id) => {
    router.push({name: 'modificar_entrega', params: {id}});
  
  }


</script>
<template>
    

<div class=" flex justify-content-center " >
    <Panel style=" position: relative; width: 80%;" >
        <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Entrega N° {{ entrega.id }}</h3>
                </div>
            </template>
            <template #icons>
                <div class="flex" style="justify-content: end;">  
                <Button  label="Atras"  style="margin-right: 1%;"  @click="vistaEntregas()" />
                <Button  label="Modificar" @click="modificarPedido(entrega.id)" />
                </div>
 
            </template>
        <div class="contenedor" >

            <div v-if="error" style="background-color: rgb(242, 222, 222); 
            border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
                <ul>
                    <li v-for="msg in mensaje" style="list-style: none;">
                    <a style="color: rgb(173, 89, 86);">{{ msg }}</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="grid " >
            <div class="field col-12 md:col-6">
                    <Card>
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Información General
                                </div>    
                            </div>
                        </template>
                        <template #content>
                            <div >
                                Fecha: {{ formatearFechaHora(entrega.fecha)}}
                            </div> 
                            <div  >
                                Estado: {{getEstadoEntrega(entrega.estado)}}
                            </div> 
                            <div >
                                Observaciones: 
                                
                                <p class="m-0">
                                    {{ entrega.observaciones }}
                                </p>
                            </div>

                        </template>
                    </Card>
                </div>
            
           <div class="field col-12 md:col-6">
            <Card>
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Entrega
                                </div>    
                            </div>
                        </template>
                        <template #content>
                            <div v-if="isRetiro(entrega.modoEntrega)" >
                                <div>
                                    Modalidad: {{ entrega.modoEntrega?.descripcion }}
                                </div>
                                <div>
                                    Punto de Retiro: {{ entrega.puntoEntrega?.descripcion }}
                                </div>
                            </div>
                            <div v-else-if="isEnvio(entrega.modoEntrega)"  >
                                <div>
                                    Modalidad: {{ entrega.modoEntrega?.descripcion }}
                                </div>
                                <div>
                                    Delivery: {{ entrega.empresaTransporte?.descripcion }}
                                </div>
                                <div>
                                    Dirección Envío: 
                                    <label >{{ entrega.direccionEnvio.direccion }},  {{ entrega.direccionEnvio.ciudad.descripcion }}</label>
                                       
                                </div>
                            </div>
                           </template>
                    </Card>
            
            </div>  
            
            <div class="col-12" >
                        <Card >
                            <template #title>
            <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    Productos
                </div>
            </div>
            
        </template>
                    
                        <template #content>
                            <div>
                                
                                <div class="card" style="width: 100%;">
    <div class="flex card-container" style="width: 100%;">
        <DataTable class="tablaCarrito" ref="dt" :value="detalle" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
            <Column  class="col" field="detalleVenta.producto.nombre" header="Nombre" aria-sort="none" ></Column>
         <Column class="col" field="producto.precio"  header="Facturado" aria-sort="none" >
            <template #body="slotProps">
            <div class="flex-auto p-fluid" >
                  {{  formatearNumero(slotProps.data.detalleVenta.cantidad) }}
              </div> 
            </template>
        </Column>
         
        <Column  class="col" field="cantidad" header="Preparado" aria-sort="none">
         </Column>
         
     </DataTable>
   </div>
 </div>
                              
                            </div>
                        </template>    
                    </Card>
                        
                    </div>
        </div>
      
    </Panel>
</div>
    
</template>
<style>
.p-inputgroup-addon{
    padding: 0%;
}

.p-inputnumber-buttons-stacked .p-inputnumber-button-group .p-button.p-inputnumber-button {
    flex: 1 1 auto;
    padding: 0rem;
    width: 1rem;
}
/*

.p-accordion-tab{
    margin: 2%;
    
    
}
.p-icon{
    color: pink;
    margin-right: 1%;
}

.p-accordion-header-link{
    height: 7vh !important;
}
.p-accordion-header-text{
    color: black;
}


.p-card-title{
    font-size:medium;
}
.p-card .p-card-body {
    padding: 1rem;
}
.p-card .p-card-content {
    padding: 0.5rem 0;
}

.principal{
    display: flex;
    border: solid palevioletred 2px;
    justify-content: center;
    border-radius: 1vh;
    margin-left: 4%;
    margin-right: 4%;
    padding: 1%;
}

h3 {
    display: flex;
    font-size: 1.17em;
    margin-block-start: 0px;
    margin-block-end: 0px;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    font-weight: bold;
    justify-content: center;
}
.p-button{
    box-shadow: 0 0 0 0 !important; 
    font-family:'primeicons' !important;
}
.p-button:hover{
    box-shadow: 0 0 0 0 !important; 
}
.p-card{
    box-shadow:none;
    font-size:14px;
}
.p-dropdown-label{
    padding: 0px !important;
}*/
</style>