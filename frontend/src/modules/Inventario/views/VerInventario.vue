
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
        <Button v-show="inventario?.estado != 'F' " label="Finalizar"  style="margin-right: 1%;" @click="finalizarInventario()" />
        <Button v-show="inventario?.estado != 'F' " label="Modificar" @click="modificarInventario()" />
    </div>

    

  </template>
    <div >
       
        <DataTable v-model:filters="filters" :value="detalleInventario" paginator :rows="10" @filter="onFilter" dataKey="id" ref="dt" filterDisplay="row" :loading="loading"
                >
            <template #empty> No customers found. </template>
            <template #loading> Loading customers data. Please wait. </template>
            <Column field="producto.nombre" sortable header="Producto" aria-sort="ascending" ></Column>
            <Column header="Categoría" filterField="producto.categoria" :showFilterMenu="false" :filterMenuStyle="{ width: '14rem' }" style="min-width: 14rem">
                <template #body="{ data }">
                    <div class="flex align-items-center gap-2">
                        <span>{{ data.producto.categoria.descripcion }}</span>
                    </div>
                </template>
            </Column>
            <Column field="cantStock" sortable header="Stock Existente" aria-sort="ascending" ></Column>
            <Column  class="col" field="cantContada" header="Cantidad Contada" aria-sort="none">
             
         </Column>
         <Column  class="col" field="diferencia" header="Diferencia" aria-sort="none" >
         </Column>
        </DataTable>
    </div>
</Panel>

</div>
    
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { FilterMatchMode } from 'primevue/api';
import { ProductoServices } from '@/services/ProductoServices';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Panel from 'primevue/panel';
import Column from 'primevue/column';
import MultiSelect from 'primevue/multiselect';
import jsPDF from "jspdf";
import router from '@/router';
import "jspdf-autotable";
import {InventarioServices} from '@/services/InventarioServices';

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
    InventarioServices.obtenerDetallesCompletos(router.currentRoute.value.params.id).then((data) => {
        console.log("prueba");
        console.log(data.data);
        inventario.value = data.data.inventario;
        console.log(inventario.value);
       
        
    });

    InventarioServices.getInventario(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        detalleInventario.value = data.data.detalle;
        
    });

    ProductoServices.obtenerCategorias().then((data) => {
        console.log(data.data);
        categorias.value = data.data;
        
    });


});

const getProductos = (data) => {
    return [...(data || [])].map((d) => {
        d.cantContada = 0;

        return d;
    });
};
const formatDate = (value) => {
    return value.toLocaleDateString('en-US', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
};
const formatCurrency = (value) => {
    return value.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
};
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


const finalizarInventario = () =>{
    
    let fechaAnticipo = new Date();


    console.log(inventario.value);
    inventario.value.estado = 'F';
    let anticipoCreationDTO = {inventario: inventario.value, detalle: detalleInventario.value};
    InventarioServices.modificarInventario(inventario.value.id, anticipoCreationDTO).then((data)=> {
        let id = data.data.id;
       // verInventario(data.data.id);

    } );
}

const modificarInventario = (id) =>{
    router.push({name: 'modificar_inventario', params: {id}});
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