<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Card from "primevue/card";
import Button from 'primevue/button';
import {CiudadServices } from '@/services/CiudadServices';
import {EnvioServices} from '@/services/EnvioServices';
import {EstadosServices} from "@/services/EstadosServices";
import {CajaServices} from '@/services/CajaServices';
import { ClienteServices } from "@/services/ClienteServices";
import { ref, onMounted } from "vue";
import RadioButton from "primevue/radiobutton";
import InputGroup from 'primevue/inputgroup';
import Textarea from "primevue/textarea";
import {DireccionServices} from '@/services/DireccionServices';
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import DatePicker from "primevue/datepicker";
const map = ref();
const direcciones = ref([]);
const direccion = ref({});
const pedido = ref({ });
const selectedCliente = ref();
const clienteDialog = ref(false);
const personaCreationDTO = ref({});
const submitted = ref(false);
const direccionSubmitted = ref(false);
const clienteSeleccionado = ref(false);
const detallePedido = ref({ });
const mensaje = ref([]);
const ciudades = ref([]);
const departamentos = ref([]);
const formasEntrega = ref([{id: 1, descripcion: 'Retiro'},{id:2, descripcion: 'Envío'}]);
const documentos = ref([]);
const visible = ref(false);
const datosEntregaUltimoGuardado = ref({});
const selectedFormaEntrega = ref({id: 1, descripcion: 'Retiro'});
const estadoPedido = ref();
const estadoPago = ref();
const pedidoId = ref(0);
const cliente = ref({});
const observaciones = ref();
const selectedOp = ref('Casi');
const direccionSelected = ref();
const direccionEnvio = ref({});
const nuevaDireccion = ref(false);
const productos= ref();
const clientes=ref();
const filteredClientes = ref();
const entrega = ref();
const envioSelected = ref();
const fecha = ref();
const fechaCompleta = ref();
const error = ref(false);
const opciones = ref(['Casi','Entre']);
const selectedEnvio = ref({});
const medios = ref();
const infoEntrega = ref([{
    valor: "Retiro"
}])
const pedidoTotalPagado = ref({});
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();

// Mostrar mensaje de error
const messageError = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'cliente',
        header: 'Ocurrio un error',
        message: msg.toUpperCase(),
        accept: () => {},
    });
};


onMounted(() => {
    // Obtener pagos del pedido
    CajaServices.obtenerPagosPedido(router.currentRoute.value.params.id).then((data) => {
        pedidoTotalPagado.value = data.data;
    });
    
    // Si hay costo de envío, asignarlo
    if (pedido.value.costoEnvio) {
        costo.value = pedido.value.costoEnvio.costo;
    }
        
    getPedido();
   
    // Obtener lista de clientes
    ClienteServices.obtenerClientes().then((data) => {
        clientes.value = data.data;
    });

    // Obtener tipos de documento
    TipoDocServices.obtenerTipoDocs().then((response) => {
        documentos.value = response.data;
    });

    // Obtener departamentos
    DepartamentoServices.obtenerDepartamentos().then((data) => {
        departamentos.value = data.data;
    });
});

// Asignar color según estado
const getSeverity = (estado) => {
    switch (estado) {
        case 'Pagado':
            return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66);';
        case 'Parcial':
            return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';
        case 'Pendiente':
            return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';
        default:
            return null;
    }
};

// Formatear número o fecha
const formatearNumero = (valor) => {
    if (typeof(valor) == "number") {
        return valor.toLocaleString("de-DE");
    }

    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' + fecha.getFullYear() + ' ' + 
                          fecha.getHours() + ':' + fecha.getMinutes() + ':' + fecha.getSeconds();
    return fechaFormateada;
}

// Obtener detalle del pedido
async function getDetalle() {
    await PedidoServices.getDetalle(router.currentRoute.value.params.id).then((data) => {
        console.log("Este", data.data);
        productos.value.setDetalle(data.data);
    });
}

// Obtener datos del pedido
const getPedido = () => {
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        pedido.value = data.data.pedido;
        let det = data.data.detalle;

        selectedCliente.value = pedido.value.cliente?.persona;
        selectedCliente.value.id = pedido.value.cliente?.id;
        console.log(det);
        
        productos.value.setDetalle(det);
        fecha.value = new Date(pedido.value.fecha);
        mostrarCliente();
    });
}

