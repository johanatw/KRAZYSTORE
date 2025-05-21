
<template>
    <div class=" flex p-fluid justify-content-center " >
        
        
        <Toast />
        <ConfirmDialog ></ConfirmDialog>
        <!--Visualizar observacion movimiento-->
        <Dialog v-model:visible="observacionDialog" modal  header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Observaciones</h3>
                </div>
            </template>
    
            <div >               
                <div>
                    {{ observacion || "Sin observaciones" }}
                </div>  
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cerrar"  style="margin-right: 1%;" @click="observacionDialog=false" />
                </div>
            </template>
        </Dialog>

        <!--Registrar movimiento-->
        <Dialog v-model:visible="visible" modal @update:visible="closeDialog($event)" header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Registrar Movimiento</h3>
                </div>
            </template>
    
            <div >               
                <div>
                  
                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="username" class="font-semibold w-9rem">Tipo transacción</label>
                        <Dropdown v-model:model-value="movimiento.concepto" :options="conceptos" optionLabel="descripcion" placeholder="Seleccione un elemento" checkmark :highlightOnSelect="false" class="w-full md:w-14rem"  />
                    </div>

                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="email" class="font-semibold w-9rem">Monto</label>
                        <InputNumber v-model:model-value="movimiento.monto" inputId="integeronly" @input="validarMonto($event)" />
                    </div>
                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="email" class="font-semibold w-9rem">Medio de pago</label>
                        <Dropdown v-model:model-value="movimiento.medio" :options="formasPago" optionLabel="descripcion"  placeholder="Seleccione un elemento" checkmark :highlightOnSelect="false" class="w-full md:w-14rem" />
                    </div>
                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="email" class="font-semibold w-9rem">Documento</label>
                        <InputText v-model:model-value="movimiento.nroDocumento" id="email" class="flex-auto" autocomplete="off"  />
                    </div>
                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="email" class="font-semibold w-9rem">Observación</label>
                        <Textarea v-model:model-value="movimiento.observacion" rows="5" cols="30"  />
                    </div>
                </div>  
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                    <Button  label="Guardar" :disabled="disabledSubmit" @click="guardarMovimiento()" />
                </div>
            </template>
        </Dialog>

        <!--Registrar pago de facturas pendientes-->
        
        <Dialog v-model:visible="registrarPagoDialog" modal @update:visible="closeDialog($event)" header="Edit Profile" :style="{ width: '40rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Registrar Pago </h3>
                    
                </div>
            </template>

            <div >
                
                        <div v-if="pagosAsociados" style="margin-bottom: 1rem; ">
                            <Message severity="info" >El pedido asociado a esta factura tiene anticipos</Message>
                        </div>
                        <div>
                            <h3 style="font-weight: bolder;margin-bottom: 1rem; " >TOTAL FACTURA: {{ formatearNumero(total) }} Gs.</h3> 
                        </div>
                        
                        <div  v-if="pagosAsociados">
                            <div> <h4 style="font-weight: bold; margin-bottom: 1rem; " >Anticipos Disponibles:</h4></div>
                            
                            <div v-for="(aplicar, index) in pagosAplicar" :key="index" class="formgrid grid">
                                <div class="field col-1 md:col-1 p-fluid" style="justify-content: start;  ">
                                <Checkbox v-model="aplicar.seleccionado" name="anticipo" :binary="true" @change="getPagosSeleccionados()"  />
                            </div>
                            <div class="field col-4 md:col-4 p-fluid" style="justify-content: start;  ">
                                <label>Anticipo #{{ aplicar.pagoPedidoCompra.id}} - {{ formatearNumero(aplicar.pagoPedidoCompra.saldo) }} Gs.</label>
                            </div>
                            <div class="field col-5 md:col-5 p-fluid" style="justify-content: start;  ">
                                <InputNumber :disabled="!aplicar.seleccionado" v-model="aplicar.monto" :max="aplicar.monto" @update:model-value="actualizarSumaTotal()" placeholder="Monto a usar" style="padding: 0rem !important; height: 100%;"/>
                            </div>
                            </div>
                        </div>
                        
                        <div>
                            <div class="formgrid grid" v-for="(item, index) in pagos" :key="index" >
                                        
                                <div class="field col-5 md:col-5 p-fluid" style="justify-content: start;  ">
                                    <Select style="padding: 0rem !important;" v-model="item.medio" :options="formasPago" @change="habilitarInput(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   />
                                </div>
                                <div  class="field col-5 md:col-5 p-fluid" style=" justify-content: start; " >
                                    <InputNumber :disabled=item.disabled name="input" style="padding: 0rem !important; height: 100%;" v-model="item.importe"  @input="actualizarImporte($event, index)"/>
                                    
                                </div>
                                <div v-if="index == 0" class=" field col-1 md:col-1 p-fluid" style=" justify-content: flex-end">
                                    <Button style="background: none !important; border: none !important " icon="pi pi-plus" severity="danger" text rounded aria-label="Cancel" @click="addRow()" />
                                </div>
                                <div v-else class=" field col-1 md:col-1 p-fluid">
                                    <Button style="background: none !important; border: none !important " icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminarRow(index)" />
                                </div>
                                
                            </div>
                            
                            
                        </div>
                        <div class="formgrid grid">
                                <div class="flex field col-12 md:col-12" >     
                                    <div class="flex field col-9 md:col-9 justify-content-start" >
                                        <label for="totalPagos"> Total abonado: </label>
                                    </div>
                                    <div class="flex field col-3 md:col-3" style=" justify-content: center; " >
                                        <span>{{ sumaTotal.toLocaleString('de-DE') }}</span>
                                    </div>
                                </div>
                            </div>
                    
            </div>
            <template #footer>
             
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                    <Button  label="Guardar" :disabled="disabledSubmit" @click="guardarPagosPendientes()" />
                </div>
            </template>
        </Dialog>

        <!--Registrar cobro de facturas pendientes-->
        
        <Dialog v-model:visible="registrarCobroDialog" modal @update:visible="closeDialog($event)" header="Edit Profile" :style="{ width: '40rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Registrar Cobro </h3>
                    
                </div>
            </template>

            <div >
                
                        <div v-if="anticiposAsociados" style="margin-bottom: 1rem; ">
                            <Message severity="info" >El pedido asociado a esta factura tiene anticipos</Message>
                        </div>
                        <div>
                            <h3 style="font-weight: bolder;margin-bottom: 1rem; " >TOTAL FACTURA: {{ formatearNumero(total) }} Gs.</h3> 
                        </div>
                        <div  v-if="anticiposAsociados">
                            <div> <h4 style="font-weight: bold; margin-bottom: 1rem; " >Anticipos Disponibles:</h4></div>
                            <div v-for="(aplicar, index) in anticiposAplicar" :key="index" class="formgrid grid">
                                <div class="field col-1 md:col-1 p-fluid" style="justify-content: start;  ">
                                <Checkbox v-model="aplicar.seleccionado" name="anticipo" :binary="true" @change="getAnticiposSeleccionados()" />
                            </div>
                            <div class="field col-4 md:col-4 p-fluid" style="justify-content: start;  ">
                                <label>Anticipo #{{ aplicar.anticipo.id}} - {{ formatearNumero(aplicar.anticipo.saldo) }} Gs.</label>
                            </div>
                            <div class="field col-5 md:col-5 p-fluid" style="justify-content: start;  ">
                                <InputNumber :disabled="!aplicar.seleccionado" v-model="aplicar.monto" :max="aplicar.anticipo.saldo" 
                                @update:model-value="actualizarSumaTotal()" placeholder="Monto a usar" style="padding: 0rem !important; height: 100%;"/>
                            </div>
                            </div>
                        </div>
                        
                        <div>
                            <div class="formgrid grid" v-for="(item, index) in pagos" :key="index" >
                                        
                                <div class="field col-5 md:col-5 p-fluid" style="justify-content: start;  ">
                                    <Select style="padding: 0rem !important;" v-model="item.medio" :options="formasPago" @change="habilitarInput(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   />
                                </div>
                                <div  class="field col-5 md:col-5 p-fluid" style=" justify-content: start; " >
                                    <InputNumber :disabled=item.disabled name="input" style="padding: 0rem !important; height: 100%;" v-model="item.importe"  @input="actualizarImporte($event, index)"/>
                                    
                                </div>
                                <div v-if="index == 0" class=" field col-1 md:col-1 p-fluid" style=" justify-content: flex-end">
                                    <Button style="background: none !important; border: none !important " icon="pi pi-plus" severity="danger" text rounded aria-label="Cancel" @click="addRow()" />
                                </div>
                                <div v-else class=" field col-1 md:col-1 p-fluid">
                                    <Button style="background: none !important; border: none !important " icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminarRow(index)" />
                                </div>
                                
                            </div>
                            
                            
                        </div>
                        <div class="formgrid grid">
                                <div class="flex field col-12 md:col-12" >     
                                    <div class="flex field col-9 md:col-9 justify-content-start" >
                                        <label for="totalPagos"> Total abonado: </label>
                                    </div>
                                    <div class="flex field col-3 md:col-3" style=" justify-content: center; " >
                                        <span>{{ sumaTotal.toLocaleString('de-DE') }}</span>
                                    </div>
                                </div>
                            </div>
                    
            </div>
            <template #footer>
             
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                    <Button  label="Guardar" :disabled="disabledSubmit" @click="guardarCobrosPendientes()" />
                </div>
            </template>
        </Dialog>
      
        <!--Pantalla principal ver movimientos de caja-->
        <Panel style=" position: relative; width: 90%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Caja N° {{ caja.id }}</h3>
                </div>
            </template>

    
            <template #icons>
                <div v-if="cajaAbierta" class="flex align-items-center">
                <Button  icon="pi pi-plus " @click="nuevoMovimiento" style="margin-right: 1%; " />
                <InputGroup>
                    <Button  label="Cerrar caja" @click="cerrarCaja()" />
                </InputGroup>
                </div>
            
            </template>

            <div >
                <div >

                    <Tabs v-model:value="tabActive" >
                    <TabList>
                        <Tab value="0" >Movimientos</Tab>
                        <Tab v-if="cajaAbierta" value="1">
                            <span class="font-bold white-space-nowrap">Pendientes de pago</span>
                                    <Badge :value="pendientesPago.length"></Badge>
                        </Tab>
                        <Tab v-if="cajaAbierta" value="2">
                            <span class="font-bold white-space-nowrap">Pendientes de cobro</span>
                                    <Badge :value="pendientesCobro.length"></Badge>
                        </Tab>
                    </TabList>
                    <TabPanels>
        <TabPanel value="0">
            <DataTable  :value="movimientos" scrollHeight="400px"  
                            :paginator="true" :rows="7" 
                            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                            currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                                <Column field="id" sortable header="#" aria-sort="ascending" >
                                    <template #body="slotProps">
                                        {{ slotProps.data.id }}
                                    </template>
                                </Column>
                                <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                                    <template #body="slotProps">
                                        {{ formatearFechaHora(slotProps.data.fecha) }}
                                    </template>
                                </Column>
                                <Column field="total"  header="Cliente/Proveedor" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                        {{ slotProps.data.cliente?.persona?.nombre || slotProps.data.proveedor?.descripcion || ' ' }} {{ slotProps.data.cliente?.persona?.apellido }}
                                    </template>        
                                </Column>
                                <Column field="concepto"  header="Concepto" aria-sort="ascending" sortable></Column>
                                <Column field="formaPago"  header="Forma" aria-sort="ascending" sortable></Column>
                                <Column field="total"  header="Monto" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                        <div :style="getColor(slotProps.data.tipo)">
                                            {{ formatearNumero(slotProps.data.total) }}
                                        </div>
                                    </template>        
                                </Column>
                                <Column field="factura"  header="N° Factura" aria-sort="ascending" sortable></Column>
                                <Column  :exportable="false">
                                    <template #body="slotProps">
                                        <Button icon="pi pi-comment" v-tooltip="'Observaciones'" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="showObservacion(slotProps.data.observacion)" />
                                        <Button v-if="cajaAbierta" :disabled="!puedeEliminarseEnCaja(slotProps.data.concepto)" icon="pi pi-trash" severity="danger" v-tooltip="'Eliminar'" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="confirm2(slotProps.data.id)"  />
                                    </template>
                                </Column>
                            </DataTable>

        </TabPanel>
        <TabPanel  value="1">
            <DataTable  :value="pendientesPago" scrollHeight="400px"  
                            :paginator="true" :rows="7" 
                            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                            currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                                <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                                    <template #body="slotProps">
                                        {{ formatearFecha(slotProps.data.fecha) }}
                                    </template>
                                </Column>
                                <Column field="concepto.descripcion"  header="Concepto" aria-sort="ascending" sortable></Column>
                                <Column field="total"  header="Proveedor" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                        {{ slotProps.data.proveedor?.descripcion || ' ' }}
                                    </template>        
                                </Column>
                                <Column field="monto"  header="Monto" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                            {{ formatearNumero(slotProps.data.monto) }}
                                    </template>        
                                </Column>
                                <Column field="nroDocumento"  header="N° Factura" aria-sort="ascending" sortable></Column>
                                <Column  :exportable="false">
                                    <template #body="slotProps">
                                        <Button  icon="pi pi-money-bill" severity="info" v-tooltip="'Registrar pago'" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="registrarPago(slotProps.data)"   />
                                    </template>
                                </Column>
                            </DataTable>
        </TabPanel>
        <TabPanel  value="2">
            <DataTable  :value="pendientesCobro" scrollHeight="400px"  
                            :paginator="true" :rows="7" 
                            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                            currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                                <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                                    <template #body="slotProps">
                                        {{ formatearFecha(slotProps.data.fecha) }}
                                    </template>
                                </Column>
                                <Column field="concepto.descripcion"  header="Concepto" aria-sort="ascending" sortable></Column>
                                <Column field="total"  header="Cliente" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                        {{ slotProps.data.cliente?.persona?.nombre || ' ' }} {{ slotProps.data.cliente?.persona?.apellido }}
                                    </template>        
                                </Column>
                                <Column field="monto"  header="Monto" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                            {{ formatearNumero(slotProps.data.monto) }}
                                    </template>        
                                </Column>
                                <Column field="nroDocumento"  header="N° Factura" aria-sort="ascending" sortable></Column>
                                <Column  :exportable="false">
                                    <template #body="slotProps">
                                        <Button  icon="pi pi-money-bill" severity="info" v-tooltip="'Registrar pago'" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="registrarCobro(slotProps.data)"   />
                                    </template>
                                </Column>
                            </DataTable>
        </TabPanel>
    </TabPanels>
                </Tabs>

                </div>
            </div>
        </Panel>      
    </div>     
