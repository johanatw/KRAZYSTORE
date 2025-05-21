
<template>
    <div class="flex p-fluid justify-content-center " >
        <Panel style=" position: relative; width: 100%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Inventario N° {{ router.currentRoute.value.params.id }}</h3>
                </div>
            </template>
     
            <template #icons>
                <div  class="flex" style="justify-content: end;">  
                    <Button label="Atras"  style="margin-right: 1%;" @click="vistaInventarios()" />
                    <Button label="Modificar" style="margin-right: 1%;" @click="modificarInventario(router.currentRoute.value.params.id)" />
                    <Button label="Ajustar" @click="ajustarInventario(router.currentRoute.value.params.id)" />
                </div>
            </template>
            
            <div>
                <div class="field col-12 md:col-6">
                    <Card>
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Información General
                                </div>    
                            </div>
                        </template>
                        <template #content>
                            <div >
                                Fecha: {{ formatearFecha(inventario.fecha) }}
                            </div> 
                            <div  >
                                Estado: {{getEstadoInventario(inventario.estado)}}
                            </div> 

                        </template>
                    </Card>
                </div> 
                <div class="field col-12 md:col-12">
                    <Card>
                        <template #content>
                            <DataTable :value="detalleInventario" paginator :rows="10" 
                            dataKey="id" ref="dt" filterDisplay="row" :loading="loading">
                                <template #empty> No hay registros para mostrar. </template>
                                    <Column field="producto.nombre" sortable header="Producto" aria-sort="ascending" ></Column>
                                    <Column header="Categoría" :showFilterMenu="false" :filterMenuStyle="{ width: '14rem' }" style="min-width: 14rem">
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2">
                                                <span>{{ data.producto.subCategoria.categoria.descripcion }}</span>
                                            </div>
                                        </template>
                                    </Column>
                                    <Column header="Sub Categoría" :showFilterMenu="false" :filterMenuStyle="{ width: '14rem' }" style="min-width: 14rem">
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2">
                                                <span>{{ data.producto.subCategoria.descripcion }}</span>
                                            </div>
                                        </template>
                                    </Column>
                                    <Column v-if="!isEnCurso(inventario.estado)" header="Stock anterior" >
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2">
                                                <span>{{ data.stockInicialInventario }}</span>
                                            </div>
                                        </template>
                                    </Column>
                                    <Column v-if="!isEnCurso(inventario.estado)" header="Contado" >
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2">
                                                <span>{{ data.cantContada }}</span>
                                            </div>
                                        </template>
                                    </Column>
                                    <Column v-if="!isEnCurso(inventario.estado)" header="Diferencia" >
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2" :style="getColor(data.diferencia)" >
                                                <span>{{ data.diferencia }}</span>
                                            </div>
                                        </template>
                                    </Column>
                </DataTable>

                        </template>
                    </Card>
                </div> 
                
                
            </div>
        </Panel>
    </div>
    
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { ProductoServices } from '@/services/ProductoServices';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Panel from 'primevue/panel';
import Column from 'primevue/column';
import MultiSelect from 'primevue/multiselect';
import jsPDF from "jspdf";
import Card from 'primevue/card';
import router from '@/router';
import "jspdf-autotable";
import {InventarioServices} from '@/services/InventarioServices';
import DatePicker from 'primevue/datepicker';
import { formatearFecha, getEstadoInventario } from '@/utils/utils';
import { useToast } from "primevue/usetoast";
const toast = useToast();
import Toast from 'primevue/toast';
const fecha = ref(new Date());
const detalleInventario = ref();
const inventario = ref({});
const categorias = ref();
const filters = ref({
    'producto.categoria': { value: null, matchMode: FilterMatchMode.IN },
});

const loading = ref(false);
const dt = ref();
const productosFiltrados = ref([]);

    const onFilter = (event) => {
        console.log(event);
        console.log(filters.value.categoria);
      // Guardar la lista filtrada reactivamente
      productosFiltrados.value = event.filteredValue || detalleInventario.value;
      console.log("Productos filtrados (evento):", productosFiltrados.value);
    };

const exportCSV = () => {
    
    const doc = new jsPDF();

    
      // Define the table headers and rows
      const headers = [["Nombre","Categoria","Cantidad Contada"]];
      const data = productosFiltrados.value.map((c) => [
        c.producto.nombre,
        c.producto.categoria.descripcion,
        
 
      ]);

      // Add the table to the PDF
      doc.autoTable({
        head: headers,
        body: data,
        startY: 10, // Adjust starting position
        styles: { fontSize: 10 },
        headStyles: { fillColor: [41, 128, 185] },
      });

      // Save the PDF
      doc.save("products.pdf");
};

onMounted(() => {
    InventarioServices.getInventario(router.currentRoute.value.params.id).then((data) => {
        inventario.value = data.data.inventario;
        detalleInventario.value = data.data.detalle;
    });
});

const getSeverity = (status) => {
    switch (status) {
        case 'unqualified':
            return 'danger';

        case 'qualified':
            return 'success';

        case 'new':
            return 'info';

        case 'negotiation':
            return 'warning';

        case 'renewal':
            return null;
    }
}

const getColor = (diferencia) => {
    console.log(diferencia);
    if (diferencia < 0) {
        return 'color: red;';
    }

    if (diferencia > 0) {
        return 'color: green;';
    }

}

const isFinalizado = (estado) => {
    switch (estado) {
        case 'P':
            return true;
        default:
            return false;

    }
}

const isEnCurso = (estado) => {
    switch (estado) {
        case 'S':
            return true;
        default:
            return false;

    }
}

const showError = (message) => {
    toast.add({
      severity: 'error',
      summary: 'Error',
      detail: message,
      life: 3000
    });
  };
  
  const showSuccess = (message) => {
    toast.add({
      severity: 'success',
      summary: 'Éxito',
      detail: message,
      life: 3000
    });
  };

const ajustarInventario = (id) =>{
    InventarioServices.ajustarInventario(id).then((data)=> {
        showSuccess('Inventario ajustado');
        vistaInventarios();
    } );
}

const finalizarInventario = () =>{
    inventario.value.estado = 'F';
    let anticipoCreationDTO = {inventario: inventario.value, detalle: detalleInventario.value};

    InventarioServices.finalizarInventario(inventario.value.id, anticipoCreationDTO).then((data)=> {
        let id = data.data.id;
        inventario.value = data.data;
    } );
}



const modificarInventario = (id) =>{
    router.push({name: 'modificar_inventario', params: {id}});
}

const vistaInventarios= () =>{
    router.push({name: 'inventario'});
}

</script>
<style>
.p-inputgroup-addon{
    padding: 0%;
}

.p-inputnumber-buttons-stacked .p-inputnumber-button-group .p-button.p-inputnumber-button {
    flex: 1 1 auto;
    padding: 0rem;
    width: 1rem;
}
</style>