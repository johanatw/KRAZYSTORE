<script setup>
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import Dialog from "primevue/dialog";
import InputText from "primevue/inputtext";
import MapComponent from '@/modules/Pedidos/components/MapComponent.vue';
import Dropdown from "primevue/dropdown";
import AutoComplete from 'primevue/autocomplete';
import Calendar from 'primevue/calendar';
import { AjusteStockServices } from "@/services/AjusteStockServices";
import Card from "primevue/card";
import { ProveedorServices } from '@/services/ProveedorServices';
import Button from 'primevue/button';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import { ProductoServices } from '@/services/ProductoServices';
import { CompraServices } from "@/services/CompraServices";
import { VentaServices } from '@/services/VentaServices';
import { CiudadServices } from '@/services/CiudadServices';
import { ref, onMounted } from "vue";
import InputNumber from 'primevue/inputnumber';
import InputGroup from 'primevue/inputgroup';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import Panel from 'primevue/panel';
import {PersonaServices} from '@/services/PersonaServices';
import router from '@/router';
import { TipoDocServices } from "@/services/TipoDocServices";
import {DepartamentoServices } from '@/services/DepartamentoServices';
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { watch } from "vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import PedidoCompraServices from "@/services/PedidoCompraServices";
import {formatearNumero, formatearFecha} from '@/utils/utils';
import DatePicker from "primevue/datepicker";
import Textarea from "primevue/textarea";

const mensaje = ref([]);
const visible = ref(false);
const productos= ref();
const error = ref(false);
const showDiferencia = ref(false);
const confirm = useConfirm();
const toast = useToast();
const detalleAjuste = ref([]);
const fechaAjuste = ref(new Date());
const observaciones = ref();

const message = (m) => {
    let id = m.id;
    let nro = m.nroFactura;
    console.log('messageError invocado');
    confirm.require({
        group: 'headless',
        header: '#'+ nro,
        message: 'Se ha generado la Factura',

        accept: () => {
            router.push({name: 'verFactura', params: {id}});
            //verPedido(router.currentRoute.value.params.id);
            
        },
        reject: () => {
            router.push({name: 'ventas'});
            //verPedido(router.currentRoute.value.params.id);
            
        },
    });
};

onMounted(() => {
    AjusteStockServices.getProductosParaAjuste().then((data) => {
        productos.value = data.data
        console.log(productos.value);
    });
});