</template>

<script setup>

import Button from 'primevue/button';
import { ref, onMounted, onBeforeMount } from "vue";
import Card from 'primevue/card';
import Dropdown from 'primevue/dropdown';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import InputNumber from 'primevue/inputnumber';
import { AnticipoServices } from '@/services/AnticipoServices';
// import as component
import Badge from 'primevue/badge';
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import Dialog from 'primevue/dialog';
import { ConceptoServices } from '@/services/ConceptoServices';
import Panel from 'primevue/panel';
import TabView from 'primevue/tabview';
import TabPanel from 'primevue/tabpanel';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import {formatearNumero, existeCajaAbierta, formatearFecha, formatearFechaHora} from '@/utils/utils';
import { PagoPedidoCompraServices } from '@/services/PagoPedidoCompraServices';
import {CajaServices} from '@/services/CajaServices';
import router from '@/router';
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";
import { computed } from 'vue';
import { watch } from 'vue';
import Message from 'primevue/message';
import Checkbox from 'primevue/checkbox';
import Select from 'primevue/select';
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
import { MedioPagoServices } from '@/services/MedioPagoCobroServices';
import Tabs from 'primevue/tabs';
import TabList from 'primevue/tablist';
import Tab from 'primevue/tab';
import TabPanels from 'primevue/tabpanels';


