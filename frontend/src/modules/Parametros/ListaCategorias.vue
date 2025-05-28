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
  
  // Estados
  const categoriaDialog = ref(false);
  const verclienteDialog = ref(false);
  const selectedOp = ref('Casi');
  const categorias = ref([]);
  const categoria = ref({});
  const direccion = ref({});
  const submitted = ref(false);
  const departamentos = ref([]);
  const opciones = ref(['Casi','Entre']);
  const ciudades = ref([]);
  const viewClienteDialog = ref(false);
  const categoriaView = ref(false);
  const direccionView = ref({});
  const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
  });
  
  // Opciones para dropdowns
  const documentos = ref([
    { id: 1, descripcion: 'CI' },
    { id: 2, descripcion: 'RUC' },
    { id: 3, descripcion: 'Pasaporte' }
  ]);
  
  const opcionesCalle = ref([
    { label: 'y', value: 'y' },
    { label: 'Entre', value: 'Entre' }
  ]);
  
  
  // Obtener datos iniciales
  onMounted(() => {
    getCategorias();
    
  });
  
  // Funciones para obtener datos
  async function getCategorias() {
    try {
      const { data } = await CategoriaServices.obtenerCategorias();
      categorias.value = data;
      console.log(categorias.value);
    } catch (error) {
      showError('Error al obtener categorias');
    }
  }
  
  async function getDepartamentos() {
    try {
      const { data } = await DepartamentoServices.obtenerDepartamentos();
      departamentos.value = data;
    } catch (error) {
      showError('Error al obtener departamentos');
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
  const registrarCategoria = () => {    
    categoria.value = {};
    categoriaDialog.value = true;
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

  async function modificarCategoria(id) {
    try {
      const { data } = await CategoriaServices.getCategoria(id);
      categoria.value = data;
      // Si hay ciudad, cargamos el departamento asociado      
      categoriaDialog.value = true;
    } catch (error) {
      showError('Error al obtener la categoria');
    }
  };
  
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
  

  async function verCategoria(id) {
    try {
      const { data } = await CategoriaServices.getCategoria(id);
      categoriaView.value = data;
      console.log(categoriaView.value);
      
      viewClienteDialog.value = true;
    } catch (error) {
      showError('Error al obtener la categoria');
    }
  };
  
  const saveCategoria = async () => {
    submitted.value = true;
    
    // Validaciones básicas
    if (!categoria.value.descripcion?.trim()) {
      showError('Complete todos los campos requeridos');
      return;
    }
  
    try {
      console.log(categoria.value);
  
      if (categoria.value.id) {
        // Actualizar cliente existente
        const { data } = await CategoriaServices.modificarCategoria(categoria.value.id, categoria.value);
        showSuccess('Categoria actualizado correctamente');
      } else {
        // Crear nuevo cliente
        const { data } = await CategoriaServices.registrarCategoria(categoria.value);
        showSuccess('Categoria creado correctamente');
      }
  
      categoriaDialog.value = false;
      getCategorias();
    } catch (error) {
      showError('Error al guardar la categoria');
    }
  };
  
  const hideDialog = () => {
    categoriaDialog.value = false;
    submitted.value = false;
  };
  </script>
<template>
    <div class=" flex p-fluid justify-content-center " >
      <ConfirmDialog></ConfirmDialog>
      <!-- Diálogo para agregar/editar cliente -->
      <Dialog v-model:visible="categoriaDialog" :style="{width: '450px'}" header="Categoria" :modal="true" class="p-fluid">
        <!-- Formulario del cliente -->
        <div class="formgrid">
          <!-- Sección de datos personales -->
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
      <!-- Diálogo para visualizar cliente -->
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
            <Button icon="pi pi-plus" @click="registrarCategoria" style="margin-right: 1%;" />
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

          <DataTable :value="categorias" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
            <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
            <Column field="descripcion" sortable header="Descripcion" aria-sort="ascending"></Column>
            <Column :exportable="false" style="min-width:10rem">
              <template #body="slotProps">
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