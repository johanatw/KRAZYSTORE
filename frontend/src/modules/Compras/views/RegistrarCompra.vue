<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import CardEntrega from "@/modules/Pedidos/components/CardEntrega.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Card from "primevue/card";
import CardPago from "@/modules/Pedidos/components/CardPago.vue";
import MiCard from "@/modules/Pedidos/components/MiCard.vue";
import Button from 'primevue/button';
import {CiudadServices } from '@/services/CiudadServices';
import {EnvioServices} from '@/services/EnvioServices';
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { ref, onMounted } from "vue";
import RadioButton from "primevue/radiobutton";
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import {DireccionServices} from '@/services/DireccionServices';
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import { ProductoServices } from "@/services/ProductoServices";
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import Calendar from "primevue/calendar";
const map = ref();
const direcciones = ref([]);
const total = ref(0);
const direccion = ref({});
const pedido = ref({ });
const selectedCliente = ref();
const clienteDialog = ref(false);
const personaCreationDTO = ref({});
const submitted = ref(false);
const direccionSubmitted = ref(false);
const selectedClient = ref({});
const clienteSeleccionado = ref(false);
const detallePedido = ref({ });
const mensaje = ref([]);
const ciudades = ref([]);
const departamentos = ref([]);
const formasEntrega = ref([{id: 1, descripcion: 'Retiro'},{id:2, descripcion: 'Envío'}]);
const barrios = ref([]);
const detalleFacturar = ref([]);
const modoEntrega = ref();
const visible = ref(false);
const selectedFormaEntrega = ref({id: 1, descripcion: 'Retiro'});
const formaPago = ref();
const cancelSubmit = ref(false);
const pedidoId = ref(0);
const cliente = ref({});
const cardCliente = ref();
const selectedOp = ref('Casi');
const cardEntrega = ref();
const direccionSelected = ref();
const direccionEnvio = ref({});
const searchCard = ref();
const productos= ref();
const clientes=ref();
const filteredClientes = ref();
const entrega = ref();
const envioSelected = ref();
const error = ref(false);
const opciones = ref(['Casi','Entre']);
const selectedEnvio = ref({});
const medios = ref();
const infoCliente = ref([{
    valor: "Cliente no seleccionado"
}])
const infoEntrega = ref([{
    valor: "Metodo de Entrega no seleccionado"
}])

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

const searchEnvio= (id) => {
    console.log("idciudad");
    console.log(id);
    EnvioServices.obtenerCostosEnvioByCiudad(id).then((data) => {
 
        medios.value=data.data;
        selectedEnvio.value = { };
        

    });
}

const changeEntrega = () => {
    if (selectedFormaEntrega.value && selectedFormaEntrega.value.descripcion === "Retiro") {
        selectedEnvio.value = {};
    }else{
        medios.value = null;
    }
}

const inicializarDatosEntrega = () => {

    selectedFormaEntrega.value = formasEntrega.value[0];
    selectedEnvio.value = {};
    direccionSelected.value = null;
    direccionEnvio.value = {};

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
        visible.value = true;
    getDireccionesCliente();
    } else {
        toast.add({ severity: 'info', summary: 'Info Message', detail: 'Message Content', life: 3000 });
    }
    
}

const saveEntrega = () =>{
    
    direccionSubmitted.value = true;
    if (datosEntregaValidos()) {
        if (selectedFormaEntrega.value.descripcion === 'Envío' &&
         direccionSelected.value === undefined && direccionSelected.value === null ) {
            direccionEnvio.value.persona = selectedCliente.value;
            direccionEnvio.value.tipo = 'E';
            generarDireccion(direccionEnvio.value);
        }
        visible.value = false;
        direccionSubmitted.value = false;
    }   
    
}

const datosEntregaValidos = () =>{
    if(selectedFormaEntrega.value.descripcion === 'Retiro'){
        return true;
    }

    if (!selectedEnvio.value.id) {
        return false;
    }
    
    if (direccionSelected.value !== undefined && direccionSelected.value !== null ) {
        return true;
    }

    
    return validarDireccionEnvio(direccionEnvio.value);
    
}






//Otros