const tabActive = ref('0');
const key = ref();
const v = ref(0);
const confirm = useConfirm();   
const toast = useToast();
const pendientesPago = ref([]);
const pendientesCobro = ref([]);
const anticiposPedidos= ref([]);
const anticiposAplicar= ref([]);
const pagosAplicar= ref([]);
const anticiposAplicados= ref([]);
const pagosAplicados= ref([]);
const cajaAbierta = ref(false);
const caja = ref({});
const registrarPagoDialog = ref(false);
const registrarCobroDialog = ref(false);
const pagos =ref([]);
const pago =ref({});
const disabled = ref(true);
const borrar = ref(true);
const selectedAnticipados = ref();
const  formasPago = ref([]);
const metaKey = ref(true);
const total = ref(0);
const abonado = ref(0);
const cambio = ref(0);
const faltante = ref(0);
const anticipos =ref([ ]);
const anticiposAsociados = ref(false);
const pagosAsociados = ref(false);
const disabledSubmit = ref(false);
const visible = ref(false);
const movimiento = ref({});
const movimientoPagar = ref({});
const movimientoCobrar = ref({});
const movimientos = ref([]);
const options = ref(['Ingreso', 'Egreso']);
const conceptos = ref();
const observacionDialog = ref(false);
const observacion = ref();


