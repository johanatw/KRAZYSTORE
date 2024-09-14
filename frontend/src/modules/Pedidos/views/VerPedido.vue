<script setup>
import CardCliente from "@/modules/Pedidos/components/CardCliente.vue";
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import CardEntrega from "@/modules/Pedidos/components/CardEntrega.vue";
import CardPago from "@/modules/Pedidos/components/CardPago.vue";
import MiCard from "@/modules/Pedidos/components/MiCard.vue";
import DetallePedido from '@/modules/Pedidos/components/DetallePedido.vue';
import Button from 'primevue/button';
import SearchCliente from "../components/SearchCliente.vue";
import { ref, onMounted } from "vue";
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import {CajaServices} from '@/services/CajaServices';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Card from "primevue/card";

import Tag from 'primevue/tag';

import router from '@/router';
const pedido = ref({ });
const selectedClient = ref();
const detallePedido = ref({ });
const mensaje = ref([]);
const modoEntrega = ref();
const estadoPedido = ref();
const estadoPago = ref();
const formaPago = ref();
const cardCliente = ref();
const cardEntrega = ref();
const searchCard = ref();
const entrega = ref();
const movimientos = ref([]);
const pagosAsociados = ref(false);
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
    CajaServices.obtenerMovimientosByPedido(router.currentRoute.value.params.id).then((data) => {

        movimientos.value = data.data;
        console.log("movimiento");
        console.log(movimientos.value.length);

});
    cardCliente.value.editable = false;
    cardEntrega.value.editable = false;
    console.log("onmountedVer");
    getPedido();
    console.log("despues de get function");
});

async function getPedido() {
    console.log("get function");
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {

    detalle.value.setDetalle(router.currentRoute.value.params.id, data.data.total, data.data.costoEnvio);
    getCliente(data.data.cliente);
    getEntrega(data.data.modoEntrega, data.data.costoEnvio);
    estadoPago.value = data.data.estadoPago;
    estadoPedido.value = data.data.estadoPedido;
    fecha.value = new Date(data.data.fecha);
    fechaCompleta.value = fecha.value.getDate() + "-"+ (fecha.value.getMonth()+1) + "-"+ fecha.value.getFullYear() ;

    pedido.value = data.data;

});
}

const modificarPedido = (id) =>{
    router.push({name: 'ModificarPedido', params: {id}});
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

const formatearNumero = (valor) =>{
    if(typeof(valor) == "number"){
        return valor.toLocaleString("de-DE");
    }

    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear()+' '+ fecha.getHours()+':'+fecha.getMinutes()+':'+fecha.getSeconds();
    return fechaFormateada;
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
    }

}


</script>
<template>
    

<div class="card flex p-fluid justify-content-center " >
 

    <Panel style=" position: relative; width: 80%;" >
        <template #header>
            <div class="flex align-items-center gap-2">
                <h3 class="font-bold">Pedido N° {{ pedido.id }}</h3>
            </div>
        </template>
        <template #icons>
        
            <Button  label="Modificar" @click="modificarPedido(router.currentRoute.value.params.id)" />
        
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
        </div>

        <div class="grid " >
            <div class="field col-12 md:col-12" >
                <Card >

                    <template #content>
                        <p class="m-0">
                            <div>
                                <div style=" display: flex; flex-direction: row;  ">
                                    Fecha: &nbsp;    
                                        {{ formatearNumero(pedido.fecha) }}
                                </div>
                                    
                                <div style="align-content: center; display: flex; flex-direction: row; ">
                                    Estado del pedido: &nbsp; 
                                    <div v-if="estadoPedido">
                                        <Tag  style="background-color: rgb(215, 227, 552); color: rgb(50, 111, 252); font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ estadoPedido.descripcion}}</Tag>
                                        
                                    </div> 
                                </div>
                                <div style=" display: flex; flex-direction: row; ">
                                    Estado de pago: &nbsp;
                                    <div v-if="estadoPago">
                                        <Tag :style="getSeverity(estadoPago.descripcion)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ estadoPago.descripcion}}</Tag>
                                    </div>
                                </div>
                            </div>
                        </p>
                    </template>
                </Card>
            </div>
            
            <div class="field col-12 md:col-6">
                <MiCard ref="cardCliente" :titulo="'Cliente'" :contenido="infoCliente" @edit="searchCard.showDialog()"/> 
            </div>  
            <div class="field col-12 md:col-6">
                <MiCard ref="cardEntrega" :titulo="'Entrega'" :contenido="infoEntrega" @edit="modoEntrega.showDialog()"/>
            </div>
            <div class="field col-12 md:col-12">
                <DetallePedido ref="detalle"/>
            </div>
            <div>
                <SearchCliente ref="searchCard" @getCliente="getCliente" />
            </div>
            <div>
                <CardEntrega ref="modoEntrega" @getEntrega="getEntrega" />
            </div>
            <div class="field col-12 md:col-12">
                <Card style="height: 100%;" >
                            <template #title>
            <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    Pagos Asociados
                </div>
        

            </div>
            
        </template>
                            <!---->
                            <template #content>
                                <div v-if="movimientos.length > 0">
                                    <DataTable :value="movimientos" >
                                    
                                    <Column field="concepto" header="Concepto" aria-sort="ascending">
                                  
                                </Column>
                                    <Column field="fecha" header="Fecha" aria-sort="ascending">
                                        <template #body="slotProps">
                                            <div >
                                                {{ formatearNumero(slotProps.data.fecha) }} 
                                            </div>
                                        </template> 
                                    </Column>
                                    
                                    <Column field="formaPago" header="Forma Pago" aria-sort="ascending">
                                     
                                    </Column>
                                    <Column field="monto" header="Monto" aria-sort="ascending">
                                        <template #body="slotProps">
                                            <div >
                                                {{ formatearNumero(slotProps.data.monto) }} 
                                            </div>
                                        </template> 
                                    </Column>
                                </DataTable>

                                    

                                    
                                </div>
                                <div v-else>
                                    El pedido aún no tiene pagos asociados.
                                </div>
                            </template>
                        </Card>
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
}*/
</style>