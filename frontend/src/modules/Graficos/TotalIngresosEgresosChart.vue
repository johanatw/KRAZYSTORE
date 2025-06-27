<template>
    <!-- Encabezado con título -->
    <div class="flex p-fluid justify-content-between">
        <h3 style="font-weight: bold;">Ingresos y Egresos</h3>
        <div class="flex" style="flex-direction: row;">
            <Select 
                v-model="añoConsultar" 
                :options="añosDisponibles"  
                placeholder="Seleccione un año" 
                class="w-full md:w-56" 
            />
        </div>
    </div>
    
    <!-- Gráfico de líneas -->
    <div>
        <Chart 
            type="line" 
            :data="chartData" 
            :options="chartOptions" 
            style="height: 250px;"  
        />
    </div>
</template>

<script setup>
// Importaciones 
import { ref, onMounted } from "vue";
import { DashboardServices } from "@/services/DashboardServices";
import Chart from 'primevue/chart';
import { Select } from "primevue";

// Variables 
const año = ref(new Date().getFullYear()); // Año actual
const mesActual = ref(new Date().getMonth()); // Mes actual (0-11)
const añoConsultar = ref(); // Año seleccionado en el dropdown
const añosDisponibles = ref([]); // Lista de años disponibles

// Lista de meses del año
const mesesBase = ref([
  "enero", "febrero", "marzo", "abril", "mayo", "junio",
  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
]);

// Estructura para los datos del gráfico
const datoscompletos = ref({
    labels: [],           // Nombres de los meses
    totalIngresos: [],    // Totales de ingresos por mes
    totalEgresos: []      // Totales de egresos por mes
});

// Variables para el gráfico
const chartData = ref();
const chartOptions = ref();
const ingresosEgresosMensuales = ref(); // Datos de ingresos/egresos


onMounted(() => {
    añoConsultar.value = año.value; // Establecer año actual por defecto
    getAñosDisponibles(); // Obtener lista de años disponibles
    getIngresosEgresosUltimos6Meses(año.value); // Obtener datos del año actual
});

// Función para obtener datos de ingresos/egresos
const getIngresosEgresosUltimos6Meses = async (año) => {
    try {
        const response = await DashboardServices.obtenerDashboard(año);
        ingresosEgresosMensuales.value = response.data;
        getDatosCompletos(); // Procesar los datos
        chartData.value = setChartData(); // Configurar datos del gráfico
        chartOptions.value = setChartOptions(); // Configurar opciones del gráfico
    } catch (error) {
        //alert(error);
    }
};

// Función para obtener años disponibles
const getAñosDisponibles = async () => {
    try {
        const response = await DashboardServices.obtenerAñosDisponibles();
        añosDisponibles.value = response.data;
    } catch (error) {
        //alert(error);
    }
};

// Función para completar los datos del gráfico
const getDatosCompletos = async () => {
    // Determinar hasta qué mes mostrar (si es año actual, hasta mes actual)
    let mesHasta = añoConsultar.value == año.value ? (mesActual.value + 1) : 12;
    
    // Procesar cada mes
    mesesBase.value.slice(0, mesHasta).map((mes) => {
        let index = ingresosEgresosMensuales.value?.labels.findIndex((r) => r.toLowerCase() === mes);
        let totalIngresos = (index > -1) ? ingresosEgresosMensuales.value.totalIngresos[index] : 0;
        let totalEgresos = (index > -1) ? ingresosEgresosMensuales.value.totalEgresos[index] : 0;
        
        // Agregar datos a la estructura
        datoscompletos.value.labels.push(mes);
        datoscompletos.value.totalIngresos.push(totalIngresos);
        datoscompletos.value.totalEgresos.push(totalEgresos);
    });
};

// Función para configurar los datos del gráfico
const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: datoscompletos.value?.labels,
        datasets: [
            {
                label: 'Ingresos',
                data: datoscompletos.value?.totalIngresos,
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-pink-600'), // Color para ingresos
                tension: 0.4 // Suavizado de la línea
            },
            {
                label: 'Egresos',
                data: datoscompletos.value?.totalEgresos,
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-yellow-600'), // Color para egresos
                tension: 0.4 // Suavizado de la línea
            }
        ]
    };
};

// Función para configurar las opciones del gráfico
const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    return {
        maintainAspectRatio: false,
        aspectRatio: 0.6,
        plugins: {
            legend: {
                labels: {
                    color: textColor // Color de las etiquetas
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary // Color de los textos en eje X
                },
                grid: {
                    color: surfaceBorder // Color de la cuadrícula
                }
            },
            y: {
                ticks: {
                    color: textColorSecondary // Color de los textos en eje Y
                },
                grid: {
                    color: surfaceBorder // Color de la cuadrícula
                }
            }
        }
    };
}
</script>