onMounted(() => {
    getCaja();
    getMovimientos();
    getMovimientosPendientesDePago();
    getMovimientosPendientesDeCobro();
    getConceptosIngresoEgreso();
    inicializarCampos();
  

});

const sumaTotal = ref(0);

// Watcher para observar cambios en sumaTotal
watch(() => sumaTotal.value, (newValue, oldValue) => {
    habilitarSubmit();
     
    });

watch(() => anticiposAplicados.value, (newValue, oldValue) => {
    console.log("watch anticipos aplicar");
    console.log(anticiposAplicados.value);
    actualizarSumaTotal();
    });

    watch(() => pagosAplicados.value, (newValue, oldValue) => {
    
    actualizarSumaTotal();
    });
// Función para actualizar el importe de un ítem al escribir
const actualizarImporte = (event, index) => {
            pagos.value[index].importe = event.value || 0;
            actualizarSumaTotal();
        };



// Función para actualizar la suma total
const actualizarSumaTotal = () => {
    console.log("actualizarTotal");
    console.log(calcularImportePagos());
    console.log(calcularImporteAnticipos());
    sumaTotal.value = calcularImportePagos() + calcularImporteAnticipos() + calcularImportePagosAplicados();
};

async function getConceptosIngresoEgreso() {
    try {
      const { data } = await ConceptoServices.obtenerConceptosIngresoEgreso();
      conceptos.value = data;
      console.log(conceptos.value);
    } catch (error) {
      //showError('Error al obtener clientes');
    }
  }

