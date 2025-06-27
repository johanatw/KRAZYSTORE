<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Panel from 'primevue/panel';
import InputText from 'primevue/inputtext';
import Dropdown from 'primevue/dropdown';
import { PersonaServices } from '@/services/PersonaServices';
import { CategoriaServices } from '@/services/CategoriaServices';
import { ClienteServices } from '@/services/ClienteServices';
import { FilterMatchMode } from '@primevue/core/api';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import { DepartamentoServices } from '@/services/DepartamentoServices';
import { CiudadServices } from '@/services/CiudadServices';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import ConfirmDialog from 'primevue/confirmdialog';

const router = useRouter();
const confirm = useConfirm();
const toast = useToast();


const categoriaDialog = ref(false); // Controla visibilidad del diálogo de categoría
const verclienteDialog = ref(false); // Diálogo para ver cliente 
const selectedOp = ref('Casi'); // Opción seleccionada ç
const categorias = ref([]); // Lista de categorías
const categoria = ref({}); // Datos de la categoría actual
const direccion = ref({}); // Datos de dirección 
const submitted = ref(false); // Estado de envío del formulario
const departamentos = ref([]); // Lista de departamentos 
const opciones = ref(['Casi','Entre']); // Opciones 
const ciudades = ref([]); // Lista de ciudades
const viewClienteDialog = ref(false); // Diálogo para ver detalles
const categoriaView = ref(false); // Datos de categoría para visualización
const direccionView = ref({}); // Datos de dirección para visualización 

// Filtros para la tabla
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});


const documentos = ref([
    { id: 1, descripcion: 'CI' },
    { id: 2, descripcion: 'RUC' },
    { id: 3, descripcion: 'Pasaporte' }
]);

const opcionesCalle = ref([
    { label: 'y', value: 'y' },
    { label: 'Entre', value: 'Entre' }
]);

// Obtener datos
onMounted(() => {
    getCategorias();
});

// Obtener lista de categorías
async function getCategorias() {
    try {
        const { data } = await CategoriaServices.obtenerCategorias();
        categorias.value = data;
    } catch (error) {
        showError('Error al obtener categorias');
    }
}

// Obtener departamentos 
async function getDepartamentos() {
    try {
        const { data } = await DepartamentoServices.obtenerDepartamentos();
        departamentos.value = data;
    } catch (error) {
        showError('Error al obtener departamentos');
    }
}

// Mostrar notificación de error
const showError = (message) => {
    toast.add({
        severity: 'error',
        summary: 'Error',
        detail: message,
        life: 3000
    });
};

// Mostrar notificación de éxito
const showSuccess = (message) => {
    toast.add({
        severity: 'success',
        summary: 'Éxito',
        detail: message,
        life: 3000
    });
};

// Abrir diálogo para nueva categoría
const registrarCategoria = () => {    
    categoria.value = {};
    categoriaDialog.value = true;
};

// Obtener y cargar datos de categoría para editar
async function modificarCategoria(id) {
    try {
        const { data } = await CategoriaServices.getCategoria(id);
        categoria.value = data;
        categoriaDialog.value = true;
    } catch (error) {
        showError('Error al obtener la categoria');
    }
};

// Confirmar y eliminar categoría
const eliminarCategoria = (id) => {
    confirm.require({
        message: '¿Está seguro de eliminar este registro?',
        header: 'Confirmación',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Sí, eliminar',
        rejectLabel: 'Cancelar',
        accept: async () => {
            try {
                await CategoriaServices.eliminar(id);
                showSuccess('Categoria eliminado correctamente');
                getCategorias();
            } catch (error) {
                showError('Error al eliminar categoria');
            }
        }
    });
};

// Ver detalles de categoría
async function verCategoria(id) {
    try {
        const { data } = await CategoriaServices.getCategoria(id);
        categoriaView.value = data;
        viewClienteDialog.value = true;
    } catch (error) {
        showError('Error al obtener la categoria');
    }
};

