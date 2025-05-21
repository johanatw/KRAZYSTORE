<template>
    <div class="card"  >
        <Chart type="bar" :data="chartData" :options="chartOptions" style="height: 250px;" />
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

const setChartData = () =>  {
    const documentStyle = getComputedStyle(document.documentElement);

    return {
        labels: chartIngresosEgresosUltimos6Meses.value?.labels,
        datasets: [
            {
                type: 'bar',
                label: 'Compras',
                backgroundColor: documentStyle.getPropertyValue('--p-pink-300'),
                data: chartIngresosEgresosUltimos6Meses.value?.egresosCompra
            },
            {
                type: 'bar',
                label: 'Anticipos',
                backgroundColor: documentStyle.getPropertyValue('--p-yellow-300'),
                data: chartIngresosEgresosUltimos6Meses.value?.egresosAnticipo
            },
            
            {
                type: 'bar',
                label: 'Otros',
                backgroundColor: documentStyle.getPropertyValue('--p-sky-300'),
                data: chartIngresosEgresosUltimos6Meses.value?.egresosOtros
            }
        ]
    };
};
const setChartOptions = () =>  {
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