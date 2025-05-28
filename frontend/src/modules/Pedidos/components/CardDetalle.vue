<script setup>

import Button from 'primevue/button';


import Card from 'primevue/card';
import { ref, onMounted } from "vue";

import Dialog from 'primevue/dialog';
import Tag from 'primevue/tag';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { ProductoServices } from '@/services/ProductoServices';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Column from 'primevue/column';
import { formatearNumero } from '@/utils/utils';

const visible = ref(false);
const det = ref(null);
const subTotal = ref(0);
const detalles= ref([]);
const costoEnvio = ref(0);
const showEnvio = ref(false);
const total = ref(0);
const existencias= ref([]);
const productos = ref();


const detalle = ref({});

onMounted(() => {
    getProductos();
});
/*
async function existeCajaAbierta() {
   let c = (await CajaServices.getCajaAbierta()).data;
   console.log(c);
   if (c == null ) {
    return false;
   } else {
    return true;
   }
   
}*/

async function getProductos(){
    productos.value = (await ProductoServices.obtenerProductos()).data;
    console.log(productos.value);
}

const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

async function setDetalle(lista) {
    productos.value = (await ProductoServices.obtenerProductos()).data;
    
    lista.forEach(element => {
        let index = productos.value.findIndex((loopVariable) => loopVariable.id === element.producto.id);
        if (index>-1) {
            element.producto = productos.value[index];
        
        }
        console.log(productos.value[index].cantDisponible);
        console.log(element);
        element.cantDisponible = productos.value[index].cantDisponible + element.cantSolicitado;
        element.cantReservada = productos.value[index].cantReservada;
        element.cantStock = productos.value[index].cantStock;
        element.cantLimBajoDemanda = productos.value[index].cantLimBajoDemanda;

    });

    detalles.value = lista;
   
    //detalles.value = lista;
    console.log('detalleEstejehje',detalles.value );
    sendSubTotal();
    
    //det.value.setDetalle(lista);
    //detalles.value = lista;
}

const setDetalleEnvio = (envio=null) => {
    
        if (envio) {
            costoEnvio.value = envio.costo;
            showEnvio.value = true;
        }else{
            costoEnvio.value = 0;
            showEnvio.value = false;
        }
        total.value = subTotal.value + costoEnvio.value;

    

}

const sePuedeReservar = (producto) => {
    
        if (producto.cantDisponible>0) {
            return true;
        }

        if(producto.cantDisponible<=0 && producto.bajoDemanda && producto.cantDisponible > -producto.cantLimBajoDemanda ){
            return true;
        }
        

    

}




/*
function prueba2(nro) {
 return new Intl.NumberFormat('es-CL').format(nro);
  
}*/

