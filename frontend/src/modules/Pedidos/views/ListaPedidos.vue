<script setup>
//Importaciones
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import {PedidoServices} from '@/services/PedidoServices';
import { CajaServices } from '@/services/CajaServices';
import Panel from 'primevue/panel';
import InputIcon from 'primevue/inputicon';
import Tooltip from 'primevue/tooltip';
import { TimbradoServices } from '@/services/TimbradoServices';

import router from '@/router';

import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';
import Card from 'primevue/card';
import ConfirmDialog from 'primevue/confirmdialog';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import IconField from 'primevue/iconfield';
import SplitButton from 'primevue/splitbutton';
const visibleDeleteDialog = ref(false);
const pedidos = ref();
const expandedRows = ref({});

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import Toast from 'primevue/toast';
import { formatearNumero, formatearFecha, getEstadoPedidoVenta, getEstadoFacturaPedidoVenta } from '@/utils/utils'; 
import {VentaServices} from '@/services/VentaServices';

//Variables
const opciones = ref([{id: 1, descripcion: 'Facturar productos disponibles en stock.'},
                    {id: 2, descripcion: 'Registrar anticipo para productos no disponibles en stock.'}]);
const confirm = useConfirm();
const confirm3 = useConfirm();
const toast = useToast();
const dialogAnticipo = ref(null);
const selectedOpcion = ref();
const idPedidoSelected = ref();
const idAnticipoGuardado = ref();
const productosStockInsuficiente = ref(false);
const productosParaFacturar = ref(false);
const visiblePedidoFacturadoDialog = ref(false);
const detallePedido = ref();
const prepararPedidoDialog = ref(false);
const pedidoPreparar = ref({});
const facturasPedido = ref([]);
const seleccionarFacturaDialog = ref(false);
const facturaPreparar = ref({});

const messageError = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'errorEliminarPedido',
        header: 'Ocurrio un error',
        message: msg,

        accept: () => {
            //getDetalle();
        },
    });
};

const messageAviso = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'info',
        message: msg,

        accept: () => {
            //getDetalle();
        },
    });
};

const getSeverity = (estado) => {
    switch (estado) {
        case 'N':
            return 'background-color: rgb(255, 210, 218); color: rgb(234, 85, 154);'+
            'font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;';

        case 'F':
            return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);'+
            'font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;';

        case 'R':
            return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);'+
            'font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;';

        default:
            return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);'+
            'font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;';
    }
};

const isNuevo = (estado) => {
    switch (estado) {
        case 'N':
            return true;
        default:
            return false;
    }
};

//Si exiten productos facturados
const existenProductosFacturados = (estado) => {
    switch (estado) {
        case 'F':
            return true;
        case 'R':
            return true;
        default:
            return false;
    }
};

//Pedido preparado
const pedidoPreparado = (estado) => {
    console.log(estado);
    switch (estado) {
        case 'P':
            return true;
        default:
            return false;
    }
};

//Total facturado
const isTotalFacturado = (estado) => {
    switch (estado) {
        case 'FACTURADO':
            return true;
        default:
            return false;
    }
};

//Preparar pedido
const prepararPedido = async () => {
    let id = facturaPreparar.value.venta?.id;
    console.log(id);
    router.push({name: 'preparar_pedido', params: {id}});
};

//Seleccionar facturas
const seleccionarFacturas = async (id) => {
    try {
        console.log(id);
        const { data } = await VentaServices.obtenerFacturasByPedido(id);
        facturasPedido.value = data;
        console.log(facturasPedido.value);
        seleccionarFacturaDialog.value = true;
    } catch (error) {
        showError('Error al obtener facturas');
    }
};

const verificarStockProductos = () => {
    console.log(detallePedido.value);
    console.log("length");
    console.log(detallePedido.value[0]);
    
    for (let i = 0; i < detallePedido.value.length; i++) {
        if (detallePedido.value[i].cantidad <= detallePedido.value[i].producto.cantStock && detallePedido.value[i].cantidad > detallePedido.value[i].cantidadFacturada) { 
            productosParaFacturar.value = true;
            break; 
        }
    }

    for (let i = 0; i < detallePedido.value.length; i++) {
        if (detallePedido.value[i].cantidad > detallePedido.value[i].producto.cantStock && detallePedido.value[i].cantidad > detallePedido.value[i].cantidadFacturada) { 
            productosStockInsuficiente.value = true;
            break; 
        }
    }
    
    if (productosParaFacturar.value && !productosStockInsuficiente.value) {
        facturarPedido();
    }

    if(!productosParaFacturar.value && !productosStockInsuficiente.value){
        messageAviso("Todos los productos de este pedido ya han sido facturados");
    }
};

const confirm2 = (id) => {
    confirm.require({
        message: 'Eliminar el pedido '+ id + '?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Eliminar',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: () => {
            deletePedido(id);
        },
    });
};

const showTemplate = () => {
    confirm.require({
        group: 'headless',
        header: 'Anticipo Guardado',
        message: 'Please confirm to proceed.',
        accept: () => {
            getPedidos();
        },
        reject: () => {
            router.push({name: 'anticipos'});
        }
    });
};

