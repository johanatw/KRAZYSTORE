<script setup>
//Importaciones
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
import Select from "primevue/select";
import {EstadosServices} from "@/services/EstadosServices";
import {CajaServices} from '@/services/CajaServices';
import { ref, onMounted } from "vue";
import RadioButton from "primevue/radiobutton";
import InputGroup from 'primevue/inputgroup';
import Textarea from "primevue/textarea";
import InputNumber from "primevue/inputnumber";
import {DireccionServices} from '@/services/DireccionServices';
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import { PuntoRetiroServices } from '@/services/PuntoEntregaServices';
import { ModosEntregaServices } from "@/services/ModosEntregaServices";
import { EmpresasDeliveryServices } from '@/services/EmpresasTransporteServices';
import {EntregaServices} from '@/services/EntregaServices';
import DatePicker from "primevue/datepicker";

//Variables
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
const entrega = ref({});
const detalles = ref([]);
const envioSelected = ref();
const fecha = ref();
const fechaCompleta = ref();
const error = ref(false);
const opciones = ref(['Casi','Entre']);
const selectedEnvio = ref({});
const medios = ref();
const direccionesCliente = ref([]);
const puntosEntrega = ref([]);
const empresasTransporte = ref([]);
const modalidadesEntrega = ref([]);
const infoEntrega = ref([{
    valor: "Retiro"
}])

const pedidoTotalPagado = ref({});

//Importaciones
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

//Invocación de mensaje
const confirm = useConfirm();
const toast = useToast();
const messageError = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'cliente',
        header: 'Ocurrio un error',
        message: msg.toUpperCase(),
        accept: () => {
        },
    });
};


onMounted(() => {
    EntregaServices.getEntrega(router.currentRoute.value.params.id).then((data) => {
        entrega.value = data.data.entrega
        detalles.value = data.data.detalle;
        cliente.value = entrega.value.pedido?.cliente;
        getDireccionesCliente(cliente.value?.id);
   });
    getPuntosEntrega();
    getModosEntrega();
    getEmpresasTransporte();
   PersonaServices.obtenerClientes().then((data) => {
       clientes.value = data.data;
   });

   TipoDocServices.obtenerTipoDocs().then((response)=>{
        documentos.value = response.data;
    });

    DepartamentoServices.obtenerDepartamentos().then((data) => {
        departamentos.value=data.data;

    });
});


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


const formatearNumero = (valor) =>{
    if(typeof(valor) == "number"){
        return valor.toLocaleString("de-DE");
    }
    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear()+' '+ fecha.getHours()+':'+fecha.getMinutes()+':'+fecha.getSeconds();
    return fechaFormateada;
}

//Obtener detalle
async function getDetalle() {
    await PedidoServices.getDetalle(router.currentRoute.value.params.id).then((data) => {
        console.log("Este", data.data);
        productos.value.setDetalle(data.data);

    });   
}

//Obtener puntos de entrega
const getPuntosEntrega = async () => {
    try {
      const response = await PuntoRetiroServices.obtenerPuntosRetiro();
      puntosEntrega.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

//Obtener empresas de transporte
const getEmpresasTransporte = async () => {
    try {
      const response = await EmpresasDeliveryServices.obtenerEmpresasDelivery();
      empresasTransporte.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

//Obtener dirección del cliente
const getDireccionesCliente = async (id) => {
    try {
      const response = await DireccionServices.getDireccionesCliente(id);
      direccionesCliente.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

//Obtener modos de entrega
const getModosEntrega = async () => {
    try {
      const response = await ModosEntregaServices.obtenerModosEntrega();
      modalidadesEntrega.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

//Obtener pedido
const getPedido = () => {
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        pedido.value = data.data.pedido;
        let detallesPedido = data.data.detalle;
        cliente.value = pedido.value.cliente;
        console.log(cliente.value);
        fecha.value = new Date();
        detallesPedido.forEach(element => {
          let e = {};
            e.cantDisponible = element.cantFacturada - element.cantEntregada;
            e.cantidad = e.cantDisponible;
            e.detallePedido = element;
            detalles.value.push(e);
        });
    });
}

//Cliente
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
                    }else if(cliente.nroDoc){
                        return (cliente.nroDoc).toString().startsWith(event.query);
                    }
                    
                }
                
            });
        }
    }, 10);
}

