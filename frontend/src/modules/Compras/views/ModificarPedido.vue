<script setup>
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
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
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
const infoEntrega = ref([{
    valor: "Retiro"
}])
const pedido = ref({});
const proveedor = ref({});

import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";

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
            //verPedido(router.currentRoute.value.params.id);
            
        },
        reject: () => {
            router.push({name: 'ventas'});
            //verPedido(router.currentRoute.value.params.id);
            
        },
    });
};

const detalleFacturar = ref([]);
const subTotal = ref(0);
const montoIva = ref(0);
const detalle = ref({});
const total = ref(0);


onMounted(() => {
    PedidoCompraServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data)
        pedido.value = data.data.pedido;
        detalleFacturar.value = data.data.detalle;
        if (pedido.value.proveedor != null) {
            proveedor.value = pedido.value.proveedor;
            selectedCliente.value = proveedor.value;
        }
       total.value = pedido.value.total;
       
        mostrarCliente();
   });

    ProductoServices.obtenerProductos().then((data) => {
     productos.value = data.data
     console.log("productosssss",productos.value);
    
    });
    
   ProveedorServices.obtenerProveedores().then((data) => {
       clientes.value = data.data;
   });


});

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

const mostrarCliente = () =>{
    console.log(selectedCliente.value);
    let texto = selectedCliente.value.descripcion;
    if (selectedCliente.value.telefono) {
        texto = texto + "\nTelefono: "+selectedCliente.value.telefono;
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
    clienteSeleccionado.value = false;
   
    
}

const registrarCliente = () =>{
    
    cliente.value = {};
    clienteDialog.value = true;
}

const modificarCliente = (cli) => {
    ProveedorServices.getProveedor(cli.id).then((data) => {
        console.log("data direccion");
       
       cliente.value = data.data;
       clienteDialog.value = true;       
       
    });
    
};


const saveCliente = () => {
    submitted.value = true;
    if (cliente?.value.descripcion?.trim() ) {
        if (cliente.value.id) {
            ProveedorServices.modificarProveedor(cliente.value.id, cliente.value).then((response)=>{
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
            ProveedorServices.registrarProveedor(cliente.value).then((response)=>{
            console.log("reg");
                clientes.value.push(response.data);
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
                selectedCliente.value = response.data;
                mostrarCliente();
            }).catch(
                (error)=>messageError("error")
            );
        }
        submitted.value = false;
        clienteDialog.value = false;
        cliente.value = {};
    }
};


//Otros


const hideDialog = () => {
    clienteDialog.value = false;
    direccion.value = {};
    submitted.value = false;
};

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
  detalle.value.costoCompra = item.costo;
  
   detalle.value.subTotal = item.precio * detalle.value.cantidad;
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

  const vistaFacturasVenta = () => {
   router.push({name: 'ventas'});
  
  }

  const verPedidoCompra = (id) => {
    router.push({name: 'ver_pedido_compra', params: {id}});
  
  }

  const sendSubTotal = () => {
  
  let monto= 0;
  
  detalleFacturar.value.forEach(e => {
     
       //e.cantReservada = e.producto.cantReservada + e.cantidad
       //e.cantDisponible = e.producto.cantDisponible - e.cantidad;
 
     
 
       monto += (e.costoCompra*e.cantidad);
       
  });
  subTotal.value = monto;
     total.value = subTotal.value ;
 
  //emit("getSubTotal",total, detalles);
 
 }


  const guardarFactura = () =>{
    if (!error.value){

    let fechaAnticipo = new Date();
    let ant = {total: total.value, fecha: pedido.value.fecha, estado: pedido.value.estado, proveedor: selectedCliente.value};


    let anticipoCreationDTO = {pedido: ant, detalle: detalleFacturar.value};
    PedidoCompraServices.modificarPedido(pedido.value.id, anticipoCreationDTO ).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
        let id = data.data.id;
        verPedidoCompra(id);
        //closeDialog();
        //emit('anticipoGuardado', data.data.id);
        
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
        <div class="field">
            <label for="name">Nombre</label>
            <InputText id="name" v-model.trim="cliente.descripcion" required="true" autofocus :class="{'p-invalid': submitted && !cliente.descripcion}" />
            <small class="p-error" v-if="submitted && !cliente.descripcion">Ingrese un Nombre</small>
        </div>
        <div class="field">
            <label for="description">RUC</label>
            <InputText id="description" v-model="cliente.ruc" required="true"  />
        </div>
        <div class="field">
            <label for="description">Correo</label>
            <InputText id="description" v-model="cliente.correo" required="true"  />
        </div>
        <div class="field">
            <label for="description">Telefono</label>
            <InputText id="description" v-model="cliente.telefono" required="true"  />
        </div>

        <template #footer>
            <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
            <Button label="Save" icon="pi pi-check" text @click="saveCliente" />
        </template>
    </Dialog>

    <Panel style=" position: relative; width: 80%;" >
        <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Modificar Pedido N° {{pedido.id}}</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" @click="verPedidoCompra(pedido.id)" />
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
                    Proveedor
                </div>    
                <div v-if="clienteSeleccionado">
                    <Button class="pi pi-times" link @click="eliminarClienteSelected"/>
                </div>   
                <div v-else>
                    <Button class="pi pi-plus" link @click="registrarCliente"/>
                </div>             
            
            </div>
            
        </template>
        <template #content>
            <div id="clienteDiv">

            </div>
            <div class="flex flex-column align-options-start">
            
                <div v-if="!clienteSeleccionado" >
                    
                    <AutoComplete v-model="selectedCliente" optionLabel="descripcion" forceSelection :suggestions="filteredClientes" @complete="search" @item-select="mostrarCliente">
                    <template #option="slotProps">
                        <div class="flex flex-column align-options-start">
                            <div>{{ slotProps.option.descripcion }}</div>
                            <div v-if="slotProps.option.telefono">{{ slotProps.option.telefono }}</div>
                            <div v-if="slotProps.option.ruc">{{ slotProps.option.ruc }}</div>
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
        <DataTable class="tablaCarrito" ref="dt" :value="detalleFacturar" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
         <Column  class="col" field="producto" header="Nombre" aria-sort="none" ></Column>
         <Column class="col" field="producto.costo"  header="Precio" aria-sort="none" >
            <template #body="slotProps">
            <div class="flex-auto p-fluid" >
                  <InputNumber class="inpCant" v-model="slotProps.data.costoCompra" mode="decimal"   @update:modelValue="sendSubTotal" />
              </div> 
            </template>
        </Column>
         
        <Column  class="col" field="cantidad" header="Uds." aria-sort="none">
            <template #body="slotProps">
                <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                  <InputNumber class="inpCant" v-model="slotProps.data.cantidad" inputId="minmax-buttons" mode="decimal" showButtons :min="1"  @update:modelValue="sendSubTotal" />
              </div>  
            </template>
             
         </Column>
         
         <Column  class="col" field="subTotal" header="Total" aria-sort="none" >
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                     <label for="subtotal"> {{  (slotProps.data.subTotal =  slotProps.data.cantidad * slotProps.data.costoCompra).toLocaleString("de-DE") }}</label>
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
                                                    <Column field="cantDisponible" header="Stock" aria-sort="none" >
                                                    <template #body="slotProps">
                                                        <h4 >{{slotProps.data.cantStock }}</h4>
                                                        
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
                                                        <Button icon="pi pi-shopping-cart" class="mod_icono"  @click="addItem(slotProps.data)"/>
                                                        
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