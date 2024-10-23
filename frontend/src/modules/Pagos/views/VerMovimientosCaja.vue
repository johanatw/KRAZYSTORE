
<template>
    <div class="card flex p-fluid justify-content-center " >
        
        <Toast />
        <ConfirmDialog ></ConfirmDialog>
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
                        <Dropdown v-model:model-value="movimiento.tipo" :options="options" @update:model-value="getConceptos(movimiento.tipo)" placeholder="Seleccione un elemento" checkmark :highlightOnSelect="false" class="w-full md:w-14rem"  />
                    </div>
                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="username" class="font-semibold w-9rem">Concepto</label>
                        <Dropdown v-model:model-value="movimiento.concepto" :options="conceptos" optionLabel="descripcion" placeholder="Seleccione un elemento" checkmark :highlightOnSelect="false" class="w-full md:w-14rem"  />
                    </div>

                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="email" class="font-semibold w-9rem">Monto</label>
                        <InputNumber v-model:model-value="movimiento.monto" inputId="integeronly" @input="validarMonto($event)" />
                    </div>
                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="email" class="font-semibold w-9rem">Medio de pago</label>
                        <Dropdown v-model:model-value="movimiento.formaPago" :options="formasPago" optionLabel="descripcion"  placeholder="Seleccione un elemento" checkmark :highlightOnSelect="false" class="w-full md:w-14rem" />
                    </div>
                    <div class="flex align-items-center gap-3 mb-2">
                        <label for="email" class="font-semibold w-9rem">Documento</label>
                        <InputText v-model:model-value="movimiento.documento" id="email" class="flex-auto" autocomplete="off"  />
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

        <!--Registrar pago/cobro de facturas pendientes-->

        <Dialog v-model:visible="registrarPagoDialog" modal @update:visible="closeDialog($event)" header="Edit Profile" :style="{ width: '40rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Registrar Pago </h3>
                </div>
            </template>

            <div >
                <Card style="height: 100%;" >

                    <template #content>
                        TOTAL FACTURA: {{ total }}
                        <div>
                            <div class="formgrid grid" v-for="item, index in pagos" :key="item" >
                                        
                                <div class="field col-5 md:col-5 p-fluid" style="justify-content: start;  ">
                                    <Dropdown style="padding: 0rem !important;" v-model="item.formaPago" :options="formasPago" @change="habilitarInput(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   />
                                </div>
                                <div  class="field col-5 md:col-5 p-fluid" style=" justify-content: start; " >
                                    <InputNumber disabled=true name="input" style="padding: 0rem !important; height: 100%;" v-model="item.importe" @input="calcularAbonado($event)"  />
                                </div>
                                <div v-if="index == 0" class=" field col-1 md:col-1 p-fluid" style=" justify-content: flex-end">
                                    <Button style="background: none !important; border: none !important " icon="pi pi-plus" severity="danger" text rounded aria-label="Cancel" @click="addRow()" />
                                </div>
                                <div v-else class=" field col-1 md:col-1 p-fluid">
                                    <Button style="background: none !important; border: none !important " icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminarRow(index)" />
                                </div>
                            </div>

                            <div class="formgrid grid">
                                <div class="flex field col-12 md:col-12" >     
                                    <div class="flex field col-9 md:col-9 justify-content-start" >
                                        <label for="totalPagos"> Total abonado: </label>
                                    </div>
                                    <div class="flex field col-3 md:col-3" style=" justify-content: center; " >
                                        {{ abonado.toLocaleString("de-DE") }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </template>
                </Card>
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                    <Button  label="Guardar" :disabled="disabledSubmit" @click="guardarPagosMovimiento()" />
                </div>
            </template>
        </Dialog>
        
        <!--Pantalla principal ver movimientos de caja-->
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Caja N° {{ caja.id }}</h3>
                </div>
            </template>

            <template #icons>
                <div v-if="cajaAbierta" class="flex align-items-center">
                    <Button  icon="pi pi-plus " @click="nuevoMovimiento" style=" width: 3rem !important; height: 2.2rem; margin-right: 1%; " />
                    <Button  label="Cerrar caja" @click="cerrarCaja()" />
                </div>
            </template>

            <div class="contenedor" style="padding-left: 4%; padding-right: 4%;">
                <div class="card">

                    <TabView>
                        <!--Lista de movimientos de caja-->
                        <TabPanel header="Movimientos">
                            <DataTable  :value="movimientos" scrollHeight="400px"  
                            :paginator="true" :rows="7" 
                            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                            currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
        
                                <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                                    <template #body="slotProps">
                                        {{ formatearNumero(slotProps.data.fecha) }}
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
                                <Column v-if="cajaAbierta" :exportable="false">
                                    <template #body="slotProps">
                                        <Button :disabled="!puedeEliminarseEnCaja(slotProps.data.concepto)" icon="pi pi-times" severity="danger" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="confirm2(slotProps.data.id)"  />
                                    </template>
                                </Column>
                            </DataTable>

                        </TabPanel>

                        <!--Facturas pendientes de pago/cobro-->
                        <TabPanel v-if="cajaAbierta"  header="Pendientes">
                            <DataTable  :value="pendientes" scrollHeight="400px"  
                            :paginator="true" :rows="7" 
                            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                            currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                                <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                                    <template #body="slotProps">
                                        {{ formatearNumero(slotProps.data.fecha) }}
                                    </template>
                                </Column>
                                <Column field="concepto.descripcion"  header="Concepto" aria-sort="ascending" sortable></Column>
                                <Column field="monto"  header="Monto" aria-sort="ascending" sortable>    
                                    <template #body="slotProps">
                                            {{ formatearNumero(slotProps.data.monto) }}
                                    </template>        
                                </Column>
                                <Column field="venta.nroFactura"  header="N° Factura" aria-sort="ascending" sortable></Column>
                                <Column  :exportable="false">
                                    <template #body="slotProps">
                                        <Button  icon="pi pi-money-bill" severity="danger" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" @click="registrarPago(slotProps.data)"   />
                                    </template>
                                </Column>
                            </DataTable>
                        </TabPanel>

                    </TabView>
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
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import Dialog from 'primevue/dialog';
import { ConceptoServices } from '@/services/ConceptoServices';
import Panel from 'primevue/panel';
import TabView from 'primevue/tabview';
import TabPanel from 'primevue/tabpanel';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import {formatearNumero, existeCajaAbierta} from '@/utils/utils';
import {CajaServices} from '@/services/CajaServices';
import router from '@/router';
import ConfirmDialog from 'primevue/confirmdialog';
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import { useConfirm } from "primevue/useconfirm";

const confirm = useConfirm();   
const toast = useToast();
const pendientes = ref([]);
const cajaAbierta = ref(false);
const caja = ref({});
const registrarPagoDialog = ref(false);
const pagos =ref([ ]);
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

const disabledSubmit = ref(false);
const visible = ref(false);
const movimiento = ref({});
const movimientoPagar = ref({});
const movimientos = ref([]);
const options = ref(['Ingreso', 'Egreso']);
const conceptos = ref();


onMounted(() => {
    getCaja();
    getMovimientos();
    getFacturasPendientes();
    inicializarCampos();
   

});

const inicializarCampos = () => {
    movimiento.value.tipo = 'Ingreso';
    movimiento.value.concepto = {id: 4, descripcion: 'Otros', tipo: 'I'};
    movimiento.value.formaPago = {id: 2, descripcion: 'Efectivo'};
    disabledSubmit.value = true;
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
                formaPago: movimiento.value.formaPago
                }
    let pagos = [pago];
    let movimientoCreationDTO = {movimiento: movimiento.value, pago: pagos};
    
    CajaServices.saveMovimiento(movimientoCreationDTO).then((data) => {
        toast.add({ severity:"success", detail: movimiento.value.tipo + ' Registrado', life: 3000 });
        getMovimientos();
        closeDialog();
        
        console.log("ok");
    });
}

