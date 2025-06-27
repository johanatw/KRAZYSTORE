<script setup>
// Importaciones 
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Calendar from 'primevue/calendar';
import Card from "primevue/card";
import { ProveedorServices } from '@/services/ProveedorServices';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { ProductoServices } from '@/services/ProductoServices';
import { CompraServices } from "@/services/CompraServices";
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import { ref, onMounted } from "vue";
import Select from "primevue/select";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from "primevue/inputgroupaddon";
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import DatePicker from 'primevue/datepicker';

// Variables 
const map = ref();
const direccion = ref({});
const selectedCliente = ref();
const clienteDialog = ref(false);
const proveedor = ref({});
const proveedores = ref();
const compraNacional = ref(false);
const compraInternacional = ref(false);
const compraIndependiente = ref(true);
const personaCreationDTO = ref({});
const submitted = ref(false);
const clienteSeleccionado = ref(false);
const mensaje = ref([]);
const ciudades = ref([]);
const departamentos = ref([]);
const id = ref();
const documentos = ref([]);
const visible = ref(false);
const pedidoExtranjero = ref(false);
const pedido = ref();
const existePedido = ref(false);
const timbrado = ref();
const selectedOp = ref('Casi');
const productos = ref();
const servicioDialog = ref(false);
const filteredClientes = ref();
const error = ref(false);
const opciones = ref(['Casi','Entre']);
const infoEntrega = ref([{valor: "Retiro"}]);

// Tipos de proveedor
const tiposProveedor = ref([
  {id: 1, descripcion: 'Nacional'},
  {id: 2, descripcion: 'Extranjero'}
]);

// Importaciones para diálogos y notificaciones
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";
import { formatearNumero } from "@/utils/utils";

// Variables para facturación
const selectedProducts = ref();
const detallePedidoFacturar = ref();
const confirm = useConfirm();
const toast = useToast();
const gastosImportacion = ref(0);
const costoEnvio = ref(0);
const totalGravada = ref(0);
const totalExentas = ref(0);
const detalleFacturar = ref([]);
const subTotal = ref(0);
const iva0 = ({id: 3, base: 100, descripcion: 'IVA 0%', porcentaje: 0 });
const searched = ref(false);
const errorNoSePuedeFacturarPedido = ref(false);
const mensajeError = ref([]);
const servicios = ref([]);
const iva5 = ref(0);
const iva10 = ref(0);
const montoIva = ref(0);
const detalle = ref({});
const total = ref(0);
const nroFactura = ref();
const fechaCompra = ref(new Date());


onMounted(() => {
    getServicios();
    ProductoServices.obtenerProductos().then((data) => {
        productos.value = data.data;
        console.log("productosssss",productos.value);
    });
    
    ProveedorServices.obtenerProveedoresNacionalesProductos().then((data) => {
        proveedores.value = data.data;
    });

    TipoDocServices.obtenerTipoDocs().then((response)=>{
        documentos.value = response.data;
    });

    DepartamentoServices.obtenerDepartamentos().then((data) => {
        departamentos.value=data.data;
    });
});


async function getServicios(){
    servicios.value = (await ProductoServices.obtenerServicios()).data;
    console.log(servicios.value);
}

// Función para filtros
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

// Buscar clientes
const search = (event) => {
    setTimeout(() => {
        if (!event.query.trim().length) {
            filteredClientes.value = [...proveedores.value];
        } else {
            filteredClientes.value = proveedores.value.filter((cliente) => {
                if (isNaN(event.query)) {
                    return (cliente.descripcion).toLowerCase().startsWith(event.query.toLowerCase());
                } else {
                    if(cliente.ruc){
                        return (cliente.ruc).toString().startsWith(event.query);
                    }else if(cliente.telefono) {
                        return (cliente.telefono).toString().startsWith(event.query);
                    }
                }
            });
        }
    }, 10);
}

// Vista de compra
const verCompra = (id) =>{
    router.push({name: 'ver_compra', params: {id}});
}

