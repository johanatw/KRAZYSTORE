<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Card from "primevue/card";
import { ProveedorServices } from '@/services/ProveedorServices';
import { RecepcionServices } from "@/services/RecepcionServices";
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { ProductoServices } from '@/services/ProductoServices';
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import Calendar from "primevue/calendar";
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import DatePicker from "primevue/datepicker";
const map = ref();
const direccion = ref({});
const selectedCliente = ref();
const clienteDialog = ref(false);
const personaCreationDTO = ref({});
const submitted = ref(false);
const clienteSeleccionado = ref(false);
const mensaje = ref([]);
const fechaRecepcion = ref(new Date());
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

const detalleRecepcion = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});
const compraNacional = ref(false);

onMounted(() => {
    PedidoCompraServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data.pedido;
        detalle.value = data.data.detalle;
        fechaRecepcion.value = pedido.value.fecha;
        console.log(data.data);
       proveedor.value = pedido.value.proveedor;
       if (data.data.pedido?.proveedor.tipo.descripcion == 'Extranjero') {
            compraNacional.value = false;
        }else{ compraNacional.value = true;} 
       mostrarCliente(proveedor.value);

       detalle.value.forEach(e=> {
            
            let d = {};
            
            d.detallePedido = e;
            d.cantRecepcionado = 0;
            d.cantRechazada = 0;
            d.cantAceptada = 0;
            if (proveedor.value.tipo.descripcion == 'Extranjero') {
                d.cantPendiente = e.cantFacturada - e.cantRecepcionada;
            }else{ 
                d.cantPendiente = e.cantSolicitada- e.cantRecepcionada;
            } 
            
            detalleRecepcion.value.push(d);
        });
   });

});


const mostrarCliente = (proveedor) =>{
    let valor;
    if(proveedor.descripcion!=null){
        valor={valor: proveedor.descripcion};
        infoProveedor.value.push(valor);
    }

    if(proveedor.telefono!=null){
        valor={valor: proveedor.telefono};
        infoProveedor.value.push(valor);
    }
    
    clienteSeleccionado.value = true;
}

const modificarPedido = (id) => {
    if (!error.value){


    let fechaAnticipo = new Date();
    let ant = {idPedido: pedido.value.id, fecha: fechaRecepcion.value};
     
    console.log(ant);
    console.log(detalleRecepcion.value);

    let anticipoCreationDTO = {recepcion: ant, detalle: detalleRecepcion.value};
   RecepcionServices.registrarRecepcion(anticipoCreationDTO ).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
        let id = data.data.id;
        verRecepcion(id);
        //closeDialog();
        //emit('anticipoGuardado', data.data.id);
        
    } );
    }
  }

  const verRecepcion = (id) =>{

    router.push({name: 'ver_recepcion', params: {id}});
}

const vistaPedidos= () =>{
    router.push({name: 'pedidos_compras'});
}

</script>
<template>
    

<div class=" flex justify-content-center " >
    <Panel style=" position: relative; width: 80%;" >
        <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Recepcionar Pedido N° {{ pedido.id }}</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" @click="vistaPedidos()" />
                        <Button  label="Guardar" @click="modificarPedido(pedido.id)" />
                    </div>  
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
                            <div class="field" >
                                Fecha: <DatePicker dateFormat="dd/mm/yy" v-model="fechaRecepcion" showIcon iconDisplay="input" />
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
        <DataTable class="tablaCarrito" ref="dt" :value="detalleRecepcion" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
         <Column  class="col" field="detallePedido.producto.nombre" header="Nombre" aria-sort="none" ></Column>
         
        <Column v-if="compraNacional" class="col" field="detallePedido.cantSolicitada" header="Solicitado" aria-sort="none">
         </Column>
         <Column v-else class="col" field="detallePedido.cantFacturada" header="Facturado" aria-sort="none">
         </Column>
         <Column  class="col" field="cantidad" header="Recepcionado" aria-sort="none">
            <template #body="slotProps">
                <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                {{ slotProps.data.detallePedido.cantRecepcionada}}  
                </div> 
            </template>
         </Column>
         <Column  class="col" field="cantidad" header="Pendiente" aria-sort="none">
            <template #body="slotProps">
                <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                {{ slotProps.data.cantPendiente }}  
                </div>  
            </template>
         </Column>
         <Column  class="col" field="cantRecepcionado" header="Recibido" aria-sort="none">
            
            <template #body="slotProps">
                <div v-if="slotProps.data.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                  <InputNumber fluid class="inpCant" v-model="slotProps.data.cantRecepcionado" :min="0" :max="slotProps.data.cantPendiente" inputId="minmax-buttons" mode="decimal" showButtons />
              </div>  
         
            </template>
         </Column>
         <Column class="col" field="cantDañada" header="Aceptar" aria-sort="none">
            <template #body="slotProps">
                <div v-if="slotProps.data.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                  <InputNumber fluid class="inpCant" v-model="slotProps.data.cantAceptada" :min="0" :max="slotProps.data.cantRecepcionado" inputId="minmax-buttons" mode="decimal" showButtons />
              </div> 
             
            </template>
         </Column>
         <Column  class="col" field="cantDañada" header="Rechazar" aria-sort="none">
            <template #body="slotProps">
              <div v-if="slotProps.data.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                  {{slotProps.data.cantRechazada = slotProps.data.cantRecepcionado - slotProps.data.cantAceptada}}
                </div> 
            </template>
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