const getAnticiposSeleccionados = () => {
    console.log(anticiposAplicar.value);
    anticiposAplicados.value = anticiposAplicar.value.filter(e => e.seleccionado );
            
};

const getPagosSeleccionados = () => {
    console.log(pagosAplicar.value);
    pagosAplicados.value = pagosAplicar.value.filter(e => e.seleccionado );
            
};

const showObservacion = (obs) => {
    observacion.value = obs;
    observacionDialog.value = true;
            
};

const calcularImportePagos = () => {
  return pagos.value.reduce((acc, item) => acc + (item.importe || 0), 0);
};

const calcularImporteAnticipos = () => {
  return anticiposAplicados.value.reduce((acc, item) => {
    console.log("calcularImporteAnticipos");
    console.log(item);
  if ( item.monto > 0) {  // Si esta checked
    console.log("if");
    return acc + item.monto;
  }
    return acc;
    }, 0);
};

const calcularImportePagosAplicados = () => {
  return pagosAplicados.value.reduce((acc, item) => {
    console.log("calcularImporteAnticipos");
    console.log(item);
  if ( item.monto > 0) {  // Si esta checked
    console.log("if");
    return acc + item.monto;
  }
    return acc;
    }, 0);
};

const inicializarCampos = () => {
    movimiento.value.concepto = {id: 7, descripcion: 'Ingresos varios', tipo: 'I'};
    movimiento.value.medio = {id: 2, descripcion: 'Efectivo'};
    disabledSubmit.value = true;
    sumaTotal.value = 0;

    getFormasPago();
};

