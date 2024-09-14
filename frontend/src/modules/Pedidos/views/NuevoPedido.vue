<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import CardEntrega from "@/modules/Pedidos/components/CardEntrega.vue";
import CardPago from "@/modules/Pedidos/components/CardPago.vue";
import MiCard from "@/modules/Pedidos/components/MiCard.vue";
import Button from 'primevue/button';
import SearchCliente from "../components/SearchCliente.vue";
import { ref, onMounted } from "vue";
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import { ProductoServices } from "@/services/ProductoServices";
import router from '@/router';
const pedido = ref({ });
const selectedClient = ref();
const detallePedido = ref({ });
const mensaje = ref([]);
const modoEntrega = ref();
const formaPago = ref();
const cancelSubmit = ref(false);
const pedidoId = ref(0);
const cliente = ref();
const cardCliente = ref();
const cardEntrega = ref();
const searchCard = ref();
const productos= ref();
const ex=ref([]);
const entrega = ref();
const envioSelected = ref();
const error = ref(false);
const infoCliente = ref([{
    valor: "Cliente no seleccionado"
}])
const infoEntrega = ref([{
    valor: "Metodo de Entrega no seleccionado"
}])


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

const editCliente = () =>{
    searchCard.value.showDialog();
    if (selectedClient.value) {
        searchCard.value.showClient(selectedClient.value);
    }  
}

const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
}

const submit = () =>{

    if (!error.value) {
        
        console.log("pedidodetalle", productos.value.detalles);
        pedido.value.cliente = selectedClient.value;

        pedido.value.modoEntrega = entrega.value;
        if (pedido.value.modoEntrega && pedido.value.modoEntrega.descripcion === "Envío") {
            pedido.value.costoEnvio = envioSelected.value;
        }

        detallePedido.value = productos.value.detalles;
       // console.log("submitdetalle",);
        //ex.value = productos.value.existencias;
        pedido.value.total = productos.value.subTotal;
        PedidoServices.savePedido(pedido.value).then((response)=>{
            pedidoId.value = response.data.id;
            pedido.value.id = response.data.id;
           // detallePedido.value.pedido = pedido.value;
            PedidoServices.registrarDetallesPedido(pedido.value.id,detallePedido.value).then((response)=>{
                pedido.value={};

                console.log("pedidoid",pedidoId.value);
                verPedido(pedidoId.value);
               /* ProductoServices.modificarExistencias(detallePedido.value).then((response)=>{

                pedido.value={};

                console.log("pedidoid",pedidoId.value);
                verPedido(pedidoId.value);
            });*/
            });
        });
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

    <Panel style=" position: relative; width: 80%;" >
        <template #header>
            <div class="flex align-items-center gap-2">
                <h3 class="font-bold">Nuevo Pedido</h3>
            </div>
        </template>
        <template #icons>
                
            <Button  label="Guardar"  @click="validarForm" />
    
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
                <MiCard ref="cardCliente" :titulo="'Cliente'" :contenido="infoCliente" @edit="editCliente"/> 
            </div>  
            <div class="field col-12 md:col-6">
                <MiCard ref="cardEntrega" :titulo="'Entrega'" :contenido="infoEntrega" @edit="modoEntrega.showDialog()"/>
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