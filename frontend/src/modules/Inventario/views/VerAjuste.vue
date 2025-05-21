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


const vistaListaAjustes = () => {
    router.push({name: 'ajustes'});
}

const modificarAjuste = (id) => {
    router.push({name: 'modificar_ajuste', params: {id}});
}


</script>
<template>
    <div class=" flex justify-content-center " >
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Baja de Stock N° {{ router.currentRoute.value.params.id }} </h3>
                </div>
            </template>
            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button label="Atras"  style="margin-right: 1%;" @click="vistaListaAjustes()" />
                    </div>  
                </div>
            </template>

            <div class="grid " >
                <!--Detalle Ajuste -->
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
            
                <!--Productos Ajustar -->
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
                                        <DataTable class="tablaCarrito" ref="dt" :value="detalleAjuste" scrollable scrollHeight="400px"  dataKey="producto.id" style="width: 100%;">
                                            <Column  class="col" field="producto.nombre" header="Nombre" aria-sort="none" ></Column>         
                                            <Column  class="col" field="cantidadAjustada" header="Cantidad" aria-sort="none"></Column>
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