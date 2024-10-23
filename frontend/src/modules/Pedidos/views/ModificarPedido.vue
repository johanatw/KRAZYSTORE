<script setup>
import CardCliente from "@/modules/Pedidos/components/CardCliente.vue";
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import CardEntrega from "@/modules/Pedidos/components/CardEntrega.vue";
import CardPago from "@/modules/Pedidos/components/CardPago.vue";
import MiCard from "@/modules/Pedidos/components/MiCard.vue";
import DetallePedido from '@/modules/Pedidos/components/DetallePedido.vue';
import Button from 'primevue/button';
import Dropdown from "primevue/dropdown";

import Tag from 'primevue/tag';

import SearchCliente from "../components/SearchCliente.vue";
import { ref, onMounted } from "vue";
import { PersonaServices } from '@/services/PersonaServices';
import {EstadosServices} from "@/services/EstadosServices";
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import {CajaServices} from '@/services/CajaServices';
import Card from "primevue/card";
import router from '@/router';
const res = ref();
const pedido = ref({ });
const selectedClient = ref();
const detallePedido = ref({ });
const mensaje = ref([]);
const modoEntrega = ref();
const estadoPedido = ref();
const estadoPago = ref();
const formaPago = ref();
const cancelSubmit = ref(false);
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();
const messageError = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'headless',
        header: 'Ocurrio un error',
        message: msg.toUpperCase(),

        accept: () => {
            getDetalle();
            getPedido();
            //verPedido(router.currentRoute.value.params.id);
            
        },
    });
};

const detalleRegistrar = ref([ ]);
const detalleEliminar = ref([ ]);
const detalleModificar = ref([ ]);
const pedidoId = ref(0);
const pedidoTotalPagado = ref({});
const cliente = ref();
const cardCliente = ref();
const cardEntrega = ref();
const searchCard = ref();
const contactClient = ref();
const selectedEstadoPedido = ref();
const selectedEstadoPago = ref();
const estadosPedido = ref();
const estadosPago = ref();
const productos= ref();
const entrega = ref();
const envioSelected = ref();
const detalle = ref(null);
const error = ref(false);
const fecha = ref();
const fechaCompleta = ref();
const infoCliente = ref([{
    valor: "Cliente no seleccionado"
}])
const infoEntrega = ref([{
    valor: "Metodo de Entrega no seleccionado"
}])

onMounted(() => {
   
    /*PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        idPedido.value = data.data.id;
        resumen.value.setPedido(data.data);
        cli.value.setCliente(data.data.cliente);
       // clienteFactura.value.setCliente(data.data.datoFacturacion);
        entrega.value.setEntrega(data.data.modoEntrega, data.data.costoEnvio);
        detalle.value.setDetalle(router.currentRoute.value.params.id);
        pedido.value = data.data;
        
        
    });*/
    
    CajaServices.obtenerPagosPedido(router.currentRoute.value.params.id).then((data) => {
        pedidoTotalPagado.value = data.data;
    });

    EstadosServices.obtenerEstadosByTipo('E').then((data) => {
        estadosPedido.value = data.data;
    });
    EstadosServices.obtenerEstadosByTipo('P').then((data) => {
        estadosPago.value = data.data;
    });
    

    if (pedido.value.costoEnvio) {
            costo.value = pedido.value.costoEnvio.costo;
        }
        
    getDetalle();
    getPedido();
    

});

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
const formatearNumero = (valor) =>{
    if(typeof(valor) == "number"){
        return valor.toLocaleString("de-DE");
    }

    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear()+' '+ fecha.getHours()+':'+fecha.getMinutes()+':'+fecha.getSeconds();
    return fechaFormateada;
}

async function getDetalle() {
    await PedidoServices.getDetalle(router.currentRoute.value.params.id).then((data) => {
        console.log("Este", data);
        productos.value.setDetalle(data.data);

    });
    
}

