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
const detalleRecepcionar = ref();
const error = ref(false);
const facturaId = ref();
const opciones = ref(['Casi','Entre']);
const infoProveedor= ref([{}])

// Importaciones
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";
import {CompraServices} from "@/services/CompraServices";

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

// Referencias para detalles de recepción
const detallesFacturasRecepcionar = ref([]);
const detalleRecepcion = ref([]);
const detalle= ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const total = ref(0);
const pedido = ref({});
const proveedor = ref({});
const compraNacional = ref(false);
const isCompraInternacional = ref(false);
const compra = ref({});


onMounted(() => {
    let idsFacturasRecepcionar = JSON.parse(sessionStorage.getItem('facturasRecepcionar'));
    console.log(idsFacturasRecepcionar);
    RecepcionServices.getDetallesFacturasRecepcionar(idsFacturasRecepcionar).then((data)=>{
        console.log(data.data);
        detalleRecepcion.value = data.data;
    });
});

// Función para mostrar información del cliente
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

// Función para verificar si es compra internacional
const esCompraInternacional = (tipoProveedor) =>{
    if (tipoProveedor == 'Extranjero') {
        return true;
    }
    return false;
}

// Función para la vista de pedidos
const verPedidos = () =>{
    router.push({name: 'pedidos_compras'});
}

// Función para validar el formulario
const validarForm = () => {
    mensaje.value = [];
    error.value = false;
    detalleRecepcionar.value = detalleRecepcion.value?.filter(d => d.cantRecepcionado > 0);
    if (detalleRecepcionar.value.length < 1) {
        error.value = true;
        mensaje.value.push("No se ingresó ninguna cantidad para recepcionar");
    } 
    modificarPedido();
}

// Función para mostrar mensaje de error
const showError = (message) => {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: message,
      life: 3000
    });
};
  
// Función para mostrar mensaje de éxito
const showSuccess = (message) => {
    toast.add({
      severity: 'success',
      summary: 'Éxito',
      detail: message,
      life: 3000
    });
};

// Función para modificar el pedido
const modificarPedido = () => {
    if (!error.value){
        let idsCompras = detalleRecepcionar.value.map(d => d.detalleCompra.idCompra);
        let fechaAnticipo = new Date();
        let compraAsociada = compra.value.id != null ? compra.value : null;
        let ant = {idPedido: pedido.value.id, fecha: fechaRecepcion.value, compra: compraAsociada};
        
        console.log(ant);
        console.log(detalleRecepcion.value);

        let anticipoCreationDTO = {recepcion: ant, detalle: detalleRecepcionar.value, idsCompras: idsCompras };
        RecepcionServices.registrarRecepcion(anticipoCreationDTO).then((data)=> {
            console.log("saveanticipothen");
            sessionStorage.removeItem('facturasRecepcionar');
            console.log("data");
            let id = data.data.id;
            showSuccess('Recepcion creado correctamente');
            verPedidos();
        });
    }
}

// Función para ver recepción
const verRecepcion = (id) =>{
    router.push({name: 'ver_recepcion', params: {id}});
}

// Función para volver a la vista de pedidos
const vistaPedidos= () =>{
    router.push({name: 'pedidos_compras'});
}
</script>

<template>
    <div class=" flex justify-content-center " >
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Nueva Recepción</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" @click="vistaPedidos()" />
                        <Button  label="Guardar" @click="validarForm()" />
                    </div>  
                </div>
            </template>
            <div class="contenedor" >
                <!-- Mensajes de error -->
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
                <!-- Sección de información general -->
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
                                Fecha: <DatePicker dateFormat="dd/mm/yy" v-model="fechaRecepcion" showIcon iconDisplay="input" />
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
                                        <DataTable class="tablaCarrito" ref="dt" :value="detalleRecepcion" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
                                            <Column  class="col" field="detalleCompra.producto.nombre" header="Nombre" aria-sort="none" ></Column>
                                            <Column  class="col" field="detallePedido.cantSolicitada" header="Facturado" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    {{ slotProps.data.detalleCompra.cantidad}}  
                                                    </div> 
                                                </template>
                                            </Column>
                                            <Column  class="col" field="cantidad" header="Recepcionado" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    {{ slotProps.data.detalleCompra.cantRecepcionada}}  
                                                    </div> 
                                                </template>
                                            </Column>
                                            <Column  class="col" field="cantidad" header="Pendiente" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    {{ slotProps.data.detalleCompra.cantPendiente }}  
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column  class="col" field="cantRecepcionado" header="Recibido" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div v-if="slotProps.data.detalleCompra.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    <InputNumber fluid class="inpCant" v-model="slotProps.data.cantRecepcionado" :min="0" :max="slotProps.data.detalleCompra.cantPendiente" inputId="minmax-buttons" mode="decimal" showButtons />
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" field="cantDañada" header="Aceptar" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div v-if="slotProps.data.detalleCompra.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    <InputNumber fluid class="inpCant" v-model="slotProps.data.cantAceptada" :min="0" :max="slotProps.data.cantRecepcionado" inputId="minmax-buttons" mode="decimal" showButtons />
                                                    </div> 
                                                </template>
                                            </Column>
                                            <Column  class="col" field="cantDañada" header="Rechazar" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div v-if="slotProps.data.detalleCompra.cantPendiente>0" class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
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