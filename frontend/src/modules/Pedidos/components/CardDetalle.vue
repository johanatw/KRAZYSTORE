<script setup>

import Button from 'primevue/button';


import Card from 'primevue/card';
import { ref, onMounted } from "vue";
import ListaProductos from '@/modules/Pedidos/components/ListaProductos.vue';
import Dialog from 'primevue/dialog';
import Detalle from '@/modules/Pedidos/components/Detalle.vue';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import { ProductoServices } from '@/services/ProductoServices';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Column from 'primevue/column';

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
ProductoServices.obtenerProductos().then((data) => {
     productos.value = data.data
     console.log("productosssss",productos.value);
    
    });
});

const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const setDetalle = (lista) =>{
    lista.forEach(element => {
        let index = productos.value.findIndex((loopVariable) => loopVariable.id === element.producto.id);
        if (index>-1) {
            element.producto = productos.value[index];
        
        }
      
        element.cantDisponible = productos.value[index].cantDisponible + element.cantidad;
        element.cantReservada = productos.value[index].cantReservada;
        element.cantStock = productos.value[index].cantStock;
        element.cantPreVenta = productos.value[index].cantPreVenta;
       
        

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

    

      monto += (e.producto.precio*e.cantidad);
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




const addItem = (item) => {
let index = detalles.value.findIndex((loopVariable) => loopVariable.producto.id === item.id);

if (index>-1) {
   detalles.value[index].cantidad++;

} else {

  detalle.value.producto = {};
   detalle.value.producto = item;
   detalle.value.cantDisponible = item.cantDisponible;
   detalle.value.cantReservada = item.cantReservada;
   detalle.value.cantStock = item.cantStock;
   detalle.value.cantPreVenta = item.cantPreVenta;
  detalle.value.cantidad = 1;
  
   detalle.value.subtotal = item.precio * detalle.value.cantidad;
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
   detalle.producto.cantDisponible = detalle.producto.cantDisponible + detalle.cantidad;
   detalle.producto.cantReservada = detalle.producto.cantReservada - detalle.cantidad;
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
                    <Button label="+ Producto" link @click="visible = true" />
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
                 <div class="flex-auto p-fluid" v-if="slotProps.data.producto.cantStock < 1" style="max-width:10lvb  !important; ">
                   <Tag value="SinStock"></Tag>
                </div>  
            </template>  
         </Column> 
         <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
         <Column class="col" field="producto.precio"  header="Precio" aria-sort="none" >
            <template #body="slotProps">
              <div>
                {{ slotProps.data.producto.precio.toLocaleString("de-DE") }}
              </div>
            </template>
        </Column>
         <Column  class="col" field="cantidad" header="Uds." aria-sort="none">
            <template #body="slotProps">
                <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                  <InputNumber v-model="slotProps.data.cantidad" inputId="minmax-buttons" mode="decimal" showButtons :min="1" :max="slotProps.data.cantDisponible" @input="prueba(slotProps.data.producto,slotProps.data.cantDisponible,$event)"  @update:modelValue="sendSubTotal" />
              </div>  
            </template>
             
         </Column>
         
         <Column  class="col" field="subtotal" header="Total" aria-sort="none" >
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                     <label for="subtotal"> {{  (slotProps.data.subtotal =  slotProps.data.cantidad * slotProps.data.producto.precio ).toLocaleString("de-DE") }}</label>
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
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            Total items 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ subTotal.toLocaleString("de-DE") }}
                                        </div>
                                    </div>
                                    <div v-if="showEnvio" class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            Envio
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ costoEnvio.toLocaleString("de-DE") }}
                                        </div>

                                    </div>
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; font-weight: bold; font-size: 16px;">
                                            Total
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
                                                        <Button v-if=" slotProps.data.cantDisponible > 0" icon="pi pi-shopping-cart" class="mod_icono"  @click="addItem(slotProps.data)"/>
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
</template>
<style>

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
</style>