//Mostrar cliente
const mostrarCliente = () =>{
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
    let textoConSaltos = texto.replace(/\n/g, "<br>");  // Reemplazar \n con <br>
    // Crear un elemento <a> con innerHTML
    let enlace = document.createElement("a");
    enlace.innerHTML = textoConSaltos;  // Insertar el texto con saltos de línea
    enlace.id = "infoCliente"
    enlace.href = "#";  // Evitar navegación
    enlace.style.cursor = "pointer";  // Cambiar cursor al pasar sobre el enlace
    // Agregar un evento click al enlace
    enlace.addEventListener("click", function(event) {
        event.preventDefault();
        modificarCliente(selectedCliente.value);
        // Aquí puedes agregar cualquier lógica adicional
    });
    // Insertar el enlace en el div
    document.getElementById("clienteDiv").appendChild(enlace);
    clienteSeleccionado.value = true;
}


//Eliminar cliente
const eliminarClienteSelected = () =>{
    document.getElementById("infoCliente").remove();
    selectedCliente.value = null;
    direccion.value = {};
    clienteSeleccionado.value = false;
    nuevaDireccion.value = false;
    inicializarDatosEntrega();
}

//Registrar cliente
const registrarCliente = () =>{
    cliente.value = {};
    clienteDialog.value = true;
}

//Modificar cliente
const modificarCliente = (cli) => {
    PersonaServices.getPersona(cli.id).then((data) => {
       cliente.value = data.data.persona;
       clienteDialog.value = true;
       if (data.data.direccion) {
        direccion.value = data.data.direccion;
        direccion.value.departamento = data.data.direccion.ciudad.departamento;
       } 

       if(data.data.direccion && data.data.direccion.ciudad != null){
        console.log("entra if ");
        getCiudades(data.data.direccion.ciudad.departamento.id);
       }
    });
};

// Validar dirección cliente
const validarDireccionCliente = (dir) => {
  if (dir.ciudad == null || dir.calle1.trim() ) {
    return false;
  }
    if (algunCampoTieneValor(dir) && (!dir.calle1 || !dir.ciudad)) {
        return false;
    }
    if (!algunCampoTieneValor(dir)) {
        return true;
    }
    return true;
};

//Guardar dirección 
const saveDireccion = () =>{
  submitted.value = true;
  if(direccionValida(direccion.value)){
    direccion.value.tipo = 'E';
    direccion.value.persona = cliente.value;
    direccion.value.direccion = generarDireccion(direccion.value);
    DireccionServices.saveDireccion(direccion.value).then((response)=>{
      direccionesCliente.value.push(response.data);
    });
  }
  hideDialog();
}

//Guardar cliente
const saveCliente = () => {
    submitted.value = true;
    console.log(direccion.value.calle1);
    if (cliente?.value.nombre?.trim() && validarDireccionCliente(direccion.value) ) {
        direccion.value.tipo = 'P';
        personaCreationDTO.value = {personaEntity: cliente.value, direccion: direccion.value};
        if (cliente.value.id) {
            PersonaServices.modificarPersona(cliente.value.id, personaCreationDTO.value).then((response)=>{
            console.log("mod");
                eliminarClienteSelected();
                clientes.value[findIndexById(cliente.value.id)] = cliente.value;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
                selectedCliente.value = response.data;
                
                mostrarCliente();
                direccion.value.tipo = null;
            }).catch(
                (error)=>messageError("error")
            );
        }
        else {
            PersonaServices.registrarPersona(personaCreationDTO.value).then((response)=>{
            console.log("reg");
            console.log(personaCreationDTO.value);
                clientes.value.push(response.data);
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
                selectedCliente.value = response.data;
                mostrarCliente();
                direccion.value.tipo = null;
            }).catch(
                (error)=>messageError("error")
            );
        }
        clienteDialog.value = false;
        cliente.value = {};
    }
};