// Buscar clientes
const search = (event) => {
    setTimeout(() => {
        if (!event.query.trim().length) {
            filteredClientes.value = [...clientes.value];
        } else {
            filteredClientes.value = clientes.value.filter((cliente) => {
                if (isNaN(event.query)) {
                    return (cliente.nombre+' '+cliente.apellido).toLowerCase().startsWith(event.query.toLowerCase());
                } else {
                    if (cliente.telefono) {
                        return (cliente.telefono).toString().startsWith(event.query);
                    } else if(cliente.nroDoc) {
                        return (cliente.nroDoc).toString().startsWith(event.query);
                    }
                }
            });
        }
    }, 10);
}

// Mostrar información del cliente seleccionado
const mostrarCliente = () => {
    console.log(selectedCliente.value);
    let texto = selectedCliente.value.nombre;
    if (selectedCliente.value.apellido) {
        texto = texto + " "+selectedCliente.value.apellido;
    }
    if (selectedCliente.value.telefono) {
        texto = texto + "\nTelefono: "+selectedCliente.value.telefono;
    }

    if (selectedCliente.value.nroDoc) {
        texto = texto + "\n"+selectedCliente.value.tipoDoc.descripcion +" "+ selectedCliente.value.nroDoc;
    }
    
    let textoConSaltos = texto.replace(/\n/g, "<br>");
    let enlace = document.createElement("a");
    enlace.innerHTML = textoConSaltos;
    enlace.id = "infoCliente";
    enlace.href = "#";
    enlace.style.cursor = "pointer";

    enlace.addEventListener("click", function(event) {
        event.preventDefault();
        modificarCliente(selectedCliente.value);
    });

    document.getElementById("clienteDiv").appendChild(enlace);
    clienteSeleccionado.value = true;
}

// Eliminar cliente seleccionado
const eliminarClienteSelected = () => {
    document.getElementById("infoCliente").remove();
    selectedCliente.value = null;
    direccion.value = {};
    clienteSeleccionado.value = false;
    nuevaDireccion.value = false;
    inicializarDatosEntrega();
}

// Abrir diálogo para registrar nuevo cliente
const registrarCliente = () => {
    cliente.value = {};
    clienteDialog.value = true;
}

// Modificar cliente existente
const modificarCliente = (cli) => {
    ClienteServices.getCliente(cli.id).then((data) => {
        cliente.value = data.data;
        clienteDialog.value = true;
        if (data.data.direccion) {
            direccion.value = data.data.direccion;
            direccion.value.departamento = data.data.direccion.ciudad.departamento;
        } 

        if(data.data.direccion && data.data.direccion.ciudad != null) {
            console.log("entra if ");
            getCiudades(data.data.direccion.ciudad.departamento.id);
        }
    });
};

// Validar dirección del cliente
const validarDireccionCliente = (dir) => {
    if (algunCampoTieneValor(dir) && (!dir.calle1 || !dir.ciudad)) {
        return false;
    }

    if (!algunCampoTieneValor(dir)) {
        return true;
    }
    
    return true;
};

// Guardar cliente
const saveCliente = () => {
    submitted.value = true;
    console.log(direccion.value);
    console.log(cliente.value);
    
    if (cliente?.value.nombre?.trim() && validarDireccionCliente(direccion.value)) {
        direccion.value.tipo = 'P';
        cliente.value.direccion = direccion.value;
        console.log(cliente.value.id);
        console.log(cliente.value);
        
        if (cliente.value.id) {
            // Modificar cliente existente
            ClienteServices.modificarCliente(cliente.value.id, cliente.value).then((response) => {
                console.log("mod");
                eliminarClienteSelected();
                clientes.value[findIndexById(response.data.id)] = response.data;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
                selectedCliente.value = response.data;
                mostrarCliente();
                direccion.value.tipo = null;
            }).catch((error) => console.log(error));
        } else {
            // Registrar nuevo cliente
            ClienteServices.registrarCliente(cliente.value).then((response) => {
                console.log("reg");
                clientes.value.push(response.data);
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
                selectedCliente.value = response.data;
                mostrarCliente();
                direccion.value.tipo = null;
            }).catch((error) => messageError("error"));
        }

        clienteDialog.value = false;
        cliente.value = {};
    }
};

// Obtener ubicación geográfica
const getUbicacion = (lat, lng) => {
    console.log(lat);
    console.log(lng);
    direccion.value.lat = lat;
    direccion.value.lng = lng;
}

// ========== FUNCIONES DE ENTREGA ==========