const filters = ref({
 'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const validarForm = () => {
    mensaje.value = [];
    error.value = false;
    
    if (detalleAjuste.value.length < 1) {
        error.value = true;
        mensaje.value.push("Debe añadir productos");
    }
    
    guardarAjuste();

}


const addItem = (item) => {
    let index = detalleAjuste.value.findIndex((loopVariable) => loopVariable.producto.id === item.producto.id);

    if (index == -1) { detalleAjuste.value.push(item) }
}

const eliminar = (detalle) => {
    const cantidad= 1;
    detalle.cantidadAjustada = 0;
    let index = detalleAjuste.value.findIndex((loopVariable) => loopVariable.producto.id === detalle.producto.id);
    detalleAjuste.value.splice(index,cantidad);
}

const vistaVerAjuste = (id) => {
    router.push({name: 'ver_ajuste', params: {id}});

}

const vistaListaAjustes = () => {
    router.push({name: 'ajustes'});
}

const guardarAjuste = () =>{
    if (!error.value){

        const username = localStorage.getItem('username');
        let ajuste = {fecha: fechaAjuste.value, observaciones: observaciones.value};
        let ajusteCreationDTO = {ajuste: ajuste, detalle: detalleAjuste.value, username:username};
        
        AjusteStockServices.registrarAjuste(ajusteCreationDTO ).then((data)=> {
            let id = data.data.id;
            vistaVerAjuste(id);
        } );
    }
}


</script>
<template>
    <div class=" flex justify-content-center " >
        <ConfirmDialog group="headless">
            <template #container="{ message, acceptCallback, rejectCallback }">
                <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                    <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                        <i class="pi pi-check text-5xl"></i>
                    </div>
                    <p class="block mb-2 mt-4">{{ message.message }}</p>
                    <span class="font-bold mb-0 ">{{ message.header }}</span>
                    
                    <div class="flex align-items-center gap-2 mt-4">
                        <Button label="Ver factura" @click="acceptCallback"></Button>
                        <Button label="Ir a facturas" @click="rejectCallback"></Button>
                    </div>
                </div>
            </template>
        </ConfirmDialog>

  

        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Nueva Baja de Stock</h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" @click="vistaListaAjustes()" />
                        <Button  label="Guardar" @click="validarForm()" />
                    </div>  
                </div>
            </template>
            <!--Barra Error -->
            <div class="contenedor" >
                <div v-if="error" style="background-color: rgb(242, 222, 222); 
                border: solid 1px rgb(215, 57, 37); padding-top: 1%; padding-bottom: 1%; margin-bottom: 1%;"> 
                    <ul>
                        <li v-for="msg in mensaje" style="list-style: none;">
                            <a style="color: rgb(173, 89, 86);">{{ msg }}</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="grid " >
                <!--Detalle Ajuste -->
                <div class="field col-12 md:col-6">
                    <Card>
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Detalle Ajuste
                                </div>    
                            </div>
                        </template>
                        <template #content>
                            <div class="field" >
                                Fecha: <DatePicker fluid v-model="fechaAjuste" dateFormat="dd/mm/yy" showIcon iconDisplay="input" />
                            </div> 
                            <div class="field">
                                Observaciones: 
                                <Textarea fluid v-model="observaciones" rows="3" cols="33" />
                            </div>
                        </template>
                    </Card>
                </div> 
                <!--Productos Ajustar -->
                <div class="col-12" >
                    <Card >
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Productos
                                </div>
                                <div >
                                    <Button label="+ Producto" link @click="visible = true" />
                                </div>
                            </div>
                        </template>
                    
                        <template #content>
                            <div>
                                <div class="card" style="width: 100%;">
                                    <div class="flex card-container" style="width: 100%;">
                                        <DataTable class="tablaCarrito" ref="dt" :value="detalleAjuste" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
                                            <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
                                   
                                            <Column  class="col" field="cantidadAjustada" header="Cantidad" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div class="flex-auto p-fluid" style="max-width:15lvb  !important; ">
                                                        <InputNumber fluid class="inpCant" v-model="slotProps.data.cantidadAjustada" inputId="minmax-buttons" mode="decimal" :min="1" showButtons />
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column  class="col" field="motivo" header="Motivo" aria-sort="none">
                                                <template #body="slotProps">
                                                    <div>
                                                        <InputText v-model="slotProps.data.motivo" type="text" size="small" placeholder="Motivo" />
                                                    </div>  
                                                </template>
                                            </Column>
                                            <Column class="col" :exportable="false" style="min-width:1rem">
                                                <template #body="slotProps">
                                                    <Button icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminar(slotProps.data)" />
                                                </template>
                                            </Column>
                                        </DataTable>
                                    </div>
                                </div>
                                <!--Seleccionar Productos Dialog -->
                                <div>
                                    <Dialog v-if="visible" v-model:visible="visible" modal header="Seleccionar productos" :closable="false" :draggable="false" :style="{ width: '40rem' }"  >
                                        <template #footer>
                                            <div class="flex justify-content-end">
                                                <Button label="Cerrar" icon="pi pi-times" text @click="visible = false" />
                                            </div>
                                        </template> 

                                        <div class="grid" style="row-gap: 2.5vh;">
                                            <div class="card col-12" style="width: 100%;">
                                                <span class="p-input-icon-left" style="width: 100%; margin-top: 0.5rem;">
                                                    <InputText  class="buscador p-fluid" style="width: 100%;" v-model="filters['global'].value" placeholder="Buscar..." />
                                                </span>
        
                                                <div class="flex card-container col-12" style="width: 100%;">
                                                    <DataTable class="tabla" ref="dt"  :value="productos"  dataKey="idProducto"
                                                        :paginator="true" :rows="7" :filters="filters"
                                                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" >
                                                
                                                        <Column field="producto.id"  header="ID" aria-sort="ascending" ></Column>
                                                        <Column field="producto.nombre" header="Nombre" aria-sort="none" ></Column>
            
                                                        <Column :exportable="false" style="min-width:8rem">
                                                            <template #body="slotProps">
                                                                <Button icon="pi pi-shopping-cart" class="mod_icono"  @click="addItem(slotProps.data)"/>
                                                            </template>
                                                        </Column>
                                                    </DataTable>
                                                </div>
                                            </div>
                                        </div>
                                    </Dialog>  
                                </div>
                            </div>
                        </template>    
                    </Card>
                </div>
            </div>
        </Panel>
    </div>
</template>

<style>
.p-inputgroup-addon{
    padding: 0%;
}

.p-inputnumber-buttons-stacked .p-inputnumber-button-group .p-button.p-inputnumber-button {
    flex: 1 1 auto;
    padding: 0rem;
    width: 1rem;
}

</style>