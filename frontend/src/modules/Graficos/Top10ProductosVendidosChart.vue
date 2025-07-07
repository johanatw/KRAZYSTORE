<script setup>
// Importaciones
import Button from 'primevue/button';
import { ref, onMounted, computed } from "vue";
import Tag from 'primevue/tag';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { Select } from 'primevue';
import { formatearNumero } from '@/utils/utils';
import { DashboardServices } from "@/services/DashboardServices";

// Variable para almacenar los productos
const topProductos = ref();

const now = ref(new Date());
const selectedYear = ref();
const selectedMonth = ref();
const years = ref();
const months = [
  { label: "Enero", value: "01" },
  { label: "Febrero", value: "02" },
  { label: "Marzo", value: "03" },
  { label: "Abril", value: "04" },
  { label: "Mayo", value: "05" },
  { label: "Junio", value: "06" },
  { label: "Julio", value: "07" },
  { label: "Agosto", value: "08" },
  { label: "Septiembre", value: "09" },
  { label: "Octubre", value: "10" },
  { label: "Noviembre", value: "11" },
  { label: "Diciembre", value: "12" }
];

onMounted(() => {
    selectedYear.value = now.value.getFullYear();
    selectedMonth.value = String(now.value.getMonth() + 1).padStart(2, '0');
    console.log(selectedMonth.value);
    years.value = getYearRange(2022, now.value.getFullYear());
    onFilterChange(); // Obtener los productos al cargar el componente
});

const getYearRange = (start, end) => {
    const years = [];
      for (let y = end; y >= start; y--) {
        years.push(y);
      }
      return years;
}

const onFilterChange = () => {
    const periodo = `${selectedYear.value}-${selectedMonth.value}`;
    getProductos(periodo);
    console.log(periodo);
}

const monthName = computed(() => {
  return months[parseInt(selectedMonth.value, 10) - 1];
});


// Función para obtener los productos más vendidos
async function getProductos(periodo){
    // Llamada al servicio para obtener top 10 productos
    topProductos.value = (await DashboardServices.obtenerTop10ProductosVendidos(periodo)).data;
    console.log(topProductos.value); // Log para depuración
}



</script>

<template>
    <div class="card">
        <div class="flex p-fluid justify-content-between" style="margin-bottom: 1%;">
            <div class="flex" style="flex-direction: column; ">
                <h3 style="font-weight: bold;">Top 10 productos más vendidos </h3>
                 {{monthName?.label}} - {{ selectedYear }} 
            </div>
        
            <div class="flex" style="flex-direction: row;">
                <Select v-model="selectedMonth" :options="months" @change="onFilterChange()" optionLabel="label"
                    optionValue="value" placeholder="Seleccione un mes" />
                <Select v-model="selectedYear" :options="years" @change="onFilterChange()"  placeholder="Seleccione un año"  />
            </div>
        </div>
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