<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Select from 'primevue/select';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { ProveedorServices } from '@/services/ProveedorServices';
import Panel from 'primevue/panel';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import Toast from 'primevue/toast';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { TipoDocServices } from '@/services/TipoDocServices';
import { DepartamentoServices } from '@/services/DepartamentoServices';
import { CiudadServices } from '@/services/CiudadServices';
import Checkbox from 'primevue/checkbox';

//Variables globales
const proveedores = ref();
const confirm = useConfirm();
const toast = useToast();
const proveedorDialog = ref(false);
const proveedor = ref({});
const tiposDoc = ref([]);
const departamentos = ref([]);
const ciudades = ref([]);
const submitted = ref(false);
const tiposProveedor = ref([
  {id: 1, descripcion: 'Nacional'},
  {id: 2, descripcion: 'Extranjero'}
]);

//Función dialogo para eliminar
const confirm2 = (id) => {
    confirm.require({
        message: 'Eliminar este registro?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Eliminar',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: () => {
            eliminarProveedor(id);
        },
    });
};

//
onMounted(() => {
  getProveedores();
    DepartamentoServices.obtenerDepartamentos().then((data) => {
        departamentos.value = data.data;
        console.log(departamentos.value);
    });  
});

//Función de filtros 
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

//Función para registrar un proveedor
const registrarProveedor = () =>{
  proveedor.value = {};
  proveedorDialog.value = true;
}

//Función para eliminar un proveedor
const eliminarProveedor = async (id) => {
    try {
        await ProveedorServices.eliminar(id);
        showSuccess('Proveedor eliminado correctamente');
        getProveedores();
    } catch (error) {
        showError('Error al eliminar el proveedor');
    }
}

//Obtener los proveedores
async function getProveedores() {
  try {
    const { data } = await ProveedorServices.obtenerProveedores();
    proveedores.value = data;
  } catch (error) {
    showError('Error al obtener proveedores');
  }
}

//Muestra mensaje de error
const showError = (message) => {
  toast.add({
    severity: 'error',
    summary: 'Error',
    detail: message,
    life: 3000
  });
};
 
//Muestra mensaje de éxito
const showSuccess = (message) => {
    toast.add({
        severity: 'success',
        summary: 'Éxito',
        detail: message,
        life: 3000
    });
};

//Obtener ciudades por departamento
const obtenerCiudadesByDepartamento = (id) =>{
  CiudadServices.obtenerCiudadesByDepartamento(id).then((data) => {
    ciudades.value = data.data;
    console.log(ciudades.value);
  });
}

//Función para modificar proveedor
const modificarProveedor = (id) => {
  ProveedorServices.getProveedor(id).then((data) => {
    console.log("data direccion");
    proveedor.value = data.data;
    proveedor.value.departamento = data.data.ciudad?.departamento;
      if (proveedor.value.departamento != null) {
        obtenerCiudadesByDepartamento(proveedor.value.departamento?.id);
      }
      proveedorDialog.value = true;        
    });
};

//Obtener id de proveedor
const findIndexById = (id) => {
  let index = -1;
  for (let i = 0; i < proveedores.value.length; i++) {
    console.log("for");
    console.log(id);
    console.log(proveedores.value[i]);
    console.log(proveedores.value[i].id);
      if (proveedores.value[i].id === id) {
        console.log("if");
        index = i;
        break;
      }
  }
  console.log(index);
  return index;
};

//Cierre de dialogos
const hideDialog = () => {
  proveedorDialog.value = false;
  submitted.value = false;
};

//Función para guardar un proveedor
const saveProveedor = () => {
    submitted.value = true;
    if (proveedor?.value.descripcion?.trim()) {
        if (proveedor.value.id) {
            console.log("proveedor.value");
            console.log(proveedor.value);
            ProveedorServices.modificarProveedor(proveedor.value.id, proveedor.value).then((response) => {
                proveedores.value[findIndexById(response.data.id)] = response.data;
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro modificado', life: 3000});
            }).catch(
                (error) => messageError("error")
            );
        } else {
            ProveedorServices.registrarProveedor(proveedor.value).then((response) => {
                console.log("reg");
                proveedores.value.unshift(response.data);
                toast.add({severity:'success', summary: 'Successful', detail: 'Registro creado', life: 3000});
            }).catch(
                (error) => messageError("error")
            );
        }
        submitted.value = false;
        proveedorDialog.value = false;
        proveedor.value = {};
    }
};

