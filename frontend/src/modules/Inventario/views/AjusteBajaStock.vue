<script setup>
// Importaciones
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
import {formatearNumero, formatearFecha, getEstadoAjuste} from '@/utils/utils';
const toast = useToast();

// Variables 
const mensaje = ref([]);
const error = ref(false);
const detalleAjuste = ref([]);
const ajuste = ref({});


onMounted(() => {
    AjusteStockServices.getAjuste(router.currentRoute.value.params.id).then((data) => {
        ajuste.value = data.data.ajuste;
        detalleAjuste.value = data.data.detalle;    
    });
});

// Vista de Ajustes
const vistaListaAjustes = () => {
    router.push({name: 'ajustes'});
}

const modificarAjuste = (id) => {
    router.push({name: 'modificar_ajuste', params: {id}});
}

// Función para validar el formulario antes de ajustar
const validarForm = (id) => {
    let detalleAjustar = detalleAjuste.value?.filter(d => d.cantidadFinal >= 0);
    
    mensaje.value = [];
    error.value = false;

    if (detalleAjustar.length < 1) {
        error.value = true;
        mensaje.value.push("Uno o más productos quedarían con stock negativo si se aplica esta baja.");
    }

    ajustarInventario(id);
}

// Función para realizar el ajuste de inventario
const ajustarInventario = (id) =>{
    if (!error.value){
        AjusteStockServices.ajustar(id).then((data)=> {
            showSuccess('Inventario ajustado');
            vistaListaAjustes();
        });
    }
}

// Función para mostrar notificación de éxito
const showSuccess = (message) => {
    toast.add({
      severity: 'success',
      summary: 'Éxito',
      detail: message,
      life: 3000
    });
};
</script>

<template>
    <div class=" flex justify-content-center " >
        <Panel style=" position: relative; width: 80%;" >
            <!-- Encabezado del panel-->
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Baja de Stock N° {{ router.currentRoute.value.params.id }} </h3>
                </div>
            </template>
            
            <!-- Botones de acciones en la parte superior derecha -->
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button label="Atras"  style="margin-right: 1%;" @click="vistaListaAjustes()" />
                        <Button style="margin-right: 1%;" label="Modificar" @click="modificarAjuste(router.currentRoute.value.params.id)" />
                        <Button label="Ajustar" @click="validarForm(router.currentRoute.value.params.id)" />
                    </div>  
                </div>
            </template>
            
            <!-- Mensajes de error -->
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

            <!-- Contenido principal -->
            <div class="grid " >
                <!-- Sección de información general del ajuste -->
                <div class="field col-12 md:col-12">
                    <Card >
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Información General
                                </div>    
                            </div>
                        </template>
                        <template #content>
                            <div>
                                Usuario: {{ ajuste.usuarioRegistro?.empleado?.persona?.nombre || '' }} {{ ajuste.usuarioRegistro?.empleado?.persona?.apellido }}
                            </div>
                            <div  >
                                Fecha: {{ formatearFecha(ajuste.fecha) }}
                            </div> 
                            <div  >
                                Estado: {{getEstadoAjuste(ajuste.estado)}}
                            </div> 
                            <div>
                                Observaciones: 
                                {{ajuste.observaciones}}
                            </div> 
                        </template>
                    </Card>
                </div> 
            
                <!-- Sección de productos a ajustar -->
                <div class="col-12" >
                    <Card >
                        <template #title>
                            <div class="flex justify-content-between ">
                                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                                    Productos
                                </div>
                            </div>
                        </template>
                    
                        <template #content>
                            <div>
                                <div class="card" style="width: 100%;">
                                    <div class="flex card-container" style="width: 100%;">
                                        <!-- Tabla con detalle de productos -->
                                        <DataTable class="tablaCarrito" ref="dt" :value="detalleAjuste" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
                                            <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>     
                                            <Column  class="col" field="cantidadAnterior" header="Cant. Anterior" aria-sort="none" ></Column>     
                                            <Column  class="col" field="cantidadAjustada" header="Cant. Disminuida" aria-sort="none"></Column>
                                            <Column  class="col" field="cantidadFinal" header="Cant. Final" aria-sort="none" ></Column> 
                                            <Column  class="col" field="motivo" header="Motivo" aria-sort="none" ></Column> 
                                        </DataTable>
                                    </div>
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