// Cálculos de IVA
const calcularIva = () =>{
    iva5.value = calcularIva5();
    iva10.value = calcularIva10();
    montoIva.value = iva5.value + iva10.value;
}

// Cálculos de IVA 5%
const calcularIva5 = () => {
    let detalle;
    if(compraIndependiente.value){
        detalle = detalleFacturar.value;
    }else{
        detalle = selectedProducts.value;
    }

    if (detalle == null) {
        return 0;
    }
    return detalle
    .filter(item => item.ivaAplicado.porcentaje === 5)
    .reduce((accu, item) => {
        let porcentaje = item.ivaAplicado.porcentaje || 0;
        let base = item.ivaAplicado.base || 0;
        let factor_iva = porcentaje + base;

        if (factor_iva === 0) return accu;
        
        let iva = item.subTotal * porcentaje / factor_iva;
        if (!isFinite(iva)) return accu;
        return accu + iva;
    }, 0);
};

// Cálculos de IVA 10%
const calcularIva10 = () => {
    let detalle;
    if(compraIndependiente.value){
        detalle = detalleFacturar.value;
    }else{
        detalle = selectedProducts.value;
    }

    if (detalle == null) {
        return 0;
    }
    return detalle
    .filter(item => item.ivaAplicado.porcentaje === 10)
    .reduce((acc, item) => {
        let factor_iva = item.ivaAplicado.porcentaje + item.ivaAplicado.base;
        return acc + ((item.subTotal * item.ivaAplicado.porcentaje / factor_iva) || 0)
    }, 0);
};

// Cálculos de IVA de Importación
const calcularIvaImportacion = () => {
    return (gastosImportacion.value !== 0)? gastosImportacion.value /11 : 0;
};

// Mostrar información del cliente
const mostrarCliente = () =>{
    console.log(selectedCliente.value);
    let texto = selectedCliente.value.descripcion;
    if (selectedCliente.value.tipo) {
        texto = texto + "\nTipo: "+selectedCliente.value.tipo.descripcion;
    }
    if (selectedCliente.value.ruc) {
        texto = texto + "\nRUC: "+selectedCliente.value.ruc;
    }
    if (selectedCliente.value.telefono) {
        texto = texto + "\nTelefono: "+selectedCliente.value.telefono;
    }

    let textoConSaltos = texto.replace(/\n/g, "<br>");

    let enlace = document.createElement("a");
    enlace.innerHTML = textoConSaltos;
    enlace.id = "infoCliente"
    enlace.href = "#";
    enlace.style.cursor = "pointer";

    enlace.addEventListener("click", function(event) {
        event.preventDefault();
        modificarCliente(selectedCliente.value);
    });

    document.getElementById("clienteDiv").appendChild(enlace);
        
    if (selectedCliente.value.tipo.descripcion == 'Extranjero') {
        detalleFacturar.value.forEach(e => {
            e.ivaAplicado = iva0;
        });
    } else {
        detalleFacturar.value.forEach(e => {
            e.ivaAplicado = e.producto.tipoIva;
        });
    }
    sendSubTotal();
    clienteSeleccionado.value = true;
}

// Eliminar cliente 
const eliminarClienteSelected = () =>{
    if (selectedCliente.value != null) {
        document.getElementById("infoCliente").remove();
        selectedCliente.value = null;
        clienteSeleccionado.value = false;
    }
}

// Registrar nuevo cliente
const registrarCliente = () =>{
    proveedor.value = {};
    clienteDialog.value = true;
}

// Modificar cliente 
const modificarCliente = (cli) => {
    ProveedorServices.getProveedor(cli.id).then((data) => {
        proveedor.value = data.data;
        proveedor.value.departamento = data.data.ciudad?.departamento;
        if (proveedor.value.departamento != null) {
            obtenerCiudadesByDepartamento(proveedor.value.departamento?.id);
        }
        clienteDialog.value = true;       
    });
};