// Guardar categoría
const saveCategoria = async () => {
    submitted.value = true;
    
    // Validar campo requerido
    if (!categoria.value.descripcion?.trim()) {
        showError('Complete todos los campos requeridos');
        return;
    }

    try {
        if (categoria.value.id) {
            // Actualizar categoría 
            await CategoriaServices.modificarCategoria(categoria.value.id, categoria.value);
            showSuccess('Categoria actualizado correctamente');
        } else {
            // Crear nueva categoría
            await CategoriaServices.registrarCategoria(categoria.value);
            showSuccess('Categoria creado correctamente');
        }

        categoriaDialog.value = false;
        getCategorias();
    } catch (error) {
        showError('Error al guardar la categoria');
    }
};

// Cerrar diálogo
const hideDialog = () => {
    categoriaDialog.value = false;
    submitted.value = false;
};
</script>

<template>
    <div class="flex p-fluid justify-content-center">
        <!-- Diálogo de confirmación -->
        <ConfirmDialog></ConfirmDialog>
        
        <!-- Diálogo para agregar/editar categoría -->
        <Dialog v-model:visible="categoriaDialog" :style="{width: '450px'}" header="Categoria" :modal="true" class="p-fluid">
            <div class="formgrid">
                <div class="field">
                    <label for="nombre">Descripcion</label>
                    <InputText fluid id="nombre" v-model.trim="categoria.descripcion" required 
                              :class="{'p-invalid': submitted && !categoria.descripcion}" />
                    <small class="p-error" v-if="submitted && !categoria.descripcion">Descripcion es requerido</small>
                </div>          
            </div>
            
            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" @click="hideDialog" class="p-button-text"/>
                <Button label="Guardar" icon="pi pi-check" @click="saveCategoria" autofocus />
            </template>
        </Dialog>

        <!-- Diálogo para visualizar detalles -->
        <Dialog v-model:visible="viewClienteDialog" :style="{width: '500px'}" header="Detalles del Cliente" :modal="true">
            <div class="p-fluid">
                <div class="field grid">
                    <label class="col-4 font-bold">Descripcion:</label>
                    <div class="col-8">{{ categoriaView.descripcion }}</div>
                </div>
            </div>

            <template #footer>
                <Button label="Cerrar" icon="pi pi-times" @click="viewClienteDialog = false" class="p-button-text"/>
            </template>
        </Dialog>
        
        <!-- Panel principal -->
        <Panel style="position: relative; width: 90%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Categorias</h3>
                </div>
            </template>
            
            <template #icons>
                <div class="flex align-items-center">
                    <!-- Botón para agregar nueva categoría -->
                    <Button icon="pi pi-plus" @click="registrarCategoria" style="margin-right: 1%;" />
                    <!-- Buscador -->
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
            
            <!-- Tabla de categorías -->
            <div class="card">
                <DataTable :value="categorias" scrollHeight="400px"  
                          :paginator="true" :rows="7" :filters="filters"
                          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
                    <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
                    <Column field="descripcion" sortable header="Descripcion" aria-sort="ascending"></Column>
                    <Column :exportable="false" style="min-width:10rem">
                        <template #body="slotProps">
                            <!-- Botones de acciones -->
                            <Button icon="pi pi-eye" text rounded  
                                    @click="verCategoria(slotProps.data.id)" 
                                    v-tooltip="'Ver detalles'"
                                    style="height: 2rem !important; width: 2rem !important;"/>
                            <Button icon="pi pi-pencil" text rounded severity="success" 
                                    @click="modificarCategoria(slotProps.data.id)" 
                                    v-tooltip="'Editar'" 
                                    style="height: 2rem !important; width: 2rem !important;"/>
                            <Button icon="pi pi-trash" text rounded severity="danger" 
                                    @click="eliminarCategoria(slotProps.data.id)" 
                                    v-tooltip="'Eliminar'"
                                    style="height: 2rem !important; width: 2rem !important;"/>
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel> 
    </div>       
</template>

<style scoped>
.p-inputgroup-addon{
    padding: 0%;
}

.p-inputnumber-buttons-stacked .p-inputnumber-button-group .p-button.p-inputnumber-button {
    flex: 1 1 auto;
    padding: 0rem;
    width: 1rem;
}
</style>