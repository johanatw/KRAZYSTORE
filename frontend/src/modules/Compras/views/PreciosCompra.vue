<script setup>
// Importación de componentes y utilidades de Vue y PrimeVue
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';

// Importación de servicios
import { ProveedorServices } from '@/services/ProveedorServices';
import { ProductoServices } from '@/services/ProductoServices';
import { PreciosVentaService } from '@/services/PreciosVentaService';

// Más importaciones de componentes
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

// Variables 
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

// Función para confirmar eliminación
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
    // Obtener datos del producto actual
    ProductoServices.obtenerProducto(router.currentRoute.value.params.id).then((data) => {
        producto.value = data.data;
    });
 
    // Obtener precios de compra del producto
    PreciosCompraServices.getPreciosProducto(router.currentRoute.value.params.id).then((data) => {
        precios.value = data.data;
        console.log(precios.value);
    });
});

// Función para obtener precios
async function getPrecios() {
    return await (PreciosVentaService.obtenerPrecios()).data;
}

// Función de búsqueda 
let timeout;
const search = (event) => {
    console.log(event);
    const searchQuery = event.query.toLowerCase();
    clearTimeout(timeout);
    timeout = setTimeout(async () => {
        if (!searchQuery.trim()) {
            productos.value = [];
            return;
        }
        try {
            const response = await ProductoServices.obtenerProductosByValor(searchQuery);
            const data = await response.data;
            productos.value = data;
        } catch (error) {
            console.error('Error al buscar clientes:', error);
        }
    }, 300); 
}

// Filtros para la tabla
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

// Función para registrar nuevo producto
const registrarProducto = () =>{
    precio.value = {};
    precio.value.producto = producto.value;
    precioDialog.value = true;
}

// Función para modificar producto 
const modificarProducto = (id) => {
    PreciosCompraServices.getPrecio(id).then((data) => {
        precio.value = data.data;
        precioDialog.value = true;       
    });
};

// Función para verificar si una fecha es pasada
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

// Función para ver detalles de producto
const verProducto = (id) => {
    ProductoServices.obtenerProducto(id).then((data) => {
        precio.value = data.data;
        visualizarProductoDialog.value = true;       
    });
};


const findIndexById = (id) => {
    let index = -1;
    for (let i = 0; i < precios.value.length; i++) {
        if (precios.value[i].id === id) {
            index = i;
            break;
        }
    }
    return index;
};

// Otros
const hideDialog = () => {
    visualizarProductoDialog.value = false;
    precioDialog.value = false;
    submitted.value = false;
};

// Función para guardar producto
const saveProducto = () => {
    submitted.value = true;

    if (precio?.value.fecha && precio?.value.costo) {
        if (precio.value.id) {
            PreciosCompraServices.modificarPrecio(precio.value.id, precio.value).then((response)=>{
                precios.value[findIndexById(response.data.id)] = response.data;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
            }).catch(
                (error)=>messageError("error")
            ); 
        }
        else {
            PreciosCompraServices.registrarPrecio(precio.value).then((response)=>{
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
    <div class="flex p-fluid justify-content-center">
        <!-- Componentes de diálogo y notificación -->
        <ConfirmDialog></ConfirmDialog>
        <Toast />
        
        <!-- Diálogo para registrar/editar precios -->
        <Dialog v-model:visible="precioDialog" :closable="false" :style="{width: '450px'}" header="Precio de compra" :modal="true" class="p-fluid">
            <div class="formgrid">
                <div class="field">
                    <label for="description">Fecha</label>
                    <DatePicker dateFormat="dd/mm/yy" fluid v-model="precio.fecha" showIcon iconDisplay="input" :invalid="submitted && !precio.fecha" :min-date="today" />
                    <small class="p-error" v-if="submitted && !precio.fecha">Seleccione una fecha</small>
                </div>
                <div class="field">
                    <label for="description">Precio de compra</label>
                    <InputNumber fluid id="description" v-model="precio.costo" required="true" :invalid="submitted && !precio.costo" />
                    <small class="p-error" v-if="submitted && !precio.costo">Ingrese el precio</small>
                </div>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveProducto" />
            </template>
        </Dialog>

        <!-- Panel principal -->
        <Panel style="position: relative; width: 90%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Precios de compra: {{ producto.nombre }}</h3>
                </div>
            </template>
        
            <template #icons>
                <div class="flex align-items-center">
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Search..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
        
            <!-- Tabla de precios -->
            <div>
                <DataTable :value="precios" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
                    <Column field="fecha" header="Fecha" aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            {{ formatearFecha(slotProps.data.fecha) }}
                        </template>
                    </Column>   
                    <Column field="precio" header="Precio Gs." aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            {{ formatearNumero(slotProps.data.costo) }}
                        </template>
                    </Column> 
                </DataTable>
            </div>
        </Panel>
    </div>
</template>