function prueba(item, limite,event) {
  console.log(event);
  let valorActual = event.value;
  let valorAnterior = event.formattedValue;
  
  if (valorActual > event.formattedValue && valorActual <= limite) {
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
  


const sendSubTotal = () => {
  
 let monto= 0;
 console.log('detalles',detalles.value);
 detalles.value.forEach(e => {
    
      //e.cantReservada = e.producto.cantReservada + e.cantidad
      //e.cantDisponible = e.producto.cantDisponible - e.cantidad;

    

      monto += (e.precio*e.cantSolicitado);
 });
 subTotal.value = monto;
    total.value = subTotal.value + costoEnvio.value;
 //emit("getSubTotal",total, detalles);

}


/*
const setDetalle = (lista) => {
  
  detalles.value = lista;
   
//detalles.value = lista;
console.log('detalleEstejehje',detalles.value );
sendSubTotal();
}*/


const showTag = (detalle) => {
    console.log(detalle);
    let solicitado = detalle.cantSolicitado;
    let facturado = detalle.cantFacturada;
    let stock = detalle.producto.cantStock;
    let pendiente = solicitado - facturado > 0? true: false;

    if (stock<solicitado && pendiente) {
        return true;
    }
    return false;
}

const addItem = (item) => {
let index = detalles.value.findIndex((loopVariable) => loopVariable.producto.id === item.id);

if (index>-1) {
   detalles.value[index].cantSolicitado++;

} else {

  detalle.value.producto = {};
   detalle.value.producto = item;
   detalle.value.cantDisponible = item.cantDisponible;
   detalle.value.cantReservada = item.cantReservada;
   detalle.value.cantStock = item.cantStock;
   detalle.value.cantLimBajoDemanda = item.cantLimBajoDemanda;
  detalle.value.cantSolicitado = 1;
  detalle.value.precio = item.precio;
   detalle.value.subTotal = detalle.value.precio * detalle.value.cantSolicitado;
   detalles.value.push(detalle.value);
   detalle.value= {};
}


    item.cantDisponible--;
    

item.cantReservada++;
sendSubTotal();

}

const eliminar = (detalle) => {
   const cantidad= 1;
  
   let index = detalles.value.findIndex((loopVariable) => loopVariable.producto.id === detalle.producto.id);
   detalles.value.splice(index,cantidad);
   detalle.producto.cantDisponible = detalle.producto.cantDisponible + detalle.cantSolicitado;
   detalle.producto.cantReservada = detalle.producto.cantReservada - detalle.cantSolicitado;
   sendSubTotal();
  
  }

 defineExpose({
    subTotal,
    detalles,
    setDetalle,
    setDetalleEnvio,
    
});

</script>
<template>
   
    
        
       
        <div class="grid">
            <div class="col-12" >
                <Card >
                    
                    <template #title> 
                        <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    Productos
                </div>
                <div >
                    <Button label="Agregar Producto" text @click="visible = true" />
                    </div>
            </div>
                         </template>
                        <template #content>
                            <div>
                                
                                <div class="card" style="width: 100%;">
    <div class="flex card-container" style="width: 100%;">
        <DataTable class="tablaCarrito" ref="dt" :value="detalles" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
          <Column  class="col"  aria-sort="none" style="width:1.5rem">
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" v-if="showTag(slotProps.data)" style="max-width:10lvb  !important; ">
                   <Tag value="SinStock"></Tag>
                </div>  
            </template>  
         </Column> 
         <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
         <Column class="col" field="producto.precio"  header="Precio" aria-sort="none" >
            <template #body="slotProps">
            <div class="flex-auto p-fluid" >
                  {{  formatearNumero(slotProps.data.precio) }}
              </div> 
            </template>
        </Column>
         <Column  class="col" field="cantidad" header="Uds." aria-sort="none">
            <template #body="slotProps">
                <div v-if="(slotProps.data.producto.cantDisponible < 1 && slotProps.data.producto.bajoDemanda )" style="max-width:15lvb  !important; " >
                  <InputNumber fluid v-model="slotProps.data.cantSolicitado" inputId="minmax-buttons" mode="decimal" showButtons :min="1" :max="slotProps.data.cantDisponible + slotProps.data.cantLimBajoDemanda"   @update:modelValue="sendSubTotal" />
              </div>  
              <div v-else style="max-width:15lvb  !important; " >
                  <InputNumber fluid v-model="slotProps.data.cantSolicitado" inputId="minmax-buttons" mode="decimal" showButtons :min="1" :max="slotProps.data.cantDisponible"   @update:modelValue="sendSubTotal" />
              </div>
            </template>
             
         </Column>
         
         <Column  class="col" field="subtotal" header="Sub Total" aria-sort="none" >
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                     <label for="subtotal"> {{  (slotProps.data.subTotal =  slotProps.data.cantSolicitado * slotProps.data.precio ).toLocaleString("de-DE") }}</label>
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
                                            Total
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; font-weight: bold; font-size: 16px;" >
                                            {{ total.toLocaleString("de-DE") }} Gs.
                                        </div>

                                    </div>
            

                                </div>
                                <div >
                                    
                       
                                    <Dialog v-if="visible" v-model:visible="visible" modal header="Seleccionar productos" :closable="false" :draggable="false" >
                                    <template #footer>
                                        <div class="flex justify-content-end">
                                            <Button label="Cerrar" icon="pi pi-times" text @click="visible = false" />
                                        </div>
                                    </template> 

                                    <div class="grid" >
                                        <div class="card col-12" style="width: 100%;">
                                            <span class="p-input-icon-left" style="width: 100%; margin-top: 0.5rem;">
                                            
                                                <InputText fluid  class="buscador p-fluid" style="width: 100%;" v-model="filters['global'].value" placeholder="Buscar..." />
                                            </span>
    
                                            <div class="flex card-container col-12" style="width: 100%;">
        
                                                <DataTable class="tabla" ref="dt"  :value="productos"  dataKey="producto.id"
                                                    :paginator="true" :rows="7" :filters="filters"
                                                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" >
                                            
                                                    <Column field="id"  header="ID" aria-sort="ascending" ></Column>
                                                    <Column field="nombre" header="Nombre" aria-sort="none" ></Column>
                                                    <Column field="cantDisponible" header="Disponible" aria-sort="none" >
                                                    <template #body="slotProps">
                                                         <h4 style="color: green !important;">{{slotProps.data.cantDisponible}}</h4>

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
                                                            <Button :disabled="!sePuedeReservar(slotProps.data)" icon="pi pi-shopping-cart" class="mod_icono" @click="addItem(slotProps.data)" />
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
</template>
<style>


</style>