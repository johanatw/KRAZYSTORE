<script setup>
// Importaciones
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Card from "primevue/card";
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Calendar from "primevue/calendar";
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Panel from 'primevue/panel';
import DatePicker from "primevue/datepicker";
import { ProveedorServices } from '@/services/ProveedorServices';
import { RecepcionServices } from "@/services/RecepcionServices";
import { ProductoServices } from '@/services/ProductoServices';
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import {PersonaServices} from '@/services/PersonaServices';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import {PedidoCompraServices} from "@/services/PedidoCompraServices";
import router from '@/router';
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

// Variables 
const map = ref();
const direccion = ref({});
const selectedCliente = ref();
const clienteDialog = ref(false);
const personaCreationDTO = ref({});
const submitted = ref(false);
const clienteSeleccionado = ref(false);
const mensaje = ref([]);
const fechaRecepcion = ref(new Date());
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
const infoProveedor= ref([{}]);
const detalleRecepcion = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});
const compraNacional = ref(false);

// Configuración de confirmación y toast
const confirm = useConfirm();
const toast = useToast();

// Función para mostrar mensaje de confirmación
const message = (m) => {
    let id = m.id;
    let nro = m.nroFactura;
    confirm.require({
        group: 'headless',
        header: '#'+ nro,
        message: 'Se ha generado la Factura',
        accept: () => {
            router.push({name: 'verFactura', params: {id}});
        },
        reject: () => {
            router.push({name: 'ventas'});
        },
    });
};


onMounted(() => {
    PedidoCompraServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data.pedido;
        detalle.value = data.data.detalle;
        fechaRecepcion.value = pedido.value.fecha;
        proveedor.value = pedido.value.proveedor;
        
        // Determinar si es compra nacional o internacional
        if (data.data.pedido?.proveedor.tipo.descripcion == 'Extranjero') {
            compraNacional.value = false;
        } else { 
            compraNacional.value = true;
        } 
        
        mostrarCliente(proveedor.value);

        // Preparar detalle de recepción
        detalle.value.forEach(e=> {
            let d = {};
            d.detallePedido = e;
            d.cantRecepcionado = 0;
            d.cantRechazada = 0;
            d.cantAceptada = 0;
            
            // Calcular cantidad pendiente según tipo de compra
            if (proveedor.value.tipo.descripcion == 'Extranjero') {
                d.cantPendiente = e.cantFacturada - e.cantRecepcionada;
            } else { 
                d.cantPendiente = e.cantSolicitada - e.cantRecepcionada;
            } 
            
            detalleRecepcion.value.push(d);
        });
   });
});

// Función para mostrar información del proveedor
const mostrarCliente = (proveedor) =>{
    let valor;
    if(proveedor.descripcion!=null){
        valor={valor: proveedor.descripcion};
        infoProveedor.value.push(valor);
    }

    if(proveedor.telefono!=null){
        valor={valor: proveedor.telefono};
        infoProveedor.value.push(valor);
    }
    
    clienteSeleccionado.value = true;
}

// Función para modificar pedido - recepción
const modificarPedido = (id) => {
    if (!error.value){
        let fechaAnticipo = new Date();
        let ant = {idPedido: pedido.value.id, fecha: fechaRecepcion.value};
        
        let anticipoCreationDTO = {recepcion: ant, detalle: detalleRecepcion.value};
        RecepcionServices.registrarRecepcion(anticipoCreationDTO).then((data)=> {
            let id = data.data.id;
            verRecepcion(id);
        });
    }
}

// Función para ver recepción
const verRecepcion = (id) =>{
    router.push({name: 'ver_recepcion', params: {id}});
}

// Función para volver a lista de pedidos
const vistaPedidos= () =>{
    router.push({name: 'pedidos_compras'});
}
</script>

<template>
    <div class="flex justify-content-center">
        <!-- Panel principal -->
        <Panel style="position: relative; width: 80%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Recepcionar Pedido N° {{ pedido.id }}</h3>
                </div>
            </template>
            
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button label="Cancelar" style="margin-right: 1%;" @click="vistaPedidos()" />
                        <Button label="Guardar" @click="modificarPedido(pedido.id)" />
                    </div>  
                </div>
            </template>
            
            <!-- Sección de mensajes de error -->
            <div class="contenedor">
                <div v-if="error" style="background-color: rgb(242, 222, 222); border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
                    <ul>
                        <li v-for="msg in mensaje" style="list-style: none;">
                            <a style="color: rgb(173, 89, 86);">{{ msg }}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="grid">
                <!-- Sección información general -->
                <div class="field col-12 md:col-6">
                    <Card>
                        <template #title>
                            <div class="flex justify-content-between">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Información General
                                </div>    
                            </div>
                        </template>
                        <template #content>
                            <div class="field">
                                Fecha: <DatePicker dateFormat="dd/mm/yy" v-model="fechaRecepcion" showIcon iconDisplay="input" />
                            </div> 
                        </template>
                    </Card>
                </div> 
                
                <!-- Sección de productos -->
                <div class="col-12">
                    <Card>
                        <template #title>
                            <div class="flex justify-content-between">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Productos
                                </div>
                            </div>
                        </template>
                        
                        <template #content>
                            <div>
                                <div class="card" style="width: 100%;">
                                    <div class="flex card-container" style="width: 100%;">
                                        <!-- Tabla de productos -->
                                        <DataTable class="tablaCarrito" ref="dt" :value="detalleRecepcion" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
                                            <!-- Columnas de la tabla -->
                                            <Column class="col" field="detallePedido.producto.nombre" header="Nombre" aria-sort="none"></Column>
                                            
                                            <Column v-if="compraNacional" class="col" field="detallePedido.cantSolicitada" header="Solicitado" aria-sort="none">
                                            </Column>
                                            
                                            <Column v-else class="col" field="detallePedido.cantFacturada" header="Facturado" aria-sort="none">
                                            </Column>
                                            
                                            <Column class="col" field="cantidad" header="Recepcionado" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{ slotProps.data.detallePedido.cantRecepcionada}}  
                                                    </div> 
                                                </template>
                                            </Column>
                                            
                                            <Column class="col" field="cantidad" header="Pendiente" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{ slotProps.data.cantPendiente }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            
                                            <Column class="col" field="cantRecepcionado" header="Recibido" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div v-if="slotProps.data.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        <InputNumber fluid class="inpCant" v-model="slotProps.data.cantRecepcionado" :min="0" :max="slotProps.data.cantPendiente" inputId="minmax-buttons" mode="decimal" showButtons />
                                                    </div>  
                                                </template>
                                            </Column>
                                            
                                            <Column class="col" field="cantDañada" header="Aceptar" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div v-if="slotProps.data.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        <InputNumber fluid class="inpCant" v-model="slotProps.data.cantAceptada" :min="0" :max="slotProps.data.cantRecepcionado" inputId="minmax-buttons" mode="decimal" showButtons />
                                                    </div> 
                                                </template>
                                            </Column>
                                            
                                            <Column class="col" field="cantDañada" header="Rechazar" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div v-if="slotProps.data.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{slotProps.data.cantRechazada = slotProps.data.cantRecepcionado - slotProps.data.cantAceptada}}
                                                    </div> 
                                                </template>
                                            </Column>
                                        </DataTable>
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
</style>