<script setup>

import Button from 'primevue/button';


import { ref, onMounted } from "vue";

import Tag from 'primevue/tag';

import DataTable from 'primevue/datatable';

import Column from 'primevue/column';
import { formatearNumero } from '@/utils/utils';
import { DashboardServices } from "@/services/DashboardServices";

const topProductos = ref();


onMounted(() => {
    getProductos();
});

async function getProductos(){
    topProductos.value = (await DashboardServices.obtenerTop10ProductosVendidos()).data;
    console.log(topProductos.value);
}
 

</script>
<template>
   
    <div class="card" >
  

                <DataTable class="tabla" ref="dt"  :value="topProductos"  dataKey="nombre" >
                    <Column field="nombre" header="Nombre" aria-sort="none" ></Column>
                    <Column field="precio"  header="Cantidad Vendida" aria-sort="none" >
                        <template #body="slotProps">
                        <div>
                            {{ formatearNumero(slotProps.data.cantidadVendida)  }}
                        </div>
                        </template>
                    </Column>
                    <Column  aria-sort="none" >
                    <template #body="slotProps">
                        <div v-if="slotProps.data.estado" >
                            <Tag value="SinStock"></Tag>
                        </div>

                    </template>

                    </Column>
                </DataTable>
            </div>
                          
</template>
<style>


</style>