const guardarPagosMovimiento = () =>{
    let fechaMovimiento = new Date();
    movimientoPagar.value.fecha = fechaMovimiento;
    movimientoPagar.value.caja = caja.value;
    /*let pago = {importe: movimientoPagar.value.monto,
                formaPago: movimiento.value.formaPago
          }
    let pagos = [pago];*/
    let movimientoCreationDTO = {movimiento: movimientoPagar.value, pago: pagos.value};
    
    CajaServices.savePagosMovimiento(movimientoCreationDTO).then((data) => {
        toast.add({ severity:"success", detail: movimiento.value.tipo + ' Registrado', life: 3000 });
        getMovimientos();
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
    let inp = document.getElementsByName("input")[index].firstElementChild;
   if (inp.disabled  ) {
        inp.disabled = false;
    }
   
    if (item.importe < 1 && abonado.value < total.value ) {
        item.importe = total.value - abonado.value;
    }

    if(item.formaPago.descripcion != 'Anticipo'){
        calcularAbonado();
    }
    
    
};

const registrarPago = (movimiento) => {
    registrarPagoDialog.value = true;
    movimientoPagar.value = movimiento;
    //total.value = movimiento.venta.montoTotal;
    console.log(movimiento);
    if (movimiento.venta != null) {
        total.value = movimiento.venta.montoTotal;
    } 
    if (movimiento.venta != null && movimiento.venta.pedido != null) {
        
        getAnticipos(movimiento.venta.pedido.id);
    } else {
        getFormasPagoSinAnticipo();
    }
};

const getAnticipos = (idPedido) => {

AnticipoServices.getAnticipos(idPedido).then((data) => {
    anticipos.value = data.data;
    console.log(formasPago.value);
    if (anticipos.value.length > 0) {
        getFormasPago();
    }else{
        getFormasPagoSinAnticipo();
    }
    

});
};


const calcularAbonado = (event,max=null) => {

    if (event !== undefined) {
        console.log(event);
        let valorActual = Number(event.value);
        let valorAnterior = Number(event.formattedValue.replaceAll(".",""));
        if ((max != null && valorActual < max ) || max==null) {
            console.log("entra max");
            console.log(max);
            console.log(valorActual);
            console.log(max != null && (valorActual < max) );
            abonado.value = abonado.value - valorAnterior + valorActual;
        }
       
        
    }else {
        let monto = 0 ;
        pagos.value.forEach(element => {
            monto += element.importe;
        })
        abonado.value = monto;
    }
    
    calcularDiferencia();
  
};

const calcularDiferencia = () => {
    console.log('antesdetalleanti');
  
  faltante.value = total.value - abonado.value;
  cambio.value = abonado.value - total.value; 
  habilitarSubmit();
};

const habilitarSubmit = () =>{
    if (total.value > 0 && abonado.value == total.value) {
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
        case 'Venta':
            return false;
            break;
        case 'Compra':
            return false;
            break;
        case 'Anticipo':
            return false;
            break;
        case 'Reembolso':
            return false;
            break;
        case 'Anulación de Factura':
            return false;
            break;
    
        default:
            return true;
            break;
    }

}

const addRow = () => {
    pagos.value.push({formaPago:null , importe: 0});
};

const eliminarRow = (index) => {
    if (index > 0) {
        pagos.value.splice(index,1);
    }
    calcularAbonado();
};



const getFormasPago = () =>{
    FormasPagoServices.obtenerFormasPago().then((data) => {
        formasPago.value=data.data;
        //selectedFormaPago.value=data.data[1];
        console.log(formasPago.value);
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pago.value.anticipo = null;
        pagos.value.push(pago.value);

    });
}

const getFormasPagoSinAnticipo = () =>{
    FormasPagoServices.obtenerFormasPagoSinAnticipo().then((data) => {
        formasPago.value=data.data;
        //selectedFormaPago.value=data.data[1];
        //console.log(formasPago.value);
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pago.value.anticipo = null;
        pagos.value.push(pago.value);

    });
}



const nuevoMovimiento = () =>{
  FormasPagoServices.obtenerFormasPagoSinAnticipo().then((data) => {
        formasPago.value = data.data;
        //console.log(reembolsos.value);
    });

    ConceptoServices.obtenerConceptosByTipo('I').then((data) => {
        conceptos.value = data.data;
        console.log('conceptos');
        console.log(conceptos.value);
    });

    visible.value = true;
}

async function getMovimientos() {
    CajaServices.obtenerMovimientosByCaja(router.currentRoute.value.params.id).then((data) => {
        console.log(data.data);
        movimientos.value = data.data;
        
        

});
}

async function getFacturasPendientes() {
    CajaServices.obtenerFacturasPendientes().then((data) => {
        console.log(data.data);
        pendientes.value = data.data;
   

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
</style>