// Obtener ciudades por departamento
const obtenerCiudadesByDepartamento = (id) =>{
    CiudadServices.obtenerCiudadesByDepartamento(id).then((data) => {
        ciudades.value = data.data;
        console.log(ciudades.value);
    });
}

// Guardar cliente
const saveCliente = () => {
    submitted.value = true;
    if (proveedor?.value.descripcion?.trim() ) {
        if (proveedor.value.id) {
            ProveedorServices.modificarProveedor(proveedor.value.id, proveedor.value).then((response)=>{
                console.log("mod");
                eliminarClienteSelected();
                proveedores.value[findIndexById(proveedor.value.id)] = proveedor.value;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
                selectedCliente.value = response.data;
                mostrarCliente();
            }).catch(
                (error)=>messageError("error")
            );
        }
        else {
            ProveedorServices.registrarProveedor(proveedor.value).then((response)=>{
                console.log("reg");
                proveedores.value.push(response.data);
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
                selectedCliente.value = response.data;
                mostrarCliente();
            }).catch(
                (error)=>messageError("error")
            );
        }
        submitted.value = false;
        clienteDialog.value = false;
        proveedor.value = {};
    }
};

// Impuestos
const impuestos = ref([
    { name: '10 %'}
]);

// Ocultar diálogo
const hideDialog = () => {
    clienteDialog.value = false;
    direccion.value = {};
    submitted.value = false;
};


const findIndexById = (id) => {
    let index = -1;
    for (let i = 0; i < proveedores.value.length; i++) {
        if (proveedores.value[i].id === id) {
            index = i;
            break;
        }
    }
    return index;
};

// Mostrar error
const mostrarError = (pedido) =>{
    console.log(pedido);
    let valor;
    valor={valor: 'Pedido #'+pedido.id};
    mensajeError.value.push(valor);
    valor={valor: 'Proveedor: '+pedido.proveedor.descripcion};
    mensajeError.value.push(valor);
    valor={valor: 'Tipo: '+pedido.proveedor.tipo.descripcion};
    mensajeError.value.push(valor);
    valor={valor: ' '};
    mensajeError.value.push(valor);
    valor={valor: '⚠ En pedidos nacionales, la recepción debe registrarse antes de facturar.'};
    mensajeError.value.push(valor);

    errorNoSePuedeFacturarPedido.value = true;
}

// Verificar si puede facturarse pedido
const puedeFacturarsePedido= (pedido) => {
    console.log('puedeFacturarsePedido');
    if (pedido == null) {
        return false;
    }

    if (pedido?.proveedor.tipo.descripcion == 'Extranjero') {
        console.log('if1');
        return true;
    }

    if (pedido?.estadoPedido != 'N' && pedido?.estadoPedido != 'A') {
        console.log(pedido);
        console.log('if2');
        return true;
    }

    console.log("return false");
    return false;
}

// Validar formulario
const validarForm = () => {
    console.log("selectedCliente.value");
    console.log(selectedCliente.value);
    mensaje.value = [];
    error.value = false;
    if (selectedCliente.value) {
    } else {
        error.value = true;
        mensaje.value.push("Debe seleccionar un proveedor");
    }

    console.log(detalleFacturar.value);
    console.log(detalleFacturar.value.length);
    if (compraIndependiente.value) {
        if (detalleFacturar.value == null || detalleFacturar.value?.length <1) {
            error.value = true;
            mensaje.value.push("Debe añadir productos");
        }
    } else {
        if (selectedProducts.value == null || selectedProducts.value?.length <1) {
            error.value = true;
            mensaje.value.push("Debe seleccionar productos");
        }
    }
    
    guardarFactura();
}

