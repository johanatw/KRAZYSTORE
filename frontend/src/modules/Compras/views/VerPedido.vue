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
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { ProductoServices } from '@/services/ProductoServices';
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import { getEstadoPedidoCompra } from "@/utils/utils";
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import DatePicker from 'primevue/datepicker';

// Variables 
const fecha = ref(new Date());
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
const infoProveedor= ref([{}]);

// Importaciones
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";
import {formatearNumero, formatearFecha} from '@/utils/utils';

// Configuración de confirmación y toast
const confirm = useConfirm();
const toast = useToast();

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

// Variables para el detalle del pedido
const detalleFacturar = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});


onMounted(() => {
    PedidoCompraServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data.pedido;
        detalle.value = data.data.detalle;
        proveedor.value = pedido.value.proveedor;
        mostrarCliente(proveedor.value);
    });
});

// Vista de pedidos
const vistaPedidos= () =>{
    router.push({name: 'pedidos_compras'});
}

// Función para mostrar información del proveedor
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

// Función para  modificar pedido
const modificarPedido = (id) => {
    router.push({name: 'modificar_pedido_compra', params: {id}});
}
</script>

<template>
    <div class="flex justify-content-center">
        <Panel style="position: relative; width: 80%;">
            <!-- Cabecera del panel con título -->
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Pedido N° {{ pedido.id }}</h3>
                </div>
            </template>
            
            <!-- Botones de acción en la cabecera -->
            <template #icons>
                <div class="flex" style="justify-content: end;">  
                    <Button label="Atras" style="margin-right: 1%;" @click="vistaPedidos()" />
                    <Button label="Modificar" @click="modificarPedido(pedido.id)" />
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
                            <div>Fecha: {{ formatearFecha(pedido.fecha)}}</div> 
                            <div>Estado: {{getEstadoPedidoCompra(pedido.estadoPedido)}}</div> 
                            <div>Observaciones:</div> 
                            <p class="m-0">{{ pedido.observaciones }}</p>
                        </template>
                    </Card>
                </div>
                
                <!-- Columna de información del proveedor -->
                <div class="field col-12 md:col-6">
                    <Card>
                        <template #title>
                            <div class="flex justify-content-between">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Proveedor
                                </div>    
                            </div>
                        </template>
                        <template #content>
                            <div class="flex flex-column align-options-start">
                                <div v-if="clienteSeleccionado">
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
                                            <Column class="col" field="producto.nombre" header="Nombre" aria-sort="none"></Column>
                                            <Column class="col" field="costoCompra" header="Precio" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid">
                                                        {{ slotProps.data.costoCompra.toLocaleString("de-DE") }}
                                                    </div> 
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantidad" header="Uds." aria-sort="none"></Column>
                                            <Column class="col" field="subTotal" header="Sub Total" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                                                        <label for="subtotal"> {{ (slotProps.data.subTotal).toLocaleString("de-DE") }}</label>
                                                    </div>
                                                </template>
                                            </Column>
                                        </DataTable>
                                    </div>
                                </div>
                                
                                <!-- Resumen de totales -->
                                <div class="grid" style="margin-top: 1rem;">
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px;">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px; font-weight: bold; font-size: 16px;">
                                            Total: 
                                        </div>
                                        <div class="field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px; font-weight: bold; font-size: 16px;">
                                            {{ formatearNumero(pedido.total) }} Gs.
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
</style>