<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";

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
import { formatearFecha } from "@/utils/utils";
import Tag from 'primevue/tag';
import { ClienteServices } from "@/services/ClienteServices";
import { getEstadoPedidoVenta } from "@/utils/utils";

import Fieldset from 'primevue/fieldset';

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
    /*CajaServices.obtenerMovimientosByPedido(router.currentRoute.value.params.id).then((data) => {

        movimientos.value = data.data;
        console.log("movimiento");
        console.log(movimientos.value.length);

    });*/
    cardCliente.value.editable = false;
    //cardEntrega.value.editable = false;
    console.log("onmountedVer");
    getPedido();
    console.log("despues de get function");
});

async function getPedido() {
    console.log("get function");
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
    detalle.value.setDetalle(data.data.detalle, data.data.pedido.total, data.data.pedido.costoEnvio);
    getCliente(data.data.pedido.cliente?.persona);
    //getEntrega(data.data.pedido.modoEntrega, data.data.pedido.costoEnvio);
    pedido.value = data.data.pedido;

});
}

const modificarPedido = (id) =>{
    router.push({name: 'ModificarPedido', params: {id}});
}

const verPedidos = () =>{
    router.push({name: 'pedidos'});
}

const getSeverity = (estado) => {
  
  
  switch (estado) {
       case 'C':
           return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66);';

       case 'R':
           return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';

       case 'P':
           return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';

       default:
           return null;
   }
};

const getEstado = (estado) => {
  
  
  switch (estado) {
       case 'C':
           return 'Pagado';

       case 'R':
           return 'Parcial';

       case 'P':
           return 'Pendiente';
        case 'N':
           return 'Nuevo';

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
                <div class="flex" style="justify-content: end;">  
                    <Button  label="Atras"  style="margin-right: 1%;"  @click="verPedidos()" />
                    <Button  label="Modificar" @click="modificarPedido(router.currentRoute.value.params.id)" />
                </div>
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
                                Fecha: {{ formatearFecha(pedido.fecha)}}
                            </div> 
                            <div  >
                                Estado: {{getEstadoPedidoVenta(pedido.estadoPedido)}}
                            </div> 
                            <div >
                                Observaciones: 
                                <p class="m-0">
                                    {{ pedido.observaciones }}
                                </p>
                            </div>
                        </template>
                    </Card>
                </div>
                <div class="field col-12 md:col-6">
                    <MiCard ref="cardCliente" :titulo="'Cliente'" :contenido="infoCliente" @edit="searchCard.showDialog()"/> 
                </div>  
                <!-- <div class="field col-12 md:col-6">
                    <MiCard ref="cardEntrega" :titulo="'Entrega'" :contenido="infoEntrega" @edit="modoEntrega.showDialog()"/>
                </div>-->
                <div class="field col-12 md:col-12">
                    <DetallePedido ref="detalle"/>
                </div>
            </div>
        </Panel>
    </div>
</template>

<style>

</style>