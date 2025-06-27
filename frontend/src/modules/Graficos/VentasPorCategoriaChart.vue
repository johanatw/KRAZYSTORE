<template>
    <!-- Contenedor principal centrado -->
    <div class="card flex justify-center" style="justify-content: center;">
        <!-- Gráfico de donut -->
        <Chart 
            type="doughnut" 
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
import Chart from "primevue/chart";


onMounted(() => {
    getVentasPorCategorias(); // Obtener datos al cargar
});

// Variables 
const chartData = ref(); // Datos para el gráfico
const chartOptions = ref(null); // Opciones del gráfico
const ventasPorCategorias = ref(); // Almacena los datos de ventas por categoría

// Función para obtener ventas por categoría
const getVentasPorCategorias = async () => {
    try {
        const response = await DashboardServices.obtenerVentasPorCategoriaChart();
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