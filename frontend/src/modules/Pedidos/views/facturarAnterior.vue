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
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { ProductoServices } from '@/services/ProductoServices';
import { VentaServices } from '@/services/VentaServices';
import {PedidoServices} from '@/services/PedidoServices';
import { CiudadServices } from '@/services/CiudadServices';
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import { formatearNumero } from "@/utils/utils";

//Variables
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
const pedido = ref();
const error = ref(false);
const opciones = ref(['Casi','Entre']);
const infoEntrega = ref([{
    valor: "Retiro"
}])

import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';

import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

const confirm = useConfirm();
const toast = useToast();
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

//Variables para calculos
const detalleFacturar = ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const detalle = ref({});
const total = ref(0);


onMounted(() => {
    getPedido();
    ProductoServices.obtenerProductos().then((data) => {
     productos.value = data.data
     console.log("productosssss",productos.value);
    });
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

async function getPedido() {
    await PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        selectedCliente.value = data.data.pedido.cliente;
        mostrarCliente();
        pedido.value = data.data.pedido;
        data.data.detalle.forEach(element => {
            if ( element.producto.cantStock > 0 ) {
            switch (true) { 
                case element.cantidad >= element.producto.cantStock:
                    element.cantidadSolicitada = element.cantidad;
                    element.cantidad = element.producto.cantStock - element.cantidadFacturada;
                    detalleFacturar.value.push(element); 
                break;
                case element.cantidad < element.producto.cantStock:
                    element.cantidadSolicitada = element.cantidad;
                    element.cantidad = element.cantidad - element.cantidadFacturada;
                    detalleFacturar.value.push(element);
                break;
            }
            total.value += (element.cantidad * element.precio);
        }
        })
    });
}


async function getDetallePedido() {
    await PedidoServices.getDetallePedido(router.currentRoute.value.params.id).then((data) => {
        data.data.forEach(element => {
            if ( element.producto.cantStock > 0 ) {
            switch (true) { 
                case element.cantidad >= element.producto.cantStock:
                    element.cantidadSolicitada = element.cantidad;
                    element.cantidad = element.producto.cantStock - element.cantidadFacturada;
                    detalleFacturar.value.push(element);
                    break;
                case element.cantidad < element.producto.cantStock:
                    element.cantidadSolicitada = element.cantidad;
                    element.cantidad = element.cantidad - element.cantidadFacturada;
                    detalleFacturar.value.push(element);  
                    break;
            }
            total.value += (element.cantidad * element.producto.precio);
        }
        })
    });
}


