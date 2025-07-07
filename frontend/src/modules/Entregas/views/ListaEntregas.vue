<script setup>
//Importaciones
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import {PedidoServices} from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';
import {ReembolsoServices} from '@/services/ReembolsoServices'
import { CompraServices } from '@/services/CompraServices';
import {CajaServices} from '@/services/CajaServices'
import { AuthServices } from '@/services/AuthServices';
import Panel from 'primevue/panel';
import { EntregaServices } from '@/services/EntregaServices';
import router from '@/router';
import { DireccionServices } from '@/services/DireccionServices';
import { ModosEntregaServices } from '@/services/ModosEntregaServices';
import { EmpresasDeliveryServices } from '@/services/EmpresasTransporteServices';
import { PuntoRetiroServices } from '@/services/PuntoEntregaServices';
import Toast from 'primevue/toast';
import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Select from 'primevue/select';
import DatePicker from 'primevue/datepicker';
import Listbox from 'primevue/listbox';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import SplitButton from 'primevue/splitbutton';
const pedidos = ref();
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { formatearNumero, formatearFecha, getEstadoFacturaCompra, getEstadoEntrega, formatearFechaHora } from '@/utils/utils'; 

//Variables
const indexRegistroActualizado = ref();
const confirm = useConfirm();
const toast = useToast();
const selectedOpcion = ref();
const idPedidoSelected = ref();
const entregas = ref();
const cajaAbierta = ref({});
const direccion = ref({});
const entrega = ref({});
const cliente = ref({});
const reprogramarDialog = ref(false);
const direccionesCliente = ref([]);
const puntosEntrega = ref([]);
const empresasTransporte = ref([]);
const clienteDialog = ref(false);
const modalidadesEntrega = ref([]);
const confirm2 = (id, index) => {
    confirm.require({
        message: 'Eliminar esta entrega?',
        header: 'Confirmacion',
        icon: 'pi pi-info-circle',
        rejectLabel: 'Cancelar',
        acceptLabel: 'Eliminar',
        rejectClass: 'p-button-secondary p-button-outlined',
        acceptClass: 'p-button-danger',
        accept: () => {
            deleteEntrega(id,index);    
        },  
    });
};


onMounted(() => {
    getEntregas();    
});

//Obtener compras
const getCompras= () => {
  CompraServices.obtenerCompras().then((data) => {
        entregas.value = data.data;
        console.log(entregas.value);
    });
};

//Reprogrmar entregas
const reprogramarEntrega= (id, index) => {
    indexRegistroActualizado.value = index;
    getPuntosEntrega();
    getModosEntrega();
    getEmpresasTransporte();
    getEntrega(id);
  reprogramarDialog.value = true;
};

//Obtener entrega
const getEntrega = async (id) => {
    try {
      const response = await EntregaServices.getEntrega(id);
      console.log(response);
      entrega.value = response.data.entrega;
      entrega.value.fecha = new Date(entrega.value.fecha);
      cliente.value = entrega.value.venta?.cliente;
      console.log(cliente.value);
        getDireccionesCliente(cliente.value.id);
    } catch (error) {
       //alert(error);
    }
};

const isRetiro = (modalidad) => {
    let descripcion = modalidad?.descripcion;
  switch (descripcion) {
       case 'Retiro':
           return true;
       default:
           return false;
   }
};

const isEnvio = (modalidad) => {
    let descripcion = modalidad?.descripcion;
  switch (descripcion) {
       case 'Envio':
           return true;
       default:
           return false;
   }
};

