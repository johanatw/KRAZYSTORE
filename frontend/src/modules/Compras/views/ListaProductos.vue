<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { ProveedorServices } from '@/services/ProveedorServices';
import { ProductoServices } from '@/services/ProductoServices';
import Panel from 'primevue/panel';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Toast from 'primevue/toast';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import Select from 'primevue/select';
import InputNumber from 'primevue/inputnumber';
import Checkbox from 'primevue/checkbox';
import router from '@/router';
import { TipoIvaServices } from '@/services/TipoIvaServices';
import { CategoriaServices } from '@/services/CategoriaServices';

// Variables 
const productos = ref();
const categorias = ref([]);
const tiposIva = ref([]);
const confirm = useConfirm();
const subCategorias = ref([]);
const toast = useToast();
const productoDialog = ref(false);
const visualizarProductoDialog = ref(false);
const producto = ref({});
const submitted = ref(false);

// Función para mostrar diálogo de confirmación de eliminación
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
    // Obtener lista de productos
    ProductoServices.obtenerProductos().then((data) => {
        productos.value = data.data;
        console.log(productos.value);
    });
    
    // Obtener lista de categorías
    ProductoServices.obtenerCategorias().then((data) => {
        categorias.value = data.data;
        console.log(productos.value);
    });
    
    // Obtener tipos de IVA
    TipoIvaServices.obtenerTiposIva().then((data) => {
        tiposIva.value = data.data;
    });    
});

// Filtros para la tabla 
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

// Función para diálogo de registro de nuevo producto
const registrarProducto = () => {
    producto.value = {};
    productoDialog.value = true;
}

// Función para obtener subcategorías según la categoría 
const obtenerSubCategoriasByCategoria = (id) => {
    CategoriaServices.obtenerSubCatByIdCat(id).then((data) => {
        console.log(data.data);
        subCategorias.value = data.data;  
    });
};

// Función para modificar un producto 
const modificarProducto = (id) => {
    ProductoServices.obtenerProducto(id).then((data) => {
        console.log("data direccion");
        producto.value = data.data;
        producto.value.categoria = data.data.subCategoria.categoria;
        obtenerSubCategoriasByCategoria(producto.value.subCategoria.categoria.id);
        productoDialog.value = true;        
    });
};

// Función para visualizar detalles de un producto
const verProducto = (id) => {
    ProductoServices.obtenerProducto(id).then((data) => {
        console.log("data direccion");
        producto.value = data.data;
        visualizarProductoDialog.value = true;        
    });   
};

// Función para encontrar el índice de un producto 
const findIndexById = (id) => {
    let index = -1;
    for (let i = 0; i < productos.value.length; i++) {
        if (productos.value[i].id === id) {
            console.log("if");
            index = i;
            break;
        }
    }
    console.log(index);
    return index;
};

// Función para cerrar diálogos
const hideDialog = () => {
    visualizarProductoDialog.value = false;
    productoDialog.value = false;
    submitted.value = false;
};

// Función para historial de precios
const verHistorialPrecios = (id) => {
    router.push({name: 'precios_compra', params: {id}});
};

// Función para guardar un producto 
const saveProducto = () => {
    submitted.value = true;
    if (producto?.value.nombre?.trim() && producto.value.categoria && producto.value.tipoIva ) {
        if (producto.value.id) {
            console.log("proveedor.value");
            console.log(producto.value);
            ProductoServices.modificarProducto(producto.value.id, producto.value).then((response)=>{
                productos.value[findIndexById(response.data.id)] = response.data;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
            }).catch(
                (error)=>messageError("error")
            );   
        }
        else {
            ProductoServices.saveProducto(producto.value).then((response)=>{
                console.log("reg");
                productos.value.unshift(response.data);
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
            }).catch(
                (error)=>messageError("error")
            );
        }
        submitted.value = false;
        productoDialog.value = false;
        producto.value = {};
    }
};
</script>