// Buscar medios de envío por ciudad
const searchMediosEnvio = (id) => {
    console.log("idciudad");
    console.log(id);
    EnvioServices.obtenerCostosEnvioByCiudad(id).then((data) => {
        medios.value = data.data;
        selectedEnvio.value = {};
    });
}

// Cambiar modalidad de entrega
const changeModalidadEntrega = () => {
    if (selectedFormaEntrega.value && selectedFormaEntrega.value.descripcion === "Retiro") {
        selectedEnvio.value = {};
        submitted.value = false;
    } else {
        medios.value = null;
    }
}

// Inicializar datos de entrega
const inicializarDatosEntrega = () => {
    selectedFormaEntrega.value = formasEntrega.value[0];
    selectedEnvio.value = {};
    direccionSelected.value = null;
    direccionEnvio.value = {};
    infoEntrega.value = [{valor: "Retiro"}];
    productos.value.setDetalleEnvio(null);
}

// Validar dirección de envío
const validarDireccionEnvio = (dir) => {
    console.log("valdirenvio");
    console.log(dir);
    if ((!dir.calle1 || !dir.ciudad)) {
        console.log("val envio false");
        return false;
    }  
    
    return true;
};

// Obtener ubicación para envío
const getUbicacionEnvio = (lat, lng) => {
    console.log(lat);
    console.log(lng);
    direccionEnvio.value.lat = lat;
    direccionEnvio.value.lng = lng;
}

// Añadir datos de entrega
const añadirDatoEntrega = () => {
    if (selectedCliente.value) {
        guardarEstadoEntregaOriginal();
        visible.value = true;
        getDireccionesCliente();
    } else {
        toast.add({ severity: 'info', summary: 'Info Message', detail: 'Seleccione un cliente', life: 3000 });
    }
}

// Agregar nueva dirección
const agregarNuevaDireccion = (val) => {
    nuevaDireccion.value = val;
    direccionSelected.value = {};
    selectedEnvio.value = {};
    medios.value = null;
}

// Guardar estado original de entrega
const guardarEstadoEntregaOriginal = () => {
    datosEntregaUltimoGuardado.value.modalidad = selectedFormaEntrega.value;
    datosEntregaUltimoGuardado.value.nuevaDireccion = nuevaDireccion.value;
    datosEntregaUltimoGuardado.value.direccionSelected = direccionSelected.value;
    datosEntregaUltimoGuardado.value.direccion = direccionEnvio.value;
    datosEntregaUltimoGuardado.value.envio = selectedEnvio.value;
}

// Guardar datos de entrega
const saveEntrega = () => {
    direccionSubmitted.value = true;
    if (datosEntregaValidos()) {
        if (selectedFormaEntrega.value.descripcion === 'Envío' && nuevaDireccion.value) {
            direccionEnvio.value.persona = selectedCliente.value;
            direccionEnvio.value.tipo = 'E';
            generarDireccion(direccionEnvio.value);
        }

        if(selectedFormaEntrega.value.descripcion === 'Retiro') {
            productos.value.setDetalleEnvio(null);
        } else {
            productos.value.setDetalleEnvio(selectedEnvio.value);
        }
        
        showInfoEntrega();
        visible.value = false;
        direccionSubmitted.value = false;
    }   
}

// Mostrar información de entrega
const showInfoEntrega = () => {
    infoEntrega.value = [];
    let valor = {valor: selectedFormaEntrega.value.descripcion};
    infoEntrega.value.push(valor);
    
    if (selectedFormaEntrega.value.descripcion === 'Envío') {
        valor = {valor: 'a ' + selectedEnvio.value.ciudad.descripcion};
        infoEntrega.value.push(valor);
        
        valor = {valor: 'Vía ' + selectedEnvio.value.envio.descripcion + ' - '+selectedEnvio.value.costo+' Gs'};
        infoEntrega.value.push(valor);
    }
}

// Validar datos de entrega
const datosEntregaValidos = () => {
    if(selectedFormaEntrega.value.descripcion === 'Retiro') {
        return true;
    }

    if (!selectedEnvio.value.id) {
        return false;
    }
    
    if (!nuevaDireccion.value && direccionSelected.value !== undefined && direccionSelected.value !== null ) {
        return true;
    }
    
    return validarDireccionEnvio(direccionEnvio.value);
}