const getPuntosEntrega = async () => {
    try {
      const response = await PuntoRetiroServices.obtenerPuntosRetiro();
      puntosEntrega.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

const getEmpresasTransporte = async () => {
    try {
      const response = await EmpresasDeliveryServices.obtenerEmpresasDelivery();
      empresasTransporte.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

//Direcciones cliente
const getDireccionesCliente = async (id) => {
    try {
      const response = await DireccionServices.getDireccionesCliente(id);
      direccionesCliente.value = response.data;
      console.log(direccionesCliente.value);
    } catch (error) {
       //alert(error);
    }
};

//Modos de entrega
const getModosEntrega = async () => {
    try {
      const response = await ModosEntregaServices.obtenerModosEntrega();
      modalidadesEntrega.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

const getEntregas = async () => {
    try {
      const response = await EntregaServices.obtenerEntregas();
      entregas.value = response.data;
    } catch (error) {
       //alert(error);
    }
};

//Obtener caja abierta
const getCajaAbierta= () => {
    CajaServices.getCajaAbierta().then((data) => {
        cajaAbierta.value=data.data;
        console.log(cajaAbierta.value);
    });
};

//Guardar entrega
const guardarEntrega = () =>{
    let entregaDTO = {entrega: entrega.value, detalle: null};
    EntregaServices.reprogramarEntrega(entrega.value.id ,entregaDTO).then((response)=>{
        entregas.value[indexRegistroActualizado.value] = response.data;
        hideReprogramarDialog();
    }).catch(
    (error)=>console.log(error)
    );
}

//Guardar dirección
const saveDireccion = () =>{
  submitted.value = true;
  if(direccionValida(direccion.value)){
    direccion.value.tipo = 'E';
    direccion.value.persona = cliente.value;
    direccion.value.direccion = generarDireccion(direccion.value);
    DireccionServices.saveDireccion(direccion.value).then((response)=>{
      direccionesCliente.value.push(response.data);
    });
  }
  //hideDialog();
}

//Dirección valida
const direccionValida = (dir) => {
    console.log("valdirenvio");
    console.log(dir);
    if ((!dir.calle1 || !dir.ciudad)) {
        console.log("val envio false");
        return false;
    }  
    return true;
};

//Agregar nueva dirección
const agregarNuevaDireccion = () =>{
  clienteDialog.value = true;
}

const hideReprogramarDialog = () => {
    reprogramarDialog.value = false;
};

//Generar dirección
const generarDireccion = (dir) => {
  console.log(dir);
    let d = dir.calle1;
    if (dir.calle2?.trim()) {
        d = d + " " +selectedOp.value + " "+ dir.calle2;
    }
    if (dir.calle3?.trim()) {
        d = d + " y " + dir.calle3;
    }
    return d;
};

//Modificar modo de entrega
const cambiarModoEntrega= (modalidad) => {
    let descripcion = modalidad?.descripcion;
    if (descripcion == 'Retiro') {
      entrega.value.empresaTransporte = null;
      entrega.value.direccionEnvio = null;
    } else {
      entrega.value.puntoEntrega = null;
    }
};

//Eliminar entrega
const deleteEntrega = (id, index) =>{
    const cantidad= 1;
    EntregaServices.eliminar(id).then((response)=>{
      console.log("response");
        entregas.value.splice(index,cantidad);
        toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 }); 
    })
}

//Marcar como entregado
const marcarComoEntregado = (id,index) =>{
    let entregaDTO = {entrega: null, detalle: null};
    EntregaServices.setEntregado(id ,entregaDTO).then((response)=>{
        entregas.value[index] = (response.data);
    }).catch(
    (error)=>console.log(error)
    );

}

//Marcar como no entregado
const marcarComoNoEntregado = (id,index) =>{
    let entregaDTO = {entrega: null, detalle: null};
    EntregaServices.setNoEntregado(id ,entregaDTO).then((response)=>{
        entregas.value[index] = (response.data);
    }).catch(
    (error)=>console.log(error)
    );
}

//Registra en caja actual abierta
const registradoEnCajaActualAbierta = (fechaRegistro) =>{
    console.log("registradoEnCajaActualAbierta");
    if (cajaAbierta.value != null && fechaRegistro >= cajaAbierta.value.fecha) {
        return false;
    } else {
        return true;
    }
};

const isPagado = (estado) => {
  switch (estado) {
       case 'C':
           return true;
       default:
           return false;
   }
};

const isNoEntregado = (estado) => {
  switch (estado) {
       case 'X':
           return true;
       default:
           return false;
   }
};

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});


const getSeverity = (estado) => {
  switch (estado) {
       case 'E':
           return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66);';

       case 'N':
           return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15);';

       case 'P':
           return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252);';

       default:
           return null;
   }
};


const addPago = () =>{
  let id = idPedidoSelected.value;
    if (selectedOpcion.value.id === 2 ) {
      router.push({name: 'AddPago', params: {id}});
    }
}

