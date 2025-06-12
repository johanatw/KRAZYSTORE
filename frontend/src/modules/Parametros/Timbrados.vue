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
  
  // Estados
  const timbradoDialog = ref(false);
  const timbrados = ref([]);
  const timbrado = ref({});
  const submitted = ref(false);
  const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
  });
    
  // Obtener datos iniciales
  onMounted(() => {
    getTimbrados();
    
  });
  
  // Funciones para obtener datos
  async function getTimbrados() {
    try {
      const { data } = await TimbradoServices.obtenerTimbrados();
      timbrados.value = data;
      console.log(timbrados.value);
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
  const registrarTimbrado = () => {    
    timbrado.value = {};
    timbradoDialog.value = true;
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

  async function modificarTimbrado(id) {
    try {
      const { data } = await TimbradoServices.getTimbrado(id);
      timbrado.value = data;
      // Si hay ciudad, cargamos el departamento asociado      
      timbradoDialog.value = true;
    } catch (error) {
      showError('Error al obtener el registro');
    }
  };
  
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
  
  
  const saveTimbrado = async () => {
    submitted.value = true;
    
    // Validaciones básicas
    if (!timbrado.value.numeroTimbrado) {
      showError('Complete todos los campos requeridos');
      return;
    }
  
    try {
      console.log(timbrado.value);
        
      if (timbrado.value.id) {
        // Actualizar cliente existente
        const { data } = await TimbradoServices.modificarTimbrado(timbrado.value.id, timbrado.value);
        showSuccess('Registro actualizado correctamente');
      } else {
        // Crear nuevo cliente
        timbrado.value.fecha_creacion = new Date();
        const { data } = await TimbradoServices.registrarTimbrado(timbrado.value);
        showSuccess('Registro creado correctamente');
      }
  
      timbradoDialog.value = false;
      getTimbrados();
    } catch (error) {
      showError('Error al guardar');
    }
  };
  
  const hideDialog = () => {
    timbradoDialog.value = false;
    submitted.value = false;
  };
  </script>
<template>
    <div class=" flex p-fluid justify-content-center " >
      <ConfirmDialog></ConfirmDialog>
      <!-- Diálogo para agregar/editar cliente -->
      <Dialog v-model:visible="timbradoDialog" :style="{width: '450px'}" header="Timbrado" :modal="true" class="p-fluid">
        <!-- Formulario del cliente -->
        <div class="formgrid">
          <!-- Sección de datos personales -->
          <div class="field">
            <label for="nombre">Número</label>
            <InputText fluid id="nombre" v-model.trim="timbrado.numeroTimbrado" required 
                      :class="{'p-invalid': submitted && !timbrado.numeroTimbrado}" />
            <small class="p-error" v-if="submitted && !timbrado.numeroTimbrado">Campo requerido</small>
          </div>
          <div class="field">
            <label for="nombre">Cod. de establecimiento</label>
            <InputText fluid id="nombre" v-model.trim="timbrado.codEstablecimiento" required 
                      :class="{'p-invalid': submitted && !timbrado.codEstablecimiento}" />
            <small class="p-error" v-if="submitted && !timbrado.codEstablecimiento">Campo requerido</small>
          </div>
          <div class="field">
            <label for="nombre">Punto de expedicion</label>
            <InputText fluid id="nombre" v-model.trim="timbrado.puntoExpedicion" required 
                      :class="{'p-invalid': submitted && !timbrado.puntoExpedicion}" />
            <small class="p-error" v-if="submitted && !timbrado.puntoExpedicion">Campo requerido</small>
          </div>  
          <div class="field">
            <label for="nombre">Fecha inicio</label>
            <DatePicker fluid dateFormat="dd/mm/yy" v-model="timbrado.vigenciaInicio" showIcon iconDisplay="input" required 
                      :class="{'p-invalid': submitted && !timbrado.vigenciaInicio}" />
            <small class="p-error" v-if="submitted && !timbrado.vigenciaInicio">Campo requerido</small>
          </div>    
          <div class="field">
            <label for="nombre">Fecha fin</label>
            <DatePicker fluid dateFormat="dd/mm/yy" v-model="timbrado.vigenciaFin" showIcon iconDisplay="input" required 
                      :class="{'p-invalid': submitted && !timbrado.vigenciaFin}" />
            <small class="p-error" v-if="submitted && !timbrado.vigenciafin">Campo requerido</small>
          </div>   
          <div class="field">
            <label for="nombre">Número inicio</label>
            <InputNumber fluid id="nombre" v-model.trim="timbrado.numeroInicio" required 
                      :class="{'p-invalid': submitted && !timbrado.numeroInicio}" />
            <small class="p-error" v-if="submitted && !timbrado.numeroInicio">Campo requerido</small>
          </div>  
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
            <h3 class="font-bold">timbrados</h3>
          </div>
        </template>
           
        <template #icons>
          <div class="flex align-items-center">
            <Button icon="pi pi-plus" @click="registrarTimbrado" style="margin-right: 1%;" />
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

          <DataTable :value="timbrados" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
            <Column field="numeroTimbrado" sortable header="Numero Timbrado" aria-sort="ascending"></Column>
            <Column field="descripcion" sortable header="Fecha Inicio Vigencia" aria-sort="ascending">
                <template #body="slotProps">
                {{ formatearFecha(slotProps.data.vigenciaInicio)  }}
              </template>
            </Column>
            <Column field="descripcion" sortable header="Fecha Fin Vigencia" aria-sort="ascending">
                <template #body="slotProps">
                {{ formatearFecha(slotProps.data.vigenciaFin)  }}
              </template>
            </Column>
            <Column field="descripcion" sortable header="Rango" aria-sort="ascending">
                <template #body="slotProps">
                {{ slotProps.data.numeroInicio  }} - {{ slotProps.data.numeroFin }}
              </template>
            </Column>
            <Column field="descripcion" sortable header="Cantidad Utilizada" aria-sort="ascending">
                <template #body="slotProps">
                {{ slotProps.data.ultimoEmitido  }}
              </template>
            </Column>
            <Column field="descripcion" sortable header="Estado" aria-sort="ascending">
                <template #body="slotProps">
                    <div v-if="slotProps.data.activo" >
                        ACTIVO
                    </div>
                    <div v-else >
                        INACTIVO
                    </div>
               
              </template>
            </Column>
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