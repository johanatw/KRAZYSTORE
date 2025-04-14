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

const productos = ref([]);
const categorias = ref([]);
const subCategorias = ref([]);
const tiposIva = ref([]);
const confirm = useConfirm();
const toast = useToast();
const productoDialog = ref(false);
const visualizarProductoDialog = ref(false);
const producto = ref({});
const submitted = ref(false);

const confirm2 = (id,index) => {
   
    confirm.require({
        message: 'Eliminar este producto ?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Eliminar',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: async () => {
        try {
          await ProductoServices.eliminar(id);
          productos.value.splice(index,1);
          showSuccess('Producto eliminado correctamente');
          
        } catch (error) {
          showError('Error al eliminar el producto');
        }
      }
        
    });
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

onMounted(() => {
    ProductoServices.obtenerProductos().then((data) => {
        productos.value = data.data;
        console.log(productos.value);
    });
 
    CategoriaServices.obtenerCategorias().then((data) => {
        categorias.value = data.data;
        
    });

    TipoIvaServices.obtenerTiposIva().then((data) => {
        tiposIva.value = data.data;
       
    });
    
});


const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const registrarProducto = () =>{
    
    producto.value = {};
    productoDialog.value = true;
}

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

const obtenerSubCategoriasByCategoria = (id) => {
    CategoriaServices.obtenerSubCatByIdCat(id).then((data) => {
        console.log(data.data);
       subCategorias.value= data.data;
             
       
    });
    
};

const modificarProducto = (id) => {
    ProductoServices.obtenerProducto(id).then((data) => {
        console.log(data.data);
       
       producto.value = data.data;
       producto.value.categoria = data.data.subCategoria.categoria;
       obtenerSubCategoriasByCategoria(producto.value.subCategoria.categoria.id);
       productoDialog.value = true;       
       
    });
    
};

const verProducto = (id) => {
    ProductoServices.obtenerProducto(id).then((data) => {
        console.log(data.data);
       
       producto.value = data.data;
       visualizarProductoDialog.value = true;       
       
    });
    
};

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

const hideDialog = () => {
    visualizarProductoDialog.value = false;
    productoDialog.value = false;
    submitted.value = false;
};

const verHistorialPrecios = (id) => {
    router.push({name: 'precios_venta', params: {id}});
};

const saveProducto = () => {
    submitted.value = true;

    if (producto?.value.nombre?.trim() && producto.value.subCategoria && producto.value.tipoIva && (!producto.value.bajoDemanda || (producto.value.bajoDemanda && producto.value.cantLimBajoDemanda))) {
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
    
  <div class="flex p-fluid justify-content-center " >

    <ConfirmDialog ></ConfirmDialog>
    <Toast />
    <!--Dialog Registrar Producto-->
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
            <label for="description">Precio venta</label>
            <InputNumber fluid id="description" v-model="producto.precio" required="true" :invalid="submitted && !producto.precio"  />
            <small class="p-error" v-if="submitted && !producto.precio">Ingrese el precio</small>
        </div>
        <div class="field flex items-center gap-2">
            <Checkbox fluid v-model="producto.bajoDemanda" binary />
            <label for="description">Disponible para pedido bajo demanda</label>
        </div>
        <div v-if="producto.bajoDemanda" class="field">
            <label for="description">Límite pedido bajo demanda</label>
            <InputNumber fluid id="description" v-model="producto.cantLimBajoDemanda" :invalid="submitted && producto.bajoDemanda && !producto.cantLimBajoDemanda" />
            <small class="p-error" v-if="submitted && producto.bajoDemanda && !producto.cantLimBajoDemanda">Ingrese la cantidad máxima para pedido bajo demanda</small>
        </div>
    </div>
        <template #footer>
            <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
            <Button label="Save" icon="pi pi-check" text @click="saveProducto" />
        </template>
    </Dialog>
    <!--Dialog Visualizar Producto-->
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
        <div class="field flex items-center gap-2">
            <label for="description">Disponible para pedido bajo demanda: </label>
            <label v-if="producto.bajoDemanda" for="bajoDemanda">si</label>
            <label v-else for="bajoDemanda">no</label>
        </div>
        <div v-if="producto.bajoDemanda" class="field">
            <label for="description">Límite pedido bajo demanda:</label>
            {{ producto.cantLimBajoDemanda }}
        </div>
    </div>
        <template #footer>
            <Button label="Cerrar" icon="pi pi-times" text @click="hideDialog"/>
        </template>
    </Dialog>
    <Panel style=" position: relative; width: 100%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Productos</h3>
        </div>
      </template>
    

      <template #icons>
        <div class="flex align-items-center">
          <Button  icon="pi pi-plus " @click="registrarProducto" style="margin-right: 1% ;"  />
          <InputGroup>
            <InputText v-model="filters['global'].value" placeholder="Buscar..." />
            <InputGroupAddon>
              <i class="pi pi-search" />
            </InputGroupAddon>
        </InputGroup>
        </div>
    
      </template>
      
  
      <div >
        
        <DataTable  :value="productos" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <Column field="nombre"  header="Producto" aria-sort="ascending" sortable>           
        </Column>
          <Column field="subCategoria.categoria.descripcion"  header="Categoría" aria-sort="ascending" sortable> 
        </Column>   
        <Column field="subCategoria.descripcion"  header="Subcategoría" aria-sort="ascending" sortable> 
        </Column> 
          <Column :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
              <Button icon="pi pi-eye" text rounded aria-label="Search" v-tooltip="'Ver detalles'" @click="verProducto(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
              <Button icon="pi pi-pencil" severity="success" v-tooltip="'Editar'" text rounded aria-label="Search" @click="modificarProducto(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-trash" severity="danger" v-tooltip="'Eliminar'" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id, slotProps.index)"  style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-money-bill" severity="info" v-tooltip="'Ver precios'" text rounded aria-label="Cancel" @click="verHistorialPrecios(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" /> 
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>