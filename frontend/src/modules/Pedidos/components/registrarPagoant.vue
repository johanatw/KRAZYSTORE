
<template>
    <div class="card flex p-fluid justify-content-center " >
        <ConfirmDialog group="registroFactura">
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
        <Panel style=" position: relative; width: 80%;" >

            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Nueva Factura</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" />
                        <Button  label="Guardar" :disabled="disabled" @click="guardarFactura()" />
                    </div>  
                </div>
            </template>
            <div class="contenedor" style="padding-left: 4%; padding-right: 4%;">      
                <div class="grid">
                <div class="field col-12 md:col-6">
                    <MiCard ref="cardCliente" :titulo="'Cliente'" :contenido="infoCliente" @edit="editCliente"/> 
                </div>  
                <div>
                    <SearchCliente ref="searchCard" @getCliente="getCliente" />
                </div> 

                
                    <div class="field col-12 md:col-6">
                        

                    </div>
                </div>
                


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
                            <div>
                                
                                <div class="card" style="width: 100%;">
    <div class="flex card-container" style="width: 100%;">
        <DataTable class="tablaCarrito" ref="dt" :value="detalleFacturar" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
          
         <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
        <Column class="col" field="producto.precio"  header="Precio" aria-sort="none" >
            <template #body="slotProps">
            <div class="flex-auto p-fluid" >
                  <InputNumber fluid class="inpCant" v-model="slotProps.data.producto.precio" mode="decimal" />
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
                  <InputNumber fluid class="inpCant" v-model="slotProps.data.cantidad" inputId="minmax-buttons" mode="decimal" showButtons :min="1" :max="slotProps.data.producto.cantStock" @input="prueba(slotProps.data.producto,slotProps.data.cantDisponible,$event)" @update:modelValue="sendSubTotal" />
              </div>  
            </template>
             
         </Column>
         
         <Column  class="col" field="subTotal" header="Total" aria-sort="none" >
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                     <label for="subtotal"> {{  (slotProps.data.subTotal =  slotProps.data.cantidad * slotProps.data.producto.precio ).toLocaleString("de-DE") }}</label>
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
                                                        <h4 v-if="slotProps.data.cantStock < 1 && slotProps.data.preVenta" style="color: tomato !important;">{{slotProps.data.cantDisponible }}</h4>
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




              
                    
            </div>
   
        </Panel>
    </div>
            
</template>

<script setup>

import Button from 'primevue/button';
import { PedidoServices } from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';

import { VentaServices } from '@/services/VentaServices';

import InputText from 'primevue/inputtext';


import Checkbox from 'primevue/checkbox';

import { ProductoServices } from '@/services/ProductoServices';

import { ref, onMounted, onBeforeMount } from "vue";
import { watch } from 'vue'
import router from '@/router';
import Dialog from 'primevue/dialog';
import Card from 'primevue/card';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Dropdown from 'primevue/dropdown';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Panel from 'primevue/panel';
import SearchCliente from '@/modules/Pedidos/components/SearchCliente.vue'
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import MiCard from '@/modules/Pedidos/components/MiCard.vue'
import InputNumber from 'primevue/inputnumber';
import ConfirmDialog from 'primevue/confirmdialog';


import { useConfirm } from "primevue/useconfirm";


const confirm = useConfirm();

const message = (m) => {
    let id = m.id;
    let nro = m.nroFactura;
    console.log('messageError invocado');
    confirm.require({
        group: 'registroFactura',
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
import Toast from 'primevue/toast';


import { useToast } from "primevue/usetoast";


const toast = useToast();
const searchCard = ref(null);
const selectedClient = ref();
const pedidosAnticipados = ref([]);
const detalleFacturar = ref([]);
const pagos =ref([ ]);
const anticipos =ref([ ]);
const pago =ref({});
const pedido = ref({});
const disabled = ref(true);
const visible = ref(false);
const subTotal = ref(0);
const key = ref(0);
const montoIva = ref(0);
const detalle = ref({});
const selectedAnticipados = ref();
const  formasPago = ref([]);
const metaKey = ref(true);
const total = ref(0);
const abonado = ref(0);
const cambio = ref(0);
const productos = ref();
const faltante = ref(0);
const infoCliente = ref([{
    valor: "Cliente no seleccionado"
}]);
const opciones = ref([
    'Seña', 'Total'
]);
/*
onBeforeMount(() => {
    FormasPagoServices.obtenerFormasPago().then((data) => {
        formasPago.value=data.data;
        //selectedFormaPago.value=data.data[1];
        console.log(formasPago.value);
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pago.value.anticipo = null;
        pagos.value.push(pago.value);

    });
});*/

onMounted(() => {
    PedidoServices.getDetallePedido(router.currentRoute.value.params.id).then((data) => {
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
    });
    ProductoServices.obtenerProductos().then((data) => {
     productos.value = data.data
     console.log("productosssss",productos.value);
    
    });
    
        
    });

    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data;
        //formaPago.value.setPago(data.data.formaPago);
        //cliente.value.setCliente(data.data.cliente);
        getCliente(data.data.cliente);
        
        
    });

    getAnticipos(router.currentRoute.value.params.id);

});

