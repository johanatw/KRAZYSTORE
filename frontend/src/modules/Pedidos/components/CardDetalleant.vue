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

onMounted(() => {
ProductoServices.obtenerProductos().then((data) => {
     productos.value = data.data
     console.log("productosssss",productos.value);
    
    });
});

const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});




const addItem = (item) =>{   
    
    det.value.addItem(item);
}



const addSubTotal = (monto, detalle) =>{
    subTotal.value = monto;
    total.value = subTotal.value + costoEnvio.value;
    detalles.value = detalle;
    
}

const setDetalle = (lista) =>{
    lista.forEach(element => {
        let index = productos.value.findIndex((loopVariable) => loopVariable.id === element.producto.id);
        if (index>-1) {
            element.producto = productos.value[index];
        
        }
        console.log("cantStock", element.producto.cantStock);
        element.cantDisponible = productos.value[index].cantDisponible + element.cantidad;
        element.cantReservada = productos.value[index].cantReservada;
        element.cantStock = productos.value[index].cantStock;
        element.cantPreVenta = productos.value[index].cantPreVenta;
       /* let detalle = {};
        detalle.cantDisponible = element.producto.cantDisponible;
        detalle.cantReservada = element.producto.cantReservada;
        detalle.cantStock = element.producto.cantStock;
        detalle.cantPreVenta = element.producto.cantPreVenta;
        //detalle.value.id = item.id;
        //detalle.value.producto.categoria = {id: item.idCategoria, descripcion: item.categoria};
        /*detalle.value.producto.cantPreVenta = item.cantPreVenta;
        detalle.value.producto.cantStock = item.cantStock;
        detalle.value.producto.cantDisponible = item.cantDisponible;
        detalle.value.producto.cantReservada = item.cantReservada;*/        
        //detalle.value.subtotal = item.precio * detalle.value.cantidad;
        //lista.push(detalle);
        

    });
    
    det.value.setDetalle(lista);
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
                    <template #title> Productos </template>
                        <template #content>
                            <div>
                                
                                <Detalle ref="det" @getSubTotal="addSubTotal" style="min-height: 100px;" />
                                <div class="grid" style="margin-top: 1rem;">
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            Total items 
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ subTotal }}
                                        </div>
                                    </div>
                                    <div v-if="showEnvio" class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; ">
                                            Envio
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; " >
                                            {{ costoEnvio }}
                                        </div>

                                    </div>
                                    <div class="flex field col-12 md:col-12" style="height: 1.5rem; margin: 0px; ">
                                        <div class="flex field col-9 md:col-9" style="justify-content: end;  margin: 0px; padding: 0px; font-weight: bold; font-size: 16px;">
                                            Total
                                        </div>
                                        <div class=" field col-3 md:col-3" style="   margin: 0px; margin-left: 1rem; padding: 0px; font-weight: bold; font-size: 16px;" >
                                            {{ total }}
                                        </div>

                                    </div>
            

                                </div>
                                <div >
                                    <div class="flex field col-3 md:col-3">
                                        <Button  label="+ Agregar producto" link @click="visible = true" style="color: palevioletred;"/>
                                    </div>
                       
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
                                                    
                                                    <Column field="precio"  header="Precio" aria-sort="none" ></Column>
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