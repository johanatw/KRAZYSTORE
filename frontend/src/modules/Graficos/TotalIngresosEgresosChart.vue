<template>
    <div  class=" flex p-fluid justify-content-between "  >
            <h3 style="font-weight: bold;" >Ingresos y Egresos</h3>
            <div class="flex " style="flex-direction: row;" >
                <Select v-model="añoConsultar" :options="añosDisponibles"  placeholder="Seleccione un año" class="w-full md:w-56" />
            </div>
    </div>
    <div>
        <Chart type="line" :data="chartData" :options="chartOptions" style="height: 250px;"  />
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { DashboardServices } from "@/services/DashboardServices";
import Chart from 'primevue/chart';
import { Select } from "primevue";
const año = ref(new Date().getFullYear());
const mesActual = ref(new Date().getMonth());
const añoConsultar = ref();
const añosDisponibles = ref([]);

onMounted(() => {
    
    añoConsultar.value = año.value;
    getAñosDisponibles();
    getIngresosEgresosUltimos6Meses(año.value);
    
    
});

const mesesBase = ref([
  "enero", "febrero", "marzo", "abril", "mayo", "junio",
  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
]);

const datoscompletos = ref({
    labels: [],
    totalIngresos: [],
    totalEgresos: []
});

const chartData = ref();
const chartOptions = ref();
const ingresosEgresosMensuales = ref();


const getIngresosEgresosUltimos6Meses = async (año) => {
    try {
      const response = await DashboardServices.obtenerDashboard(año);
      ingresosEgresosMensuales.value = response.data;
      getDatosCompletos();
      console.log(ingresosEgresosMensuales.value);
      chartData.value = setChartData();
      chartOptions.value = setChartOptions();
    } catch (error) {
       //alert(error);
    }
};

const getAñosDisponibles = async () => {
    console.log("AÑOS DI");
    try {
      const response = await DashboardServices.obtenerAñosDisponibles();
      añosDisponibles.value = response.data;
      console.log(añosDisponibles.value);
    } catch (error) {
       //alert(error);
    }
};

const getDatosCompletos = async () =>  {
    let mesHasta = añoConsultar.value == año.value?(mesActual.value + 1):12;
    mesesBase.value.slice(0, mesHasta).map((mes) => {
        let index = ingresosEgresosMensuales.value?.labels.findIndex((r) => r.toLowerCase() === mes);
            let totalIngresos = (index>-1)?ingresosEgresosMensuales.value.totalIngresos[index]:0;
            let totalEgresos = (index>-1)?ingresosEgresosMensuales.value.totalEgresos[index]:0;
            datoscompletos.value.labels.push(mes);
            datoscompletos.value.totalEgresos.push(totalEgresos);
            datoscompletos.value.totalIngresos.push(totalIngresos);
        
        });
};
        
const setChartData = () => {
    const documentStyle = getComputedStyle(document.documentElement);
    console.log(ingresosEgresosMensuales.value);
    return {
        labels: datoscompletos.value?.labels,
        datasets: [
            {
                label: 'Ingresos',
                data: datoscompletos.value?.totalIngresos,
                fill: false,
                borderColor: documentStyle.getPropertyValue('--p-pink-600'),
                tension: 0.4
            },
            {
                label: 'Egresos',
                data: datoscompletos.value?.totalEgresos,
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