const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const getCliente = (client) =>{
    selectedClient.value = client;
    infoCliente.value = [];
    let valor = {valor: client.nombre};
    infoCliente.value.push(valor);
    valor = {valor: client.telefono};
    infoCliente.value.push(valor);
    if (client.nroDoc) {
        valor = {valor: client.tipoDoc.descripcion +' - '+ client.nroDoc};
        infoCliente.value.push(valor);
    }
    if (client.email) {
        valor = {valor: client.email};
        infoCliente.value.push(valor);
    }

    
}

const editCliente = () =>{
    searchCard.value.showDialog();
    if (selectedClient.value) {
        searchCard.value.showClient(selectedClient.value);
    }  
}

const verificarSaldoAnticipo = (item, index) =>{

    let falta = faltante.value + item.importe;
    
         if (falta>0 && item.anticipo.saldo < falta) {
            item.importe = item.anticipo.saldo;
         }

         if (falta>0 && item.anticipo.saldo >= falta ) {
            item.importe = falta;
         }
        
        calcularAbonado();
    
}

const habilitarInput = (index, item) => {
    let inp = document.getElementsByName("input")[index].firstElementChild;
   if (inp.disabled  ) {
        inp.disabled = false;
    }
   
    if (item.importe < 1 && abonado.value < total.value ) {
        item.importe = total.value - abonado.value;
    }

    if(item.formaPago.descripcion != 'Anticipo'){
        calcularAbonado();
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
const calcularAbonado = (event,max=null) => {

    if (event !== undefined) {
        console.log(event);
        let valorActual = Number(event.value);
        let valorAnterior = Number(event.formattedValue.replaceAll(".",""));
        if ((max != null && valorActual < max ) || max==null) {
            console.log("entra max");
            console.log(max);
            console.log(valorActual);
            console.log(max != null && (valorActual < max) );
            abonado.value = abonado.value - valorAnterior + valorActual;
        }
       
        
    }else {
        let monto = 0 ;
        pagos.value.forEach(element => {
            monto += element.importe;
        })
        abonado.value = monto;
    }
    
    calcularDiferencia();
  
};

const prueba2 = (event,max) => {


    console.log(event);
    calcularAbonado();

};

const calcularDiferencia = () => {
    console.log('antesdetalleanti');
    console.log(selectedAnticipados.value);
  faltante.value = total.value - abonado.value;
  cambio.value = abonado.value - total.value; 
  habilitarSubmit();
};

const habilitarSubmit = () =>{
    if (total.value > 0 && abonado.value == total.value) {
    disabled.value = false;
  }else{
    disabled.value = true;
  }
}

const calcularTotalSeleccionado = (id=null) => {
   // console.log(document.getElementsByClassName("col")[1].childNodes[2].ariaSelected);
    //document.getElementsByClassName("col")[2].childNodes[2].ariaSelected = true;

    total.value = 0;/*
    selectedAnticipados.value.forEach(element => {
        switch (true) {
        case id && element.idTemp == id && element.tipo == "Seña":
            element.subTotal = element.producto.seña * element.cantidad;
            break;
        case id && element.idTemp == id && element.tipo == "Total":
            element.subTotal = element.producto.precio * element.cantidad;
            break;                    
        }
        total.value += element.subTotal;
        
    })*/
    if (selectedAnticipados.value) {
        selectedAnticipados.value.forEach(element => {
        switch (true) {
        case id && element.idTemp == id && element.tipoPago == "Seña":
            element.subTotal = element.producto.seña * element.cantidad;
            
            break;
        case id && element.idTemp == id && element.tipoPago == "Total":
            element.subTotal = element.producto.precio * element.cantidad;
        
            break;                    
        }
        total.value += element.subTotal;
        
    })
    }
    
   
    calcularDiferencia();
};


const addRow = () => {
    pagos.value.push({formaPago:null , importe: 0});
};

const eliminarRow = (index) => {
    if (index > 0) {
        pagos.value.splice(index,1);
    }
    calcularAbonado();
};

const guardarAnticipo = () =>{
    console.log("Guardaara");
    console.log(selectedClient.value);
    console.log(pagos.value);
    console.log(total.value);
    console.log(selectedAnticipados.value);
    
        
    
    let fechaAnticipo = new Date();
    let ant = {total: total.value, fecha: fechaAnticipo, cliente: selectedClient.value, pedido: pedido.value};

    

    //selectedAnticipados.value.sort(function(a, b){return (a.idTemp) - (b.idTemp)});
   
    
    selectedAnticipados.value.forEach(element => {

        
        if (element.tipoPago=="Seña") {
            element.tipo ='S';
        }else if(element.tipoPago=="Total"){
            element.tipo ='T';
        }else{
            element.tipo = 'R';
        }

       
    })
    console.log(selectedAnticipados.value);

    let anticipoCreationDTO = {anticipo: ant, detalle: selectedAnticipados.value, pagos: pagos.value};
    AnticipoServices.saveAnticipo(anticipoCreationDTO ).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
        console.log(data.data);
        verAnticipo(data.data.id)} );

    
    
}

    
const verAnticipo = (id) =>{
    console.log("veranticipofuncion");
    router.push({name: 'verAnticipo', params: {id}});
}


