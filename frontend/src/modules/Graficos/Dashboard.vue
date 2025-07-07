<template>
    <!-- Contenedor principal flex -->
    <div class="flex p-fluid justify-content-center">
        <!-- Panel del dashboard -->
        <Panel style=" position: relative; width: 90%;">
            <!-- Encabezado del panel -->
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Dashboard</h3>
                </div>
            </template>
            
            <!-- Contenido principal -->
            <div>
                <div class="card">
                    <div class="formgrid grid">
                        <!-- Columna para gráfico de ingresos -->
                        <div class="field col-12 md:col-6 p-fluid">
                            <Card>
                                <template #content>
                                    <IngresosChart />
                                </template>
                            </Card>
                        </div>
                        
                        <!-- Columna para gráfico de egresos -->
                        <div class="field col-12 md:col-6 p-fluid">
                            <Card>
                                <template #content>
                                    <EgresosChart />
                                </template>
                            </Card>
                        </div>
                        
                        <!-- Columna completa para gráfico combinado -->
                        <div class="field col-12 md:col-12 p-fluid" style="justify-content: start;">
                            <Card>
                                <template #content>
                                    <TotalIngresosEgresosChart />
                                </template>
                            </Card>
                        </div>
                        
                        <!-- Columna para ventas por categoría -->
                        <div class="field col-12 md:col-6 p-fluid" style="justify-content: center;">
                            <Card>
                                <template #content>
                                    <VentasPorCategoriaChart />
                                </template>
                            </Card>
                        </div>
                        
                        <!-- Columna para productos más vendidos -->
                        <div class="field col-12 md:col-6 p-fluid" style="justify-content: center;">
                            <Card>
                                <template #content>
                                    <Top10ProductosVendidosChart />
                                </template>
                            </Card>
                        </div>
                    </div>
                </div>
            </div>
        </Panel>
    </div>
</template>

<script setup>
// Importaciones 
import { ref, onMounted } from "vue";
import Card from "primevue/card";
import Panel from "primevue/panel";
import Chart from 'primevue/chart';
import { DashboardServices } from "@/services/DashboardServices";
import TotalIngresosEgresosChart from "./TotalIngresosEgresosChart.vue";
import IngresosChart from "./IngresosChart.vue";
import EgresosChart from "./EgresosChart.vue";
import VentasPorCategoriaChart from "./VentasPorCategoriaChart.vue";
import Top10ProductosVendidosChart from "./Top10ProductosVendidosChart.vue";


onMounted(() => {
    chartData.value = setChartData();
    chartOptions.value = setChartOptions();
    getIngresosEgresosUltimos6Meses();
});

// Función para obtener ingresos/egresos últimos 6 meses
const getIngresosEgresosUltimos6Meses = async () => {
    try {
        const response = await DashboardServices.obtenerVentasPorCategoriaChart();
        console.log(response);
    } catch (error) {
        //alert(error);
    }
};

// Variables para el gráfico
const chartData = ref();
const chartOptions = ref();

// Configuración de datos del gráfico
const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                type: 'bar',
                label: 'Dataset 1',
                backgroundColor: documentStyle.getPropertyValue('--p-cyan-500'),
                data: [50, 25, 12, 48, 90, 76, 42]
            },
            {
                type: 'bar',
                label: 'Dataset 2',
                backgroundColor: documentStyle.getPropertyValue('--p-gray-500'),
                data: [21, 84, 24, 75, 37, 65, 34]
            },
            {
                type: 'bar',
                label: 'Dataset 3',
                backgroundColor: documentStyle.getPropertyValue('--p-orange-500'),
                data: [41, 52, 24, 74, 23, 21, 32]
            }
        ]
    };
};

// Configuración de opciones del gráfico
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
                stacked: true,
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y: {
                stacked: true,
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