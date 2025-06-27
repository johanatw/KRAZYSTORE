<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { DatePicker } from 'primevue';
import Panel from 'primevue/panel';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import { PersonaServices } from '@/services/PersonaServices';
import { CategoriaServices } from '@/services/CategoriaServices';
import { EmpresasDeliveryServices } from '@/services/EmpresasTransporteServices';
import { TimbradoServices } from '@/services/TimbradoServices';
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
import { formatearFecha } from '@/utils/utils';

const router = useRouter();
const confirm = useConfirm();
const toast = useToast();


const timbradoDialog = ref(false); // Controla visibilidad del diálogo
const timbrados = ref([]); // Lista de timbrados
const timbrado = ref({}); // Datos del timbrado actual
const submitted = ref(false); // Estado de envío del formulario

// Filtros para la tabla
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

// Obtener datos
onMounted(() => {
    getTimbrados();
});

// Obtener lista de timbrados
async function getTimbrados() {
    try {
        const { data } = await TimbradoServices.obtenerTimbrados();
        timbrados.value = data;
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

// Abrir diálogo para nuevo timbrado
const registrarTimbrado = () => {    
    timbrado.value = {};
    timbradoDialog.value = true;
};

// Obtener y cargar datos de timbrado para editar
async function modificarTimbrado(id) {
    try {
        const { data } = await TimbradoServices.getTimbrado(id);
        timbrado.value = data;
        timbradoDialog.value = true;
    } catch (error) {
        showError('Error al obtener el registro');
    }
};

// Confirmar y dar de baja timbrado
const eliminarTimbrado = (id) => {
    confirm.require({
        message: '¿Está seguro de dar de baja este timbrado?',
        header: 'Confirmación',
        icon: 'pi pi-exclamation-triangle',
        acceptLabel: 'Sí, dar de baja',
        rejectLabel: 'Cancelar',
        accept: async () => {
            try {
                await TimbradoServices.eliminar(id);
                showSuccess('Registro eliminado correctamente');
                getTimbrados();
            } catch (error) {
                showError('Error al eliminar');
            }
        }
    });
};

// Guardar timbrado
const saveTimbrado = async () => {
    submitted.value = true;
    
    // Validar campos requeridos
    if (!timbrado.value.numeroTimbrado || 
        !timbrado.value.codEstablecimiento || 
        !timbrado.value.puntoExpedicion || 
        !timbrado.value.vigenciaInicio || 
        !timbrado.value.vigenciaFin || 
        !timbrado.value.numeroInicio || 
        !timbrado.value.numeroFin) {
        showError('Complete todos los campos requeridos');
        return;
    }

    try {
        if (timbrado.value.id) {
            // Actualizar timbrado existente
            await TimbradoServices.modificarTimbrado(timbrado.value.id, timbrado.value);
            showSuccess('Registro actualizado correctamente');
        } else {
            // Crear nuevo timbrado
            timbrado.value.fecha_creacion = new Date();
            await TimbradoServices.registrarTimbrado(timbrado.value);
            showSuccess('Registro creado correctamente');
        }

        timbradoDialog.value = false;
        getTimbrados();
    } catch (error) {
        showError('Error al guardar');
    }
};

// Cerrar diálogo
const hideDialog = () => {
    timbradoDialog.value = false;
    submitted.value = false;
};
</script>

<template>
    <div class="flex p-fluid justify-content-center">
        <!-- Diálogo de confirmación -->
        <ConfirmDialog></ConfirmDialog>
        
        <!-- Diálogo para agregar/editar timbrado -->
        <Dialog v-model:visible="timbradoDialog" :style="{width: '450px'}" header="Timbrado" :modal="true" class="p-fluid">
            <div class="formgrid">
                <!-- Número de timbrado -->
                <div class="field">
                    <label for="nombre">Número</label>
                    <InputText fluid id="nombre" v-model.trim="timbrado.numeroTimbrado" required 
                              :class="{'p-invalid': submitted && !timbrado.numeroTimbrado}" />
                    <small class="p-error" v-if="submitted && !timbrado.numeroTimbrado">Campo requerido</small>
                </div>
                
                <!-- Código de establecimiento -->
                <div class="field">
                    <label for="nombre">Cod. de establecimiento</label>
                    <InputText fluid id="nombre" v-model.trim="timbrado.codEstablecimiento" required 
                              :class="{'p-invalid': submitted && !timbrado.codEstablecimiento}" />
                    <small class="p-error" v-if="submitted && !timbrado.codEstablecimiento">Campo requerido</small>
                </div>
                
                <!-- Punto de expedición -->
                <div class="field">
                    <label for="nombre">Punto de expedicion</label>
                    <InputText fluid id="nombre" v-model.trim="timbrado.puntoExpedicion" required 
                              :class="{'p-invalid': submitted && !timbrado.puntoExpedicion}" />
                    <small class="p-error" v-if="submitted && !timbrado.puntoExpedicion">Campo requerido</small>
                </div>  
                
                <!-- Fecha inicio vigencia -->
                <div class="field">
                    <label for="nombre">Fecha inicio</label>
                    <DatePicker fluid dateFormat="dd/mm/yy" v-model="timbrado.vigenciaInicio" showIcon iconDisplay="input" required 
                              :class="{'p-invalid': submitted && !timbrado.vigenciaInicio}" />
                    <small class="p-error" v-if="submitted && !timbrado.vigenciaInicio">Campo requerido</small>
                </div>    
                
                <!-- Fecha fin vigencia -->
                <div class="field">
                    <label for="nombre">Fecha fin</label>
                    <DatePicker fluid dateFormat="dd/mm/yy" v-model="timbrado.vigenciaFin" showIcon iconDisplay="input" required 
                              :class="{'p-invalid': submitted && !timbrado.vigenciaFin}" />
                    <small class="p-error" v-if="submitted && !timbrado.vigenciafin">Campo requerido</small>
                </div>   
                
                <!-- Número inicio -->
                <div class="field">
                    <label for="nombre">Número inicio</label>
                    <InputNumber fluid id="nombre" v-model.trim="timbrado.numeroInicio" required 
                              :class="{'p-invalid': submitted && !timbrado.numeroInicio}" />
                    <small class="p-error" v-if="submitted && !timbrado.numeroInicio">Campo requerido</small>
                </div>  
                
                <!-- Número fin -->
                <div class="field">
                    <label for="nombre">Número fin</label>
                    <InputNumber fluid id="nombre" v-model.trim="timbrado.numeroFin" required 
                              :class="{'p-invalid': submitted && !timbrado.numerofin}" />
                    <small class="p-error" v-if="submitted && !timbrado.numeroFin">Campo requerido</small>
                </div>     
            </div>
            
            <template #footer>
                <Button label="Cancelar" icon="pi pi-times" @click="hideDialog" class="p-button-text"/>
                <Button label="Guardar" icon="pi pi-check" @click="saveTimbrado" autofocus />
            </template>
        </Dialog>

        <!-- Panel principal -->
        <Panel style="position: relative; width: 90%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Timbrados</h3>
                </div>
            </template>
            
            <template #icons>
                <div class="flex align-items-center">
                    <!-- Botón para agregar nuevo timbrado -->
                    <Button icon="pi pi-plus" @click="registrarTimbrado" style="margin-right: 1%;" />
                    <!-- Buscador -->
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
            
            <!-- Tabla de timbrados -->
            <div class="card">
                <DataTable :value="timbrados" scrollHeight="400px"  
                          :paginator="true" :rows="7" :filters="filters"
                          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
                    <!-- Número de timbrado -->
                    <Column field="numeroTimbrado" sortable header="Numero Timbrado" aria-sort="ascending"></Column>
                    
                    <!-- Fecha inicio vigencia -->
                    <Column sortable header="Fecha Inicio Vigencia" aria-sort="ascending">
                        <template #body="slotProps">
                            {{ formatearFecha(slotProps.data.vigenciaInicio) }}
                        </template>
                    </Column>
                    
                    <!-- Fecha fin vigencia -->
                    <Column sortable header="Fecha Fin Vigencia" aria-sort="ascending">
                        <template #body="slotProps">
                            {{ formatearFecha(slotProps.data.vigenciaFin) }}
                        </template>
                    </Column>
                    
                    <!-- Rango de números -->
                    <Column sortable header="Rango" aria-sort="ascending">
                        <template #body="slotProps">
                            {{ slotProps.data.numeroInicio }} - {{ slotProps.data.numeroFin }}
                        </template>
                    </Column>
                    
                    <!-- Cantidad utilizada -->
                    <Column sortable header="Cantidad Utilizada" aria-sort="ascending">
                        <template #body="slotProps">
                            {{ slotProps.data.ultimoEmitido }}
                        </template>
                    </Column>
                    
                    <!-- Estado -->
                    <Column sortable header="Estado" aria-sort="ascending">
                        <template #body="slotProps">
                            <div v-if="slotProps.data.activo">ACTIVO</div>
                            <div v-else>INACTIVO</div>
                        </template>
                    </Column>
                    
                    <!-- Acciones -->
                    <Column :exportable="false" style="min-width:10rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-pencil" text rounded severity="success" 
                                    @click="modificarTimbrado(slotProps.data.id)" 
                                    v-tooltip="'Editar'" 
                                    style="height: 2rem !important; width: 2rem !important;"/>
                            <Button icon="pi pi-times" text rounded severity="danger" 
                                    @click="eliminarTimbrado(slotProps.data.id)" 
                                    v-tooltip="'Dar de baja'"
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