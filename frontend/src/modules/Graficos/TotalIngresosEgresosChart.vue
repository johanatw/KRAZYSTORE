<template>
    <div class="card">
        <Chart type="line" :data="chartData" :options="chartOptions" style="height: 250px;"  />
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { DashboardServices } from "@/services/DashboardServices";
import Chart from 'primevue/chart';

onMounted(() => {
    getIngresosEgresosUltimos6Meses();
    
    
});


const chartData = ref();
const chartOptions = ref();
const chartIngresosEgresosUltimos6Meses = ref();

const getIngresosEgresosUltimos6Meses = async () => {
    try {
      const response = await DashboardServices.obtenerDashboard();
      chartIngresosEgresosUltimos6Meses.value = response.data;
      console.log(chartIngresosEgresosUltimos6Meses.value);
      chartData.value = setChartData();
      chartOptions.value = setChartOptions();
    } catch (error) {
       //alert(error);
    }
};
        
const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    console.log(chartIngresosEgresosUltimos6Meses.value);
    return {
        labels: chartIngresosEgresosUltimos6Meses.value?.labels,
        datasets: [
            {
                label: 'Ingresos',
                data: chartIngresosEgresosUltimos6Meses.value?.totalIngresos,
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-pink-600'),
                tension: 0.4
            },
            {
                label: 'Egresos',
                data: chartIngresosEgresosUltimos6Meses.value?.totalEgresos,
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-yellow-600'),
                tension: 0.4
            }
        ]
    };
};
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
                    color: textColor
                }
            }
        },
        scales: {
            x: {
                ticks: {
                    color: textColorSecondary
                },
                grid: {
                    color: surfaceBorder
                }
            },
            y: {
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