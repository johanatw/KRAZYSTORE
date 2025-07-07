<template>
    <!-- Contenedor principal centrado -->
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
        <!-- Gráfico de donut -->
         <div class="card flex justify-center" style="justify-content: center;">

            <Chart 
            type="doughnut" 
            :data="chartData" 
            :options="chartOptions" 
            style="height: 250px;"  
        />
         </div>
        
    </div>
</template>

<script setup>
// Importaciones 
import { ref, onMounted, computed } from "vue";
import { DashboardServices } from "@/services/DashboardServices";
import { Select } from "primevue";
import Chart from "primevue/chart";

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
    getVentasPorCategorias(periodo);
    console.log(periodo);
}

const monthName = computed(() => {
  return months[parseInt(selectedMonth.value, 10) - 1];
});

// Variables 
const chartData = ref(); // Datos para el gráfico
const chartOptions = ref(null); // Opciones del gráfico
const ventasPorCategorias = ref(); // Almacena los datos de ventas por categoría

// Función para obtener ventas por categoría
const getVentasPorCategorias = async (periodo) => {
    try {
        const response = await DashboardServices.obtenerVentasPorCategoriaChart(periodo);
        ventasPorCategorias.value = response.data;
        chartData.value = setChartData(); // Configurar datos del gráfico
        chartOptions.value = setChartOptions(); // Configurar opciones del gráfico
    } catch (error) {
        // Manejo de errores
    }
};

// Función para configurar los datos del gráfico
const setChartData = () => {
    const documentStyle = getComputedStyle(document.body);

    return {
        // Usar las etiquetas de los datos o un valor por defecto
        labels: ventasPorCategorias.value?.labels || ['Sin dato'],
        datasets: [
            {
                // Usar las cantidades de los datos o 0 por defecto
                data: ventasPorCategorias.value?.cantidades || 0,
                // Colores para las secciones del gráfico
                backgroundColor: [
                    documentStyle.getPropertyValue('--p-pink-400'), 
                    documentStyle.getPropertyValue('--p-sky-400'), 
                    documentStyle.getPropertyValue('--p-yellow-400')
                ],
                // Colores al pasar el mouse
                hoverBackgroundColor: [
                    documentStyle.getPropertyValue('--p-pink-300'), 
                    documentStyle.getPropertyValue('--p-sky-300'), 
                    documentStyle.getPropertyValue('--p-yellos-300') // Nota: hay un typo en 'yellow'
                ]
            }
        ]
    };
};

// Función para configurar las opciones del gráfico
const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');

    return {
        plugins: {
            legend: {
                labels: {
                    cutout: '60%', // Tamaño del agujero central
                    color: textColor // Color del texto
                }
            }
        }
    };
};
</script>