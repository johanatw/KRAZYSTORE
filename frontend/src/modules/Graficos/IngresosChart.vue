<template>
    <!-- Encabezado con título y selector de año -->
    <div class="flex p-fluid justify-content-between">
        <h3 style="font-weight: bold;">Ingresos</h3>
        <div>
            <Select v-model="añoConsultar" :options="añosDisponibles" placeholder="Seleccione un año" class="w-full md:w-56" />
        </div>
    </div>
    
    <!-- Gráfico de barras -->
    <div>
        <Chart type="bar" :data="chartData" :options="chartOptions" style="height: 250px;" />
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

// Variables para el gráfico
const chartData = ref();
const chartOptions = ref();
const chartIngresosEgresosUltimos6Meses = ref(); // Datos de ingresos por mes

// Estructura para almacenar datos completos del gráfico
const datoscompletos = ref({
    labels: [],              // Nombres de los meses
    ingresosAnticipo: [],    // Ingresos por anticipos
    ingresosOtros: [],       // Otros ingresos
    ingresosVenta: []        // Ingresos por ventas
});


onMounted(() => {
    añoConsultar.value = año.value; // Establecer año actual por defecto
    getAñosDisponibles(); // Obtener lista de años disponibles
    getIngresosPorAño(año.value); // Obtener datos del año actual
});

// Función para obtener ingresos por año
const getIngresosPorAño = async (año) => {
    try {
        const response = await DashboardServices.obtenerIngresosPorAño(año);
        chartIngresosEgresosUltimos6Meses.value = response.data;
        getDatosCompletos(); // Procesar datos para el gráfico
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

// Función para completar datos del gráfico
const getDatosCompletos = async () => {
    // Determinar hasta qué mes mostrar (si es año actual, hasta mes actual)
    let mesHasta = añoConsultar.value == año.value ? (mesActual.value + 1) : 12;
    
    // Procesar cada mes
    mesesBase.value.slice(0, mesHasta).map((mes) => {
        let index = chartIngresosEgresosUltimos6Meses.value?.labels.findIndex((r) => r.toLowerCase() === mes);
        let montoAnticipos = (index > -1) ? chartIngresosEgresosUltimos6Meses.value.ingresosAnticipo[index] : 0;
        let montoVentas = (index > -1) ? chartIngresosEgresosUltimos6Meses.value.ingresosVenta[index] : 0;
        let montoOtros = (index > -1) ? chartIngresosEgresosUltimos6Meses.value.ingresosOtros[index] : 0;
        
        // Agregar datos a la estructura
        datoscompletos.value.labels.push(mes);
        datoscompletos.value.ingresosAnticipo.push(montoAnticipos);
        datoscompletos.value.ingresosVenta.push(montoVentas);
        datoscompletos.value.ingresosOtros.push(montoOtros);
    });
};

// Función para configurar datos del gráfico
const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: datoscompletos.value?.labels,
        datasets: [
            {
                type: 'bar',
                label: 'Ventas',
                backgroundColor: documentStyle.getPropertyValue('--p-pink-300'),
                data: datoscompletos.value?.ingresosVenta
            },
            {
                type: 'bar',
                label: 'Anticipos',
                backgroundColor: documentStyle.getPropertyValue('--p-yellow-300'),
                data: datoscompletos.value?.ingresosAnticipo
            },
            {
                type: 'bar',
                label: 'Otros',
                backgroundColor: documentStyle.getPropertyValue('--p-sky-300'),
                data: datoscompletos.value?.ingresosOtros
            }
        ]
    };
};

// Función para configurar opciones del gráfico
const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--p-text-muted-color');
    const surfaceBorder = documentStyle.getPropertyValue('--p-content-border-color');

    return {
        maintainAspectRatio: false,
        aspectRatio: 0.8,
        plugins: {
            tooltips: {
                mode: 'index',
                intersect: false
            },
            legend: {
                labels: {
                    color: textColor
                }
            }
        },
        scales: {
            x: {
                stacked: true, // Barras apiladas en eje X
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y: {
                stacked: true, // Barras apiladas en eje Y
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            }
        }
    };
}
</script>