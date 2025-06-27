<template>
    <div class="flex p-fluid justify-content-center " >
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Nuevo Control de Inventario</h3>
                </div>
            </template>
     
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                     <Button  label="Cancelar"  style="margin-right: 1%;" @click="vistaInventarios()" />
                     <Button  label="Guardar" @click="guardarInventario()" />
                </div>
            </template>
                <div >
                    <!--Info General Inventario -->
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
                    <!--Filtros -->
                    <div class="field col-12 md:col-12">
                        <Card>
                            <template #content>
                                <div class="flex justify-content-between flex-wrap">
                                    <div >
                                        <div class="flex align-items-center gap-2 mb-3">
                                            <label for="categoria-filter">Categoría:</label>
                                            <MultiSelect v-model="selectedCategorias" 
                                            :options="categorias" optionLabel="descripcion" 
                                            placeholder="Seleccione un valor"
                                            :maxSelectedLabels="1"  
                                            @change="getSubCategorias()" />
                                        </div>
                                    </div>
                                    <div >
                                        <div class="flex align-items-center gap-2 mb-3">
                                            <label for="categoria-filter">Sub Categoría:</label>
                                            <MultiSelect v-model="selectedSubCategorias" 
                                            :options="subCategorias" optionLabel="descripcion" 
                                            placeholder="Seleccione un valor"
                                            :maxSelectedLabels="1"   />
                                        </div>
                                    </div>
                                    <div >
                                        <Button icon="pi pi-search" label="Buscar" @click="getProductosByFiltros()" />
                                    </div>
                                </div>
                            </template>
                        </Card>
                    </div>
                    <!--Lista de productos-->
                    <div class="field col-12 md:col-12">
                        <Card>
                            <template #content>
                                <div class="flex justify-content-end flex-wrap">
                                    <div >
                                        <Button icon="pi pi-external-link" label="Export" @click="exportCSV()" />
                                    </div>
                                </div>
                                <DataTable v-model:filters="filters" 
                                :value="productosFiltrados" paginator :rows="10" dataKey="id" ref="dt">
                                    <template #empty> No hay registros para mostrar. </template>
                                    <template #loading> Loading customers data. Please wait. </template>
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
                                </DataTable>
                            </template>
                        </Card>
                    </div>
                </div>
            </Panel>
        </div>
</template>

<script setup>
//Importaciones
import { ref, onMounted, computed } from 'vue';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { ProductoServices } from '@/services/ProductoServices';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Card from 'primevue/card';
import Panel from 'primevue/panel';
import Column from 'primevue/column';
import MultiSelect from 'primevue/multiselect';
import jsPDF from "jspdf";
import "jspdf-autotable";
import {InventarioServices} from '@/services/InventarioServices';
import { CategoriaServices } from '@/services/CategoriaServices';
import DatePicker from 'primevue/datepicker';
import router from '@/router';
import { useToast } from "primevue/usetoast";

//Variables
const toast = useToast();
const categorias = ref();
const subCategorias = ref([]);
const selectedCategorias = ref([]);
const selectedSubCategorias = ref([]);

const fecha = ref(new Date());

const loading = ref(false);
const dt = ref();
const productosFiltrados = ref([]);

const exportCSV = () => {
    
    const doc = new jsPDF();

      // Define the table headers and rows
      const headers = [["Nombre","Categoria","Sub Categoria","Cantidad Contada"]];
      const data = productosFiltrados.value.map((c) => [
        c.producto.nombre,
        c.producto.subCategoria.categoria.descripcion,
        c.producto.subCategoria.descripcion,
 
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

    CategoriaServices.obtenerCategorias().then((data) => {
        console.log(data.data);
        categorias.value = data.data;
        
    });

});

const getSubCategorias = async () => {
    try {
        let categoriasIds = getCategoriasIds();
        const response = await CategoriaServices.obtenerSubCatByIdsCat(categoriasIds);
        subCategorias.value= response.data;
    } catch (error) {
        
    }
};


const getCategoriasIds = () => {
   return selectedCategorias.value.map(c => c.id) // Solo guardamos los IDs
}

const getSubCategoriasIds = () => {
   return selectedSubCategorias.value.map(c => c.id) // Solo guardamos los IDs
}

const getProductosByFiltros = async () => {
    try {
        let subCategoriasIds = getSubCategoriasIds();
        const response = await InventarioServices.getDetallesInventarioIniciales(subCategoriasIds);
        console.log(response.data);
        productosFiltrados.value = response.data;
    } catch (error) {
        
    }
};


const guardarInventario = () =>{
    let fecha = new Date();
    let ant = {fecha: fecha, estado: 'N'};
    let anticipoCreationDTO = {inventario: ant, detalle: productosFiltrados.value};

    InventarioServices.registrarInventario(anticipoCreationDTO).then((data)=> {
        showSuccess('Control de Inventario creado');
        let id = data.data.id;
        vistaInventarios();        
    } );
}

const verInventario = (id) =>{
    router.push({name: 'ver_inventario', params: {id}});
}

const vistaInventarios= () =>{
    router.push({name: 'inventario'});
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