// Cerrar diálogo de entrega
const closeEntregaDialog = () => {
    selectedFormaEntrega.value = datosEntregaUltimoGuardado.value.modalidad;
    nuevaDireccion.value = datosEntregaUltimoGuardado.value.nuevaDireccion;
    direccionSelected.value = datosEntregaUltimoGuardado.value.direccionSelected;
    direccionEnvio.value = datosEntregaUltimoGuardado.value.direccion;
    selectedEnvio.value = datosEntregaUltimoGuardado.value.envio;

    direccionSubmitted.value = false;
    visible.value = false;
}

// ========== OTRAS FUNCIONES ==========

// Obtener direcciones del cliente
const getDireccionesCliente = () => {
    DireccionServices.getDireccionesCliente(selectedCliente.value.id).then((data) => {
        console.log("data.data");
        console.log(data.data);
        direcciones.value = data.data;
        if (direcciones.value.length < 1) {
            console.log("direcciones.value.length");
            console.log(direcciones.value.length);
            nuevaDireccion.value = true;
        }
    });
}

// Obtener ciudades por departamento
const getCiudades = (id) => {
    CiudadServices.obtenerCiudadesByDepartamento(id).then((data) => {
        console.log("data ciudades");
        console.log(data.data);
        ciudades.value = data.data;
    });
};

// Ocultar diálogo
const hideDialog = () => {
    clienteDialog.value = false;
    direccion.value = {};
    submitted.value = false;
};

// Generar texto de dirección
const generarDireccion = (dir) => {
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " + selectedOp.value + " "+ dir.calle2;
    }
    if (dir.calle3?.trim()) {
        d = d + " y " + dir.calle3;
    }

    dir.direccion = d;
};

// Verificar si algún campo tiene valor
const algunCampoTieneValor = (dir) => {
    return Object.values(dir).some(valor => valor !== "" && valor !== null && valor !== undefined);
}

// Encontrar índice por ID
const findIndexById = (id) => {
    let index = -1;
    for (let i = 0; i < clientes.value.length; i++) {
        if (clientes.value[i].id === id) {
            index = i;
            break;
        }
    }
    return index;
};

// ========== FUNCIONES DE PEDIDO ==========

// Ver pedido
const verPedido = (id) => {
    router.push({name: 'VisualizarPedido', params: {id}});
}

// Enviar datos del pedido
const submit = () => {
    if (!error.value) {
        console.log("pedidodetalle", productos.value.detalles);
        pedido.value.cliente = selectedCliente.value;
        pedido.value.fecha = fecha.value;
        
        detallePedido.value = productos.value.detalles;
        pedido.value.total = productos.value.subTotal;
        
        let pedidoDTO = {pedido: pedido.value, detalle: detallePedido.value};
        PedidoServices.modificarPedido(pedido.value.id, pedidoDTO).then((response) => {
            verPedido(response.data.id);
        }).catch((error) => messageError(error.response.data.mensaje));
    }
}

// Validar formulario
const validarForm = (event) => {
    console.log("selectedCliente.value");
    console.log(selectedCliente.value);
    mensaje.value = [];
    error.value = false;
    
    if (selectedCliente.value) {
        // Cliente válido
    } else {
        error.value = true;
        mensaje.value.push("Debe seleccionar un cliente");
    }

    if (productos.value.subTotal < 1) {
        error.value = true;
        mensaje.value.push("Debe añadir productos al pedido");
    }
    
    submit();
}

</script>

