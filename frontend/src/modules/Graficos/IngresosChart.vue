<template>
    <div  class=" flex p-fluid justify-content-between "  >
            <h3 style="font-weight: bold;" >Ingresos</h3>
            <div >
                <Select v-model="añoConsultar" :options="añosDisponibles"  placeholder="Seleccione un año" class="w-full md:w-56" />
            </div>
    </div>
    <div>
        <Chart type="bar" :data="chartData" :options="chartOptions" style="height: 250px;"  />
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
    getIngresosPorAño(año.value);
    
    
});



const mesesBase = ref([
  "enero", "febrero", "marzo", "abril", "mayo", "junio",
  "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
]);

const chartData = ref();
const chartOptions = ref();
const chartIngresosEgresosUltimos6Meses = ref();
const datoscompletos = ref({
    labels: [],
    ingresosAnticipo: [],
    ingresosOtros: [],
    ingresosVenta: []
});

const getIngresosPorAño = async (año) => {
    try {
      const response = await DashboardServices.obtenerIngresosPorAño(año);
      chartIngresosEgresosUltimos6Meses.value = response.data;
        getDatosCompletos();
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
        let index = chartIngresosEgresosUltimos6Meses.value?.labels.findIndex((r) => r.toLowerCase() === mes);
            let montoAnticipos = (index>-1)?chartIngresosEgresosUltimos6Meses.value.ingresosAnticipo[index]:0;
            let montoVentas = (index>-1)?chartIngresosEgresosUltimos6Meses.value.ingresosVenta[index]:0;
            let montoOtros = (index>-1)?chartIngresosEgresosUltimos6Meses.value.ingresosOtros[index]:0;
            datoscompletos.value.labels.push(mes);
            datoscompletos.value.ingresosAnticipo.push(montoAnticipos);
            datoscompletos.value.ingresosVenta.push(montoVentas);
            datoscompletos.value.ingresosOtros.push(montoOtros);
        
        });
};

const setChartData = () =>  {
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