const cerrarCaja= () =>{
    let id = caja.value.id;

    CajaServices.cerraCaja(id).then((data) => {
        cajaAbierta.value = false;
        toast.add({ severity:"success", detail: 'Caja Cerrada', life: 3000 });
    });
}

const closeDialog = (event) =>{

    visible.value = false;
    registrarPagoDialog.value = false;
    registrarCobroDialog.value = false;
    pagosAplicar.value = [];
    anticiposAplicar.value = [];
    anticiposAplicados.value = [];
    pagosAplicados.value = [];
    reiniciarDialog();
    
}

const reiniciarDialog = () =>{
    movimiento.value = {};
    pagos.value = [];
    abonado.value = 0;
    inicializarCampos();

}
const validarMonto = (event) => {
    if (event.value > 0) {
        disabledSubmit.value = false;
    } else {
        disabledSubmit.value = true;
    }
};

const guardarMovimiento = () =>{
    let fechaMovimiento = new Date();
    movimiento.value.fecha = fechaMovimiento;
    movimiento.value.caja = caja.value;

    let pago = {importe: movimiento.value.monto,
                medio: movimiento.value.medio
                }
    let pagos = [pago];

    if (movimiento.value.tipo == 'Ingreso') {
        let ingresoDTO = {movimiento: movimiento.value, cobros: pagos};
        CajaServices.saveIngresoVario(ingresoDTO).then((data) => {
            toast.add({ severity:"success", detail: movimiento.value.tipo + ' Registrado', life: 3000 });
            getMovimientos();
            closeDialog();
            
            console.log("ok");
        });
    } else {
        let egresoDTO = {movimiento: movimiento.value, pagos: pagos};
        CajaServices.saveEgresoVario(egresoDTO).then((data) => {
            toast.add({ severity:"success", detail: movimiento.value.tipo + ' Registrado', life: 3000 });
            getMovimientos();
            closeDialog();
            
            console.log("ok");
        });
    }
 
    
}

const guardarPagosPendientes = () =>{
    let fechaMovimiento = new Date();
    movimientoPagar.value.fecha = fechaMovimiento;
    movimientoPagar.value.caja = caja.value;
    //obtenerAnticiposAplicados();
    
    
    console.log(pagos.value);
    console.log(pagosAplicados.value);
    let movimientoCreationDTO = {movimiento: movimientoPagar.value, pagos: pagos.value, pagosPedidoCompraAplicados: pagosAplicados.value};
    
    CajaServices.savePagosPendiente(movimientoCreationDTO).then((data) => {
        toast.add({ severity:"success", detail: movimiento.value.tipo + ' Registrado', life: 3000 });
        getMovimientos();
        getMovimientosPendientesDePago();
        closeDialog();
        
        console.log("ok");
    });
}

