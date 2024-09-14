<script setup>


import DetallePedido from './DetallePedido.vue'

import PedidoFormaPago from './PedidoFormaPago.vue';
import PedidoModoEntrega from './PedidoModoEntrega.vue';
import Button from 'primevue/button';
import PedidoCliente from './PedidoCliente.vue';
import { ref, onMounted } from "vue";
import { PersonaServices } from '../services/PersonaServices';
import Panel from 'primevue/panel';
import {PedidoServices} from '../services/PedidoServices';
import router from '@/router';
const pedido = ref({ });
const detallePedido = ref({ });
const mensaje = ref([]);
const modoEntrega = ref();
const formaPago = ref();

const pedidoId = ref(0);
const cliente = ref();
const productos= ref();
const error = ref(false);

const updateClient = (cliente) =>{
    PersonaServices.modificarPersona(cliente.id, cliente);
}


const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
}

const submit = () =>{

    if (!error.value) {
        if (cliente.value.updateContact) {
            updateClient(cliente.value.selectedPersona);
        } 
        
        if (cliente.value.updateFactura) {
            updateClient(cliente.value.personaFactura);
        }

        pedido.value.cliente = cliente.value.selectedPersona;

        if (cliente.value.personaFactura) {
            pedido.value.datoFacturacion = cliente.value.personaFactura;
        } else {
            pedido.value.datoFacturacion = cliente.value.selectedPersona;
            
        }
        

        pedido.value.modoEntrega = modoEntrega.value.selectedFormaEntrega;
        if (pedido.value.modoEntrega.descripcion === "Envío") {
            pedido.value.costoEnvio = modoEntrega.value.costoEnvio;
        }
        
        pedido.value.formaPago = formaPago.value.selectedFormaPago;
    
        

        console.log("pedidocliente", pedido.value.cliente);
        detallePedido.value.detalle = productos.value.detalles.value;
        pedido.value.total = productos.value.subTotal;
            PedidoServices.savePedido(pedido.value).then((response)=>{
                    pedidoId.value = response.data.id;
                    pedido.value.id = response.data.id;
                    detallePedido.value.pedido = pedido.value;
                    PedidoServices.registrarDetallePedido(detallePedido.value).then((response)=>{
                        pedido.value={};
                        verPedido(pedidoId.value);
                        
                    });
                  });
    }
}

const validarForm = (event) => {
    
    mensaje.value = [];
    error.value = false;
    if (cliente.value.selectedPersona) {
        if (!cliente.value.selectedPersona.telefono) {
            error.value = true;
            mensaje.value.push("Debe ingresar n° de contacto");
            
        }
        if (cliente.value.personaFactura) {
                if (!cliente.value.personaFactura.nroDoc) {
                    error.value = true;
                    mensaje.value.push("Debe ingresar n° de documento");
                    
                } 
                
            } else {
                console.log("4");
                if (!cliente.value.selectedPersona.nroDoc) {
                    error.value = true;
                    mensaje.value.push("Debe ingresar n° de documento");
                }
                
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
 

 <Panel style=" position: relative; width: 100%;" >
   <template #header>
     <div class="flex align-items-center gap-2">
         <h3 class="font-bold">Nuevo Pedido</h3>
     </div>
   </template>
   <template #icons>
        
    <Button  label="Guardar" @click="validarForm" />
    
      </template>
   <div class="contenedor" style="padding-left: 4%; padding-right: 4%;">
        <div v-if="error" style="background-color: rgb(242, 222, 222); 
        border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
            <ul>
                <li v-for="msg in mensaje" style="list-style: none;">
                <a style="color: rgb(173, 89, 86);">{{ msg }}</a>
                </li>
            </ul>
        </div>
        <PedidoCliente ref="cliente"/>     
        <DetallePedido ref="productos"/>
        <PedidoModoEntrega ref="modoEntrega"/>
        <PedidoFormaPago ref="formaPago"/>
        
        </div>
  

   
 </Panel>
</div>
    
    
            
        
        

</template>
<style>

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
</style>