onMounted(() => {
    getPedidos();
});

const getPedidos = async () => {
    try {
        const response = await PedidoServices.getPedidos();
        pedidos.value = response.data;
    } catch (error) {
        //alert(error);
    }
};

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const addPago = () => {
    let id = idPedidoSelected.value;
    if (selectedOpcion.value.id === 2 ) {
        router.push({name: 'AddPago', params: {id}});
    }
}

const showDialogPagoGuardado = (idAnticipo) => {
    idAnticipoGuardado.value = idAnticipo;
    showTemplate();
}

const cancelar = () => {
    visible.value = false;
    selectedOpcion.value = null;
}

const verPedido = (id) => {
    router.push({name: 'VisualizarPedido', params: {id}});
}

const facturarPedido = async (id) => {
    const response = await TimbradoServices.obtenerTimbradoVigente();
    console.log(response.data);
    if (response.data.timbrado != null) {
        router.push({name: 'facturar', params: {id}});
    } else {
        showError('No existe timbrado vigente para la factura');
    }
}

const cancelarPedido = async (pedido) => {
    console.log(pedido);
    const response = await PedidoServices.cancelarPedido(pedido.id, pedido);
    getPedidos();
    console.log(response.data);
    if (response.data != null) {
        
    } else {
        showError('No se pudo cancelar');
    }
}

const showError = (message) => {
    toast.add({
        severity: 'error',
        summary: 'Error',
        detail: message,
        life: 3000
    });
};

async function deletePedido(id) {
    const cantidad= 1;
    const index = pedidos.value.findIndex((loopVariable) => loopVariable.id === id);
    PedidoServices.deletePedido(id).then((response)=>{
        pedidos.value.splice(index,cantidad);
        toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
    })
}

const nuevoPedido = () => {
    router.push({name: 'nuevo_pedido'});
}

const prueba = (event) => {
    if (event == false) {
        reiniciarDialog();
    }
}

const closeDialog = () => {
    productosStockInsuficiente.value = false;
    reiniciarDialog();
}

const reiniciarDialog = () => {
    productosParaFacturar.value = false;
    detallePedido.value = [];
    idPedidoSelected.value = null;
}

const irAnticipos = () => {
    router.push({name: 'anticipos'});
}

const reload = () => {
    window.location.reload();
}
</script>