//Obtener ubicación
const getUbicacion = (lat, lng) =>{
    console.log(lat);
    console.log(lng);
    direccion.value.lat = lat;
    direccion.value.lng = lng;
}


//Entrega
const searchMediosEnvio= (id) => {
    console.log("idciudad");
    console.log(id);
    EnvioServices.obtenerCostosEnvioByCiudad(id).then((data) => {
        medios.value=data.data;
        selectedEnvio.value = { };
    });
}

const changeModalidadEntrega = () => {
    if (selectedFormaEntrega.value && selectedFormaEntrega.value.descripcion === "Retiro") {
        selectedEnvio.value = {};
        submitted.value = false;
    }else{
        medios.value = null;
    }
}

const inicializarDatosEntrega = () => {
    selectedFormaEntrega.value = formasEntrega.value[0];
    selectedEnvio.value = {};
    direccionSelected.value = null;
    direccionEnvio.value = {};
    infoEntrega.value = [{
    valor: "Retiro"}];
    productos.value.setDetalleEnvio(null);
}

const direccionValida = (dir) => {
    console.log("valdirenvio");
    console.log(dir);
    if ((!dir.calle1 || !dir.ciudad)) {
        console.log("val envio false");
        return false;
    }  
    return true;
};

const getUbicacionEnvio = (lat, lng) =>{
    console.log(lat);
    console.log(lng);
    direccionEnvio.value.lat = lat;
    direccionEnvio.value.lng = lng;
}

const añadirDatoEntrega = () =>{
    if (selectedCliente.value) {
        guardarEstadoEntregaOriginal();
        visible.value = true;
    getDireccionesCliente();
    } else {
        toast.add({ severity: 'info', summary: 'Info Message', detail: 'Seleccione un cliente', life: 3000 });
    }
}

const agregarNuevaDireccion = () =>{
  clienteDialog.value = true;
}

const guardarEstadoEntregaOriginal = () =>{
    datosEntregaUltimoGuardado.value.modalidad = selectedFormaEntrega.value;
    datosEntregaUltimoGuardado.value.nuevaDireccion = nuevaDireccion.value;
    datosEntregaUltimoGuardado.value.direccionSelected = direccionSelected.value;
    datosEntregaUltimoGuardado.value.direccion = direccionEnvio.value;
    datosEntregaUltimoGuardado.value.envio = selectedEnvio.value;
}

const verEntrega = (id) =>{
    router.push({name: 'entrega', params: {id}});
}

const saveEntrega = () =>{
    direccionSubmitted.value = true;
    if (datosEntregaValidos()) {
        if (selectedFormaEntrega.value.descripcion === 'Envío' &&
            nuevaDireccion.value) {
            direccionEnvio.value.persona = selectedCliente.value;
            direccionEnvio.value.tipo = 'E';
            generarDireccion(direccionEnvio.value);
        }

        if(selectedFormaEntrega.value.descripcion === 'Retiro'){
            productos.value.setDetalleEnvio(null);
        }else{
            productos.value.setDetalleEnvio(selectedEnvio.value);
        }
        showInfoEntrega();
        visible.value = false;
        direccionSubmitted.value = false;
    }   
    
}

