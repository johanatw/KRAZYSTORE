<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Card from "primevue/card";
import { ProveedorServices } from '@/services/ProveedorServices';
import Button from 'primevue/button';
import Calendar from "primevue/calendar";
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { CompraServices } from "@/services/CompraServices";
import { ProductoServices } from '@/services/ProductoServices';
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';

import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
const map = ref();
const direccion = ref({});
const selectedCliente = ref();
const clienteDialog = ref(false);
const personaCreationDTO = ref({});
const submitted = ref(false);
const clienteSeleccionado = ref(false);
const mensaje = ref([]);
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
    
}]);

const iva5 = ref(0);
const iva10 = ref(0);

//router.currentRoute.value.params.id

import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";
import {formatearNumero, formatearFecha} from '@/utils/utils';

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

const detalleFacturar = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});

onMounted(() => {
    CompraServices.getCompra(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data.compra;
        detalle.value = data.data.detalle;
        calcularIva();
        console.log(detalle.value);
       proveedor.value = pedido.value.proveedor;
       mostrarCliente(proveedor.value);
   });



});


const mostrarCliente = (proveedor) =>{
    let valor;
    if(proveedor.descripcion!=null){
        valor={valor: proveedor.descripcion};
        infoProveedor.value.push(valor);
    }

    if(proveedor.tipo!=null){
        valor={valor: 'Tipo: '+ proveedor.tipo.descripcion};
        infoProveedor.value.push(valor);
    }

    if(proveedor.ruc!=null){
        valor={valor: 'RUC: '+ proveedor.ruc};
        infoProveedor.value.push(valor);
    }

    if(proveedor.telefono!=null){
        valor={valor: 'Telefono: '+ proveedor.telefono};
        infoProveedor.value.push(valor);
    }
    
    clienteSeleccionado.value = true;
}

const modificarPedido = (id) => {
    router.push({name: 'modificar_compra', params: {id}});
  
  }

  const puedeModificar = () => {
    return !isPagado(pedido.value.estado);
  }

  const isPagado = (estado) => {
  
  switch (estado) {
       case 'C':
           return true;
       default:
           return false;
   }
};

const vistaCompras= () =>{
    router.push({name: 'compras'});
}

const calcularIva = () =>{
   iva5.value = calcularIva5();
   iva10.value = calcularIva10();
   montoIva.value = iva5.value + iva10.value;
    
}

const calcularIva5 = () => {
    console.log(detalle.value);
  return detalle.value
  .filter(item => item.ivaAplicado.porcentaje === 5) // Filtra los productos con IVA 5%
    .reduce((accu, item) => {
        console.log(item);
        console.log("item.subTotal1");
        console.log(item.subTotal);
        let porcentaje = item.ivaAplicado.porcentaje || 0;
        let base = item.ivaAplicado.base || 0;
        let factor_iva = porcentaje + base;

        if (factor_iva === 0) return accu; // Evita división por 0
        
        let iva = item.subTotal * porcentaje / factor_iva;
        if (!isFinite(iva)) return accu; // Evita errores matemáticos
        return accu + iva;
    }, 0); // Luego suma
};

const calcularIva10 = () => {
  return detalle.value
    .filter(item => item.ivaAplicado.porcentaje === 10) // Filtra 
    .reduce((acc, item) => {
        let factor_iva = item.ivaAplicado.porcentaje + item.ivaAplicado.base;
        return acc + ((item.subTotal * item.ivaAplicado.porcentaje / factor_iva) || 0)
    }, 0); // Luego suma
};

const calcularIvaImportacion = () => {
    return (pedido.value.gastosImportacion !== 0)? pedido.value.gastosImportacion /11 : 0;
};

</script>
<template>
    

<div class=" flex justify-content-center " >
    <Panel style=" position: relative; width: 80%;" >
        <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Compra N° {{ pedido.id }}</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Atras"  style="margin-right: 1%;" @click="vistaCompras()" />
                        <Button v-if="puedeModificar()" label="Modificar" @click="modificarPedido(pedido.id)" />
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
            
            <Card >
        <template #title>
            <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    Información General
                </div>    
         
            
            </div>
            
        </template>
        <template #content>
            <div  >
                
                Fecha: {{ formatearFecha(pedido.fecha)}}
            </div> 
            <div  >
                Timbrado: {{pedido.timbrado}}
            </div> 
            <div  >
                N° Factura: {{pedido.nroFactura}}
            </div> 
        </template>
    </Card>
            </div>
            
           <div class="field col-12 md:col-6">
            
            <Card >
        <template #title>
            <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    Proveedor
                </div>    
            </div>
            
        </template>
        <template #content>
        
            <div class="flex flex-column align-options-start">
            
                <div v-if="clienteSeleccionado" >
                    <p class="m-0">
                        <div v-for="v in infoProveedor">
                            {{ v.valor }}
                        </div>
                        
                    </p>
                    
                </div>
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
        <DataTable class="tablaCarrito" ref="dt" :value="detalle" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
         <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
         <Column class="col" field="costoCompra"  header="Costo" aria-sort="none" >
            <template #body="slotProps">
            <div class="flex-auto p-fluid" >
                {{  slotProps.data.costoCompra.toLocaleString("de-DE") }}
                  </div> 
            </template>
        </Column>
         
        <Column  class="col" field="cantidad" header="Uds." aria-sort="none">
         </Column>
         <Column  class="col" field="cantidad" header="IVA" aria-sort="none">
                <template #body="slotProps">
                    <div class="flex-auto p-fluid" style="max-width:15lvb  !important; ">
                        <label for="subtotal"> {{  (slotProps.data.ivaAplicado.porcentaje) }}%</label>
                    </div>  
                </template>
                
            </Column>
  
         <Column  class="col" field="subTotal" header="Sub Total" aria-sort="none" >
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                     <label for="subtotal"> {{  (slotProps.data.subTotal).toLocaleString("de-DE") }}</label>
                  </div>
            </template>
         </Column>
     </DataTable>
   </div>
 </div>
                                <div class="grid" style="margin-top: 1rem;">
                                                               
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; font-weight: bold; font-size: 16px;">
                                            Total: 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; font-weight: bold; font-size: 16px;" >
                                            {{ formatearNumero(pedido.total) }} Gs.
                                           
                                        </div>

                                    </div>  
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            IVA 5%: 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ ( Math.round(iva5)).toLocaleString("de-DE") }} Gs.
                                        </div>
                                    </div>   
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            IVA 10%: 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ ( Math.round(iva10)).toLocaleString("de-DE") }} Gs.
                                        </div>
                                    </div>  
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            Total IVA: 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ ( Math.round(montoIva)).toLocaleString("de-DE") }} Gs.
                                        </div>
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