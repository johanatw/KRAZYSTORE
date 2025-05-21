
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
                    <Button  label="Cancelar"  style="margin-right: 1%;"  @click="vistaListaInventarios()" />
                    <Button  label="Finalizar" @click="guardarInventario()" />
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
                            <div >
                                Fecha: {{ formatearFecha(inventario.fecha) }}
                            </div> 
                        </template>
                    </Card>
                </div> 

                    <!--Lista de productos-->
                    <div class="field col-12 md:col-12">
                        <Card>
                            <template #content>
                                <DataTable v-model:filters="filters" 
                                :value="productosFiltrados" paginator :rows="10" dataKey="id" ref="dt">
                                    <template #empty> No hay registros para mostrar. </template>
                                    <template #loading> Loading customers data. Please wait. </template>
                                    <Column field="producto.nombre"  header="Producto" aria-sort="ascending" ></Column>
                                    <Column header="Categoría" >
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2">
                                                <span>{{ data.producto.subCategoria.categoria.descripcion }}</span>
                                            </div>
                                        </template>
                                    </Column>
                                    <Column header="Sub Categoría">
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2">
                                                <span>{{ data.producto.subCategoria.descripcion }}</span>
                                            </div>
                                        </template>
                                    </Column>
                      
                                    <Column header="Contado" >
                                        <template #body="{ data }">
                                            <div class="flex align-items-center gap-2">
                                                <div class="flex-auto p-fluid" style="max-width:10lvb  !important; ">
                                                    <InputNumber fluid class="inpCant" v-model="data.cantContada" mode="decimal" :min="0"  />
                                                </div>
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
import { CategoriaServices } from '@/services/CategoriaServices';
import Card from 'primevue/card';
import DatePicker from 'primevue/datepicker';
import { useToast } from "primevue/usetoast";
import { formatearFecha } from '@/utils/utils';
const toast = useToast();
const categorias = ref();
const subCategorias = ref([]);
const selectedCategorias = ref([]);
const selectedSubCategorias = ref([]);

const detalleInventario = ref();
const inventario = ref({});
const filtros = ref();
const fecha= ref();
const filters = ref({
    categoria: { value: null, matchMode: FilterMatchMode.IN },
});
const showDiferencia = ref(false);
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
    
    InventarioServices.getInventario(router.currentRoute.value.params.id).then((data) => {
        inventario.value = data.data.inventario;
        productosFiltrados.value = data.data.detalle;
        console.log(productosFiltrados.value);
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
    InventarioServices.finalizarInventario(inventario.value.id, anticipoCreationDTO).then((data)=> {
        let id = data.data.id;
        showSuccess('Inventario finalizado correctamente');
        vistaListaInventarios();
    } );
}

const verInventario = (id) =>{
    router.push({name: 'ver_inventario', params: {id}});
}

const vistaListaInventarios = () => {
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