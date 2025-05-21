<script setup>
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
import { RecepcionServices } from "@/services/RecepcionServices";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import Select from "primevue/select";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import DatePicker from 'primevue/datepicker';
import { formatearNumero } from "@/utils/utils";
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
const proveedor = ref({});
const selectedOp = ref('Casi');
const productos= ref();
const proveedores=ref();
const filteredClientes = ref();
const idRecepcion = ref();
const error = ref(false);
const opciones = ref(['Casi','Entre']);
const infoEntrega = ref([{
valor: "Retiro"
}])
const iva5 = ref(0);
const iva10 = ref(0);
const iva0 = ref({id: 3, porcentaje: 0});
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import {PedidoCompraServices} from "@/services/PedidoCompraServices";

const confirm = useConfirm();
const toast = useToast();
const timbrado = ref();
const tiposProveedor = ref([
  {id: 1, descripcion: 'Nacional'},
  {id: 2, descripcion: 'Extranjero'}
]);

const servicio = ref([
  {id: 9, descripcion: 'Gastos de envío'}
]);

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
const selectedProducts = ref();
const subTotal = ref(0);
const montoIva = ref(0);
const detalle = ref({});
const total = ref(0);
const nroFactura = ref();
const fechaCompra = ref(new Date());
const recepcion = ref();
const pedido = ref();
const compraNacional = ref(false);
const compraInternacional = ref(false);
const recepcionesFacturarIds = ref();

onMounted(() => {
    //console.log(sessionStorage.getItem('recepcionesFacturar'));
    idRecepcion.value = router.currentRoute.value.params.id;
    getRecepcion(router.currentRoute.value.params.id);
    ProductoServices.obtenerServicioTransporte().then((data)=>{
        console.log(data.data);
        let detalle = {producto: data.data, ivaAplicado: data.data.tipoIva, cantidad: 1, costoCompra:data.data.costo};
        detalleFacturar.value.push(detalle);
        console.log(detalle);
        calcularTotales();
        });
    /*RecepcionServices.getRecepcion(idRecepcion).then((data)=>{
            console.log(data.data);
            pedido.value = data.data.pedido;
            detalleFacturar.value = data.data.detalle;
            selectedProducts.value = data.data.detalle;
            selectedCliente.value = data.data.pedido.proveedor;
            mostrarCliente(); 
            compraInternacional.value = esCompraInternacional(selectedCliente.value.tipo.descripcion)?true:false;

            detalleFacturar.value.forEach(e => {
                e.cantidad = e.cantRecepcionada;
                e.ivaAplicado = compraInternacional.value?iva0.value:e.producto.tipoIva;
            
            });
            console.log(detalleFacturar.value);
            calcularTotales();
        });

    //recepcionesFacturarIds.value =JSON.parse(sessionStorage.getItem('recepcionesFacturar'));
    //if (recepcionesFacturarIds.value) {
       /* compraNacional.value = true;
        PedidoCompraServices.getDetalleRecepcionesFacturar(idPedido,recepcionesFacturarIds.value).then((data)=>{
            console.log(data.data);
            pedido.value = data.data.pedido;
            detalleFacturar.value = data.data.detalle;
            selectedProducts.value = data.data.detalle;
            selectedCliente.value = data.data.pedido.proveedor;
            mostrarCliente(); 
            compraInternacional.value = esCompraInternacional(selectedCliente.value.tipo.descripcion)?true:false;

            detalleFacturar.value.forEach(e => {
                e.cantidad = e.cantRecepcionada;
                e.ivaAplicado = compraInternacional.value?iva0.value:e.producto.tipoIva;
            
            });
            console.log(detalleFacturar.value);
            calcularTotales();
        });
   /* } else {
        
    
    PedidoCompraServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data.pedido;
        detalleFacturar.value = data.data.detalle;  
        /*if (data.data.pedido?.proveedor.tipo.descripcion == 'Extranjero') {
            compraNacional.value = false;
        }else{ compraNacional.value = true;} 
        selectedCliente.value = data.data.pedido.proveedor;
        mostrarCliente(); 
        console.log(data.data);
        
        detalleFacturar.value.forEach(e => {
            e.id = null;
            console.log(e.cantSolicitada);
            e.cantidad = e.cantSolicitada - e.cantFacturada;
            e.ivaAplicado = e.producto.tipoIva;
          
        });


        calcularTotales();

   });

}*/

ProductoServices.obtenerProductos().then((data) => {
 productos.value = data.data
 console.log("productosssss",productos.value);

});

   ProveedorServices.obtenerProveedores().then((data) => {
   proveedores.value = data.data;
   });

   TipoDocServices.obtenerTipoDocs().then((response)=>{
documentos.value = response.data;
});

DepartamentoServices.obtenerDepartamentos().then((data) => {
departamentos.value=data.data;

});


});

const impuestos = ref([
{ name: '10 %'}
]);

const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const esCompraInternacional = (tipoProveedor) =>{

if (tipoProveedor == 'Extranjero') {
    return true;
}

return false;
}

