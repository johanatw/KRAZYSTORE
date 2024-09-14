<script setup>


import Accordion from 'primevue/accordion';

import AccordionTab from 'primevue/accordiontab';

import AutoComplete from 'primevue/autocomplete';

import Button from 'primevue/button';

import RadioButton from 'primevue/radiobutton';
import Panel from 'primevue/panel';

import InputText from 'primevue/inputtext';
import PedidoResumen from '@/components/PedidoResumen.vue';

import Card from 'primevue/card';
import { ref, onMounted, onBeforeMount } from "vue";

import Dialog from 'primevue/dialog';
import {PedidoServices} from '@/services/PedidoServices';

import  DatosCliente  from "@/components/DatosCliente.vue";
import DatosFacturacion from '@/components/DatosFacturacion.vue';
import DetallePedidoVer from '@/components/DetallePedidoVer.vue';
import DatosEntrega from '@/components/DatosEntrega.vue';
import MetodoPago from '@/components/MetodoPago.vue';

import router from '@/router';
const pedido = ref({});
const cli = ref(null);
const clienteFactura = ref(null);
const detalle = ref(null);
const entrega = ref(null);
const pago = ref(null);
const resumen = ref(null);

const idCliente = ref();


onMounted(() => {
    getPedido();
    

});

async function getPedido() {
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        resumen.value.setPedido(data.data);
        cli.value.setCliente(data.data.cliente);
        clienteFactura.value.setCliente(data.data.datoFacturacion);
        entrega.value.setEntrega(data.data.modoEntrega, data.data.costoEnvio);
        pago.value.setModoPago(data.data.formaPago);
        detalle.value.setDetalle(router.currentRoute.value.params.id);
        pedido.value = data.data;
        
        
    });
}

</script>
<template>
<div class="card flex p-fluid justify-content-center " >
 

 <Panel style=" position: relative; width: 100%;" >
   <template #header>
     <div class="flex align-items-center gap-2">
         <h3 class="font-bold">Pedido N° {{ pedido.id }}</h3>
     </div>
   </template>
   <div class="contenedor" style="padding-left: 4%; padding-right: 4%;">
        
        <PedidoResumen ref="resumen" />
        <div class="grid">   
            <div class="col-12 md:col-6">
                <DatosCliente ref="cli"  />
                
            </div>
            <div class="col-12 md:col-6">
                <DatosFacturacion ref="clienteFactura"/>

            </div>
            <div class="col-12 md:col-12">
                <DetallePedidoVer ref="detalle"/>

            </div>
            <div class="col-12 md:col-6">
                <DatosEntrega ref="entrega"/>

            </div>
            <div class="col-12 md:col-6">
                <MetodoPago ref="pago" />

            </div>
        
        
        
        
        
        
    </div>

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
    font-weight: bolder;
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