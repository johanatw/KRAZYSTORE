<script setup>
// Importaciones
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Card from "primevue/card";
import { ProveedorServices } from '@/services/ProveedorServices';
import { RecepcionServices } from "@/services/RecepcionServices";
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { ProductoServices } from '@/services/ProductoServices';
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import Calendar from "primevue/calendar";
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import { formatearFecha, getEstadoRecepcion } from "@/utils/utils";

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

// Importaciones
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";

// Configuración de confirmación y toast
const confirm = useConfirm();
const toast = useToast();
const fecha = ref(new Date());

// Función para mostrar mensaje de confirmación
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
        },
        reject: () => {
            router.push({name: 'ventas'});
        },
    });
};

// Variables para el detalle de la recepción
const detalleFacturar = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});
const compraNacional = ref(true);


onMounted(() => {
    RecepcionServices.getRecepcion(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        pedido.value = data.data.recepcion;
        detalle.value = data.data.detalle;
        fecha.value = new Date(pedido.value.fecha);
        detalle.value.forEach(element => {
            element.detalleCompra.cantPendiente = element.detalleCompra.cantidad - element.detalleCompra.cantRecepcionada;
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

// Función para verificar si está facturada
const isFacturada = (estado) => {
    switch (estado) {
        case 'F':
            return true;
        default:
            return false;
    }
};

// Función para modificar recepción
const modificarRecepcion = (id) => {
    router.push({name: 'modificar_recepcion', params: {id}});
}

// Función para modificar pedido 
const modificarPedido = (id) => {
    if (!error.value){
        let fechaAnticipo = new Date();
        console.log(detalle.value);

        let anticipoCreationDTO = {recepcion: pedido.value, detalle: detalle.value};
        RecepcionServices.modificarRecepcion(pedido.value.id, anticipoCreationDTO).then((data)=> {
            console.log("saveanticipothen");
            console.log("data");
            let id = data.data.id;
        } );
    }
}

// Vista de recepciones
const vistaRecepciones= () =>{
    router.push({name: 'recepciones'});
}
</script>

<template>
    <div class="flex justify-content-center">
        <Panel style="position: relative; width: 80%;">
            <!-- Cabecera del panel con título -->
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Recepción N° {{ pedido.id }}</h3>
                </div>
            </template>
            
            <!-- Botones de acción en la cabecera -->
            <template #icons>
                <div class="card flex" style="justify-content: end;">  
                    <Button label="Atras" style="margin-right: 1%;" @click="vistaRecepciones()" />
                    <Button v-if="!isFacturada(pedido.estado)" label="Modificar" @click="modificarRecepcion(pedido.id)" />
                </div>  
            </template>
            
            <!-- Mensajes de error -->
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
                <!-- Columna de información general -->
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
                            <div>Fecha: {{ formatearFecha(fecha)}}</div> 
                            <div>Estado: {{getEstadoRecepcion(pedido.estado)}}</div> 
                        </template>
                    </Card>
                </div>
                
                <!-- Columna de productos -->
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
                                <!-- Tabla de productos -->
                                <div class="card" style="width: 100%;">
                                    <div class="flex card-container" style="width: 100%;">
                                        <DataTable class="tablaCarrito" ref="dt" :value="detalle" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
                                            <Column class="col" field="detalleCompra.producto.nombre" header="Nombre" aria-sort="none"></Column>
                                            <Column class="col" field="detalleCompra.cantSolicitada" header="Facturado" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{ slotProps.data.detalleCompra.cantidad }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantidad" header="Pendiente de recepción" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{ slotProps.data.detalleCompra.cantPendiente }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantRecepcionado" header="Recibido" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{ slotProps.data.cantRecepcionado }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantDañada" header="Aceptado" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{ slotProps.data.cantAceptada }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantDañada" header="Rechazado" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                        {{ slotProps.data.cantRechazada }}  
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