const reembolsar = (id) =>{
    router.push({name: 'reembolsar', params: {id}});
}

const cancelar = ()=>{
  visible.value = false;
  selectedOpcion.value = null;
}

const verEntrega = (id) =>{
    router.push({name: 'entrega', params: {id}});    
}

const verAnticipo = (id) =>{
    console.log("veranticipofuncion");
    router.push({name: 'verAnticipo', params: {id}});
}

const deletePedido = (id) =>{
    const cantidad= 1;
    const index = pedidos.value.findIndex((loopVariable) => loopVariable.id === id);
    PedidoServices.deleteDetallesPedido(id).then((response)=>{
        console.log(response);
        PedidoServices.deletePedido(id).then((response)=>{
            pedidos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 3000 });
        })
    });
}

const nuevoPedido = () =>{
    router.push({name: 'nueva_compra'});
}

</script>

<template> 
    <div class="flex p-fluid justify-content-center " >
        <ConfirmDialog ></ConfirmDialog>
        <Toast />
        <!--Dialog Registrar Modificar Cliente-->
        <Dialog v-model:visible="clienteDialog" :closable="false" :style="{width: '450px'}" header="Cliente" :modal="true" class="p-fluid">
            <div class="field">
                <label for="description">Calle Principal</label>
                <InputText fluid id="description" v-model="direccion.calle1" required="true" :class="{'p-invalid': submitted && !direccionValida(direccion) && !direccion.calle1}" />
                <small class="p-error" v-if="submitted && !direccionValida(direccion) && !direccion.calle1">Ingrese Calle Principal</small>
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
                <label for="description">N° Casa</label>
                <InputText fluid id="description" v-model="direccion.nroCasa" required="true"  />
            </div>
            <div class="field " >
                <label for="nombreu">Departamento</label>
                <Dropdown fluid v-model="direccion.departamento" :options="departamentos" optionLabel="descripcion" placeholder="Seleccione un departamento" @change="getCiudades(direccion.departamento.id)"  />
            </div>
            <div class="field " >
                <label for="nombreu">Ciudad</label>
                <Dropdown fluid v-model="direccion.ciudad" :options="ciudades" optionLabel="descripcion" placeholder="Seleccione una ciudad" :class="{'p-invalid': submitted && !direccionValida(direccion) && !direccion.ciudad}"  />
                <small class="p-error" v-if="submitted && !direccionValida(direccion) && !direccion.ciudad">Ingrese Ciudad</small>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="saveDireccion" />
            </template>
        </Dialog>
        <!--Dialog Registrar Modificar Cliente-->
        <Dialog v-model:visible="reprogramarDialog" :closable="false" :style="{width: '450px'}" header="Reprogramar" :modal="true" class="p-fluid">
            <div class="field" >
                Fecha: <DatePicker fluid dateFormat="dd/mm/yy" v-model="entrega.fecha"showTime hourFormat="24" showIcon iconDisplay="input" />
            </div>
            <div class="field" >
                Modalidad: 
                <Select fluid v-model="entrega.modoEntrega" :options="modalidadesEntrega" optionLabel="descripcion" placeholder="Seleccione una modalidad" class="w-full md:w-56" @change="cambiarModoEntrega(entrega.modoEntrega)" />
            </div> 
            <div class="field" v-if="isRetiro(entrega.modoEntrega)" >
                Punto de entrega: 
                <Select fluid v-model="entrega.puntoEntrega" :options="puntosEntrega" optionLabel="descripcion" placeholder="Seleccione un punto de entrega" class="w-full md:w-56" />
            </div>
            <div class="field" v-else-if="isEnvio(entrega.modoEntrega)" >
                Delivery: 
                <Select fluid v-model="entrega.empresaTransporte" :options="empresasTransporte" optionLabel="descripcion" placeholder="Seleccione un delivery" class="w-full md:w-56" />
            </div>
            <div class="field" v-if="isEnvio(entrega.modoEntrega)">
                <div>
                    <label for="description">Dirección de envío:</label>
                        <Select fluid v-model="entrega.direccionEnvio" :options="direccionesCliente" optionLabel="descripcion" placeholder="Seleccione una dirección" class="w-full md:w-56">
                            <template #value="slotProps">
                                <div v-if="slotProps.value" class="flex items-center">
                                    <div>{{ slotProps.value.direccion }}<br>{{ slotProps.value.ciudad?.descripcion }}-> {{ slotProps.value.ciudad?.departamento?.descripcion }}</div>
                                </div>
                                <span v-else>
                                    {{ slotProps.placeholder }}
                                </span>
                            </template>
                            <template fluid #option="slotProps">                            
                                {{ slotProps.option.direccion }}<br>{{ slotProps.option.ciudad?.descripcion }}<br>{{ slotProps.option.ciudad?.departamento?.descripcion }}
                            </template>
                        </Select>
                </div>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" text @click="hideReprogramarDialog"/>
                <Button label="Save" icon="pi pi-check" text @click="guardarEntrega" />
            </template>
        </Dialog>
        <Panel style=" position: relative; width: 90%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Entregas</h3>
                </div>
            </template>   
            <template #icons>
                <div class="flex align-items-center">
                    <InputGroup>
                        <InputText v-model="filters['global'].value" placeholder="Buscar..." />
                        <InputGroupAddon>
                            <i class="pi pi-search" />
                        </InputGroupAddon>
                    </InputGroup>
                </div>
            </template>
            <div >
                <DataTable  :value="entregas"  scrollHeight="400px"  
                    :paginator="true" :rows="7" :filters="filters"
                    paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                    currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                    <template #empty> No hay registros para mostrar. </template>
                    <template #loading> Cargando. </template>
                    <Column field="fecha" sortable header="N° Pedido" aria-sort="ascending" >
                        <template #body="slotProps">
                            {{ slotProps.data.venta?.pedido?.id }}
                        </template>
                    </Column>
                    <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                        <template #body="slotProps">
                            {{ formatearFechaHora(slotProps.data.fecha) }}
                        </template>
                    </Column>
                    <Column field="proveedor.descripcion"  header="Cliente" aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            <div>
                                {{ slotProps.data.venta?.pedido?.cliente?.persona.nombre }}
                                <label v-if="slotProps.data.venta?.pedido?.cliente?.persona.apellido" for="apellido">
                                    {{ slotProps.data.venta?.pedido?.cliente?.persona.apellido }}
                                </label>
                            </div>
                        </template>           
                    </Column>
                    <Column field="proveedor.descripcion"  header="Modalidad" aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            <div>
                                {{ slotProps.data.modoEntrega?.descripcion }}
                            </div>
                        </template>          
                    </Column>
                    <Column field="proveedor.descripcion"  header="Delivery/Punto de Retiro" aria-sort="ascending" sortable> 
                        <template #body="slotProps">
                            <div v-if="slotProps.data.modoEntrega?.descripcion == 'Retiro'">
                                {{ slotProps.data.puntoEntrega?.descripcion }}
                            </div>
                            <div v-else>
                                {{ slotProps.data.empresaTransporte?.descripcion }}
                            </div>  
                        </template>          
                    </Column>
                    <Column field="estado"  header="Estado" aria-sort="ascending" sortable>    
                        <template #body="slotProps">
                                <Tag :style="getSeverity(slotProps.data.estado)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ getEstadoEntrega(slotProps.data.estado)}}</Tag>
                            </template>          
                    </Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-eye" v-tooltip="'Ver detalles'" text rounded aria-label="Search" @click="verEntrega(slotProps.data.id)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-check" v-tooltip="'Entregado'" :disabled="isPagado(slotProps.data.estado)" text rounded aria-label="Search" severity="success" @click="marcarComoEntregado(slotProps.data.id,slotProps.index )" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-times" v-tooltip="'No Entregado'" :disabled="isPagado(slotProps.data.estado)" text rounded aria-label="Search" severity="warn" @click="marcarComoNoEntregado(slotProps.data.id, slotProps.index)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button icon="pi pi-sync" v-tooltip="'Reprogramar'" :disabled="!isNoEntregado(slotProps.data.estado)" severity="info" text rounded aria-label="Search" @click="reprogramarEntrega(slotProps.data.id, slotProps.index)" style="height: 2rem !important; width: 2rem !important;" />
                            <Button  icon="pi pi-trash" v-tooltip="'Eliminar'"  severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id, slotProps.index )"  style="height: 2rem !important; width: 2rem !important;" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div> 
</template>