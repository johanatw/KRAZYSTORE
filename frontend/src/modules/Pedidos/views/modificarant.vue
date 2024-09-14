<script setup>
import CardCliente from "@/modules/Pedidos/components/CardCliente.vue";
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import CardEntrega from "@/modules/Pedidos/components/CardEntrega.vue";
import CardPago from "@/modules/Pedidos/components/CardPago.vue";

import Button from 'primevue/button';

import { ref, onMounted } from "vue";
import { PersonaServices } from '@/services/PersonaServices';
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import router from '@/router';
const res = ref();
const pedido = ref({ });
const detallePedido = ref({ });
const detalleRegistrar = ref([ ]);
const detalleEliminar = ref([ ]);
const detalleModificar = ref([ ]);
const mensaje = ref([]);
const modoEntrega = ref();
const formaPago = ref();
const cancelSubmit = ref(false);
const pedidoId = ref(0);
const cliente = ref();
const productos= ref();
const error = ref(false);
onMounted(() => {
    PedidoServices.getDetallePedido(router.currentRoute.value.params.id).then((data) => {
        productos.value.setDetalle(data.data);

    });
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        formaPago.value.setPago(data.data.formaPago);
        modoEntrega.value.setEntrega(data.data.modoEntrega, data.data.costoEnvio);
        cliente.value.setCliente(data.data.cliente);
        pedido.value = data.data;
        
        
    });

});

const validateUpdate = () =>{
    if (cliente.value.updateContact && cliente.value.updateFactura) {
        update(cliente.value.selectedPersona).then((response)=>{
            update(cliente.value.personaFactura).then((response)=>{
                submit();
            }).catch((e)=>showError(e.response.data.mensaje));
        }).catch((e)=>showError(e.response.data.mensaje));
    } else {
        if (cliente.value.updateContact) {
            update(cliente.value.selectedPersona).then((response)=>submit()).catch((e)=>showError(e.response.data.mensaje));
        }
        if (cliente.value.updateFactura) {
            update(cliente.value.personaFactura).then((response)=>submit()).catch((e)=>showError(e.response.data.mensaje));
        }
        console.log("despues de factura");
    }
    
}

const showError = (msg) =>{
    error.value = true;
    mensaje.value.push(msg);
    
}


const update = (client) =>{
          return PersonaServices.modificarPersona(client.id, client);
           }

const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
}

const compareDetalle = () =>{
    let index;
    PedidoServices.getDetallePedido(router.currentRoute.value.params.id).then((data) => {
        data.data.forEach(element => {
        index = productos.value.detalles.value.findIndex((loopVariable) => loopVariable.producto.id === element.producto.id );
      
        if (index >-1 && (element.cantidad != productos.value.detalles.value[index].cantidad) ) {
            detalleModificar.value.push(productos.value.detalles.value[index]);
        }else if (index == -1) {
            detalleEliminar.value.push(element.id);
        }   

    });
    productos.value.detalles.value.forEach(element => {
        index = data.data.findIndex((loopVariable) => loopVariable.producto.id === element.producto.id );
      
        if (index == -1) {
            detalleRegistrar.value.push(element);
        }
    });

        console.log(' detalleRegistrar.value',detalleRegistrar.value);
        console.log(' detalleEliminar.value',detalleEliminar.value);
        console.log('detalleModificar.value',detalleModificar.value);
  /*  console.log('detalleactual', productos.value.detalles.value);
    detallePedido.value.forEach(element => {
        index = productos.value.detalles.value.findIndex((loopVariable) => loopVariable.producto.id === element.producto.id );
        console.log('element.cantidad', element.cantidad);
            console.log('productos.value.detalles.value[index].cantidad', productos.value.detalles.value[index].cantidad);
       
        if (index >-1 && (element.cantidad != productos.value.detalles.value[index].cantidad) ) {
            console.log('element.cantidad', element.cantidad);
            console.log('productos.value.detalles.value[index].cantidad', productos.value.detalles.value[index].cantidad);
        }*/
            //submit();
    });
    
    //productos.value[index] = data.data;
}

const submit = () =>{

    if (!error.value) {
        compareDetalle();
            console.log("cancelvalue", cancelSubmit.value);
            pedido.value.cliente = cliente.value.selectedPersona;

        /*if (cliente.value.personaFactura) {
            pedido.value.datoFacturacion = cliente.value.personaFactura;
        } else {
            pedido.value.datoFacturacion = cliente.value.selectedPersona;
            
        }*/
        

        pedido.value.modoEntrega = modoEntrega.value.selectedFormaEntrega;
        if (pedido.value.modoEntrega.descripcion === "Envío") {
            pedido.value.costoEnvio = modoEntrega.value.costoEnvio;
        }else{
            pedido.value.costoEnvio = null;
        }
        
        pedido.value.formaPago = formaPago.value.selectedFormaPago;
    


        console.log("pedidocliente", pedido.value.cliente);
        detallePedido.value.detalle = detalleRegistrar.value;
        pedido.value.total = productos.value.subTotal;
            PedidoServices.modificarPedido(pedido.value.id, pedido.value).then((response)=>{
                    
                    detallePedido.value.pedido = pedido.value;
                    if (detalleEliminar.value.length > 0) {
                        PedidoServices.deleteDetallesByIds(detalleEliminar.value);
                    }
                    if (detalleModificar.value.length > 0) {
                        PedidoServices.modificarDetalle(pedido.value.id, detalleModificar.value);
                    }

                    if (detalleRegistrar.value.length > 0) {
                        PedidoServices.registrarDetallePedido(detallePedido.value);
                    }
                    verPedido(pedido.value.id);
                    
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
        /*if (cliente.value.personaFactura) {
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
                
            }*/
    } else {
        error.value = true;
            mensaje.value.push("Debe seleccionar un cliente");
    }

    if (productos.value.subTotal <1) {
        error.value = true;
        mensaje.value.push("Debe añadir productos al pedido");
    }
    
   /* if (cliente.value.updateContact || cliente.value.updateFactura) {
        
        validateUpdate();
            
                
            
            //submit();
        
    }else{
        submit();
    }*/
    submit();
    
    console.log(error.value);

    
    

    

    
    


}
</script>
<template>

<div class="card flex p-fluid justify-content-center " >
 

 <Panel style=" position: relative; width: 100%;" >
   <template #header>
     <div class="flex align-items-center gap-2">
         <h3 class="font-bold">Modificar Pedido N° {{ pedido.id }}</h3>
     </div>
   </template>
   <template #icons>
    <div class="card flex" style="justify-content: end;">
                
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;"  @click="verPedido(pedido.id)" />
                    <Button  label="Guardar" @click="validarForm" />
                </div>
                
                      
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
        <CardCliente ref="cliente"/>     
        <CardDetalle ref="productos"/>
        <CardEntrega ref="modoEntrega"/>
        <CardPago ref="formaPago"/>
        
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