const showInfoEntrega= () => {
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

const datosEntregaValidos = () =>{
    if(selectedFormaEntrega.value.descripcion === 'Retiro'){
        return true;
    }
    if (!selectedEnvio.value.id) {
        return false;
    }
    if (!nuevaDireccion.value && direccionSelected.value !== undefined && direccionSelected.value !== null ) {
        return true;
    }
    return direccionValida(direccionEnvio.value);
}


const closeEntregaDialog = () => {
    selectedFormaEntrega.value = datosEntregaUltimoGuardado.value.modalidad;
    nuevaDireccion.value = datosEntregaUltimoGuardado.value.nuevaDireccion;
    direccionSelected.value = datosEntregaUltimoGuardado.value.direccionSelected;
    direccionEnvio.value = datosEntregaUltimoGuardado.value.direccion;
    selectedEnvio.value = datosEntregaUltimoGuardado.value.envio;
    direccionSubmitted.value = false;
    visible.value = false;
}

const getCiudades = (id) => {
    CiudadServices.obtenerCiudadesByDepartamento(id).then((data) => {
        console.log("data ciudades");
        console.log(data.data);
       ciudades.value = data.data;   
    });
};

const verentregas = () =>{
    router.push({name: 'entregas'});
}

const isRetiro = (descripcion) => {
  switch (descripcion) {
       case 'Retiro':
           return true;
       default:
           return false;
   }
};

const isEnvio = (descripcion) => {
  switch (descripcion) {
       case 'Envio':
           return true;
       default:
           return false;
   }
};

const hideDialog = () => {
    clienteDialog.value = false;
    direccion.value = {};
    submitted.value = false;
};

const generarDireccion = (dir) => {
  console.log(dir);
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " +selectedOp.value + " "+ dir.calle2;
    }
    if (dir.calle3?.trim()) {
        d = d + " y " + dir.calle3;
    }

    return d;
};

const algunCampoTieneValor = (dir) => {
      return Object.values(dir).some(valor => valor !== "" && valor !== null && valor !== undefined);
}

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

const cambiarModoEntrega= (modalidad) => {
    let descripcion = modalidad?.descripcion;

    if (descripcion == 'Retiro') {
      entrega.value.empresaTransporte = null;
      entrega.value.direccionEnvio = null;
    } else {
      entrega.value.puntoEntrega = null;
      getDireccionesCliente(cliente.value.id);
    }
};

//-----


//Pedido
const verPedido = (id) =>{
    router.push({name: 'entrega', params: {id}});
}

const submit = () =>{
    if (!error.value) {
        let entregaDTO = {entrega: entrega.value, detalle: detalles.value};
        EntregaServices.modificarEntrega(entrega.value.id ,entregaDTO).then((response)=>{
            verPedido(entrega.value.id);
        }).catch(
        (error)=>console.log(error)
        );
    }
}

const validarForm = (event) => {
    console.log("selectedCliente.value");
    console.log(selectedCliente.value);
    mensaje.value = [];
    error.value = false;
    if (entrega.value.modoEntrega == null) {
      error.value = true;
      mensaje.value.push("Debe seleccionar una modalidad de entrega");
    }
    if (isRetiro(entrega.value.modoEntrega?.descripcion) && entrega.value.puntoEntrega == null ) {
      error.value = true;
      mensaje.value.push("Debe seleccionar un punto de entrega");
    }
    submit();
}
//---

</script>

