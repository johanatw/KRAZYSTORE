<script setup>

import { ref, onMounted } from "vue";

import {EstadosServices} from '@/services/EstadosServices'
import {PedidoServices} from '@/services/PedidoServices'
const props = defineProps(['pedido']);
const fecha = ref();
const fechaCompleta = ref();


const costo = ref(0);
const selectedEstadoPedido = ref();
const selectedEstadoPago = ref();
const estadosPedido = ref([ ]);
const estadosPago = ref([ ]);



 const pedido = ref({});

const setPedido = (p) => {

    pedido.value = p;

    EstadosServices.obtenerEstadosByTipo('E').then((data) => {
        estadosPedido.value = data.data;
    });
    EstadosServices.obtenerEstadosByTipo('P').then((data) => {
        estadosPago.value = data.data;
    });
    console.log("resumen estado", pedido.value.estadoPedido);

    if (pedido.value.costoEnvio) {
            costo.value = pedido.value.costoEnvio.costo;
        }
        selectedEstadoPedido.value = pedido.value.estadoPedido;
        selectedEstadoPago.value = pedido.value.estadoPago;
        console.log("estadopedido", selectedEstadoPedido.value);
        
        fecha.value = new Date(pedido.value.fecha);
        fechaCompleta.value = fecha.value.getDate() + "-"+ (fecha.value.getMonth()+1) + "-"+ fecha.value.getFullYear() ;

}

const updateEstado = () => {
    pedido.value.estadoPedido = selectedEstadoPedido.value;
    pedido.value.estadoPago = selectedEstadoPago.value;
    PedidoServices.modificarPedido(pedido.value.id, pedido.value);

}

defineExpose({
    setPedido
});

</script>
<template>

    <div class="principal" style="margin-bottom: 2%;">
        <div  style="width: 100%;" >
        <div class="grid"  style="width: 100%;" >
            <div class="col-12 md:col-2" style="justify-content: center;align-content: center; display: flex; flex-direction: column; ">
                <h3 >Total</h3>
                <h3>{{ pedido.total }} Gs</h3>

            </div>
            <div class="col-12 md:col-2" style="justify-content: center;align-content: center; display: flex; flex-direction: column; ">
                <h3>Envío</h3>
                <h3>{{ costo }} Gs</h3>

            </div>
            <div class="col-12 md:col-2" style="justify-content: center;align-content: center; display: flex; flex-direction: column; " >
                <h3>Fecha</h3>
                <h3>{{ fechaCompleta }}</h3>

            </div>
            <div class="col-12 md:col-3" style="justify-content: center;align-content: center; display: flex; flex-direction: column; ">
                <h3>Estado del pedido</h3>
                <div class="formgrid grid"  >
                        <div class="field col-12 md:col-12" style="margin-bottom: 0%;" >
                            <select v-model="selectedEstadoPedido" id="estado" @change="updateEstado()" class="w-full text-base text-color surface-overlay border-1 border-solid surface-border border-round outline-none focus:border-primary" style="appearance: auto; height:28.06px" >
                               <option  v-for="estado in estadosPedido" :value="estado">{{ estado.descripcion }}</option>                            </select>

                        </div>   
                    </div>

            </div>
            <div class="col-12 md:col-3" style="justify-content: center;align-content: center; display: flex; flex-direction: column; ">
                <h3>Estado de pago</h3>
                <div class="formgrid grid">
                        <div class="field col-12 md:col-12" style="margin-bottom: 0%;">
                            <select v-model="selectedEstadoPago" id="estado" @change="updateEstado()" class="w-full text-base text-color surface-overlay border-1 border-solid surface-border border-round outline-none focus:border-primary" style="appearance: auto; height:28.06px">
                               <option  v-for="estado in estadosPago" :value="estado">{{ estado.descripcion }}</option>                            </select>

                        </div>   
                    </div>

            </div>
        </div>
        </div>
        
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
    
    justify-content: center;
   
    margin-left: 0% !important;
    margin-right: 0% !important;
    padding: 1%;
    background: #ffffff;
    color: #4b5563;
    box-shadow: 0 2px 1px -1px rgba(0, 0, 0, 0.2), 0 1px 1px 0 rgba(0, 0, 0, 0.14), 0 1px 3px 0 rgba(0, 0, 0, 0.12);
    border-radius: 6px;
    border: 1px solid #e5e7eb !important;
    border-top: 0 none !important;

 

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
</style>