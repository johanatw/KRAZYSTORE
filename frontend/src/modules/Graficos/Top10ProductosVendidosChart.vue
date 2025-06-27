<script setup>
// Importaciones
import Button from 'primevue/button';
import { ref, onMounted } from "vue";
import Tag from 'primevue/tag';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { formatearNumero } from '@/utils/utils';
import { DashboardServices } from "@/services/DashboardServices";

// Variable para almacenar los productos
const topProductos = ref();


onMounted(() => {
    getProductos(); // Obtener los productos al cargar el componente
});

// Función para obtener los productos más vendidos
async function getProductos(){
    // Llamada al servicio para obtener top 10 productos
    topProductos.value = (await DashboardServices.obtenerTop10ProductosVendidos()).data;
    console.log(topProductos.value); // Log para depuración
}
</script>

<template>
    <div class="card">
        <!-- Tabla de productos -->
        <DataTable 
            class="tabla" 
            ref="dt"  
            :value="topProductos"  
            dataKey="nombre"
        >
            <!-- Columna para el nombre del producto -->
            <Column field="nombre" header="Nombre" aria-sort="none"></Column>
            
            <!-- Columna para la cantidad vendida -->
            <Column field="precio" header="Cantidad Vendida" aria-sort="none">
                <template #body="slotProps">
                    <div>
                        {{ formatearNumero(slotProps.data.cantidadVendida) }}
                    </div>
                </template>
            </Column>
            
            <!-- Columna para el estado del producto -->
            <Column aria-sort="none">
                <template #body="slotProps">
                    <div v-if="slotProps.data.estado">
                        <Tag value="SinStock"></Tag> <!-- Mostrar etiqueta si no hay stock -->
                    </div>
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<style>

</style>