<template>
    <div class="flex p-fluid justify-content-center ">
        <ConfirmDialog group="cliente">
            <template #container="{ message, acceptCallback }">
                <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                    <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                        <i class="pi pi-times text-5xl"></i>
                    </div>
                    <span class="font-bold text-2xl block mb-2 mt-4">{{ message.header }}</span>
                    <p class="mb-0">{{ message.message }}</p>
                    <div class="flex align-items-center gap-2 mt-4">
                        <Button label="Ok" @click="acceptCallback"></Button>
                    </div>
                </div>
            </template>
        </ConfirmDialog>

        <Dialog v-model:visible="visible" :closable="false" :style="{width: '450px'}" header="Entrega" :modal="true" class="p-fluid">
            <div class="field">
                <div v-for="entrega in formasEntrega" :key="entrega.id">
                    <RadioButton v-model="selectedFormaEntrega" :inputId="entrega.id.toString()" name="dynamic" :value="entrega" @change="changeModalidadEntrega" />
                    <label :for="entrega.id" class="ml-2">{{ entrega.descripcion }}</label>
                </div>
            </div>

            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="closeEntregaDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveEntrega" />
            </template>
        </Dialog>

        <!--Dialog Registrar Modificar Cliente-->
        <Dialog v-model:visible="clienteDialog" :closable="false" :style="{width: '450px'}" header="Cliente" :modal="true" class="p-fluid">
            <div class="field">
                <label for="name">Nombre</label>
                <InputText fluid id="name" v-model.trim="cliente.nombre" required="true" autofocus :class="{'p-invalid': submitted && !cliente.nombre}" />
                <small class="p-error" v-if="submitted && !cliente.nombre">Ingrese un Nombre</small>
            </div>
            <div class="field">
                <label for="description">Apellido</label>
                <InputText fluid id="description" v-model="cliente.apellido" required="true"  />
            </div>
            <div class="field">
                <label for="inventoryStatus" class="mb-3">Tipo Documento</label>
                <Dropdown fluid id="inventoryStatus" v-model="cliente.tipoDoc" :options="documentos" optionLabel="descripcion" placeholder="Seleccione el tipo de documento" />
            </div>
            <div class="field">
                <label for="description">Nro Documento</label>
                <InputText fluid id="description" v-model="cliente.nroDoc" required="true"  />
            </div>
            <div class="field">
                <label for="description">Telefono</label>
                <InputText fluid id="description" v-model="cliente.telefono" required="true"  />
            </div>
            <div class="field">
                <label for="description">Calle Principal</label>
                <InputText fluid id="description" v-model="direccion.calle1" required="true" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.calle1}" />
                <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.calle1">Ingrese Calle Principal</small>
            </div>
            
            <div class="field">
                <label for="description">Calle Secundaria</label>
                <InputGroup fluid>
                    <Dropdown v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
                    <InputText id="description" v-model="direccion.calle2" required="true"  />
                </InputGroup>
            </div>
            <div class="field" v-if="selectedOp=='Entre'">
                <label for="description">Calle Transversal</label>
                <InputText fluid id="description" v-model="direccion.calle3" required="true"  />
            </div>
            <div class="field">
                <label for="description">N° Casa</label>
                <InputText fluid id="description" v-model="direccion.nroCasa" required="true"  />
            </div>
            <div class="field " >
                <label for="nombreu">Departamento</label>
                <Dropdown fluid v-model="direccion.departamento" :options="departamentos" optionLabel="descripcion" placeholder="Seleccione un departamento" @change="getCiudades(direccion.departamento.id)"  />
            </div>
            <div class="field " >
                <label for="nombreu">Ciudad</label>
                <Dropdown fluid v-model="direccion.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.ciudad}"  />
                <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.ciudad">Ingrese Ciudad</small>
            </div>

            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveCliente" />
            </template>
        </Dialog>

        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Modificar Pedido N° {{ pedido.id }}</h3>
                </div>
            </template>
            <template #icons>
                <div class="flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;"  @click="verPedido(pedido.id)" />
                    <Button  label="Guardar" @click="validarForm" />
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
                                Fecha: <DatePicker fluid dateFormat="dd/mm/yy" v-model="fecha" showIcon iconDisplay="input" />
                            </div> 
                            <div class="field">
                                Observaciones: 
                                <Textarea fluid v-model="pedido.observaciones" rows="3" cols="33" />
                            </div>
                        </template>
                    </Card>
                </div> 
                <div class="field col-12 md:col-6">
                    <Card >
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Cliente
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
                                <div v-if="!clienteSeleccionado" >
                                    <AutoComplete v-model="selectedCliente" fluid optionLabel="nombre" forceSelection :suggestions="filteredClientes" @complete="search" @item-select="mostrarCliente">
                                        <template #option="slotProps">
                                            <div class="flex flex-column align-options-start">
                                                <div>{{ slotProps.option.nombre }} {{ slotProps.option.apellido || ' ' }}</div>
                                                <div v-if="slotProps.option.telefono">{{ slotProps.option.telefono }}</div>
                                                <div v-if="slotProps.option.nroDoc">{{ slotProps.option.tipoDoc.descripcion }} - {{ slotProps.option.nroDoc }}</div>
                                            </div>
                                        </template>
                                    </AutoComplete>
                                </div>
                            </div>
                        </template>
                    </Card>
                </div>  
                
                <div class="field col-12 md:col-12">
                    <CardDetalle ref="productos"/>
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