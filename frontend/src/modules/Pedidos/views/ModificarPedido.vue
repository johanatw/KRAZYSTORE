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
import { ref, onMounted } from "vue";
import RadioButton from "primevue/radiobutton";
import InputGroup from 'primevue/inputgroup';
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
const messageError = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'cliente',
        header: 'Ocurrio un error',
        message: msg.toUpperCase(),

        accept: () => {
            //getDetalle();
            //getPedido();
            //verPedido(router.currentRoute.value.params.id);
            
        },
    });
};

onMounted(() => {

    CajaServices.obtenerPagosPedido(router.currentRoute.value.params.id).then((data) => {
        pedidoTotalPagado.value = data.data;
    });
    

    if (pedido.value.costoEnvio) {
            costo.value = pedido.value.costoEnvio.costo;
        }
        

    getPedido();
   
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

async function getDetalle() {
    await PedidoServices.getDetalle(router.currentRoute.value.params.id).then((data) => {
        console.log("Este", data.data);
        productos.value.setDetalle(data.data);

    });
    
}

const getPedido = () => {
    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        /*formaPago.value.setPago(data.data.formaPago);
        modoEntrega.value.setEntrega(data.data.modoEntrega, data.data.costoEnvio);
        cliente.value.setCliente(data.data.cliente);*/
        //getCliente(data.data.cliente);
        pedido.value = data.data.pedido;
        let det = data.data.detalle;

        selectedCliente.value = pedido.value.cliente;
        selectedFormaEntrega.value = pedido.value.modoEntrega;
        selectedEnvio.value = pedido.value.costoEnvio;
        direccionSelected.value = pedido.value.direccionEnvio;
        
        console.log(det);
        
        productos.value.setDetalle(det);
        //getEntrega(data.data.modoEntrega, data.data.costoEnvio);

       

        fecha.value = new Date(pedido.value.fecha);

        
        mostrarCliente();
        saveEntrega();
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



const eliminarClienteSelected = () =>{
    
    document.getElementById("infoCliente").remove();
    selectedCliente.value = null;
    direccion.value = {};
    clienteSeleccionado.value = false;
    nuevaDireccion.value = false;
    inicializarDatosEntrega();
}

const registrarCliente = () =>{
    
    cliente.value = {};
    clienteDialog.value = true;
}

const modificarCliente = (cli) => {
    PersonaServices.getPersona(cli.id).then((data) => {
        console.log("data direccion");
        console.log(data.data.direccion);
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

const validarDireccionCliente = (dir) => {
    if (algunCampoTieneValor(dir) && (!dir.calle1 || !dir.ciudad)) {
        return false;
    }

    if (!algunCampoTieneValor(dir)) {
        return true;
    }
    
    
    return true;

};

const saveCliente = () => {
    submitted.value = true;
    console.log(direccion.value.calle1);
    if (cliente?.value.nombre?.trim() && validarDireccionCliente(direccion.value) ) {
        direccion.value.tipo = 'P';
        generarDireccion(direccion.value);
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

const validarDireccionEnvio = (dir) => {
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

const agregarNuevaDireccion = (val) =>{
    nuevaDireccion.value = val;
    direccionSelected.value = {};
    selectedEnvio.value = {};
    medios.value = null;
}

const guardarEstadoEntregaOriginal = () =>{
    datosEntregaUltimoGuardado.value.modalidad = selectedFormaEntrega.value;
    datosEntregaUltimoGuardado.value.nuevaDireccion = nuevaDireccion.value;
    datosEntregaUltimoGuardado.value.direccionSelected = direccionSelected.value;
    datosEntregaUltimoGuardado.value.direccion = direccionEnvio.value;
    datosEntregaUltimoGuardado.value.envio = selectedEnvio.value;
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

    
    return validarDireccionEnvio(direccionEnvio.value);
    
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



//Otros

const getDireccionesCliente = () =>{
    DireccionServices.getDireccionesCliente(selectedCliente.value.id).then((data) => {
        console.log("data.data");
        console.log(data.data);
        direcciones.value=data.data;
        if (direcciones.value.length < 1) {
            console.log("direcciones.value.length");
            console.log(direcciones.value.length);
            nuevaDireccion.value = true;
        }

    });
}

const getCiudades = (id) => {
    CiudadServices.obtenerCiudadesByDepartamento(id).then((data) => {
        console.log("data ciudades");
        console.log(data.data);
       ciudades.value = data.data;
       
    });
    
};

const hideDialog = () => {
    clienteDialog.value = false;
    direccion.value = {};
    submitted.value = false;
};

const generarDireccion = (dir) => {
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " +selectedOp.value + " "+ dir.calle2;
    }
    if (dir.calle3?.trim()) {
        d = d + " y " + dir.calle3;
    }

    dir.direccion = d;
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




//Pedido

const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
}

const submit = () =>{

    if (!error.value) {

        
        
        console.log("pedidodetalle", productos.value.detalles);
        pedido.value.cliente = selectedCliente.value;
        pedido.value.fecha = fecha.value;
        pedido.value.modoEntrega = selectedFormaEntrega.value;
        if (selectedFormaEntrega.value?.descripcion === "Envío") {
            pedido.value.costoEnvio = selectedEnvio.value;

            if (nuevaDireccion.value) {
                pedido.value.direccionEnvio = direccionEnvio.value; 
            } else {
                pedido.value.direccionEnvio = direccionSelected.value;
            }

        }

        if (selectedFormaEntrega.value?.descripcion === 'Retiro') {
            pedido.value.direccionEnvio = null;
            pedido.value.costoEnvio = null;
        }
        
        detallePedido.value = productos.value.detalles;
       // console.log("submitdetalle",);
        //ex.value = productos.value.existencias;
        pedido.value.total = productos.value.subTotal;
        let pedidoDTO = {pedido: pedido.value, detalle: detallePedido.value};
        PedidoServices.modificarPedido(pedido.value.id, pedidoDTO).then((response)=>{
            verPedido(response.data.id);
        }).catch(
        (error)=>messageError(error.response.data.mensaje)
        );
    }
 }


const validarForm = (event) => {
    console.log("selectedCliente.value");
    console.log(selectedCliente.value);
    mensaje.value = [];
    error.value = false;
    if (selectedCliente.value) {
        

    } else {
        error.value = true;
            mensaje.value.push("Debe seleccionar un cliente");
    }

    if (productos.value.subTotal <1) {
        error.value = true;
        mensaje.value.push("Debe añadir productos al pedido");
    }
    
  
    submit();

}


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

           
        <div class="field" v-show="selectedFormaEntrega.descripcion === 'Envío'">
            <div v-if="direcciones.length > 0 && !nuevaDireccion">
                <label for="description">Dirección</label>
                <div v-for="d in direcciones" :key="d.id">
                    <RadioButton v-model="direccionSelected" :value="d" name="dynamic" @change="searchMediosEnvio(d.ciudad.id)" />
                    <label :for="d.id" class="ml-2">{{ d.direccion }} <br> 
                        <div style="font-size: small;">{{ d.ciudad.departamento.descripcion }} > {{ d.ciudad.descripcion }}</div></label>
                </div>
                <div style="justify-content: start;" >
                    <Button label="+ Nueva Direccion" link @click="agregarNuevaDireccion(true)" style="justify-content: start; width: max-content;" />
                </div>
            </div>
            <div v-if="nuevaDireccion">
                <div class="field">
                    <label for="description">Calle Principal</label>
                    <InputText id="description" v-model="direccionEnvio.calle1" required="true"  :class="{'p-invalid': direccionSubmitted && !direccionEnvio.calle1}"   />
                    <small class="p-error" v-if="direccionSubmitted && !direccionEnvio.calle1">Ingrese Calle principal</small>
                </div>
            
                <div class="field">
                    <label for="description">Calle 2</label>
                    <InputGroup>
                        <Dropdown v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
                        <InputText id="description" v-model="direccionEnvio.calle2" required="true"  />
                    </InputGroup>
                </div>
                <div class="field" v-if="selectedOp=='Entre'">
                    <label for="description">Calle 3</label>
                    <InputText id="description" v-model="direccionEnvio.calle3" required="true"  />
                </div>
                <div class="field">
                    <label for="description">N° Casa</label>
                    <InputText id="description" v-model="direccionEnvio.nroCasa" required="true"  />
                </div>
                <div class="field " >
                    <label for="nombreu">Departamento</label>
                    <Dropdown v-model="direccionEnvio.departamento" :options="departamentos" optionLabel="descripcion" placeholder="Seleccione un departamento"  @change="getCiudades(direccionEnvio.departamento.id)"  />
                </div>
                <div class="field " >
                    <label for="nombreu">Ciudad</label>
                    <Dropdown v-model="direccionEnvio.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad"  @change="searchMediosEnvio(direccionEnvio.ciudad.id)" :class="{'p-invalid': direccionSubmitted && !direccionEnvio.ciudad}" />
                    <small class="p-error" v-if="direccionSubmitted && !direccionEnvio.ciudad">Ingrese Ciudad</small>
                </div>
                <div class="field">
                    <label for="description">Ubicar en el mapa</label>
                    <MapComponent @getUbicacion="getUbicacionEnvio"  :lat="direccionEnvio.lat" :lng="direccionEnvio.lng"/>
                </div>
                <div class="flex" v-if="direcciones.length > 0" style="justify-content: start; width: max-content;" >
                    <Button label="Ver direcciones" link @click="agregarNuevaDireccion(false)" />
                </div>

            </div>
            <div class="field ">
                            
                <label for="state">Envío con:&nbsp;</label>
                <Dropdown v-model="selectedEnvio" :options="medios" optionLabel="envio" placeholder="Seleccione un elemento" class="w-full md:w-14rem" :class="{'p-invalid': direccionSubmitted && !selectedEnvio.id}">
                    <template #value="slotProps">
                        <div v-if="slotProps.value && slotProps.value.envio" class="flex align-items-center">
                            <div>{{ slotProps.value.envio.descripcion }} - {{ slotProps.value.costo }} Gs</div>
                        </div>
                        <span v-else>
                            {{ slotProps.placeholder }}
                        </span>
                    </template>
                    <template #option="slotProps">
                        <div class="flex align-items-center">
                            <div>{{ slotProps.option.envio.descripcion}} - {{ slotProps.option.costo }} Gs</div>
                            
                        </div>
                    </template>
                </Dropdown>
                <small class="p-error" v-if="direccionSubmitted && !selectedEnvio.id">Ingrese Medio de envío</small>
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
            <InputText id="name" v-model.trim="cliente.nombre" required="true" autofocus :class="{'p-invalid': submitted && !cliente.nombre}" />
            <small class="p-error" v-if="submitted && !cliente.nombre">Ingrese un Nombre</small>
        </div>
        <div class="field">
            <label for="description">Apellido</label>
            <InputText id="description" v-model="cliente.apellido" required="true"  />
        </div>
        <div class="field">
            <label for="inventoryStatus" class="mb-3">Tipo Documento</label>
            <Dropdown id="inventoryStatus" v-model="cliente.tipoDoc" :options="documentos" optionLabel="descripcion" placeholder="Select a Status" />
        </div>
        <div class="field">
            <label for="description">Nro Documento</label>
            <InputText id="description" v-model="cliente.nroDoc" required="true"  />
        </div>
        <div class="field">
            <label for="description">Telefono</label>
            <InputText id="description" v-model="cliente.telefono" required="true"  />
        </div>
        <div class="field">
            <label for="description">Calle Principal</label>
            <InputText id="description" v-model="direccion.calle1" required="true" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.calle1}" />
            <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.calle1">Ingrese Calle Principal</small>
        </div>
        
        <div class="field">
            <label for="description">Calle 2</label>
            <InputGroup>
                <Dropdown v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
                <InputText id="description" v-model="direccion.calle2" required="true"  />
            </InputGroup>
        </div>
        <div class="field" v-if="selectedOp=='Entre'">
            <label for="description">Calle 3</label>
            <InputText id="description" v-model="direccion.calle3" required="true"  />
        </div>
        <div class="field">
            <label for="description">N° Casa</label>
            <InputText id="description" v-model="direccion.nroCasa" required="true"  />
        </div>
        <div class="field " >
            <label for="nombreu">Departamento</label>
            <Dropdown v-model="direccion.departamento" :options="departamentos" optionLabel="descripcion" placeholder="Seleccione un departamento" @change="getCiudades(direccion.departamento.id)"  />
        </div>
        <div class="field " >
            <label for="nombreu">Ciudad</label>
            <Dropdown v-model="direccion.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.ciudad}"  />
            <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.ciudad">Ingrese Ciudad</small>
        </div>
        <div class="field">
            <label for="description">Ubicar en el mapa</label>
            <MapComponent @getUbicacion="getUbicacion" ref="map" :lat="direccion.lat" :lng="direccion.lng" />
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
            <div class="field col-12 md:col-12">
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
                                Fecha: <DatePicker dateFormat="dd/mm/yy" v-model="fecha" showIcon iconDisplay="input" />
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
            <div id="clienteDiv">

            </div>
            <div class="flex flex-column align-options-start">
            
                <div v-if="!clienteSeleccionado" >
                    
                    <AutoComplete v-model="selectedCliente" fluid optionLabel="nombre" forceSelection :suggestions="filteredClientes" @complete="search" @item-select="mostrarCliente">
                    <template #option="slotProps">
                        <div class="flex flex-column align-options-start">
                            <div>{{ slotProps.option.nombre }}</div>
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
            <div class="field col-12 md:col-6">
                <Card >
        <template #title>
            <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    Entrega
                </div>    
                <div>
                    <Button icon="pi pi-pencil" link @click="añadirDatoEntrega()"/>
                </div>              
            
            </div>
            
        </template>
        <template #content>
            <div id="entregaDiv"  >
                <p class="m-0">
                <div v-for="v in infoEntrega">
                    {{ v.valor }} <br>
                </div>
                
            </p>
                
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