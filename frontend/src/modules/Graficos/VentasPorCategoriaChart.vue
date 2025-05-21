
<template>
    <div class="card flex justify-center" style="justify-content: center;">
        <Chart type="doughnut" :data="chartData" :options="chartOptions" style="height: 250px;"  />
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { DashboardServices } from "@/services/DashboardServices";
import Chart from "primevue/chart";

onMounted(() => {
    getVentasPorCategorias();
});


const chartData = ref();
const chartOptions = ref(null);

const ventasPorCategorias= ref();

const getVentasPorCategorias = async () => {
    try {
      const response = await DashboardServices.obtenerVentasPorCategoriaChart();
      ventasPorCategorias.value = response.data;
      console.log(ventasPorCategorias.value);
      chartData.value = setChartData();
      chartOptions.value = setChartOptions();
    } catch (error) {
       //alert(error);
    }
};

const setChartData = () => {
    const documentStyle = getComputedStyle(document.body);

    return {
        labels: ventasPorCategorias.value?.labels || ['Sin dato'],
        datasets: [
            {
                data: ventasPorCategorias.value?.cantidades || 0,
                backgroundColor: [documentStyle.getPropertyValue('--p-pink-400'), documentStyle.getPropertyValue('--p-sky-400'), documentStyle.getPropertyValue('--p-yellow-400')],
                hoverBackgroundColor: [documentStyle.getPropertyValue('--p-pink-300'), documentStyle.getPropertyValue('--p-sky-300'), documentStyle.getPropertyValue('--p-yellos-300')]
            }
        ]
    };
};

const setChartOptions = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--p-text-color');

    return {
        plugins: {
            legend: {
                labels: {
                    cutout: '60%',
                    color: textColor
                }
            }
        }
    };
};
</script>