const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

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
                    if(cliente.nroDoc){
                        return (cliente.nroDoc).toString().startsWith(event.query);
                    }else if(cliente.telefono) {
                        return (cliente.telefono).toString().startsWith(event.query);
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
        submitted.value = false;
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

//Otros
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


const validarForm = () => {
    console.log("selectedCliente.value");
    console.log(selectedCliente.value);
    mensaje.value = [];
    error.value = false;
    if (selectedCliente.value) {
        if (!selectedCliente.value.nroDoc) {
            error.value = true;
            mensaje.value.push("Número de documento del cliente faltante");
        }
    } else {
        error.value = true;
            mensaje.value.push("Debe seleccionar un cliente");
    }
    if (total.value <1) {
        error.value = true;
        mensaje.value.push("Debe añadir productos");
    }
    guardarFactura();
}

const addItem = (item) => {
    let index = detalleFacturar.value.findIndex((loopVariable) => loopVariable.producto.id === item.id);
    if (index>-1) {
        detalleFacturar.value[index].cantidad++;
        console.log("holaaa");
    } else {
        console.log("holaaaitem",item);
        detalle.value.producto = {};
        detalle.value.producto = item;
        detalle.value.cantDisponible = item.cantDisponible;
        detalle.value.cantReservada = item.cantReservada;
        detalle.value.cantStock = item.cantStock;
        detalle.value.cantPreVenta = item.cantPreVenta;
        detalle.value.cantidad = 1;
        detalle.value.precio = item.precio;
        detalle.value.subtotal = detalle.value.precio * detalle.value.cantidad;
        detalleFacturar.value.push(detalle.value);
        detalle.value= {};
    }
    item.cantDisponible--;
    item.cantReservada++;
    sendSubTotal();
}

const eliminar = (detalle) => {
   const cantidad= 1;
   let index = detalleFacturar.value.findIndex((loopVariable) => loopVariable.producto.id === detalle.producto.id);
   detalleFacturar.value.splice(index,cantidad);
   detalle.producto.cantDisponible = detalle.producto.cantDisponible + detalle.cantidad;
   detalle.producto.cantReservada = detalle.producto.cantReservada - detalle.cantidad;
   sendSubTotal();
}

const vistaPedidos = () => {
   router.push({name: 'pedidos'});
}

const sendSubTotal = () => {  
  let monto= 0;
  detalleFacturar.value.forEach(e => {
       monto += (e.precio*e.cantidad);
  });
  subTotal.value = monto;
     total.value = subTotal.value ;
}

function prueba(item, limite,event) {
  console.log(event);
  let valorActual = event.value;
  let valorAnterior = event.formattedValue;
  if (valorActual > event.formattedValue && valorActual <= limite) {
    console.log("if1");
    item.cantDisponible = item.cantDisponible - (valorActual-event.formattedValue);
    item.cantReservada = item.cantReservada + (valorActual-event.formattedValue);
  }else if(valorActual > event.formattedValue && event.value > limite){
    item.cantReservada = item.cantReservada + item.cantDisponible;
    item.cantDisponible =0;
  }else if(valorActual < event.formattedValue && valorActual < 1){
    item.cantDisponible = item.cantDisponible + (event.formattedValue - valorActual)-1;
    item.cantReservada = item.cantReservada - (event.formattedValue - valorActual)+1;
  } else {
    item.cantDisponible = item.cantDisponible + (event.formattedValue - valorActual);
    item.cantReservada = item.cantReservada - (event.formattedValue - valorActual);
  }
}

const guardarFactura = () =>{
    if (!error.value){
    let fechaAnticipo = new Date();
    let ant = {montoTotal: total.value, fecha: fechaAnticipo, montoIva: montoIva.value, cliente: selectedCliente.value, pedido: pedido.value};
    let anticipoCreationDTO = {venta: ant, detalle: detalleFacturar.value ,pagos: null};
    VentaServices.saveVenta(anticipoCreationDTO ).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
        let id = data.data.id;
        message(data.data);        
    } );
}
}
</script>