// Agregar producto
const addItem = (item) => {
    let index = detalleFacturar.value.findIndex((loopVariable) => loopVariable.producto.id === item.id);

    if (index>-1) {
        detalleFacturar.value[index].cantidad++;
    } else {
        console.log("holaaaitem",item);
        detalle.value.producto = {};
        detalle.value.producto = item;
        detalle.value.cantDisponible = item.cantDisponible;
        detalle.value.cantReservada = item.cantReservada;
        detalle.value.cantStock = item.cantStock;
        detalle.value.cantPreVenta = item.cantPreVenta;
        detalle.value.cantidad = 1;
        detalle.value.costoCompra = detalle.value.producto.costo;
        detalle.value.subTotal = detalle.value.costoCompra * detalle.value.cantidad;
        detalle.value.ivaAplicado = (selectedCliente.value?.tipo.descripcion == 'Extranjero')?iva0:detalle.value.producto.tipoIva;
        detalleFacturar.value.push(detalle.value);
        detalle.value= {};
    }
    item.cantDisponible--;
    item.cantReservada++;
    sendSubTotal();
}

// Agregar servicio
const addServicio= (item) => {
    let index = detalleFacturar.value.findIndex((loopVariable) => loopVariable.producto.id === item.id);

    if (index>-1) {
        detalleFacturar.value[index].cantidad++;
    } else {
        console.log("holaaaitem",item);
        detalle.value.producto = {};
        detalle.value.producto = item;
        detalle.value.cantidad = 1;
        detalle.value.costoCompra = item.costo;
        detalle.value.ivaAplicado = (selectedCliente.value?.tipo.descripcion == 'Extranjero')?iva0:detalle.value.producto.tipoIva;
        detalle.value.subTotal = detalle.value.costoCompra * detalle.value.cantidad;
        detalleFacturar.value.push(detalle.value);
        detalle.value= {};
    }

    sendSubTotal();
}

// Eliminar producto
const eliminar = (detalle) => {
    const cantidad= 1;
    let index = detalleFacturar.value.findIndex((loopVariable) => loopVariable.producto.id === detalle.producto.id);
    detalleFacturar.value.splice(index,cantidad);
    detalle.producto.cantDisponible = detalle.producto.cantDisponible + detalle.cantidad;
    detalle.producto.cantReservada = detalle.producto.cantReservada - detalle.cantidad;
    sendSubTotal();
}

// Vista de facturas de compras
const vistaFacturasCompras = () => {
    router.push({name: 'compras'});
}

// Ver pedido de compra
const verPedidoCompra = (id) => {
    router.push({name: 'ver_pedido_compra', params: {id}});
}

// Calcular totales
const calcularTotales = () => {
    if (selectedProducts.value?.length > 0) {
        let monto= 0;
        selectedProducts.value.forEach(e => {
            e.subTotal = (e.costoCompra*e.cantidad);
            monto += (e.costoCompra*e.cantidad);
        });
        subTotal.value = monto;
        total.value = subTotal.value;
    } else{
        subTotal.value = 0;
        total.value = subTotal.value;
    }
    calcularIva();
}

// Calcular subtotal
const sendSubTotal = () => {
    let monto= 0;
    detalleFacturar.value.forEach(e => {
        e.subTotal = e.costoCompra*e.cantidad
        monto += e.subTotal;
    });
    subTotal.value = monto;
    total.value = subTotal.value;
    calcularIva();
}

// Guardar factura
const guardarFactura = () =>{
    if (!error.value){
        let fechaAnticipo = new Date();
        let gravada = total.value - montoIva.value;
        let ant = {total: total.value, timbrado:timbrado.value ,pedido: pedido.value,totalGravada: gravada, montoIva: montoIva.value ,fecha: fechaCompra.value, proveedor: selectedCliente.value, nroFactura: nroFactura.value};
        let detalle;
        if (compraIndependiente.value) {
            detalle = detalleFacturar.value;
        } else {
            detalle = selectedProducts.value;
        }

        let anticipoCreationDTO = {compra: ant, detalle: detalle};
        CompraServices.registrarCompra(anticipoCreationDTO).then((data)=> {
            console.log("saveanticipothen");
            console.log("data");
            let id = data.data.id;
            showSuccess('Factura guardado correctamente');
            vistaFacturasCompras();
        });
    }
}

