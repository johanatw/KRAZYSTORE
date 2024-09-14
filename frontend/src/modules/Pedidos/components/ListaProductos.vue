
<template>

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
             <h4 v-if="slotProps.data.cantStock < 1" style="color: tomato !important;">{{slotProps.data.cantDisponible }}</h4>
             <h4 v-else style="color: green !important;">{{slotProps.data.cantDisponible}}</h4>

           </template>

         </Column>
         
         <Column field="precio"  header="Precio" aria-sort="none" ></Column>
         <Column :exportable="false" style="min-width:8rem">
           <template #body="slotProps">
               <Button v-if="(slotProps.data.bajoDemanda && (slotProps.data.cantReservada <= slotProps.data.cantLimBajoDemanda)) || slotProps.data.cantDisponible > 0" icon="pi pi-shopping-cart" class="mod_icono"  @click="addItem(slotProps.data)"/>
                 <Button v-else disabled="true" icon="pi pi-shopping-cart" class="mod_icono" />
           </template>
         </Column>
     </DataTable>
   </div>
   
   
 </div>
 
 </div>


</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ProductoServices } from '@/services/ProductoServices';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import Tag from 'primevue/tag'
import InputSwitch from 'primevue/inputswitch';
import InputNumber from 'primevue/inputnumber';
import Dialog from 'primevue/dialog';

import { defineEmits } from 'vue';

const cantPreventa = ref(1);
const emit = defineEmits(['addDetalle']);

const addItem = (item) => {
 emit("addDetalle",item);

}





const checked = ref(false);
const visible = ref();

onMounted(() => {
ProductoServices.obtenerProductos().then((data) => ( productos.value = data.data));
});

const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const productos = ref();
const producto = ref();

</script>
<style>
.card{
justify-self: center;
align-items: center;
padding: 0px !important;
 
}

.tabla{
width: 100% !important;
justify-self: center;
align-items: center;

}

.p-inputswitch-slider:before {
 position: absolute;
 content: '';
 top: 10% !important;
}
</style>

