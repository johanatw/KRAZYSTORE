<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { ProveedorServices } from '@/services/ProveedorServices';
import { ProductoServices } from '@/services/ProductoServices';
import { PreciosVentaService } from '@/services/PreciosVentaService';
import Panel from 'primevue/panel';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Toast from 'primevue/toast';
import DatePicker from 'primevue/datepicker';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import Select from 'primevue/select';
import InputNumber from 'primevue/inputnumber';
import Checkbox from 'primevue/checkbox';
import AutoComplete from 'primevue/autocomplete';
import { formatearFecha, formatearNumero } from '@/utils/utils';
import router from '@/router';
import { PreciosCompraServices } from '@/services/PreciosCompraServices';

const productos = ref();
const precios = ref();
const selectedProduct = ref();
const categorias = ref([]);
const confirm = useConfirm();
const toast = useToast();
const precioDialog = ref(false);
const visualizarProductoDialog = ref(false);
const precio = ref({});
const submitted = ref(false);
const today = ref(new Date());
const producto = ref({});

const confirm2 = (id) => {
   
    confirm.require({
        message: 'Eliminar el reembolso #'+ id + '?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Eliminar',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: () => {
            deleteReembolso(id);
            
        },
        
    });
};
onMounted(() => {

    ProductoServices.obtenerProducto(router.currentRoute.value.params.id).then((data) => {
        producto.value = data.data;
       
    });
 
    PreciosCompraServices.getPreciosProducto(router.currentRoute.value.params.id).then((data) => {
        precios.value = data.data;
        console.log(precios.value);
    });
 
   /* ProductoServices.obtenerCategorias().then((data) => {
        categorias.value = data.data;
        console.log(productos.value);
    });*/
    
});

async function getPrecios() {
    return await (PreciosVentaService.obtenerPrecios()).data;
}

let timeout;

const search = (event) => {
    console.log(event);
    const searchQuery = event.query.toLowerCase();

    // Limpiar el timeout anterior
    clearTimeout(timeout);

    // Establecer un nuevo timeout para ejecutar la búsqueda después de 300ms
    timeout = setTimeout(async () => {
        if (!searchQuery.trim()) {
            // Si la búsqueda está vacía, puedes optar por mostrar todos los resultados
            productos.value = [];
            return;
        }

        // Realizar la llamada a la API (o backend) para obtener los resultados filtrados
        try {
            const response = await ProductoServices.obtenerProductosByValor(searchQuery);
            const data = await response.data;

            // Asignar los resultados filtrados a la lista
            productos.value = data;  // Asumiendo que la respuesta contiene los datos de clientes
        } catch (error) {
            console.error('Error al buscar clientes:', error);
        }
    }, 300); 
}

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const registrarProducto = () =>{
    
    precio.value = {};
    precio.value.producto = producto.value;
    precioDialog.value = true;
}

const modificarProducto = (id) => {
    PreciosCompraServices.getPrecio(id).then((data) => {
        console.log("data direccion");
       
       precio.value = data.data;
       precioDialog.value = true;       
       
    });
    
};

const isfechaPasada = (fecha) => {
    let fechaActual = new Date();
    let fechaPrecio = new Date(fecha);
    let FechaSinHoraActual = new Date(fechaActual.toDateString());
    let FechaSinHoraPrecio = new Date(fechaPrecio.toDateString());
  
    if (FechaSinHoraPrecio < FechaSinHoraActual) {
        return true;
    } else {
        return false;
    }    
};

const verProducto = (id) => {
    ProductoServices.obtenerProducto(id).then((data) => {
        console.log("data direccion");
       
       precio.value = data.data;
       visualizarProductoDialog.value = true;       
       
    });
    
};

const findIndexById = (id) => {
    let index = -1;
    for (let i = 0; i < precios.value.length; i++) {

        if (precios.value[i].id === id) {
          console.log("if");
            index = i;
            break;
        }
    }
    console.log(index);
    return index;
};

const hideDialog = () => {
    visualizarProductoDialog.value = false;
    precioDialog.value = false;
    submitted.value = false;
};

const saveProducto = () => {
    submitted.value = true;

    if (precio?.value.fecha && precio?.value.costo) {
        if (precio.value.id) {
          console.log("proveedor.value");
          console.log(precio.value);

          PreciosCompraServices.modificarPrecio(precio.value.id, precio.value).then((response)=>{

                precios.value[findIndexById(response.data.id)] = response.data;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
            }).catch(
                (error)=>messageError("error")
            );
            
        }
        else {
            PreciosCompraServices.registrarPrecio(precio.value).then((response)=>{
            console.log(response.data);
                precios.value.push(response.data);
                 toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
            }).catch(
                (error)=>messageError("error")
            );
        }
        submitted.value = false;
        precioDialog.value = false;
        precio.value = {};
        
    }
};

</script>

<template>
    
  <div class="flex p-fluid justify-content-center " >

    <ConfirmDialog ></ConfirmDialog>
    <Toast />
    <!--Dialog Registrar Producto-->
    <Dialog v-model:visible="precioDialog" :closable="false" :style="{width: '450px'}" header="Precio de compra" :modal="true" class="p-fluid">
        <div class="formgrid">
        <div class="field">
            <label for="description">Fecha</label>
            <DatePicker dateFormat="dd/mm/yy" fluid v-model="precio.fecha" showIcon iconDisplay="input" :invalid="submitted && !precio.fecha" :min-date="today" />
            <small class="p-error" v-if="submitted && !precio.fecha">Seleccione una fecha</small>
        </div>
        <div class="field">
            <label for="description">Precio de compra</label>
            <InputNumber fluid id="description" v-model="precio.costo" required="true" :invalid="submitted && !precio.costo"  />
            <small class="p-error" v-if="submitted && !precio.costo">Ingrese el precio</small>
        </div>
    </div>
        <template #footer>
            <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
            <Button label="Save" icon="pi pi-check" text @click="saveProducto" />
        </template>
    </Dialog>

    <Panel style=" position: relative; width: 90%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Precios de compra: {{ producto.nombre }}</h3>
        </div>
      </template>
    

      <template #icons>
        <div class="flex align-items-center">
          <!--<Button  icon="pi pi-plus " @click="registrarProducto" style="margin-right: 1% ;"  />-->
          <InputGroup>
            <InputText v-model="filters['global'].value" placeholder="Search..." />
            <InputGroupAddon>
              <i class="pi pi-search" />
            </InputGroupAddon>
        </InputGroup>
        </div>
    
      </template>
      
  
      <div >
        
        <DataTable  :value="precios" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <Column field="fecha"  header="Fecha" aria-sort="ascending" sortable> 
            <template #body="slotProps" >
                {{ formatearFecha(slotProps.data.fecha) }}
            </template>
        </Column>   
        <Column field="precio"  header="Precio Gs." aria-sort="ascending" sortable> 
            <template #body="slotProps" >
                {{ formatearNumero(slotProps.data.costo) }}
            </template>
        </Column> 
        <!--
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <Button icon="pi pi-pencil" :disabled="isfechaPasada(slotProps.data.fecha)" text rounded aria-label="Search" @click="modificarProducto(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-times" :disabled="isfechaPasada(slotProps.data.fecha)" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>-->
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>