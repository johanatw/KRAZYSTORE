<script setup>
import { ref, onMounted } from 'vue';
import Card from 'primevue/card';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import {PedidoServices} from '@/services/PedidoServices';
import Tag from 'primevue/tag';
const props = defineProps(['pedido']);
const subTotal = ref(0);
const detalles = ref([]);
const costoEnvio = ref(0);
const showEnvio = ref(false);
const total = ref(0);
onMounted(() => {
    console.log("card detalle pedido");
   
    
});

const setDetalle = (idPedido,totalItems=null,envio=null) => {
    PedidoServices.getDetalle(idPedido).then((data) => {
        detalles.value = data.data;
        console.log("detallee",detalles.value);
        if (total) {
            subTotal.value = totalItems;
        }
        if (envio) {
            costoEnvio.value = envio.costo;
            showEnvio.value = true;
        }
        total.value = subTotal.value + costoEnvio.value;

    });
    



}

defineExpose({
    setDetalle
});

</script>
<template>
   

            <Card style="height: 100%;" >
                <template #title> Productos </template>
                <template #content> 
                    <div class="card" style="width: 100%;">
    
    <div class="flex card-container" style="width: 100%;">
      
        <DataTable class="tablaCarrito" ref="dt" :value="detalles" scrollable scrollHeight="400px" dataKey="producto.id" style="width: 100%;">
            <Column  class="col"  aria-sort="none" style="max-width:3rem">
             <template #body="slotProps">
                 <div class="flex-auto p-fluid" v-if="slotProps.data.producto.cantStock<1" style="max-width:10lvb  !important; ">
                   <Tag value="SinStock"></Tag>
         
                 
             </div>
               
           </template>
             
         </Column>
            <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
            <Column class="col" field="producto.precio"  header="Precio" aria-sort="none" >
            <template #body="slotProps">
            {{ slotProps.data.precio }}
            </template>
        </Column>
            <Column  class="col" field="cantidad" header="Uds." aria-sort="none">                
            </Column>
            <Column  class="col" field="cantidadFacturada" header="Facturada" aria-sort="none">                
            </Column>
            
            <Column  class="col" field="subtotal" header="Total" aria-sort="none" >
                <template #body="slotProps">
                    {{ slotProps.data.subtotal.toLocaleString("de-DE") }}
            </template>
            </Column>
            
            
        </DataTable>
        
      </div>
      <div class="grid" style="margin-top: 1rem;">
        <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
            <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                Total items 
                        </div>
                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 2.5rem; padding: 0px; " >
                            {{ subTotal.toLocaleString("de-DE") }}
                        </div>
        </div>
        <div v-if="showEnvio" class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
            <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                Envio
            </div>
            <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 2.5rem; padding: 0px; " >
                {{ costoEnvio.toLocaleString("de-DE") }}
            </div>

        </div>
        <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px;">
            <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; font-weight: bold; font-size: 16px; ">
                Total
            </div>
            <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 2.5rem; padding: 0px;  font-weight: bold; font-size: 16px; " >
                {{ total.toLocaleString("de-DE") }}
            </div>

        </div>
            

        </div>
        

        </div>
      
    
                    
                
                </template>

            </Card>

       
        
</template>

<style>
.p-datatable .p-datatable-tbody > tr > td {
    font-size: 14px;
}


</style>