const getAnticipos = (idPedido) => {

AnticipoServices.getAnticipos(idPedido).then((data) => {
    anticipos.value = data.data;
    console.log(formasPago.value);
    if (anticipos.value.length > 0) {
        getFormasPago();
    }else{
        getFormasPagoSinAnticipo();
    }
    

});
};

const getFormasPago = () =>{
    FormasPagoServices.obtenerFormasPago().then((data) => {
        formasPago.value=data.data;
        //selectedFormaPago.value=data.data[1];
        console.log(formasPago.value);
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pago.value.anticipo = null;
        pagos.value.push(pago.value);

    });
}

const getFormasPagoSinAnticipo = () =>{
    FormasPagoServices.obtenerFormasPagoSinAnticipo().then((data) => {
        formasPago.value=data.data;
        //selectedFormaPago.value=data.data[1];
        console.log(formasPago.value);
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pago.value.anticipo = null;
        pagos.value.push(pago.value);

    });
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

  const sendSubTotal = () => {
  
  let monto= 0;
  
  detalleFacturar.value.forEach(e => {
     
       //e.cantReservada = e.producto.cantReservada + e.cantidad
       //e.cantDisponible = e.producto.cantDisponible - e.cantidad;
 
     
 
       monto += (e.producto.precio*e.cantidad);
  });
  subTotal.value = monto;
     total.value = subTotal.value ;
     calcularDiferencia();
  //emit("getSubTotal",total, detalles);
 
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
    console.log("if2");
    console.log(valorActual);
    console.log(event.formattedValue);
    console.log(event.value);
    console.log(limite);
    console.log(item.cantReservada);
    console.log(item.cantDisponible);
    item.cantReservada = item.cantReservada + item.cantDisponible;
    item.cantDisponible =0;
    console.log(item.cantReservada);
    console.log(item.cantDisponible);
    
  }else if(valorActual < event.formattedValue && valorActual < 1){
    console.log("if3");
    item.cantDisponible = item.cantDisponible + (event.formattedValue - valorActual)-1;
    item.cantReservada = item.cantReservada - (event.formattedValue - valorActual)+1;
  } else {
    console.log("if4");
    console.log(valorActual);
    console.log(event.formattedValue);
    console.log(event.value);
    console.log(limite);
    item.cantDisponible = item.cantDisponible + (event.formattedValue - valorActual);
    item.cantReservada = item.cantReservada - (event.formattedValue - valorActual);
  }
  }

  const guardarFactura = () =>{
    console.log("pagos");
    console.log(pagos.value);
    console.log("detalle");
    console.log(detalleFacturar.value);
    
    let fechaAnticipo = new Date();
    let ant = {montoTotal: abonado.value, fecha: fechaAnticipo, pedido: pedido.value,cliente: selectedClient.value, montoIva: montoIva.value};


    let anticipoCreationDTO = {venta: ant, detalle: detalleFacturar.value ,pagos: pagos.value};
    VentaServices.saveVenta(anticipoCreationDTO ).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
      
        message(data.data);
        //closeDialog();
        //emit('anticipoGuardado', data.data.id);
        
    } );
}
</script>
<style>

.p-inputnumber-button-group{
 padding: 0rem !important;
 width: 1rem;
}

.p-inputnumber-button{
 padding: 0rem !important;
 width: 1rem;
}

</style>