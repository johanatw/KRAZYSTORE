<script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import DataTable from 'primevue/datatable';
  import Column from 'primevue/column';
  import Panel from 'primevue/panel';
  import InputText from 'primevue/inputtext';
  import Dropdown from 'primevue/dropdown';
  import { PersonaServices } from '@/services/PersonaServices';
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
  const clienteDialog = ref(false);
  const verclienteDialog = ref(false);
  const selectedOp = ref('Casi');
  const clientes = ref([]);
  const cliente = ref({});
  const direccion = ref({});
  const submitted = ref(false);
  const departamentos = ref([]);
  const opciones = ref(['Casi','Entre']);
  const ciudades = ref([]);
  const viewClienteDialog = ref(false);
  const clienteView = ref(false);
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
    getClientes();
    getDepartamentos();
  });
  
  // Funciones para obtener datos
  async function getClientes() {
    try {
      const { data } = await PersonaServices.obtenerClientes();
      clientes.value = data;
      console.log(clientes.value);
    } catch (error) {
      showError('Error al obtener clientes');
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
  
  async function getCiudades(departamentoId) {
    try {
      const { data } = await CiudadServices.obtenerCiudadesByDepartamento(departamentoId);
      ciudades.value = data;
    } catch (error) {
      showError('Error al obtener ciudades');
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
  
  const algunCampoTieneValor = (obj) => {
    return Object.values(obj).some(val => val !== null && val !== undefined && val !== '');
  };
  
  const generarDireccionCompleta = (dir) => {
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " +selectedOp.value + " "+ dir.calle2;
    }
    if (dir.calle3?.trim()) {
        d = d + " y " + dir.calle3;
    }

    return d;
};
  
  // Funciones para CRUD
  const registrarCliente = () => {    
    cliente.value = {};
    direccion.value = {};
    clienteDialog.value = true;
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

  async function modificarCliente(id) {
    try {
      const { data } = await PersonaServices.getPersona(id);
      cliente.value = data.persona;
      direccion.value = data.direccion ? { ...data.direccion } : {};
      // Si hay ciudad, cargamos el departamento asociado
      if (direccion.value.ciudad) {
        direccion.value.departamento = direccion.value.ciudad.departamento;
        getCiudades(direccion.value.departamento.id);
      }
      
      clienteDialog.value = true;
    } catch (error) {
      showError('Error al obtener el cliente');
    }
  };
  
  const eliminarCliente = (id) => {
    confirm.require({
      message: '¿Está seguro de eliminar este cliente?',
      header: 'Confirmación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
    
      accept: async () => {
        try {
          await PersonaServices.eliminar(id);
          showSuccess('Cliente eliminado correctamente');
          getClientes();
        } catch (error) {
          showError('Error al eliminar cliente');
        }
      }
    });
  };
  

  async function verCliente(id) {
    try {
      const { data } = await PersonaServices.getPersona(id);
      clienteView.value = data.persona;
      console.log(clienteView.value);
      direccionView.value = data.direccion ? { ...data.direccion } : {};
      // Si hay ciudad, cargamos el departamento asociado
      if (direccionView.value.ciudad) {
        direccionView.value.departamento = direccionView.value.ciudad.departamento;
      }
      
      viewClienteDialog.value = true;
    } catch (error) {
      showError('Error al obtener el cliente');
    }
  };
  
  const saveCliente = async () => {
    submitted.value = true;
    
    // Validaciones básicas
    if (!cliente.value.nombre?.trim() || !cliente.value.apellido?.trim() || 
        !cliente.value.tipoDoc || !cliente.value.nroDoc) {
      showError('Complete todos los campos requeridos');
      return;
    }
  
    // Validar dirección si hay algún campo completado
    if (algunCampoTieneValor(direccion.value)) {
      if (!direccion.value.calle1 || !direccion.value.ciudad) {
        showError('Complete los campos requeridos de la dirección');
        return;
      }
    }
  
    try {
      direccion.value.tipo = 'P';
      console.log(cliente.value);
      console.log(direccion.value);
      const direccionCompleta = generarDireccionCompleta(direccion.value);
      direccion.value.direccion = direccionCompleta;
      const payload = {
        personaEntity: cliente.value,
        direccion: direccion.value
      };
  
      if (cliente.value.id) {
        // Actualizar cliente existente
        const { data } = await PersonaServices.modificarPersona(cliente.value.id, payload);
        showSuccess('Cliente actualizado correctamente');
      } else {
        // Crear nuevo cliente
        const { data } = await PersonaServices.registrarPersona(payload);
        showSuccess('Cliente creado correctamente');
      }
  
      clienteDialog.value = false;
      getClientes();
    } catch (error) {
      showError('Error al guardar cliente');
    }
  };
  
  const hideDialog = () => {
    clienteDialog.value = false;
    submitted.value = false;
  };
  </script>
<template>
    <div class=" flex p-fluid justify-content-center " >
      <ConfirmDialog></ConfirmDialog>


        <!-- Diálogo para agregar/editar cliente -->
      <Dialog v-model:visible="verClienteDialog" :style="{width: '450px'}" header="Cliente" :modal="true" class="p-fluid">
        <!-- Formulario del cliente -->
        <div class="formgridgrid">
          <!-- Sección de datos personales -->
          <div class="field">
            <label for="nombre">Nombre</label>
            <InputText id="nombre" v-model.trim="cliente.nombre" required 
                      :class="{'p-invalid': submitted && !cliente.nombre}" />
            <small class="p-error" v-if="submitted && !cliente.nombre">Nombre es requerido</small>
          </div>
          
          <div class="field">
            <label for="apellido">Apellido</label>
            <InputText id="apellido" v-model.trim="cliente.apellido" required 
                      :class="{'p-invalid': submitted && !cliente.apellido}" />
            <small class="p-error" v-if="submitted && !cliente.apellido">Apellido es requerido</small>
          </div>
          
          <div class="field">
            <label for="tipoDoc">Tipo Documento</label>
            <Dropdown id="tipoDoc" v-model="cliente.tipoDoc" :options="documentos" optionLabel="descripcion" 
                     placeholder="Seleccione tipo" :class="{'p-invalid': submitted && !cliente.tipoDoc}" />
            <small class="p-error" v-if="submitted && !cliente.tipoDoc">Tipo documento es requerido</small>
          </div>
          
          <div class="field">
            <label for="nroDoc">Nro. Documento</label>
            <InputText id="nroDoc" v-model.trim="cliente.nroDoc" required 
                      :class="{'p-invalid': submitted && !cliente.nroDoc}" />
            <small class="p-error" v-if="submitted && !cliente.nroDoc">Nro. documento es requerido</small>
          </div>
          
          <div class="field">
            <label for="telefono">Teléfono</label>
            <InputText id="telefono" v-model.trim="cliente.telefono" />
          </div>
          
          <!-- Sección de dirección -->
              <div class="field">
            <label for="description">Calle Principal</label>
            <InputText fluid id="description" v-model="direccion.calle1" required="true" :class="{'p-invalid': submitted && !validarDireccionCliente(direccion) && !direccion.calle1}" />
            <small class="p-error" v-if="submitted && !validarDireccionCliente(direccion) && !direccion.calle1">Ingrese Calle Principal</small>
        </div>
        
        <div class="field">
            <label for="description">Calle 2</label>
            <InputGroup fluid>
                <Dropdown v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
                <InputText id="description" v-model="direccion.calle2" required="true"  />
            </InputGroup>
        </div>
        <div class="field" v-if="selectedOp=='Entre'">
            <label for="description">Calle 3</label>
            <InputText fluid id="description" v-model="direccion.calle3" required="true"  />
        </div>
          <div class="field">
            <label for="nroCasa">N° Casa</label>
            <InputText id="nroCasa" v-model.trim="direccion.nroCasa" />
          </div>
          
          <div class="field">
            <label for="departamento">Departamento</label>
            <Dropdown id="departamento" v-model="direccion.departamento" :options="departamentos" 
                     optionLabel="descripcion" placeholder="Seleccione departamento"
                     @change="getCiudades(direccion.departamento.id)"
                     :class="{'p-invalid': submitted && algunCampoTieneValor(direccion) && !direccion.departamento}" />
            <small class="p-error" v-if="submitted && algunCampoTieneValor(direccion) && !direccion.departamento">Departamento es requerido</small>
          </div>
          
          <div class="field">
            <label for="ciudad">Ciudad</label>
            <Dropdown id="ciudad" v-model="direccion.ciudad" :options="ciudades" 
                     optionLabel="descripcion" placeholder="Seleccione ciudad"
                     :class="{'p-invalid': submitted && algunCampoTieneValor(direccion) && !direccion.ciudad}" />
            <small class="p-error" v-if="submitted && algunCampoTieneValor(direccion) && !direccion.ciudad">Ciudad es requerida</small>
          </div>
        </div>
  
        <template #footer>
          <Button label="Cancelar" icon="pi pi-times" @click="hideDialog" class="p-button-text"/>
        </template>
      </Dialog>
      <!-- Diálogo para agregar/editar cliente -->
      <Dialog v-model:visible="clienteDialog" :style="{width: '450px'}" header="Datos del Cliente" :modal="true" class="p-fluid">
        <!-- Formulario del cliente -->
        <div class="formgrid">
          <!-- Sección de datos personales -->
          <div class="field">
            <label for="nombre">Nombre</label>
            <InputText fluid id="nombre" v-model.trim="cliente.nombre" required 
                      :class="{'p-invalid': submitted && !cliente.nombre}" />
            <small class="p-error" v-if="submitted && !cliente.nombre">Nombre es requerido</small>
          </div>
          
          <div class="field">
            <label for="apellido">Apellido</label>
            <InputText fluid id="apellido" v-model.trim="cliente.apellido" required 
                      :class="{'p-invalid': submitted && !cliente.apellido}" />
            <small class="p-error" v-if="submitted && !cliente.apellido">Apellido es requerido</small>
          </div>
          
          <div class="field">
            <label for="tipoDoc">Tipo Documento</label>
            <Dropdown fluid id="tipoDoc" v-model="cliente.tipoDoc" :options="documentos" optionLabel="descripcion" 
                     placeholder="Seleccione tipo" :class="{'p-invalid': submitted && !cliente.tipoDoc}" />
            <small class="p-error" v-if="submitted && !cliente.tipoDoc">Tipo documento es requerido</small>
          </div>
          
          <div class="field">
            <label for="nroDoc">Nro. Documento</label>
            <InputText fluid id="nroDoc" v-model.trim="cliente.nroDoc" required 
                      :class="{'p-invalid': submitted && !cliente.nroDoc}" />
            <small class="p-error" v-if="submitted && !cliente.nroDoc">Nro. documento es requerido </small>
          </div>
          
          <div class="field">
            <label for="telefono">Teléfono</label>
            <InputText fluid id="telefono" v-model.trim="cliente.telefono" required="true" />
          </div>
          
          <!-- Sección de dirección -->
          <div class="field">
            <label for="calle1">Calle Principal</label>
            <InputText fluid id="calle1" v-model="direccion.calle1" 
                      :class="{'p-invalid': submitted && algunCampoTieneValor(direccion) && !direccion.calle1}" />
            <small class="p-error" v-if="submitted && algunCampoTieneValor(direccion) && !direccion.calle1">Calle principal es requerida</small>
          </div>
          
          <div class="field">
            <label for="calle2">Calle Secundaria</label>
            <InputGroup fluid>
            <Dropdown v-model="selectedOp" :options="opciones"  placeholder="Select a City" style="width: 0.1rem !important;" />
                <InputText id="calle2" v-model="direccion.calle2" required="true"  />
            </InputGroup>
        </div>
          
          <div class="field" v-if="selectedOp=='Entre'">
            <label for="calle3">Calle Transversal</label>
            <InputText fluid id="calle3" v-model="direccion.calle3" required="true" />
          </div>
          
          <div class="field">
            <label for="nroCasa">N° Casa</label>
            <InputText fluid id="nroCasa" v-model="direccion.nroCasa" required="true" />
          </div>
          
          <div class="field">
            <label for="departamento">Departamento</label>
            <Dropdown fluid id="departamento" v-model="direccion.departamento" :options="departamentos" 
                     optionLabel="descripcion" placeholder="Seleccione departamento"
                     @change="getCiudades(direccion.departamento.id)"
                     :class="{'p-invalid': submitted && algunCampoTieneValor(direccion) && !direccion.departamento}" />
            <small class="p-error" v-if="submitted && algunCampoTieneValor(direccion) && !direccion.departamento">Departamento es requerido</small>
          </div>
          
          <div class="field">
            <label for="ciudad">Ciudad</label>
            <Dropdown fluid id="ciudad" v-model="direccion.ciudad" :options="ciudades" 
                     optionLabel="descripcion" placeholder="Seleccione ciudad"
                     :class="{'p-invalid': submitted && algunCampoTieneValor(direccion) && !direccion.ciudad}" />
            <small class="p-error" v-if="submitted && algunCampoTieneValor(direccion) && !direccion.ciudad">Ciudad es requerida</small>
          </div>
        </div>
  
        <template #footer>
          <Button label="Cancelar" icon="pi pi-times" @click="hideDialog" class="p-button-text"/>
          <Button label="Guardar" icon="pi pi-check" @click="saveCliente" autofocus />
        </template>
      </Dialog>
      <!-- Diálogo para visualizar cliente -->
<Dialog v-model:visible="viewClienteDialog" :style="{width: '500px'}" header="Detalles del Cliente" :modal="true">
  <div class="p-fluid">
    <div class="field grid">
      <label class="col-4 font-bold">Nombre:</label>
      <div class="col-8">{{ clienteView.nombre }}</div>
    </div>
    
    <div class="field grid">
      <label class="col-4 font-bold">Apellido:</label>
      <div class="col-8">{{clienteView.apellido }}</div>
    </div>
    
    <div class="field grid">
      <label class="col-4 font-bold">Documento:</label>
      <div class="col-8">
        {{ clienteView.nroDoc }} ({{ clienteView.tipoDoc?.descripcion || 'Sin especificar' }})
      </div>
    </div>
    
    <div class="field grid">
      <label class="col-4 font-bold">Teléfono:</label>
      <div class="col-8">{{ clienteView.telefono || 'No registrado' }}</div>
    </div>
    
    <Divider />
    
    <div class="field grid">
      <label class="col-4 font-bold">Dirección:</label>
      <div class="col-8">{{ direccionView.direccion || 'Sin dirección registrada' }}</div>
    </div>
    
    <div v-if="direccionView.departamento" class="field grid">
      <label class="col-4 font-bold">Departamento:</label>
      <div class="col-8">{{ direccionView.departamento.descripcion }}</div>
    </div>
    
    <div v-if="direccionView.ciudad" class="field grid">
      <label class="col-4 font-bold">Ciudad:</label>
      <div class="col-8">{{ direccionView.ciudad.descripcion }}</div>
    </div>
  </div>

  <template #footer>
    <Button label="Cerrar" icon="pi pi-times" @click="viewClienteDialog = false" class="p-button-text"/>
  </template>
</Dialog>
  
      <!-- Panel principal -->
      <Panel style="position: relative; width: 100%;">
        <template #header>
          <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Clientes</h3>
          </div>
        </template>
           
        <template #icons>
          <div class="flex align-items-center">
            <Button icon="pi pi-plus" @click="registrarCliente" style="margin-right: 1%;" />
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

          <DataTable :value="clientes" scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
            <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
            <Column field="nombre" sortable header="Nombre y Apellido" aria-sort="ascending">
              <template #body="slotProps">
                {{ slotProps.data.nombre }} {{ slotProps.data.apellido }}
              </template>
            </Column>
            <Column field="nroDoc" header="Documento" aria-sort="ascending" sortable>
              <template #body="slotProps">
                <div v-if="slotProps.data.nroDoc">
                  {{ slotProps.data.nroDoc }} ({{ slotProps.data.tipoDoc?.descripcion || '' }})
                </div>
              </template>
            </Column>
            <Column field="telefono" header="Teléfono" aria-sort="ascending" sortable></Column>
            <Column :exportable="false" style="min-width:10rem">
              <template #body="slotProps">
                <Button icon="pi pi-eye" text rounded  
                        @click="verCliente(slotProps.data.id)" 
                        v-tooltip="'Ver detalles'"
                        class="p-button-sm mr-2" />
                <Button icon="pi pi-pencil" text rounded severity="success" 
                        @click="modificarCliente(slotProps.data.id)" 
                        v-tooltip="'Editar'"
                        class="p-button-sm mr-2" />
                <Button icon="pi pi-trash" text rounded severity="danger" 
                        @click="eliminarCliente(slotProps.data.id)" 
                        v-tooltip="'Eliminar'"
                        class="p-button-sm" />
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