const getDireccionesCliente = () =>{
    DireccionServices.getDireccionesCliente(selectedCliente.value.id).then((data) => {
        console.log("data.data");
        console.log(data.data);
        direcciones.value=data.data;

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
   
    submitted.value = false;
};

const generarDireccion = (dir) => {
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " +selectedOp.value + " "+ dir.calle2;
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


const getEntrega = (formaEntrega, envio) =>{
    if (formaEntrega) {
        entrega.value = formaEntrega;
    envioSelected.value = envio;
    infoEntrega.value = [];
    let valor = {valor: formaEntrega.descripcion};
    infoEntrega.value.push(valor);
    if (envio) {
        valor = {valor: 'a ' + envio.ciudad.descripcion};
        infoEntrega.value.push(valor);
        
        valor = {valor: 'Vía ' + envio.envio.descripcion + ' - '+envio.costo+' Gs'};
        infoEntrega.value.push(valor);
    }
    
    productos.value.setDetalleEnvio(envio);
    }
  
}


const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
}

const submit = () =>{

    if (!error.value) {

        
        
        console.log("pedidodetalle", productos.value.detalles);
        pedido.value.cliente = selectedCliente.value;

        pedido.value.modoEntrega = selectedFormaEntrega.value;
        if (selectedFormaEntrega.value?.descripcion === "Envío") {
            pedido.value.costoEnvio = selectedEnvio.value;

            if (direccionSelected.value) {
                pedido.value.direccionEnvio = direccionSelected.value;
            }else if(direccion.value){
                pedido.value.direccionEnvio = direccionEnvio.value;   
            }
        }

        if (selectedFormaEntrega.value?.descripcion === 'Retiro') {
            pedido.value.direccionEnvio = null;
        }
        
        detallePedido.value = productos.value.detalles;
       // console.log("submitdetalle",);
        //ex.value = productos.value.existencias;
        pedido.value.total = productos.value.subTotal;
        let pedidoDTO = {pedido: pedido.value, detalle: detallePedido.value};
        PedidoServices.savePedido(pedidoDTO).then((response)=>{
            pedidoId.value = response.data.id;
            verPedido(pedidoId.value);
        });
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
    

<div class="card flex p-fluid justify-content-center " >
   

   

    <Panel style=" position: relative; width: 80%;" >
        <template #header>
            <div class="flex align-items-center gap-2">
                <h3 class="font-bold">Compra</h3>
            </div>
        </template>
        <template #icons>
                
            <Button  label="Guardar"   />
    
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
            <div class="formgrid grid" >  
          
            
           <div class="field col-12 md:col-12">
            <div class="field " style=" display: flex; flex-direction: row; ">
                    <label for="description" style="width: 8rem;">Proveedor: </label>
                    <InputGroup>
                        <AutoComplete v-model="selectedCliente" optionLabel="nombre" forceSelection :suggestions="filteredClientes" @complete="search" >
                    <template #option="slotProps">
                        <div class="flex flex-column align-options-start">
        
                        </div>
                    </template>
                </AutoComplete>
                <Button class="pi pi-plus" link />
                    </InputGroup>
                </div>
            
            
            
            <div class="field">
                <div  style=" display: flex; flex-direction: row; ">
                    <label for="description" style="width: 8rem;">N° Factura:</label>
                    <InputText class="p-fluid" id="username" style="height: 2rem;" v-model="value" aria-describedby="username-help" />
                </div>
                </div>
                <div class="flex field" >
                    <div  style=" display: flex; flex-direction: row; ">
                    <label for="description" style="width: 8rem;" >Fecha: </label>
                    <Calendar class="p-fluid" v-model="buttondisplay" showIcon :showOnFocus="false" />
                </div>
                </div>
            
            
            </div> 
        </div>
            <div class="field col-12 md:col-12">
                <div class="grid">
                    <div class="col-12" >
                        <Card >
                            <template #title>
            <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    Productos
                </div>
                <div >
                    <Button label="+ Producto" link @click="visible = true" />
                    </div>

            </div>
            
        </template>
                    
                        <template #content>
                            
                                
                                <div class="card" style="width: 100%;">
    <div class="flex card-container" style="width: 100%;">
        <DataTable class="tablaCarrito" ref="dt" :value="detalleFacturar" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
          
         <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
         <Column class="col" field="producto.precio"  header="Precio" aria-sort="none" >
          
        </Column>
         
        <Column  class="col" field="cantidad" header="Uds." aria-sort="none">
        
             
         </Column>
         
         <Column  class="col" field="subTotal" header="Total" aria-sort="none" >
           
         </Column>
         <Column class="col" :exportable="false" style="min-width:1rem">
          
         </Column>
     </DataTable>
   </div>
 </div>
                                <div class="grid" style="margin-top: 1rem;">
                                    
                                    
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; font-weight: bold; font-size: 16px;">
                                            Total: 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; font-weight: bold; font-size: 16px;" >
                                            {{ total.toLocaleString("de-DE") }}
                                        </div>

                                    </div>
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            IVA 10%: 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ (montoIva = Math.round(total/11)).toLocaleString("de-DE") }}
                                        </div>
                                    </div>
            

                                </div>
                                
                                </template>    
                    </Card>
                        
                    </div>
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