const guardarCobrosPendientes = () =>{
    let fechaMovimiento = new Date();
    movimientoCobrar.value.fecha = fechaMovimiento;
    movimientoCobrar.value.caja = caja.value;
    //obtenerAnticiposAplicados();
    
    console.log(movimientoCobrar.value);
    console.log(pagos.value);
    console.log(anticiposAplicados.value);
    let movimientoCreationDTO = {movimiento: movimientoCobrar.value, cobros: pagos.value, anticiposAplicados: anticiposAplicados.value};
    
    CajaServices.saveCobrosPendiente(movimientoCreationDTO).then((data) => {
        toast.add({ severity:"success", detail: movimiento.value.tipo + ' Registrado', life: 3000 });
        getMovimientos();
        getMovimientosPendientesDeCobro();
        closeDialog();
        
        console.log("ok");
    });
}

const deleteMovimiento = (id) =>{
    const cantidad= 1;
    const index = movimientos.value.findIndex((loopVariable) => loopVariable.id === id);
    CajaServices.deleteMovimiento(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      getMovimientos();
      getMovimientosPendientesDePago();
      getMovimientosPendientesDeCobro();
        movimientos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
        })

}

const confirm2 = (id) => {
   
   confirm.require({
       message: 'Eliminar el movimiento '+ id + '?',
       header: 'Confirmacion',
       icon: 'pi pi-info-circle',
       rejectLabel: 'Cancelar',
       acceptLabel: 'Eliminar',
       rejectClass: 'p-button-secondary p-button-outlined',
       acceptClass: 'p-button-danger',
       accept: () => {
           deleteMovimiento(id);
           
       },
       
       
   });
};



const habilitarInput = (index, item) => {

    if (item.disabled) {
        item.disabled = false;
    }

    if (item.importe < 1 && sumaTotal.value < total.value ) {
        item.importe = total.value - sumaTotal.value;
    }

    actualizarSumaTotal();    
};


const registrarPago = (movimiento) => {
    console.log("registrar pago");
    registrarPagoDialog.value = true;
    movimientoPagar.value = movimiento;
    //total.value = movimiento.venta.montoTotal;
    console.log(movimiento);
    total.value = movimiento.monto;
    /*if (movimiento.venta != null) {
        total.value = movimiento.venta.montoTotal;
    } */
    if (movimiento.venta != null && movimiento.venta.pedido != null) {
        getAnticiposByIdPedidoVenta(movimiento.venta.pedido.id);
    } 

    if(movimiento.compra != null && movimiento.compra.pedido != null ){
        getPagosByIdPedidoCompra(movimiento.compra.pedido.id);
    }
};

const registrarCobro = (movimiento) => {
    console.log("registrar pago");
    registrarCobroDialog.value = true;
    movimientoCobrar.value = movimiento;
    //total.value = movimiento.venta.montoTotal;
    console.log(movimiento);
    total.value = movimiento.monto;
    /*if (movimiento.venta != null) {
        total.value = movimiento.venta.montoTotal;
    } */
    if (movimiento.venta != null && movimiento.venta.pedido != null) {
        getAnticiposByIdPedidoVenta(movimiento.venta.pedido.id);
    } 

};

const getAnticiposByIdPedidoVenta = (idPedido) => {
console.log("getAnticipos");
AnticipoServices.getAnticiposByIdPedidoVenta(idPedido).then((data) => {
    anticiposAplicar.value = data.data;
    console.log(data.data);
    if (anticiposAplicar.value.length > 0) {
        anticiposAsociados.value = true;
    }   

});
};

const getPagosByIdPedidoCompra = (id) => {
console.log("getCompra");
PagoPedidoCompraServices.findPagosAplicarByIdPedidoCompra(id).then((data) => {
    pagosAplicar.value = data.data;
    console.log(pagosAplicar.value);
    if (pagosAplicar.value.length > 0) {
        pagosAsociados.value = true;
    }   

});
};


const habilitarSubmit = () =>{
    if (total.value > 0 && sumaTotal.value == total.value) {
    disabledSubmit.value = false;
  }else{
    disabledSubmit.value = true;
  }
}

