<script setup>


import Accordion from 'primevue/accordion';

import AccordionTab from 'primevue/accordiontab';

import AutoComplete from 'primevue/autocomplete';

import Button from 'primevue/button';

import RadioButton from 'primevue/radiobutton';
import Panel from 'primevue/panel';

import InputText from 'primevue/inputtext';
import ResumenPedido from '@/modules/Pedidos/components/ResumenPedido.vue';

import Card from 'primevue/card';
import { ref, onMounted, onBeforeMount } from "vue";

import Dialog from 'primevue/dialog';
import {PedidoServices} from '@/services/PedidoServices';

import  DatosCliente  from "@/modules/Pedidos/components/DatosCliente.vue";
import DatosFacturacion from '@/modules/Pedidos/components/DatosFacturacion.vue';
import DetallePedido from '@/modules/Pedidos/components/DetallePedido.vue';
import DatosEntrega from '@/modules/Pedidos/components/DatosEntrega.vue';
import DatosPago from '@/modules/Pedidos/components/DatosPago.vue';

import router from '@/router';
const pedido = ref({});
const cli = ref(null);
const clienteFactura = ref(null);
const detalle = ref(null);
const entrega = ref(null);
const pago = ref(null);
const resumen = ref(null);

const idPedido= ref();


onMounted(() => {
    
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        idPedido.value = data.data.id;
        resumen.value.setPedido(data.data);
        cli.value.setCliente(data.data.cliente);
       // clienteFactura.value.setCliente(data.data.datoFacturacion);
        entrega.value.setEntrega(data.data.modoEntrega, data.data.costoEnvio);
        detalle.value.setDetalle(router.currentRoute.value.params.id);
        pedido.value = data.data;
        
        
    });

});

const modificarPedido = (id) =>{
    router.push({name: 'ModificarPedido', params: {id}});
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
   <template #icons>
        
        <Button  label="Modificar" @click="modificarPedido(router.currentRoute.value.params.id)" />
        
          </template>
   <div class="contenedor" style="padding-left: 4%; padding-right: 4%;">
        
        <ResumenPedido ref="resumen" />
        <div class="grid">   
            <div class="col-12 md:col-12">
        
                <DatosCliente ref="cli"  />
                
            </div>
            
            <div class="col-12 md:col-12">
                <DetallePedido ref="detalle"/>

            </div>
            <div class="col-12 md:col-6">
                <DatosEntrega ref="entrega"/>

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