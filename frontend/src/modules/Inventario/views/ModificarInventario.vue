
<template>
    <div class="flex p-fluid justify-content-center " >
        <Panel style=" position: relative; width: 100%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Inventario N° {{ router.currentRoute.value.params.id }}</h3>
                </div>
            </template>
     
            <template #icons>
                <div class="flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;"  @click="verInventario(router.currentRoute.value.params.id)" />
                    <Button  label="Guardar" @click="guardarInventario()" />
                </div>
            </template>
            <div >
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
                            <div class="field" >
                                Fecha: <DatePicker v-model="fecha" dateFormat="dd/mm/yy" showIcon iconDisplay="input" />
                            </div> 

                        </template>
                    </Card>
                </div> 
                <div class="field col-12 md:col-12">
                    <Card>
                        <template #content>
                            <div class="flex justify-content-between flex-wrap">
                    <div >
                        <div class="flex align-items-center gap-2 mb-3">
                            <label for="categoria-filter">Filtrar por Categoría:</label>
                            <MultiSelect fluid
                                id="categoria-filter"
                                v-model="filters.categoria.value"
                                :options="categorias"
                                optionLabel="descripcion"
                                placeholder="Seleccione una categoría"
                                style="min-width: 14rem"
                                :maxSelectedLabels="1"
                                @change="onFilterChange"
                            >
                                <template #option="slotProps">
                                    <div class="flex align-items-center gap-2">
                                        <span>{{ slotProps.option.descripcion }}</span>
                                    </div>
                                </template>
                            </MultiSelect>
                        </div>
                    </div>
                    <div >
                        <Button icon="pi pi-external-link" label="Export" @click="exportCSV()" />
                    </div>
                </div>
                <DataTable v-model:filters="filters" :value="detalleInventario" paginator :rows="10" @filter="onFilter" dataKey="id" ref="dt" filterDisplay="row" :loading="loading"
                >
                    <template #empty> No customers found. </template>
                    <template #loading> Loading customers data. Please wait. </template>
                    <Column field="producto" sortable header="Producto" aria-sort="ascending" ></Column>
                    <Column header="Categoría" filterField="categoria" :showFilterMenu="false" :filterMenuStyle="{ width: '14rem' }" style="min-width: 14rem">
                        <template #body="{ data }">
                            <div class="flex align-items-center gap-2">
                                <span>{{ data.categoria.descripcion }}</span>
                            </div>
                        </template>
                    </Column>
                    <Column field="stockActual" sortable header="Stock Existente" aria-sort="ascending" ></Column>
                    <Column  class="col" field="cantContada" header="Cantidad Contada" aria-sort="none">
                        <template #body="slotProps">
                            <div class="flex-auto p-fluid" style="max-width:15lvb  !important; ">
                                <InputNumber fluid class="inpCant" v-model="slotProps.data.cantContada" inputId="minmax-buttons" mode="decimal" showButtons :min="0"  />
                            </div>  
                        </template>
                    </Column>
                    <Column  class="col" field="diferencia" header="Diferencia" aria-sort="none" >
                        <template #body="slotProps">
                            <div class="flex-auto p-fluid" style="max-width: 20dvh;">
                                <label for="diferencia"> {{  (slotProps.data.cantContada - slotProps.data.stockActual).toLocaleString("de-DE") }}</label>
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
import router from '@/router';
import jsPDF from "jspdf";
import "jspdf-autotable";
import {InventarioServices} from '@/services/InventarioServices';
import { formatearNumero } from '@/utils/utils';
import Card from 'primevue/card';
import DatePicker from 'primevue/datepicker';

const detalleInventario = ref();
const inventario = ref();
const categorias = ref();
const filtros = ref();
const fecha= ref();
const filters = ref({
    categoria: { value: null, matchMode: FilterMatchMode.IN },
});

const loading = ref(false);
const dt = ref();
const productosFiltrados = ref([]);

const onFilter = (event) => {
    // Guardar la lista filtrada reactivamente
    productosFiltrados.value = event.filteredValue || detalleInventario.value;
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
    InventarioServices.obtenerDetallesCompletos(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data.inventario);
        console.log(data.data.detalle);
        console.log(data.data.filtrosInventario);
        inventario.value = data.data.inventario;
        detalleInventario.value = data.data.detalle;
        filtros.value = data.data.filtrosInventario;
        filters.value.categoria.value = filtros.value;
        fecha.value = new Date(data.data.inventario.fecha);
    });

    ProductoServices.obtenerCategorias().then((data) => {
        categorias.value = data.data;
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

const guardarInventario = () =>{
    let fechaAnticipo = new Date();
    let ant = {fecha: fechaAnticipo, estado: 'N'};

    let anticipoCreationDTO = {inventario: inventario.value, detalle: productosFiltrados.value};
    InventarioServices.modificarInventario(inventario.value.id, anticipoCreationDTO).then((data)=> {
        let id = data.data.id;
        verInventario(data.data.id);
    } );
}

const verInventario = (id) =>{
    router.push({name: 'ver_inventario', params: {id}});
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