<template>    
    <div class="flex p-fluid justify-content-center">
        <ConfirmDialog></ConfirmDialog>
        <Toast />
        
        <!-- Diálogo para registrar/editar producto -->
        <Dialog v-model:visible="productoDialog" :closable="false" :style="{width: '450px'}" header="Producto" :modal="true" class="p-fluid">
            <div class="formgrid">
                <div class="field">
                    <label for="name">Nombre</label>
                    <InputText fluid id="name" v-model.trim="producto.nombre" required="true" autofocus :class="{'p-invalid': submitted && !producto.nombre}" />
                    <small class="p-error" v-if="submitted && !producto.nombre">Ingrese un Nombre</small>
                </div>
                <div class="field">
                    <label for="name">Descripcion</label>
                    <InputText fluid id="name" v-model.trim="producto.descripcion" required="true" autofocus />
                </div>
                <div class="field">
                    <label for="description">Categoria</label>
                    <Select fluid v-model="producto.categoria" :options="categorias" @change="obtenerSubCategoriasByCategoria(producto.categoria.id)" optionLabel="descripcion" placeholder="Seleccione una categoría" class="w-full md:w-56"  :class="{'p-invalid': submitted && !producto.categoria}"/>
                    <small class="p-error" v-if="submitted && !producto.categoria">Seleccione una categoria</small>
                </div>
                <div class="field">
                    <label for="description">Sub Categoria</label>
                    <Select fluid v-model="producto.subCategoria" :options="subCategorias" optionLabel="descripcion" placeholder="Seleccione una subcategoría" class="w-full md:w-56"  :class="{'p-invalid': submitted && !producto.subCategoria}"/>
                    <small class="p-error" v-if="submitted && !producto.subCategoria">Seleccione una categoria</small>
                </div>
                <div class="field">
                    <label for="description">Iva</label>
                    <Select fluid v-model="producto.tipoIva" :options="tiposIva" optionLabel="descripcion" placeholder="Seleccione el IVA" class="w-full md:w-56"  :class="{'p-invalid': submitted && !producto.tipoIva}"/>
                    <small class="p-error" v-if="submitted && !producto.tipoIva">Seleccione el IVA</small>
                </div>
                <div v-if="!producto.id" class="field">
                    <label for="description">Precio compra</label>
                    <InputNumber fluid id="description" v-model="producto.costo" required="true" :invalid="submitted && !producto.costo"  />
                    <small class="p-error" v-if="submitted && !producto.costo">Ingrese el precio</small>
                </div>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveProducto" />
            </template>
        </Dialog>
        
        <!-- Diálogo para visualizar producto -->
        <Dialog v-model:visible="visualizarProductoDialog" :closable="false" :style="{width: '450px'}" header="Producto" :modal="true" class="p-fluid">
            <div class="formgrid">
                <div class="field">
                    <label for="name">Nombre: </label>
                    {{ producto.nombre }}
                </div>
                <div class="field">
                    <label for="name">Descripcion: </label>
                    {{ producto.descripcion }}
                </div>
                <div class="field">
                    <label for="name">Categoria: </label>
                    {{ producto.subCategoria.categoria.descripcion }}
                </div>
                <div class="field">
                    <label for="name">Sub Categoria: </label>
                    {{ producto.subCategoria.descripcion }}
                </div>
                <div class="field">
                    <label for="name">Iva: </label>
                    {{ producto.tipoIva.descripcion }}
                </div>
            </div>
            <template #footer>
                <Button label="Cerrar" icon="pi pi-times" text @click="hideDialog"/>
            </template>
        </Dialog>
        
        <Panel style="position: relative; width: 90%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Productos</h3>
                </div>
            </template>
            
            <template #icons>
                <div class="flex align-items-center">
                    <Button icon="pi pi-plus" @click="registrarProducto" style="margin-right: 1%;" />
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
            
            <div>
                <DataTable :value="productos" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
                    <Column field="nombre" header="Producto" aria-sort="ascending" sortable></Column>
                    <Column field="subCategoria.categoria.descripcion" header="Categoría" aria-sort="ascending" sortable></Column>   
                    <Column field="subCategoria.descripcion" header="Subcategoría" aria-sort="ascending" sortable></Column> 
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-eye" v-tooltip="'Ver detalles'" text rounded aria-label="Search" @click="verProducto(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-pencil" v-tooltip="'Editar'" severity="success" text rounded aria-label="Search" @click="modificarProducto(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-trash" v-tooltip="'Modificar'" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-money-bill" v-tooltip="'Ver precios'" severity="info" text rounded aria-label="Cancel" @click="verHistorialPrecios(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" /> 
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
</template>