const getColor = (tipo) => {
  
  
  switch (tipo) {
       case 'I':
           return 'color: rgb(34, 177, 76);';

       case 'E':
           return 'color: red;';

       default:
           return null;
   }
};

const puedeEliminarseEnCaja = (concepto) =>{
    switch (concepto) {
        
        case 'Anticipo cliente':
            return false;
            break;
        case 'Anticipo proveedor':
            return false;
            break;
        case 'Reembolso cliente':
            return false;
            break;
        case 'Reembolso proveedor':
            return false;
            break;
   
        default:
            return true;
            break;
    }

}

const addRow = () => {
    pagos.value.push({medio:null , importe: 0, disabled: true});
};

const eliminarRow = (index) => {
    if (index > 0) {
        pagos.value.splice(index,1);
    }
    actualizarSumaTotal();
};



const getFormasPago = () =>{
    MedioPagoServices.obtenerMediosPago().then((data) => {
        formasPago.value=data.data;
        //selectedFormaPago.value=data.data[1];
        console.log(formasPago.value);
        pago.value.medio = null;
        pago.value.importe = 0;
        pago.value.disabled = true;
        pagos.value.push(pago.value);

    });
}


const nuevoMovimiento = () =>{
    MedioPagoServices.obtenerMediosPago().then((data) => {
        formasPago.value = data.data;
        //console.log(reembolsos.value);
    });

    /*ConceptoServices.obtenerConceptosByTipo('I').then((data) => {
        conceptos.value = data.data;
        console.log('conceptos');
        console.log(conceptos.value);
    });*/

    visible.value = true;
}

const getConceptos = (tipo) =>{
    /*let tipoChar;
    if(tipo == 'Ingreso'){
        tipoChar = 'I';
    }else{
        tipoChar = 'E';
    }
    console.log(tipoChar);
    ConceptoServices.obtenerConceptosByTipo(tipoChar).then((data) => {
        conceptos.value = data.data;
        console.log('conceptos');
        console.log(conceptos.value);
    });*/

}

async function getMovimientos() {
    CajaServices.obtenerMovimientosByCaja(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        movimientos.value = data.data;
        
        

});
}

async function getMovimientosPendientesDePago() {
    console.log("getMovPagos");
    CajaServices.obtenerMovimientosPendientesDePago().then((data) => {
        console.log(data.data);
        pendientesPago.value = data.data;
   

});
}

async function getMovimientosPendientesDeCobro() {
    console.log("getMovCobros");
    CajaServices.obtenerMovimientosPendientesDeCobro().then((data) => {
        console.log(data.data);
        pendientesCobro.value = data.data;
   

});
}

async function getCaja() {
    CajaServices.obtenerCajaById(router.currentRoute.value.params.id).then((data) => {
        caja.value = data.data;
        if (caja.value.estado == 'A') {
            cajaAbierta.value = true;
        }

});
}

const actualizarImporteAnticipo = (event,i) =>{
    
    if (event.value <= anticipos.value[i].saldo) {
        anticipos.value[i].importe = event.value|| 0;
    }else{
        anticipos.value[i].importe = anticipos.value[i].saldo;
    }
    actualizarSumaTotal();
    

}

const obtenerAnticiposAplicados = () =>{
    
    anticipos.value.forEach((a) => {
        if (a.seleccionado && a.importe > 0) {
            pagos.value.push({anticipo: a, importe: a.importe});
        }
    });
    
    

}


</script>
<style>

.p-inputnumber-button-group{
 padding: 0rem !important;
 width: 1rem;
}

.p-inputnumber-button{
 padding: 0rem !important;
 width: 1rem;
}

a, .green {
    padding: 6px;
}

.p-message-wrapper{
    padding: 3px;
}

.p-message-close {
    margin-left: 30%;
}

.p-dialog-content {
    padding-top: 0px !important;
}
</style>