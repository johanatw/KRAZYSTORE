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
  
  // Estados
  const puntoRetiroDialog = ref(false);
  const puntosRetiro = ref([]);
  const puntoRetiro = ref({});
  const submitted = ref(false);
  const viewClienteDialog = ref(false);
  const puntoRetiroView = ref(false);
  const direccionView = ref({});
  const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
  });
    
  // Obtener datos iniciales
  onMounted(() => {
    getPuntosRetiro();
    
  });
  
  // Funciones para obtener datos
  async function getPuntosRetiro() {
    try {
      const { data } = await PuntoRetiroServices.obtenerPuntosRetiro();
      puntosRetiro.value = data;
      console.log(puntosRetiro.value);
    } catch (error) {
      showError('Error al obtener registros');
    }
  }
  
  // Funciones de utilidad
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
  
  
  // Funciones para CRUD
  const registrarPuntoRetiro = () => {    
    puntoRetiro.value = {};
    puntoRetiroDialog.value = true;
  };
  /*
  const modificarCliente = (cli) => {
    cliente.value = { ...cli };
    direccion.value = cli.direccion ? { ...cli.direccion } : {};
    
    if (direccion.value.ciudad) {
      direccion.value.departamento = direccion.value.ciudad.departamento;
      getCiudades(direccion.value.departamento.id);
    }
    
    clienteDialog.value = true;
  };*/

  async function modificarPuntoRetiro(id) {
    try {
      const { data } = await PuntoRetiroServices.getPuntoRetiro(id);
      puntoRetiro.value = data;
      // Si hay ciudad, cargamos el departamento asociado      
      puntoRetiroDialog.value = true;
    } catch (error) {
      showError('Error al obtener el registro');
    }
  };
  
  const eliminarPuntoRetiro = (id) => {
    confirm.require({
      message: '¿Está seguro de eliminar este registro?',
      header: 'Confirmación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
    
      accept: async () => {
        try {
          await PuntoRetiroServices.eliminar(id);
          showSuccess('Registro eliminado correctamente');
          getPuntosRetiro();
        } catch (error) {
          showError('Error al eliminar');
        }
      }
    });
  };
  
  
  const savePuntoRetiro = async () => {
    submitted.value = true;
    
    // Validaciones básicas
    if (!puntoRetiro.value.descripcion?.trim()) {
      showError('Complete todos los campos requeridos');
      return;
    }
  
    try {
      console.log(puntoRetiro.value);
  
      if (puntoRetiro.value.id) {
        // Actualizar cliente existente
        const { data } = await PuntoRetiroServices.modificarPuntoRetiro(puntoRetiro.value.id, puntoRetiro.value);
        showSuccess('Registro actualizado correctamente');
      } else {
        // Crear nuevo cliente
        const { data } = await PuntoRetiroServices.registrarPuntoRetiro(puntoRetiro.value);
        showSuccess('Registro creado correctamente');
      }
  
      puntoRetiroDialog.value = false;
      getPuntosRetiro();
    } catch (error) {
      showError('Error al guardar');
    }
  };
  
  const hideDialog = () => {
    puntoRetiroDialog.value = false;
    submitted.value = false;
  };
  </script>
<template>
    <div class=" flex p-fluid justify-content-center " >
      <ConfirmDialog></ConfirmDialog>
      <!-- Diálogo para agregar/editar cliente -->
      <Dialog v-model:visible="puntoRetiroDialog" :style="{width: '450px'}" header="Punto de Retiro" :modal="true" class="p-fluid">
        <!-- Formulario del cliente -->
        <div class="formgrid">
          <!-- Sección de datos personales -->
          <div class="field">
            <label for="nombre">Descripcion</label>
            <InputText fluid id="nombre" v-model.trim="puntoRetiro.descripcion" required 
                      :class="{'p-invalid': submitted && !puntoRetiro.descripcion}" />
            <small class="p-error" v-if="submitted && !puntoRetiro.descripcion">Descripcion es requerido</small>
          </div>          
        </div>
  
        <template #footer>
          <Button label="Cancelar" icon="pi pi-times" @click="hideDialog" class="p-button-text"/>
          <Button label="Guardar" icon="pi pi-check" @click="savePuntoRetiro" autofocus />
        </template>
      </Dialog>

  
      <!-- Panel principal -->
      <Panel style="position: relative; width: 90%;">
        <template #header>
          <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Puntos de Retiro</h3>
          </div>
        </template>
           
        <template #icons>
          <div class="flex align-items-center">
            <Button icon="pi pi-plus" @click="registrarPuntoRetiro" style="margin-right: 1%;" />
            <InputGroup>
              <InputText v-model="filters['global'].value" placeholder="Buscar..." />
              <InputGroupAddon>
                <i class="pi pi-search" />
              </InputGroupAddon>
            </InputGroup>
          </div>
        </template>
        
        <!-- Tabla de clientes -->
        <div class="card">

          <DataTable :value="puntosRetiro" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
            <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
            <Column field="descripcion" sortable header="Descripcion" aria-sort="ascending"></Column>
            <Column :exportable="false" style="min-width:10rem">
              <template #body="slotProps">
                <Button icon="pi pi-pencil" text rounded severity="success" 
                        @click="modificarPuntoRetiro(slotProps.data.id)" 
                        v-tooltip="'Editar'" 
                        style="height: 2rem !important; width: 2rem !important;"/>
                <Button icon="pi pi-trash" text rounded severity="danger" 
                        @click="eliminarPuntoRetiro(slotProps.data.id)" 
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