<template>
    <div class="flex p-fluid justify-content-center">
        <ConfirmDialog group="errorEliminarPedido">
            <template #container="{ message, acceptCallback }">
                <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                    <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                        <i class="pi pi-times text-4xl"></i>
                    </div>
                    <span class="font-bold text-2xl block mb-2 mt-4">{{ message.header }}</span>
                    <p class="mb-0">{{ message.message }}</p>
                    <div class="flex align-items-center gap-2 mt-4">
                        <Button label="Ok" @click="acceptCallback"></Button>
                    </div>
                </div>
            </template>
        </ConfirmDialog>
        <ConfirmDialog group="info">
            <template #container="{ message, acceptCallback }">
                <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                    <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                        <i class="pi pi-info text-4xl"></i>
                    </div>
                    <span class="font-bold block mb-2 mt-4" style="font-size: larger;" >{{ message.message }}</span>
                    <div class="flex align-items-center gap-2 mt-4">
                        <Button label="Ok" @click="acceptCallback"></Button>
                    </div>
                </div>
            </template>
        </ConfirmDialog>
        
        <Toast />
        <ConfirmDialog></ConfirmDialog>

        <Dialog v-model:visible="productosStockInsuficiente" modal @update:visible="prueba($event)" header="Edit Profile" :style="{ width: '40rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Productos sin stock disponible</h3>
                </div>
            </template>

            <div> 
                <div class="flex align-items-center mb-3">
                    Algunos productos del pedido no cuentan con stock suficiente. 
                    Solo se facturarán los disponibles.
                </div> 
                <div class="flex align-items-center mb-1" style="justify-content: start; font-weight: bold;" >
                    Detalles
                </div>
                
                <div>
                    <DataTable :value="detallePedido">
                        <Column field="producto.nombre" header="Producto" aria-sort="ascending"></Column>
                        <Column field="producto.cantStock" header="Stock" aria-sort="ascending">
                            <template #body="slotProps">
                                <div v-if="slotProps.data.cantidad > slotProps.data.producto.cantStock" style="color: red;">
                                    {{ slotProps.data.producto.cantStock }} 
                                </div>
                                <div v-else style="color: green;">
                                    {{ slotProps.data.producto.cantStock }} 
                                </div>
                            </template> 
                        </Column>
                        <Column field="cantidad" header="Cantidad Solicitada" aria-sort="ascending"></Column>
                        <Column field="cantidadFacturada" header="Cantidad Facturada" aria-sort="ascending"></Column>
                    </DataTable>
                </div>
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">  
                    <Button v-if="productosParaFacturar" label="Facturar disponibles" style="margin-right: 1%;" @click="facturarPedido()" />
                    <Button label="Cancelar" @click="closeDialog()" />
                </div>
            </template>
        </Dialog>

        <div>
            <!--Modal Seleccionar Factura-->
            <Dialog v-model:visible="seleccionarFacturaDialog" modal header="Edit Profile">
                <template #header>
                    <div class="flex align-items-center gap-2">
                        <h3 class="font-bold">Seleccionar Factura</h3>
                    </div>
                </template>
        
                <div class="contenedor"> 
                    <div>     
                        <DataTable v-model:selection="facturaPreparar" v-model:expandedRows="expandedRows" :value="facturasPedido" dataKey="venta.id" tableStyle="min-width: 30rem">
                            <Column selectionMode="single" headerStyle="width: 3rem"></Column>
                            <Column expander style="width: 5rem" />
                            <Column field="venta.nroFactura" header="Factura#"></Column>
                            <Column field="venta.fecha" header="Fecha">
                                <template #body="slotProps">
                                    {{ formatearFecha(slotProps.data.venta.fecha) }}   
                                </template>
                            </Column>
                            <template #expansion="slotProps">
                                <div>
                                    <table width="100%" align="center" border="1" style="border-collapse: collapse;">
                                        <thead>
                                            <tr>
                                                <th style="font-weight: bold;">Producto</th>
                                                <th style="font-weight: bold;">Cantidad Facturada</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="d in slotProps.data.detalle" :key="d.id">
                                                <td>{{ d.producto.nombre }}</td>
                                                <td>{{ d.cantidad }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </template>
                        </DataTable>
                    </div>
                </div>
                <template #footer>
                    <div class="card flex" style="justify-content: end;">  
                        <Button label="Cancelar" style="margin-right: 1%;" />
                        <Button label="Aceptar" @click="prepararPedido()" />
                    </div>
                </template>
            </Dialog>
        </div>
        
        <Panel style="position: relative; width: 90%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Pedidos</h3>
                </div>
            </template>
            
            <template #icons>
                <div class="flex align-items-center">
                    <Button icon="pi pi-plus" @click="nuevoPedido" style="margin-right: 1%;" />
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
            
            <div>
                <DataTable :value="pedidos" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
                    <template #empty> No hay registros para mostrar. </template>
                    <template #loading> Cargando. </template>

                    <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
                    <Column field="fecha" header="Fecha" aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            {{ formatearFecha(slotProps.data.fecha) }}   
                        </template>        
                    </Column>
                    <Column field="cliente" header="Cliente" aria-sort="ascending" sortable>
                        <template #body="slotProps">
                            {{ slotProps.data.cliente || ' ' }} 
                        </template>
                    </Column>
                    <Column field="telefono" header="Telefono" aria-sort="ascending" sortable></Column>
                    <Column field="estadoPedido" header="Estado Pedido" aria-sort="ascending" sortable>
                        <template #body="slotProps">
                            <Tag :style="getSeverity(slotProps.data.estadoPedido)">{{ getEstadoPedidoVenta(slotProps.data.estadoPedido)}}</Tag>
                        </template>
                    </Column>
                    <Column field="estadoPedido" header="Estado Facturación" aria-sort="ascending" sortable>
                        <template #body="slotProps">
                            <Tag :style="getSeverity(slotProps.data.estadoFacturacion)">{{ getEstadoFacturaPedidoVenta(slotProps.data.estadoFacturacion)}}</Tag>
                        </template>
                    </Column>
                
                    <Column field="total" header="Total Gs." aria-sort="ascending" sortable>
                        <template #body="slotProps">
                            {{ formatearNumero(slotProps.data.total) }}   
                        </template>
                    </Column>
                    <Column class="col" aria-sort="none">
                        <template #body="slotProps">
                            <div class="flex-auto p-fluid" v-if="slotProps.data.cantPreVenta > 0 && slotProps.data.estadoPedido!='C'" style="max-width:10lvb !important; font-size: 12px;">
                                <Tag style="font-size: 10px;" value="SinStock"></Tag>
                                {{ slotProps.data.cantPreVenta }}/{{ slotProps.data.totalItems }} item
                            </div>
                        </template>
                    </Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-eye" v-tooltip="'Ver detalles'" text rounded aria-label="Search" @click="verPedido(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-trash" v-tooltip="'Eliminar'" severity="danger" :disabled="!isNuevo(slotProps.data.estadoPedido)" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button v-tooltip="'Facturar'" :disabled="isTotalFacturado(slotProps.data.estadoFacturacion)" icon="pi pi-receipt" severity="info" text rounded aria-label="Cancel" @click="facturarPedido(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button v-tooltip="'Preparar'" icon="pi pi-box" severity="success" :disabled="pedidoPreparado(slotProps.data.estadoPedido) || !existenProductosFacturados(slotProps.data.estadoFacturacion)" text rounded aria-label="Cancel" @click="seleccionarFacturas(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-times" v-tooltip="'Cancelar'" :disabled="existenProductosFacturados(slotProps.data.estadoFacturacion)" text rounded aria-label="Search" severity="warn" @click="cancelarPedido(slotProps.data)" style="height: 2rem !important; width: 2rem !important;" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
</template>