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
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import DatePicker from "primevue/datepicker";

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
const pedidoNacional = ref(false);
const infoProveedor= ref([{}])

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

// Variables para el detalle de recepción
const detalleFacturar = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});


onMounted(() => {
    RecepcionServices.getRecepcion(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data.recepcion;
        fechaRecepcion.value = new Date(pedido.value.fecha);
        detalle.value = data.data.detalle;
        console.log(pedido.value);
        console.log(detalle.value);

        // Calcular cantidades pendientes
        detalle.value.forEach(element => {
            element.cantPendiente = element.detallePedido.cantFacturada - element.detallePedido.cantRecepcionada + element.cantRecepcionado;
        });
   });
});

// Mostrar información del proveedor
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

// Validar formulario antes de guardar
const validarForm = () => {
    mensaje.value = [];
    error.value = false;
    const productosRecepcionados = detalle.value?.filter(d => d.cantRecepcionado > 0)
    if (productosRecepcionados.length < 1) {
            error.value = true;
            mensaje.value.push("No se ingresó ninguna cantidad para recepcionar");
    } 
    
    modificarPedido();
}

// Modificar pedido de recepción
const modificarPedido = () => {
    if (!error.value){
        let fechaAnticipo = new Date();
        pedido.value.fecha = fechaRecepcion.value;
        console.log(detalle.value);

        let anticipoCreationDTO = {recepcion: pedido.value, detalle: detalle.value};
        RecepcionServices.modificarRecepcion(pedido.value.id, anticipoCreationDTO).then((data)=> {
            console.log("saveanticipothen");
            console.log("data");
            let id = data.data.id;
            verRecepcion(id);
        } );
    }
}

// Vista de recepción
const verRecepcion = (id) =>{
    console.log("veranticipofuncion");
    router.push({name: 'ver_recepcion', params: {id}});
}
</script>

<template>
    <div class=" flex justify-content-center " >
        <!-- Panel principal -->
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Recepción N° {{ pedido.id }}</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" @click="verRecepcion(pedido.id)" />
                        <Button  label="Guardar" @click="validarForm()" />
                    </div>  
                </div>
            </template>
            <div class="contenedor" >
                <div v-if="error" style="background-color: rgb(242, 222, 222); border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
                    <ul>
                        <li v-for="msg in mensaje" style="list-style: none;">
                        <a style="color: rgb(173, 89, 86);">{{ msg }}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="grid " >
                <!-- Información general -->
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
                            <div class="field" >
                                Fecha: <DatePicker dateFormat="dd/mm/yy"  v-model="fechaRecepcion" showIcon iconDisplay="input" />
                            </div> 
                        </template>
                    </Card>
                </div> 
                
                <!-- Sección de productos -->
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
                                        <!-- Tabla de productos -->
                                        <DataTable class="tablaCarrito" ref="dt" :value="detalle" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
                                            <Column class="col" field="detallePedido.producto.nombre" header="Nombre" aria-sort="none" ></Column>
                                            <Column class="col" field="detallePedido.cantSolicitada" header="Facturado" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    {{ slotProps.data.detallePedido.cantFacturada }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantidad" header="Pendiente de recepción" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    {{ slotProps.data.cantPendiente }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantRecepcionado" header="Recibido" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:12lvb  !important; ">
                                                        <InputNumber fluid class="inpCant" v-model="slotProps.data.cantRecepcionado" :min="1" :max="slotProps.data.cantPendiente" inputId="minmax-buttons" mode="decimal" showButtons />
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantDañada" header="Aceptar" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:12lvb  !important; ">
                                                        <InputNumber fluid class="inpCant" v-model="slotProps.data.cantAceptada" :min="0" :max="slotProps.data.cantRecepcionado" inputId="minmax-buttons" mode="decimal" showButtons />
                                                    </div> 
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantDañada" header="Rechazar" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:12lvb  !important; ">
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