const calcularTotales = () => {
    console.log('calculartotales');
  
        console.log('calculartotalesid');
        let monto= 0;

        detalleFacturar.value.forEach(e => {
            e.subTotal = (e.costoCompra*e.cantidad);
            monto += (e.costoCompra*e.cantidad);
            console.log(e.costoCompra);
            console.log(e.cantidad);
            console.log(monto);
        });
        subTotal.value = monto;
        total.value = subTotal.value ;

  

  
        calcularIva();
    


}

//Cliente
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

async function getRecepcion(id) {
    try {
      const { data } = await RecepcionServices.getRecepcion(id);
      recepcion.value = data?.recepcion;
      console.log(recepcion.value);
    } catch (error) {
      showError('Error al obtener clientes');
    }
  }

const registrarCliente = () =>{

proveedor.value = {};
clienteDialog.value = true;
}

const modificarCliente = (cli) => {
ProveedorServices.getProveedor(cli.id).then((data) => {
console.log("data direccion");
   
   proveedor.value = data.data;
   proveedor.value.departamento = data.data.ciudad?.departamento;
    if (proveedor.value.departamento != null) {
    obtenerCiudadesByDepartamento(proveedor.value.departamento?.id);
    }
   clienteDialog.value = true;   
   
});

};

const obtenerCiudadesByDepartamento = (id) =>{
    CiudadServices.obtenerCiudadesByDepartamento(id).then((data) => {
        ciudades.value = data.data;
        console.log(ciudades.value);
    });
}
const saveCliente = () => {
console.log(proveedor.value);
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


//Otros


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

const calcularIva = () =>{
   iva5.value = calcularIva5();
   iva10.value = calcularIva10();
   montoIva.value = iva5.value + iva10.value;
    
}

const calcularIva5 = () => {
    let detalle;

    detalle = detalleFacturar.value;
    if (detalle == null) {
        return 0;
    }
  return detalle
  .filter(item => item.producto?.tipoIva?.porcentaje === 5) // Filtra los productos con IVA 5%
    .reduce((accu, item) => {
        let porcentaje = item.producto.tipoIva.porcentaje || 0;
        let base = item.producto.tipoIva.base || 0;
        let factor_iva = porcentaje + base;

        if (factor_iva === 0) return accu; // Evita división por 0
        
        let iva = item.subTotal * porcentaje / factor_iva;
        if (!isFinite(iva)) return accu; // Evita errores matemáticos
        return accu + iva;
    }, 0); // Luego suma
};

const calcularIva10 = () => {
    let detalle;

    detalle = detalleFacturar.value;
    if (detalle == null) {
        return 0;
    }
  return detalle
    .filter(item => item.producto.tipoIva.porcentaje === 10) // Filtra 
    .reduce((acc, item) => {
        let factor_iva = item.producto.tipoIva.porcentaje + item.producto.tipoIva.base;
        return acc + ((item.subTotal * item.producto.tipoIva.porcentaje / factor_iva) || 0)
    }, 0); // Luego suma
};

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
   detalle.value.subTotal = detalle.value.costoCompra * detalle.value.cantidad;
   detalle.value.iva = { name: '10 %'};
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

  const vistaRecepciones= () =>{
router.push({name: 'recepciones'});
}

  const verPedidoCompra = (id) => {
router.push({name: 'ver_pedido_compra', params: {id}});
  
  }

  const vistaPedidos= () =>{
    router.push({name: 'pedidos_compras'});
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
 calcularTotales();
  //emit("getSubTotal",total, detalles);
 
 }

 const verCompra = (id) =>{
router.push({name: 'ver_compra', params: {id}});

}

const verPedidos = () =>{
    router.push({name: 'pedidos_compras'});
}

const showError = (message) => {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: message,
      life: 3000
    });
  };
  
  const showSuccess = (message) => {
    toast.add({
      severity: 'success',
      summary: 'Éxito',
      detail: message,
      life: 3000
    });
  };

  const guardarFactura = () =>{
if (!error.value){
console.log();
let fechaAnticipo = new Date();
let gravada = total.value - montoIva.value;
let ant = {total: total.value, fecha: fechaCompra.value,totalGravada: gravada, montoIva: montoIva.value, proveedor: selectedCliente.value, nroFactura: nroFactura.value, timbrado: timbrado.value, recepcion: {id: idRecepcion.value}, pedido: {id: recepcion.value?.idPedido} };

let ids =  recepcionesFacturarIds.value;
/*if (compraNacional.value) {
    console.log('nacional');
    ids = recepcionesFacturarIds.value;
} else {
    console.log('internacional');
    ids = JSON.parse('[ ]');
}*/

let anticipoCreationDTO = {compra: ant, detalle: detalleFacturar.value};
console.log(anticipoCreationDTO);

CompraServices.registrarCompra(anticipoCreationDTO ).then((data)=> {
console.log("saveanticipothen");
console.log("data");
sessionStorage.removeItem('recepcionesFacturar');
let id = data.data.id;
showSuccess('Factura guardado correctamente');
verPedidos();
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
<Dialog v-model:visible="clienteDialog" :closable="false" :style="{width: '450px'}" header="Proveedor" :modal="true" class="p-fluid">
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
            <InputText fluid id="description" v-model="proveedor.ruc" required="true"  />
        </div>
        
        <div class="field">
            <label for="description">Correo</label>
            <InputText fluid id="description" v-model="proveedor.correo" required="true"  />
        </div>
        <div class="field">
            <label for="description">Telefono</label>
            <InputText fluid id="description" v-model="proveedor.telefono" required="true"  />
        </div>
        <div class="field">
            <label for="description">Departamento</label>
            <Select fluid v-model="proveedor.departamento" :options="departamentos" @change="obtenerCiudadesByDepartamento(proveedor.departamento.id)"  optionLabel="descripcion" placeholder="Seleccione un departamento" class="w-full md:w-56" />
        </div>
        <div class="field">
            <label for="description">Ciudad</label>
            <Select fluid v-model="proveedor.ciudad" :options="ciudades"  optionLabel="descripcion" placeholder="Seleccione una ciudad" class="w-full md:w-56" />
        </div>
        <div class="field">
            <label for="description">Direccion</label>
            <InputText fluid id="description" v-model="proveedor.direccion" required="true"  />
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
 
<Button  label="Cancelar"  style="margin-right: 1%;" @click="vistaPedidos()" />
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

<Card >
<template #title>
<div class="flex justify-content-between ">
<div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
Información General
</div>
</div>

</template>
<template #content>
<div class="field" >

Fecha: <DatePicker fluid dateFormat="dd/mm/yy" v-model="fechaCompra" showIcon iconDisplay="input" />
</div> 
<div class="field" >
Timbrado: <InputText fluid type="text" v-model="timbrado" />
</div> 
<div class="field" >
N° Factura: <InputText fluid type="text" v-model="nroFactura" />
</div> 
</template>
</Card>
</div> 
   <div class="field col-12 md:col-6">

<Card >
<template #title>
<div class="flex justify-content-between ">
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
<div id="clienteDiv">

</div>
<div class="flex flex-column align-options-start">

<div v-if="!clienteSeleccionado" >

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
   

<div class="col-12" >

    <div v class="col-12" >
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
<DataTable :key="'table3'"  :value="detalleFacturar" >
 <Column class="col" field="producto.nombre" header="Descripción" aria-sort="none" ></Column>

 <Column class="col"  header="Costo" aria-sort="none" >
                <template #body="slotProps">
                <div  class="flex-auto p-fluid" >
                    <InputNumber fluid class="inpCant" v-model="slotProps.data.costoCompra" mode="decimal"   @update:modelValue="sendSubTotal" />
                </div> 
            
            </template>
        </Column>

<Column class="col" field="cantidad" header="Facturar" aria-sort="none">
 <template #body="slotProps">
 <div class="flex-auto p-fluid" style="max-width:15lvb !important; ">
 <InputNumber fluid class="inpCant" v-model="slotProps.data.cantidad" inputId="minmax-buttons" mode="decimal" showButtons :min="1" @update:modelValue="calcularTotales" />
 </div> 
 </template>
</Column>
<Column  class="col" field="cantidad" header="IVA" aria-sort="none">
    <template #body="slotProps">
        {{ slotProps.data.producto.tipoIva.porcentaje}}%
    </template>
    
</Column>
<Column field="subTotal" header="Sub Total">
 <template #body="slotProps">
 <div class="flex-auto p-fluid" style="max-width: 20dvh;">
 <label for="subtotal"> {{ formatearNumero(slotProps.data.cantidad * slotProps.data.costoCompra) }}</label>
 </div>
</template>
</Column>

</DataTable>
</div>


</template>  

</Card>

    </div>
    <div class="grid" style="margin-top: 1rem;">
<div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
<div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px; font-weight: bold; font-size: 16px;">
Total: 
</div>
<div class=" field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px; font-weight: bold; font-size: 16px;" >
{{ formatearNumero(total) }} Gs.
</div>
</div> 
<div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
<div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px; ">
IVA 5%: 
</div>
<div class=" field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px; " >
{{ ( Math.round(iva5)).toLocaleString("de-DE") }} Gs.
</div>
</div>  
<div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
<div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px; ">
IVA 10%: 
</div>
 <div class=" field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px; " >
 {{ ( Math.round(iva10)).toLocaleString("de-DE") }} Gs.
 </div>
 </div> 
 <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
 <div class="flex field col-9 md:col-9" style="justify-content: end; margin: 0px; padding: 0px; ">
 Total IVA: 
 </div>
 <div class=" field col-3 md:col-3" style="margin: 0px; margin-left: 1rem; padding: 0px; " >
 {{ ( Math.round(montoIva)).toLocaleString("de-DE") }} Gs.
 </div>
 </div> 

    </div>
    <div >
 
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