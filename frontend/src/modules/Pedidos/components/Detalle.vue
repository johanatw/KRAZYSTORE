
<template>
       
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
                {{ slotProps.data.producto.precio }}
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
                     <label for="subtotal"> {{  slotProps.data.subtotal =  slotProps.data.cantidad * slotProps.data.producto.precio  }}</label>
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
</template>

<script setup>
import Column from 'primevue/column';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Tag from 'primevue/tag';
import { PrimeIcons } from 'primevue/api';
import { ref, onMounted } from 'vue';
import { ProductoServices } from '@/services/ProductoServices';
import InputNumber from 'primevue/inputnumber';
import { defineExpose } from 'vue';
import Card from 'primevue/card';
import { defineEmits } from 'vue';

const detalles= ref([]);

const detalle = ref({});
const emit = defineEmits(['getSubTotal', 'deleteItem']);

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
  
 let total= 0;
 console.log('detalles',detalles.value);
 detalles.value.forEach(e => {
    
      //e.cantReservada = e.producto.cantReservada + e.cantidad
      //e.cantDisponible = e.producto.cantDisponible - e.cantidad;

    

     total += (e.producto.precio*e.cantidad);
 });
 emit("getSubTotal",total, detalles);

}

const setDetalle = (lista) => {
  
    detalles.value = lista;
     
  //detalles.value = lista;
  console.log('detalleEstejehje',detalles.value );
  sendSubTotal();
}




const addItem = (item) => {
 let index = detalles.value.findIndex((loopVariable) => loopVariable.producto.id === item.id);

 if (index>-1) {
     detalles.value[index].cantidad++;
     console.log("holaaa");
 } else {
  console.log("holaaaitem",item);
    detalle.value.producto = {};
     detalle.value.producto = item;
     detalle.value.cantDisponible = item.cantDisponible;
     detalle.value.cantReservada = item.cantReservada;
     detalle.value.cantStock = item.cantStock;
     detalle.value.cantPreVenta = item.cantPreVenta;
     //detalle.value.id = item.id;
     //detalle.value.producto.categoria = {id: item.idCategoria, descripcion: item.categoria};
     /*detalle.value.producto.cantPreVenta = item.cantPreVenta;
     detalle.value.producto.cantStock = item.cantStock;
     detalle.value.producto.cantDisponible = item.cantDisponible;
     detalle.value.producto.cantReservada = item.cantReservada;*/

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
addItem,
setDetalle,

});

</script>
<style>
.p-fluid .p-button-icon-only {
 width: fit-content;
}
.p-inputnumber-input{
 min-width: fit-content !important;
}

.p-inputnumber-button{
  padding: 0% !important;
}

</style>