async function getPedido() {
    await PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        /*formaPago.value.setPago(data.data.formaPago);
        modoEntrega.value.setEntrega(data.data.modoEntrega, data.data.costoEnvio);
        cliente.value.setCliente(data.data.cliente);*/
        getCliente(data.data.cliente);
        getEntrega(data.data.modoEntrega, data.data.costoEnvio);
       // estadoPago.value = data.data.estadoPago;
       // estadoPedido.value = data.data.estadoPedido;
       selectedEstadoPedido.value = data.data.estadoPedido;
        selectedEstadoPago.value = data.data.estadoPago;

        fecha.value = new Date(data.data.fecha);
        fechaCompleta.value = fecha.value.getDate() + "-"+ (fecha.value.getMonth()+1) + "-"+ fecha.value.getFullYear() ;

        pedido.value = data.data;
        
        
    });
}


const editCliente = () =>{
    searchCard.value.showDialog();
    if (selectedClient.value) {
        searchCard.value.showClient(selectedClient.value);
    }  
}

const editEnvio = () =>{
    
    modoEntrega.value.showDialog();
    if (entrega.value) {
        modoEntrega.value.setEntrega(entrega.value, envioSelected.value);
    }

}

const getCliente = (client) =>{
    selectedClient.value = client;
    infoCliente.value = [];
    let valor;
    if (client.apellido) {
        valor = {valor: client.nombre + ' '+ client.apellido};
    }else{
        valor = {valor: client.nombre};
    }
    infoCliente.value.push(valor);
    valor = {valor: client.telefono};
    infoCliente.value.push(valor);
    if (client.nroDoc) {
        valor = {valor: client.tipoDoc.descripcion +' - '+ client.nroDoc};
        infoCliente.value.push(valor);
    }
    if (client.email) {
        valor = {valor: client.email};
        infoCliente.value.push(valor);
    }

    
}

const getEntrega = (formaEntrega, envio) =>{
    if (formaEntrega) {
        entrega.value = formaEntrega;
    envioSelected.value = envio;
    infoEntrega.value = [];
    let valor = {valor: formaEntrega.descripcion};
    infoEntrega.value.push(valor);
    if (envio) {
        valor = {valor: 'a ' + envio.ciudad.descripcion};
        infoEntrega.value.push(valor);
        
        valor = {valor: 'Vía ' + envio.envio.descripcion + ' - '+envio.costo+' Gs'};
        infoEntrega.value.push(valor);
    }
    
    productos.value.setDetalleEnvio(envio);
    }
  
}

const getEstadoPago = () =>{
    if (pedidoTotalPagado.value.totalPagos == 0) {
        return selectedEstadoPago.value;
    }
    let estado = {};
    
    if (productos.value.subTotal > pedidoTotalPagado.value.totalPagos) {
        console.log("estado.id = 7");
        estado.id = 7;
    }else{
        console.log("estado.id = 2");
        estado.id = 2;
    }
  
    return estado;
}



const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
    
}


const submit = () =>{

    if (!error.value) {
            console.log("cancelvalue", cancelSubmit.value);
            pedido.value.cliente = selectedClient.value;

        /*if (cliente.value.personaFactura) {
            pedido.value.datoFacturacion = cliente.value.personaFactura;
        } else {
            pedido.value.datoFacturacion = cliente.value.selectedPersona;
            
        }*/
        pedido.value.estadoPedido = selectedEstadoPedido.value;
        pedido.value.estadoPago = getEstadoPago();
        console.log("pedido.value.estadoPago");
        console.log(pedido.value.estadoPago);
        pedido.value.modoEntrega = entrega.value;
        if (pedido.value.modoEntrega && pedido.value.modoEntrega.descripcion === "Envío") {
            pedido.value.costoEnvio = envioSelected.value;
        }else{
            pedido.value.costoEnvio = null;
        }
        
        //pedido.value.formaPago = formaPago.value.selectedFormaPago;
    

        //messageError(error.response.data.mensaje)
        detallePedido.value = productos.value.detalles;
        console.log("detallemodificar", detallePedido.value);
        pedido.value.total = productos.value.subTotal;
        let pedidoDTO = {pedido: pedido.value, detalle: detallePedido.value};
        
            PedidoServices.modificarPedido(pedido.value.id, pedidoDTO).then((response)=>{
                verPedido(response.data.id);

                  }).catch(
                    (error)=>messageError(error.response.data.mensaje)
                  );
                  
        }

        
    }


