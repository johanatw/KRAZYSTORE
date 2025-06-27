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
import { EmpresasDeliveryServices } from '@/services/EmpresasTransporteServices';
import { PuntoRetiroServices } from '@/services/PuntoEntregaServices';
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


const deliveryDialog = ref(false); // Controla visibilidad del diálogo
const deliverys = ref([]); // Lista de empresas delivery
const delivery = ref({}); // Datos de la empresa actual
const submitted = ref(false); // Estado de envío del formulario

// Filtros para la tabla
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

// Obtener datos 
onMounted(() => {
    getDeliverys();
});

// Obtener lista de empresas delivery
async function getDeliverys() {
    try {
        const { data } = await EmpresasDeliveryServices.obtenerEmpresasDelivery();
        deliverys.value = data;
    } catch (error) {
        showError('Error al obtener registros');
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

// Abrir diálogo para nueva empresa
const registrarDelivery = () => {    
    delivery.value = {};
    deliveryDialog.value = true;
};

// Obtener y cargar datos de empresa para editar
async function modificarDelivery(id) {
    try {
        const { data } = await EmpresasDeliveryServices.getDelivery(id);
        delivery.value = data;
        deliveryDialog.value = true;
    } catch (error) {
        showError('Error al obtener el registro');
    }
};

// Confirmar y eliminar empresa
const eliminarDelivery = (id) => {
    confirm.require({
        message: '¿Está seguro de eliminar este registro?',
        header: 'Confirmación',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Sí, eliminar',
        rejectLabel: 'Cancelar',
        accept: async () => {
            try {
                await EmpresasDeliveryServices.eliminar(id);
                showSuccess('Registro eliminado correctamente');
                getDeliverys();
            } catch (error) {
                showError('Error al eliminar');
            }
        }
    });
};

// Guardar empresa 
const saveDelivery = async () => {
    submitted.value = true;
    
    // Validar campo requerido
    if (!delivery.value.descripcion?.trim()) {
        showError('Complete todos los campos requeridos');
        return;
    }

    try {
        if (delivery.value.id) {
            // Actualizar empresa existente
            await EmpresasDeliveryServices.modificarDelivery(delivery.value.id, delivery.value);
            showSuccess('Registro actualizado correctamente');
        } else {
            // Crear nueva empresa
            await EmpresasDeliveryServices.registrarDelivery(delivery.value);
            showSuccess('Registro creado correctamente');
        }

        deliveryDialog.value = false;
        getDeliverys();
    } catch (error) {
        showError('Error al guardar');
    }
};

// Cerrar diálogo
const hideDialog = () => {
    deliveryDialog.value = false;
    submitted.value = false;
};
</script>

<template>
    <div class="flex p-fluid justify-content-center">
        <ConfirmDialog></ConfirmDialog>
        <!-- Diálogo para agregar/editar empresa -->
        <Dialog v-model:visible="deliveryDialog" :style="{width: '450px'}" header="Delivery" :modal="true" class="p-fluid">
            <div class="formgrid">
                <div class="field">
                    <label for="nombre">Descripción</label>
                    <InputText fluid id="nombre" v-model.trim="delivery.descripcion" required 
                              :class="{'p-invalid': submitted && !delivery.descripcion}" />
                    <small class="p-error" v-if="submitted && !delivery.descripcion">Descripción es requerido</small>
                </div>          
            </div>
            
            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" @click="hideDialog" class="p-button-text"/>
                <Button label="Guardar" icon="pi pi-check" @click="saveDelivery" autofocus />
            </template>
        </Dialog>

        <!-- Panel principal -->
        <Panel style="position: relative; width: 90%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Empresas de Delivery</h3>
                </div>
            </template>
            
            <template #icons>
                <div class="flex align-items-center">
                    <!-- Botón para agregar nueva empresa -->
                    <Button icon="pi pi-plus" @click="registrarDelivery" style="margin-right: 1%;" />
                    <!-- Buscador -->
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
            
            <!-- Tabla de empresas -->
            <div class="card">
                <DataTable :value="deliverys" scrollHeight="400px"  
                          :paginator="true" :rows="7" :filters="filters"
                          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
                    <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
                    <Column field="descripcion" sortable header="Descripción" aria-sort="ascending"></Column>
                    <Column :exportable="false" style="min-width:10rem">
                        <template #body="slotProps">
                            <!-- Botones de acciones -->
                            <Button icon="pi pi-pencil" text rounded severity="success" 
                                    @click="modificarDelivery(slotProps.data.id)" 
                                    v-tooltip="'Editar'" 
                                    style="height: 2rem !important; width: 2rem !important;"/>
                            <Button icon="pi pi-trash" text rounded severity="danger" 
                                    @click="eliminarDelivery(slotProps.data.id)" 
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