</script>

<template>
    <div class="flex p-fluid justify-content-center">
        <ConfirmDialog></ConfirmDialog>
        <Toast />
        <!-- Diálogo de proveedor -->
        <Dialog v-model:visible="proveedorDialog" :closable="false" :style="{width: '450px'}" 
                header="Proveedor" :modal="true" class="p-fluid">
            <div class="formgrid">
                <div class="field">
                    <label for="name">Nombre</label>
                    <InputText fluid id="name" v-model.trim="proveedor.descripcion" required="true" autofocus 
                              :class="{'p-invalid': submitted && !proveedor.descripcion}" />
                    <small class="p-error" v-if="submitted && !proveedor.descripcion">Ingrese un Nombre</small>
                </div>
                
                <div class="field">
                    <label for="description">Tipo Proveedor</label>
                    <Select fluid v-model="proveedor.tipo" :options="tiposProveedor" 
                            optionLabel="descripcion" placeholder="Seleccione un tipo" class="w-full md:w-56" />
                </div>
                
                <div class="field">
                    <label for="description">RUC</label>
                    <InputText fluid id="description" v-model="proveedor.ruc" required="true" />
                </div>
                
                <div class="field">
                    <label for="description">Correo</label>
                    <InputText fluid id="description" v-model="proveedor.correo" required="true" />
                </div>
                
                <div class="field">
                    <label for="description">Telefono</label>
                    <InputText fluid id="description" v-model="proveedor.telefono" required="true" />
                </div>
                
                <div class="field">
                    <label for="description">Departamento</label>
                    <Select fluid v-model="proveedor.departamento" :options="departamentos" 
                            @change="obtenerCiudadesByDepartamento(proveedor.departamento.id)"  
                            optionLabel="descripcion" placeholder="Seleccione un departamento" class="w-full md:w-56" />
                </div>
                
                <div class="field">
                    <label for="description">Ciudad</label>
                    <Select fluid v-model="proveedor.ciudad" :options="ciudades"  
                            optionLabel="descripcion" placeholder="Seleccione una ciudad" class="w-full md:w-56" />
                </div>
                
                <div class="field">
                    <label for="description">Direccion</label>
                    <InputText fluid id="description" v-model="proveedor.direccion" required="true" />
                </div>
                
                <div class="field flex items-center gap-2">
                    <Checkbox fluid v-model="proveedor.esProveedorImportacion" binary />
                    <label for="description">Es proveedor de servicios de importación</label>
                </div>
            </div>
            
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveProveedor" />
            </template>
        </Dialog>
        
        <!-- Panel Principal -->
        <Panel style="position: relative; width: 90%;">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Proveedores</h3>
                </div>
            </template>
            
            <template #icons>
                <div class="flex align-items-center">
                    <Button icon="pi pi-plus" @click="registrarProveedor" style="margin-right: 1%;" />
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
            
            <div>
                <DataTable :value="proveedores" scrollHeight="400px"  
                          :paginator="true" :rows="7" :filters="filters"
                          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros">
                    <Column field="id" sortable header="N°" aria-sort="ascending"></Column>
                    <Column field="descripcion" header="Proveedor" aria-sort="ascending" sortable></Column>
                    <Column field="tipo.descripcion" header="Tipo" aria-sort="ascending" sortable></Column>
                    <Column field="ruc" header="Ruc" aria-sort="ascending" sortable></Column>
                    
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-pencil" v-tooltip="'Editar'" severity="success" text rounded 
                                    aria-label="Search" @click="modificarProveedor(slotProps.data.id)" 
                                    style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-trash" v-tooltip="'Eliminar'" severity="danger" text rounded 
                                    aria-label="Cancel" @click="confirm2(slotProps.data.id)"  
                                    style="height: 2rem !important; width: 2rem !important;" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
</template>