<template>
    <div class=" flex justify-content-center " >
        <ConfirmDialog group="headless">
            <template #container="{ message, acceptCallback, rejectCallback }">
                <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                    <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                        <i class="pi pi-check text-5xl"></i>
                    </div>
                    <p class="block mb-2 mt-4">{{ message.message }}</p>
                    <span class="font-bold mb-0 ">{{ message.header }}</span>         
                    <div class="flex align-items-center gap-2 mt-4">
                        <Button label="Ver factura" @click="acceptCallback"></Button>
                        <Button label="Ir a facturas" @click="rejectCallback"></Button>
                    </div>
                </div>
            </template>
        </ConfirmDialog>
        <!--Dialog Registrar Modificar Cliente-->
        <Dialog v-model:visible="clienteDialog" :closable="false" :style="{width: '450px'}" header="Cliente" :modal="true" class="p-fluid">
            <div class="formgrid">
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
                    <Dropdown fluid id="inventoryStatus" v-model="cliente.tipoDoc" :options="documentos" optionLabel="descripcion" placeholder="Select a Status" />
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
                    <label for="description">Calle 2</label>
                    <InputGroup fluid>
                        <Dropdown fluid v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
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
                    <Dropdown fluid v-model="direccion.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.ciudad}"  />
                    <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.ciudad">Ingrese Ciudad</small>
                </div>
                <div class="field">
                    <label for="description">Ubicar en el mapa</label>
                    <MapComponent @getUbicacion="getUbicacion" ref="map" :lat="direccion.lat" :lng="direccion.lng" />
                </div>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveCliente" />
            </template>
        </Dialog>
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                    <div class="flex align-items-center gap-2">
                        <h3 class="font-bold">Nueva Factura</h3>
                    </div>
                </template>
                <template #icons>
                    <div class="card flex" style="justify-content: end;">   
                        <div class="card flex" style="justify-content: end;">  
                            <Button  label="Cancelar"  style="margin-right: 1%;" @click="vistaPedidos()" />
                            <Button  label="Guardar" @click="validarForm" />
                        </div>  
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
                    <Card >
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Cliente
                                </div>    
                                <div v-if="clienteSeleccionado">
                                    <Button icon="pi pi-times"  link @click="eliminarClienteSelected"/>
                                </div>   
                                <div v-else>
                                    <Button icon="pi pi-plus"  link @click="registrarCliente"/>
                                </div>             
                            </div>
                        </template>
                        <template #content>
                        <div id="clienteDiv">
                        </div>
                        <div class="flex flex-column align-options-start">
                            <div v-if="!clienteSeleccionado" >
                                <AutoComplete v-model="selectedCliente" optionLabel="nombre" forceSelection :suggestions="filteredClientes" @complete="search" @item-select="mostrarCliente">
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
                                <div>
                                    
                                    <div class="card" style="width: 100%;">
        <div class="flex card-container" style="width: 100%;">
            <DataTable class="tablaCarrito" ref="dt" :value="detalleFacturar" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
            
            <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
            <Column class="col" field="producto.precio"  header="Precio" aria-sort="none" >
                <template #body="slotProps">
                <div class="flex-auto p-fluid" style="max-width:20lvb  !important; " >
                    {{ formatearNumero(slotProps.data.precio) }}
                </div> 
                </template>
            </Column>
            <Column  class="col" field="cantidadSolicitada" header="Solicitado" aria-sort="none">
                
                
            </Column>
            <Column  class="col" field="cantidadFacturada" header="Facturada" aria-sort="none">
                
                
            </Column>
            
            <Column  class="col" field="cantidad" header="Uds." aria-sort="none">
                <template #body="slotProps">
                    <div class="flex-auto p-fluid" style="max-width:15lvb  !important; ">
                    <InputNumber fluid class="inpCant" v-model="slotProps.data.cantidad" inputId="minmax-buttons" mode="decimal" showButtons :min="1" :max="slotProps.data.cantidadSolicitada" @input="prueba(slotProps.data.producto,slotProps.data.cantDisponible,$event)" @update:modelValue="sendSubTotal" />
                </div>  
                </template>
                
            </Column>
            
            <Column  class="col" field="subTotal" header="Total" aria-sort="none" >
                <template #body="slotProps">
                    <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                        <label for="subtotal"> {{  (slotProps.data.subTotal =  slotProps.data.cantidad * slotProps.data.precio ).toLocaleString("de-DE") }}</label>
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
                                    <div >
                                        
                        
                                        <Dialog v-if="visible" v-model:visible="visible" modal header="Seleccionar productos" :closable="false" :draggable="false" :style="{ width: '40rem' }"  >
                                        <template #footer>
                                            <div class="flex justify-content-end">
                                                <Button label="Cerrar" icon="pi pi-times" text @click="visible = false" />
                                            </div>
                                        </template> 

                                        <div class="grid" style="row-gap: 2.5vh;">
                                            <div class="card col-12" style="width: 100%;">
                                                <span class="p-input-icon-left" style="width: 100%; margin-top: 0.5rem;">
                                                
                                                    <InputText  class="buscador p-fluid" style="width: 100%;" v-model="filters['global'].value" placeholder="Buscar..." />
                                                </span>
        
                                                <div class="flex card-container col-12" style="width: 100%;">
            
                                                    <DataTable class="tabla" ref="dt"  :value="productos"  dataKey="producto.id"
                                                        :paginator="true" :rows="7" :filters="filters"
                                                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" >
                                                
                                                        <Column field="id"  header="ID" aria-sort="ascending" ></Column>
                                                        <Column field="nombre" header="Nombre" aria-sort="none" ></Column>
                                                        <Column field="cantDisponible" header="Disponible" aria-sort="none" >
                                                        <template #body="slotProps">
                                                            <h4 v-if="slotProps.data.cantStock < 1 && slotProps.data.bajoDemanda" style="color: tomato !important;">{{slotProps.data.cantDisponible }}</h4>
                                                            <h4 v-else style="color: green !important;">{{slotProps.data.cantDisponible}}</h4>

                                                        </template>

                                                        </Column>
                                                        
                                                        <Column field="precio"  header="Precio" aria-sort="none" >
                                                            <template #body="slotProps">
                                                            <div>
                                                                {{ slotProps.data.precio.toLocaleString("de-DE") }}
                                                            </div>
                                                            </template>
                                                        </Column>
                                                        <Column :exportable="false" style="min-width:8rem">
                                                        <template #body="slotProps">
                                                            <Button v-if=" slotProps.data.cantDisponible > 0 && slotProps.data.cantStock > 0" icon="pi pi-shopping-cart" class="mod_icono"  @click="addItem(slotProps.data)"/>
                                                                <Button v-else disabled="true" icon="pi pi-shopping-cart" class="mod_icono" />
                                                        </template>
                                                        </Column>
                                                    </DataTable>
                                                </div>
                                            </div>
                                        </div>
                                        </Dialog>  
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