<template>
    <div class="flex p-fluid justify-content-center " >
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
                <label for="description">Calle Principal</label>
                <InputText fluid id="description" v-model="direccion.calle1" required="true" :class="{'p-invalid': submitted && !direccionValida(direccion) && !direccion.calle1}" />
                <small class="p-error" v-if="submitted && !direccionValida(direccion) && !direccion.calle1">Ingrese Calle Principal</small>
            </div>            
            <div class="field">
                <label for="description">Calle 2</label>
                <InputGroup fluid>
                    <Dropdown v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
                    <InputText id="description" v-model="direccion.calle2" required="true"  />
                </InputGroup>
            </div>
            <div class="field" v-if="selectedOp=='Entre'">
                <label for="description">Calle 3</label>
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
                <Dropdown fluid v-model="direccion.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad" :class="{'p-invalid': submitted && !direccionValida(direccion) && !direccion.ciudad}"  />
                <small class="p-error" v-if="submitted && !direccionValida(direccion) && !direccion.ciudad">Ingrese Ciudad</small>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveDireccion" />
            </template>
        </Dialog>
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Modificar Entrega N° {{ entrega.id }}</h3>
                </div>
            </template>
            <template #icons>
                <div class="flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;"  @click="verPedido(entrega.id)" />
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
                                Fecha: <DatePicker fluid dateFormat="dd/mm/yy" v-model="entrega.fecha" showIcon showTime hourFormat="24" iconDisplay="input" />
                            </div> 
                                <div class="field">
                                    Observaciones: 
                                    <Textarea fluid v-model="entrega.observaciones" rows="3" cols="33" />
                                </div>
                        </template>
                    </Card>
                </div> 
                <div class="field col-12 md:col-6">
                    <Card >
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Entrega
                                </div>    
                            </div>
                        </template>
                        <template #content>
                                <div class="field" >
                                    Modalidad: 
                                    <Select fluid v-model="entrega.modoEntrega" :options="modalidadesEntrega" optionLabel="descripcion" placeholder="Seleccione una modalidad" class="w-full md:w-56" @change="cambiarModoEntrega(entrega.modoEntrega)" />
                                </div> 
                                <div class="field" v-if="isRetiro(entrega.modoEntrega?.descripcion)" >
                                    Punto de retiro: 
                                    <Select fluid v-model="entrega.puntoEntrega" :options="puntosEntrega" optionLabel="descripcion" placeholder="Seleccione un punto de retiro" class="w-full md:w-56" />
                                </div>
                                <div class="field" v-else-if="isEnvio(entrega.modoEntrega?.descripcion)" >
                                    Delivery: 
                                    <Select fluid v-model="entrega.empresaTransporte" :options="empresasTransporte" optionLabel="descripcion" placeholder="Seleccione un delivery" class="w-full md:w-56" />
                                </div>
                                <div class="field" v-if="isEnvio(entrega.modoEntrega?.descripcion)">
                                <div>
                                    <label for="description">Dirección de envío:</label>
                                    <Select fluid v-model="entrega.direccionEnvio" :options="direccionesCliente" optionLabel="descripcion" placeholder="Seleccione una dirección" class="w-full md:w-56">
                                        <template #value="slotProps">
                                            <div v-if="slotProps.value" class="flex items-center">
                                                <div>{{ slotProps.value.direccion }}<br>{{ slotProps.value.ciudad?.descripcion }}-> {{ slotProps.value.ciudad?.departamento?.descripcion }}</div>
                                            </div>
                                            <span v-else>
                                                {{ slotProps.placeholder }}
                                            </span>
                                        </template>
                                        <template fluid #option="slotProps">
                                            {{ slotProps.option.direccion }}<br>{{ slotProps.option.ciudad?.descripcion }}<br>{{ slotProps.option.ciudad?.departamento?.descripcion }}
                                        </template>
                                    </Select>
                                    <div style="justify-content: start;" >
                                        <Button label="+ Nueva Direccion" link @click="agregarNuevaDireccion()" style="justify-content: start; width: max-content;" />
                                    </div>
                                </div>
                            </div>
                        </template>
                    </Card>
                </div>  
                <div class="field col-12 md:col-12">
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
                                            <DataTable class="tablaCarrito" ref="dt" :value="detalles" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
                                                <Column  class="col" field="detallePedido.producto.nombre" header="Nombre" aria-sort="none" ></Column>
                                                <Column class="col" field="producto.precio"  header="Solicitado" aria-sort="none" >
                                                    <template #body="slotProps">
                                                        <div class="flex-auto p-fluid" >
                                                            {{  formatearNumero(slotProps.data.detallePedido.cantSolicitado) }}
                                                        </div> 
                                                    </template>
                                                </Column>
                                                <Column  class="col" field="cantidad" header="Preparado" aria-sort="none">
                                                </Column>
                                            </DataTable>
                                        </div>
                                    </div>
                                </div>
                            </template>    
                        </Card>
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