const validarForm = (event) => {
    
    mensaje.value = [];
    error.value = false;
    if (selectedClient.value) {
        if (!selectedClient.value.telefono) {
            error.value = true;
            mensaje.value.push("Debe ingresar n° de contacto");
            
        }
    } else {
        error.value = true;
            mensaje.value.push("Debe seleccionar un cliente");
    }

    if (productos.value.subTotal <1) {
        error.value = true;
        mensaje.value.push("Debe añadir productos al pedido");
    }
    
    submit();

}
</script>
<template>
    
    

<div class="card flex p-fluid justify-content-center " >
    <ConfirmDialog group="headless">
        <template #container="{ message, acceptCallback }">
            <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                    <i class="pi pi-times text-5xl"></i>
                </div>
                <span class="font-bold text-2xl block mb-2 mt-4">{{ message.header }}</span>
                <p class="mb-0">{{ message.message }}</p>
                <div class="flex align-items-center gap-2 mt-4">
                    <Button label="Ok" @click="acceptCallback"></Button>
                </div>
            </div>
        </template>
    </ConfirmDialog>
    
    

 <Panel style=" position: relative; width: 80%;" >
    <template #header>
     <div class="flex align-items-center gap-2">
         <h3 class="font-bold">Modificar Pedido N° {{ pedido.id }}</h3>
     </div>
   </template>
   <template #icons>
        
    <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;"  @click="verPedido(pedido.id)" />
                    <Button  label="Guardar" @click="validarForm" />
                </div>
        
          </template>
      <div class="contenedor" >

<div v-if="error" style="background-color: rgb(242, 222, 222); 
border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
    <ul>
        <li v-for="m in mensaje" style="list-style: none;">
        <a style="color: rgb(173, 89, 86);">{{ m }}</a>
        </li>
    </ul>
</div>


</div>

      <div class="grid " >
        <div class="field col-12 md:col-12" >
    <Card >

        <template #content>
            <p class="m-0">
                <div  >
                <div style=" display: flex; flex-direction: row;  ">
                    Fecha: &nbsp;
                    
                        {{ formatearNumero(pedido.fecha) }}
                    

                </div>
                    
                <div style="align-content: center; display: flex; flex-direction: row; ">
                Estado del pedido: &nbsp; 
                <div v-if="pedido.estadoPedido">
                    <Tag  style="background-color: rgb(215, 227, 552); color: rgb(50, 111, 252); font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ pedido.estadoPedido.descripcion}}</Tag>
                    
                </div> 
            </div>
            <div style=" display: flex; flex-direction: row; ">
                Estado de pago: &nbsp;
                <div v-if="pedido.estadoPago">
                    <Tag :style="getSeverity(pedido.estadoPago.descripcion)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ pedido.estadoPago.descripcion}}</Tag>
                </div>
            </div>
                

            </div>
                
            </p>
        </template>
    </Card>
            
            
          
        </div>
            
           <div class="field col-12 md:col-6">
                <MiCard ref="cardCliente" :titulo="'Cliente'" :contenido="infoCliente" @edit="editCliente"/> 
            </div>  
            <div class="field col-12 md:col-6">
                <MiCard ref="cardEntrega" :titulo="'Entrega'" :contenido="infoEntrega" @edit="editEnvio"/>
            </div>
            <div class="field col-12 md:col-12">
                <CardDetalle ref="productos"/>
            </div>
            <div>
                <SearchCliente ref="searchCard" @getCliente="getCliente" />
            </div>
            <div>
                <CardEntrega ref="modoEntrega" @getEntrega="getEntrega" />
            </div>
         
    
            
        </div>
      
   
  

   
 </Panel>
</div>
    
    
            
        
        

</template>
<style>
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

.p-card{
    box-shadow:none;
    font-size:14px;
}
.p-card-title{
    font-size:medium;
    font-weight: bolder !important;
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

.field {
    margin-bottom: 0rem;
    padding: 0rem;
}

.p-inputtext {
    padding: 0rem;
    font-size: 14px; 
}*/
</style>