// Mostrar mensajes de error
const showError = (message) => {
    toast.add({
        severity: 'error',
        summary: 'Error',
        detail: message,
        life: 3000
    });
};

// Mostrar mensajes de éxito
const showSuccess = (message) => {
    toast.add({
        severity: 'success',
        summary: 'Éxito',
        detail: message,
        life: 3000
    });
};
</script>

<template>
    <div class="flex justify-content-center">
        <ConfirmDialog group="headless">
            <template #container="{ message, acceptCallback, rejectCallback }">
                <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                    <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                        <i class="pi pi-check text-5xl"></i>
                    </div>
                    <p class="block mb-2 mt-4">{{ message.message }}</p>
                    <span class="font-bold mb-0">{{ message.header }}</span>
                    <div class="flex align-items-center gap-2 mt-4">
                        <Button label="Ver factura" @click="acceptCallback"></Button>
                        <Button label="Ir a facturas" @click="rejectCallback"></Button>
                    </div>
                </div>
            </template>
        </ConfirmDialog>
        <!-- Diálogo para registrar/modificar cliente -->
        <Dialog v-model:visible="clienteDialog" :closable="false" :style="{width: '450px'}" header="Cliente" :modal="true" class="p-fluid">
            <div class="formgrid">
                <div class="field">
                    <label for="name">Nombre</label>
                    <InputText fluid id="name" v-model.trim="proveedor.descripcion" required="true" autofocus :class="{'p-invalid': submitted && !proveedor.descripcion}" />
                    <small class="p-error" v-if="submitted && !proveedor.descripcion">Ingrese un Nombre</small>
                </div>
                <div class="field">
                    <label for="description">Tipo Proveedor</label>
                    <Select fluid v-model="proveedor.tipo" :options="tiposProveedor" optionLabel="descripcion" placeholder="Seleccione un tipo" class="w-full md:w-56" />
                </div>
                <div class="field">
                    <label for="description">RUC</label>
                    <InputText fluid id="description" v-model="proveedor.ruc" required="true" />
                </div>
                <div class="field">
                    <label for="description">Correo</label>
                    <InputText fluid id="description" v-model="proveedor.correo" required="true" />
                </div>
                <div class="field">
                    <label for="description">Telefono</label>
                    <InputText fluid id="description" v-model="proveedor.telefono" required="true" />
                </div>
                <div class="field">
                    <label for="description">Departamento</label>
                    <Select fluid v-model="proveedor.departamento" :options="departamentos" @change="obtenerCiudadesByDepartamento(proveedor.departamento.id)" optionLabel="descripcion" placeholder="Seleccione un departamento" class="w-full md:w-56" />
                </div>
                <div class="field">
                    <label for="description">Ciudad</label>
                    <Select fluid v-model="proveedor.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad" class="w-full md:w-56" />
                </div>
                <div class="field">
                    <label for="description">Direccion</label>
                    <InputText fluid id="description" v-model="proveedor.direccion" required="true" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveCliente" />
            </template>
        </Dialog>

        <!-- Panel principal -->
        <Panel style="position: relative; width: 80%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Nueva Compra</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button label="Cancelar" style="margin-right: 1%;" @click="vistaFacturasCompras()" />
                        <Button label="Guardar" @click="validarForm" />
                    </div>  
                </div>
            </template>
            <div class="contenedor">
                <!-- Mensajes de error -->
                <div v-if="error" style="background-color: rgb(242, 222, 222); border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
                    <ul>
                        <li v-for="msg in mensaje" style="list-style: none;">
                            <a style="color: rgb(173, 89, 86);">{{ msg }}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="grid">
                <!-- Información general -->
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
                                Fecha: <DatePicker dateFormat="dd/mm/yy" fluid v-model="fechaCompra" showIcon iconDisplay="input" />
                            </div> 
                            <div class="field">
                                Timbrado: <InputText fluid type="text" v-model="timbrado" />
                            </div> 
                            <div class="field">
                                N° Factura: <InputText fluid type="text" v-model="nroFactura" />
                            </div> 
                        </template>
                    </Card>
                </div> 
                
                <!-- Sección de proveedor -->
                <div class="field col-12 md:col-6">
                    <Card>
                        <template #title>
                            <div class="flex justify-content-between">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Proveedor
                                </div>
                                <div v-if="clienteSeleccionado">
                                    <Button icon="pi pi-times" link @click="eliminarClienteSelected"/>
                                </div>   
                                <div v-else>
                                    <Button icon="pi pi-plus" link @click="registrarCliente"/>
                                </div>   
                            </div>
                        </template>
                        <template #content>
                            <div id="clienteDiv"></div>
                            <div class="flex flex-column align-options-start">
                                <div v-if="!clienteSeleccionado">
                                    <AutoComplete fluid v-model="selectedCliente" optionLabel="descripcion" forceSelection :suggestions="filteredClientes" @complete="search" @item-select="mostrarCliente">
                                        <template #option="slotProps">
                                            <div class="flex flex-column align-options-start">
                                                <div>{{ slotProps.option.descripcion }}</div>
                                                <div v-if="slotProps.option.ruc">RUC: {{ slotProps.option.ruc }}</div>
                                                <div v-if="slotProps.option.telefono">Telefono: {{ slotProps.option.telefono }}</div>
                                            </div>
                                        </template>
                                    </AutoComplete>
                                </div>
                            </div>
                        </template>
                    </Card>
                </div>  
                
                <!-- Sección de productos -->
                <div class="col-12">
                    <div class="col-12">
                        <Card>
                            <template #title>
                                <div class="flex justify-content-between">
                                    <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                        Productos
                                    </div>
                                    <div>
                                        <Button label="Agregar Producto" text @click="visible = true" />
                                    </div>
                                </div>
                            </template>
                            <template #content>
                                <div>
                                    <DataTable :key="'table2'" :value="detalleFacturar">
                                        <Column class="col" field="producto.nombre" header="Nombre" aria-sort="none"></Column>
                                        <Column class="col" header="Costo" aria-sort="none">
                                            <template #body="slotProps">
                                                <div class="flex-auto p-fluid">
                                                    <InputNumber fluid class="inpCant" v-model="slotProps.data.costoCompra" mode="decimal" @update:modelValue="sendSubTotal" />
                                                </div> 
                                            </template>
                                        </Column>
                                        <Column class="col" field="cantidad" header="Uds." aria-sort="none">
                                            <template #body="slotProps">
                                                <div class="flex-auto p-fluid" style="max-width:10lvb !important;">
                                                    <InputNumber fluid class="inpCant" v-model="slotProps.data.cantidad" inputId="minmax-buttons" mode="decimal" showButtons :min="1" @update:modelValue="sendSubTotal" />
                                                </div>  
                                            </template>
                                        </Column>
                                        <Column class="col" field="cantidad" header="IVA" aria-sort="none">
                                            <template #body="slotProps">
                                                {{ slotProps.data.ivaAplicado.porcentaje}}%
                                            </template>
                                        </Column>
                                        <Column class="col" field="subTotal" header="Sub Total" aria-sort="none">
                                            <template #body="slotProps">
                                                <div class="flex-auto p-fluid">
                                                    <label for="subtotal"> {{ (slotProps.data.cantidad * slotProps.data.costoCompra)}}</label>
                                                </div>
                                            </template>
                                        </Column>
                                        <Column class="col" :exportable="false" style="min-width:1rem">
                                            <template #body="slotProps">
                                                <Button icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminar(slotProps.data)" />
                                            </template>
                                        </Column>
                                    </DataTable>
                                </div>
                            </template>    
                        </Card>
                    </div>

                    <!-- Totales -->
                    <div class="grid" style="margin-top: 1rem;">                                    
                        <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px;">
                            <div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px; font-weight: bold; font-size: 16px;">
                                Total: 
                            </div>
                            <div class="field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px; font-weight: bold; font-size: 16px;">
                                {{ formatearNumero(total) }} Gs.
                            </div>
                        </div>  
                        <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px;">
                            <div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px;">
                                IVA 5%: 
                            </div>
                            <div class="field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px;">
                                {{ (Math.round(iva5)).toLocaleString("de-DE") }} Gs.
                            </div>
                        </div>   
                        <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px;">
                            <div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px;">
                                IVA 10%: 
                            </div>
                            <div class="field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px;">
                                {{ (Math.round(iva10)).toLocaleString("de-DE") }} Gs.
                            </div>
                        </div>  
                        <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px;">
                            <div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px;">
                                Total IVA: 
                            </div>
                            <div class="field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px;">
                                {{ (Math.round(montoIva)).toLocaleString("de-DE") }} Gs.
                            </div>
                        </div>         
                    </div>

                    <!-- Diálogo para seleccionar productos -->
                    <div>
                        <Dialog v-if="visible" v-model:visible="visible" modal header="Seleccionar productos" :closable="false" :draggable="false">
                            <template #footer>
                                <div class="flex justify-content-end">
                                    <Button label="Cerrar" icon="pi pi-times" text @click="visible = false" />
                                </div>
                            </template> 
                            <div class="grid">
                                <div class="card col-12" style="width: 100%;">
                                    <span class="p-input-icon-left" style="width: 100%; margin-top: 0.5rem;">
                                        <InputText fluid class="buscador p-fluid" style="width: 100%;" v-model="filters['global'].value" placeholder="Buscar..." />
                                    </span>
                                    <div class="flex card-container col-12" style="width: 100%;">
                                        <DataTable class="tabla" ref="dt" :value="productos" dataKey="producto.id"
                                            :paginator="true" :rows="7" :filters="filters"
                                            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown">
                                            <Column field="id" header="ID" aria-sort="ascending"></Column>
                                            <Column field="nombre" header="Nombre" aria-sort="none"></Column>
                                            <Column field="cantDisponible" header="Stock" aria-sort="none">
                                                <template #body="slotProps">
                                                    <h4>{{slotProps.data.cantStock }}</h4>
                                                </template>
                                            </Column>
                                            <Column field="precio" header="Costo" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div>
                                                        {{ slotProps.data.costo.toLocaleString("de-DE") }}
                                                    </div>
                                                </template>
                                            </Column>
                                            <Column :exportable="false" style="min-width:8rem">
                                                <template #body="slotProps">
                                                    <Button icon="pi pi-shopping-cart" class="mod_icono" @click="addItem(slotProps.data)"/>
                                                </template>
                                            </Column>
                                        </DataTable>
                                    </div>
                                </div>
                            </div>
                        </Dialog>  
                    </div>
                    <!-- Diálogo para seleccionar servicios -->
                    <div>
                        <Dialog v-if="servicioDialog" v-model:visible="servicioDialog" modal header="Seleccionar servicios" :closable="false" :draggable="false">
                            <template #footer>
                                <div class="flex justify-content-end">
                                    <Button label="Cerrar" icon="pi pi-times" text @click="servicioDialog = false" />
                                </div>
                            </template> 
                            <div class="grid">
                                <div class="card col-12" style="width: 100%;">
                                    <div class="flex card-container col-12" style="width: 100%;">
                                        <DataTable class="tabla" ref="dt" :value="servicios" dataKey="producto.id">
                                            <Column field="nombre" header="Descripcion" aria-sort="none" style="min-width:30rem"></Column>
                                            <Column :exportable="false">
                                                <template #body="slotProps">
                                                    <Button icon="pi pi-shopping-cart" class="mod_icono" @click="addServicio(slotProps.data)"/>
                                                </template>
                                            </Column>
                                        </DataTable>
                                    </div>
                                </div>
                            </div>
                        </Dialog>  
                    </div>
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