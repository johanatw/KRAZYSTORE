<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import { AnticipoServices } from '@/services/AnticipoServices';
import { CajaServices } from '@/services/CajaServices';
import Panel from 'primevue/panel';
import router from '@/router';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Card from 'primevue/card';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import { PagoServices } from '@/services/PagoServices';
import Toast from 'primevue/toast';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import InputGroup from 'primevue/inputgroup';
import {formatearNumero, existeCajaAbierta} from '@/utils/utils';


const visible = ref(false);
const  formasPago = ref();
const disabled = ref(true);
const disabledSubmit = ref(true);
const pedido = ref();
const anticipos= ref([]);
const montoReembolsar = ref(0);
const motivo = ref(null);
const confirm = useConfirm();
const toast = useToast();
const pagosAnticipo =ref([ ]);
const pagos =ref([ ]);
const pago =ref({});
const searched =ref(false);
const abonado = ref(0);
const dialogRegistrar = ref(false);
const disabledSubmitRegistrar = ref(true);
const anticipoToDelete = ref();
const color = ref('#4b5563');
const id = ref();
const visibleDeleteDialog = ref(false);
const anticipo = ref();
const cajaAbierta = ref({});


//Funciones Registrar Anticipo
const buscarPedido= (id) => {
  
    CajaServices.obtenerPagosPedido(id).then((data) => {
        pedido.value = data.data;
        searched.value = true;
        console.log(data.data);
    });

}

const addRowAnticipo = () => {
    pagosAnticipo.value.push({formaPago:null , importe: 0});
};

const eliminarRowAnticipo = (index) => {
    if (index > 0) {
        pagosAnticipo.value.splice(index,1);
    }
    calcularAbonadoAnticipo();
};

const habilitarInputAnticipo = (index, item) => {
    let inp = document.getElementsByName("input")[index].firstElementChild;
    if (inp.disabled  ) {
        inp.disabled = false;
    }

    calcularAbonadoAnticipo();
    
};

const calcularAbonadoAnticipo = (event) => {
    if (event !== undefined) {
        console.log(event);
        let valorActual = Number(event.value);
        let valorAnterior = Number(event.formattedValue.replaceAll(".",""));
        abonado.value = abonado.value - valorAnterior + valorActual;
    }else {
        let monto = 0 ;
        pagos.value.forEach(element => {
            monto += element.importe;
        })
        abonado.value = monto;
    }
    calcularDiferenciaRegistrar();
  
};

const habilitarSubmitRegistrar = () =>{
    if (abonado.value > 0 && pedido.value && abonado.value <= (pedido.value.total - pedido.value.totalPagos)) {
        disabledSubmitRegistrar.value = false;
        color.value = '#4b5563';
            
    }else{
        disabledSubmitRegistrar.value = true;
        color.value = 'red';
     
    }
}

const registrarAnticipo = () =>{
    inicializarPagosAnticipo();
    dialogRegistrar.value = true;
    
};

const registradoEnCajaActualAbierta = (fechaRegistro) =>{
    console.log("registradoEnCajaActualAbierta");
    if (cajaAbierta.value != null && fechaRegistro >= cajaAbierta.value.fecha) {
        return false;
    } else {
        return true;
    }
};

async function guardarAnticipo() {
    const d = await existeCajaAbierta();
    console.log(d);
    if (d) {
        let fechaAnticipo = new Date();
    let ant = {total: abonado.value, fecha: fechaAnticipo, pedido: pedido.value};

    let anticipoCreationDTO = {anticipo: ant, pagos: pagosAnticipo.value};
    CajaServices.saveAnticipo(anticipoCreationDTO ).then((data)=> {
        getAnticipos();
        closeDialogRegistrar();
        toast.add({ severity:"success", detail: 'Anticipo registrado', life: 3000 });
    } );
    } else {
        toast.add({ severity:"error", detail: 'No existe una caja abierta', life: 3000 });
    }
};



const calcularDiferenciaRegistrar = () => {
    habilitarSubmitRegistrar();
};

const closeDialogRegistrar = (event) =>{
    if (event == false) {
        reiniciarDialogRegistrar();
    }

    if (event == null) {
        dialogRegistrar.value = false;
        reiniciarDialogRegistrar();
    }
    
}

const reiniciarDialogRegistrar = () =>{
    pedido.value = null;
    pagosAnticipo.value =[];
    color.value = '#4b5563';
    abonado.value = 0;
    disabled.value = true;
    searched.value = false;
    id.value = null;

}


const inicializarPagosAnticipo = () => {
    pago.value.formaPago = null;
    pago.value.importe = 0;
    pagosAnticipo.value.push(pago.value);

};





//Funciones Registrar Reembolso

const addRow = () => {
    pagos.value.push({formaPago:null , importe: 0});
};

const eliminarRow = (index) => {
    if (index > 0) {
        pagos.value.splice(index,1);
    }
    calcularAbonado();
};

const habilitarInput = (index, item) => {
    let inp = document.getElementsByName("input")[index].firstElementChild;
    if (inp.disabled  ) {
        inp.disabled = false;
    }
   
    if (abonado.value < anticipo.value.saldo ) {
        item.importe = anticipo.value.saldo - abonado.value;
    }
    calcularAbonado();
    
};

const calcularAbonado = (event) => {
    if (event !== undefined) {
        console.log(event);
        let valorActual = Number(event.value);
        let valorAnterior = Number(event.formattedValue.replaceAll(".",""));
        abonado.value = abonado.value - valorAnterior + valorActual;
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
    habilitarSubmit();
};

const habilitarSubmit = () =>{
    if (abonado.value > anticipo.value.saldo || abonado.value<1) {
        disabledSubmit.value = true;
        color.value = 'red';
    }else{
        disabledSubmit.value = false;
        color.value = '#4b5563';
    }
}

const showDialogReembolso = (ant) =>{

    anticipo.value = ant;
        
    montoReembolsar.value = ant.saldo;
    inicializarPagosReembolso();
    
    visible.value = true;
}

const inicializarPagosReembolso = () => {
    pago.value.formaPago = null;
    pago.value.importe = 0;
    pagos.value.push(pago.value);

}; 
  
const closeDialog = (event) =>{
    if (event == false) {
        reiniciarDialog();
    }

    if (event == null) {
        visible.value = false;
        reiniciarDialog();
    }
      
}
  
const reiniciarDialog = () =>{
    pagos.value =[];
    color.value = '#4b5563';
    motivo.value = null;
    abonado.value = 0;
    disabled.value = true;
  
}
  
  
const guardarReembolso = () =>{
    if (cajaAbierta.value != null) {
        montoReembolsar.value = abonado.value;
        let fecha = new Date();
        let reembolso = {monto: montoReembolsar.value,
        fecha: fecha,
        motivo: motivo.value,
        anticipo: anticipo.value};
        
        let reembolsoCreationDTO = {reembolso: reembolso, pagos: pagos.value};
        CajaServices.saveReembolso(reembolsoCreationDTO).then((data)=> {
            
            getAnticipos();
            closeDialog();
            toast.add({ severity:"success", detail: 'Reembolso registrado', life: 3000 });
        });
    } else {
        toast.add({ severity:"error", detail: 'No existe una caja abierta', life: 3000 });
    }
    
};



//Funciones Anticipos

async function getAnticipos() {
  await AnticipoServices.obtenerAnticipos().then((data) => {
       anticipos.value = data.data;
   });
 
}
const confirm2 = (id) => {
   
   confirm.require({
       message: 'Eliminar el anticipo '+ id + '?',
       header: 'Confirmacion',
       icon: 'pi pi-info-circle',
       rejectLabel: 'Cancelar',
       acceptLabel: 'Eliminar',
       rejectClass: 'p-button-secondary p-button-outlined',
       acceptClass: 'p-button-danger',
       accept: () => {
           verificarEstadoAnticipo(id);
       },
       
   });
};

const messageError = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'errorAnticipoUtilizado',
        header: 'Ocurrio un error',
        message: msg,

        accept: () => {
            //getDetalle();
           
            
            
        },
    });
};

const deleteAnticipoReembolsos = () =>{
    const cantidad= 1;
    const index = anticipos.value.findIndex((loopVariable) => loopVariable.id === anticipoToDelete.value);

    CajaServices.deleteAnticipoReembolsos(anticipoToDelete.value).then((data)=> {

        anticipos.value.splice(index,cantidad);
        visibleDeleteDialog.value= false;
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
        
    } );
    
};

const deleteAnticipo = (id) =>{
    const cantidad= 1;
    const index = anticipos.value.findIndex((loopVariable) => loopVariable.id === id);
    anticipoToDelete.value = id;
    CajaServices.deleteAnticipo(id).then((response)=>{

        anticipos.value.splice(index,cantidad);
        toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });

    })

}

const verificarEstadoAnticipo = (id) =>{
    const index = anticipos.value.findIndex((loopVariable) => loopVariable.id === id);
    
    if (anticipos.value[index].utilizado > 0) {
      
        messageError("Este anticipo ya fue utilizado");
    } else if (anticipos.value[index].reembolsado > 0) {
        visibleDeleteDialog.value = true;
        anticipoToDelete.value = id;
    } else {
        deleteAnticipo(id);
      }
    
   
}

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});





//Otros

onMounted(() => {
    getAnticipos();
    getFormasPago();
    getCajaAbierta();
});


const getCajaAbierta= () => {
    CajaServices.getCajaAbierta().then((data) => {
        cajaAbierta.value=data.data;
        console.log(cajaAbierta.value);
    });
};



const getFormasPago= () => {
    FormasPagoServices.obtenerFormasPago().then((data) => {
        formasPago.value=data.data;
    });
};


</script>

<style>
.p-card-title {
    font-size: medium;
}
</style>

<template>

    <div class="card flex p-fluid justify-content-center " >

        <div class="card flex p-fluid justify-content-center " >

            <!--Dialog Eliminar Anticipo-->
            <ConfirmDialog ></ConfirmDialog>
            <Toast />

            <ConfirmDialog group="errorAnticipoUtilizado">
        <template #container="{ message, acceptCallback }">
            <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
                <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-6rem w-6rem -mt-8">
                    <i class="pi pi-times text-4xl"></i>
                </div>
                <span class="font-bold text-2xl block mb-2 mt-4">{{ message.header }}</span>
                <p class="mb-0">{{ message.message }}</p>
                <div class="flex align-items-center gap-2 mt-4">
                    <Button label="Ok" @click="acceptCallback"></Button>
                </div>
            </div>
        </template>
    </ConfirmDialog>

            <!--Modal Aviso Reembolsos Asociados-->
            <Dialog v-model:visible="visibleDeleteDialog" modal  header="Edit Profile" :style="{ width: '50rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
                <template #header>
                    <div class="flex align-items-center gap-2">
                        <h3 class="font-bold">Este anticipo tiene reembolsos asociados</h3>
                    </div>
                </template>
               
                <div class="flex align-items-center mb-3">
                    Si continúas, también serán eliminados. ¿Deseas proceder?"
                </div> 
                        
                <template #footer>
                    <div class="card flex" style="justify-content: end;">  
                        <Button label="Aceptar" style="margin-right: 1%;" @click="deleteAnticipoReembolsos()" />
                        <Button  label="Cancelar"  @click="visibleDeleteDialog=false"  />
                    </div>
                </template>
            </Dialog>


            <!--Modal Reembolsar Anticipo-->
            <Dialog v-model:visible="visible" modal @update:visible="closeDialog($event)" header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
                <template #header>
                    <div class="flex align-items-center gap-2">
                        <h3 class="font-bold">Reembolsar Anticipo</h3>
                    </div>
                </template>
    
                <div class="contenedor" >      
                    <div class="formgrid grid" >  

             
                        <div class="field col-12 md:col-12">
                            <div class="field col-12 md:col-12">
                                <Card style="height: 100%;" >
                                    <template #title> Detalle del Anticipo </template>
                                    <template #content>
                                        <div >
                                            <label for="fecha" style="font-weight: 600;" >Fecha:</label>
                                            {{ formatearNumero(anticipo.fecha) }}<br>
                                            <label for="fecha" style="font-weight: 600;" >Monto total Anticipo:</label>
                                            {{ formatearNumero(anticipo.total) }} Gs.<br>
                                            <label for="fecha" style="font-weight: 600;"  >Monto utilizado:</label>
                                            {{ formatearNumero(anticipo.utilizado) }} Gs.<br>
                                            <label for="fecha" style="font-weight: 600;"  >Monto reembolsado:</label>
                                            {{ formatearNumero(anticipo.reembolsado) }} Gs.<br>
                                            <div style="color: green;">
                                                <label for="fecha" style="font-weight: 600;" >Monto disponible:</label>
                                                {{ formatearNumero(anticipo.saldo) }} Gs.<br>
                                            </div>
                                        </div> 
                                    </template>
                                </Card>
                            </div>
                
                            <div class="field col-12 md:col-12">
                                <Card style="height: 100%;" >
                                    <template #title> Reembolsar</template>
                                    <template #content>
                                        <div>
                                            <div class="formgrid grid" v-for="item, index in pagos" :key="item" >
                                                <div class="field col-12 md:col-6" style="justify-content: start;  ">
                                                    <Dropdown class="flex flex-grow-1" style="padding: 0rem !important;" v-model="item.formaPago" :options="formasPago" @change="habilitarInput(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   /> 
                                                </div>
                                    
                                                <div class="flex field col-12 md:col-5" style=" justify-content: start; " >
                                                    <InputNumber class="p-fluid " disabled=true name="input" style="padding: 0rem !important;  " v-model="item.importe" @input="calcularAbonado($event)"  />
                                                </div>
                                                <div v-if="index > 0" class=" field col-12 md:col-1" style=" justify-content: flex-end;">
                                                    <Button style="background: none !important; border: none !important;  " icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminarRow(index)" />
                                                </div>
                                                <div v-else class=" field col-12 md:col-1" style=" justify-content: flex-end;">
                                                    <Button style="background: none !important; border: none !important " icon="pi pi-plus" severity="danger" text rounded aria-label="Cancel" @click="addRow()" />
                                                </div>
                                            </div>

                                            <div class="formgrid grid">
                                                <div class="flex field col-12 md:col-6 " >
                                                    <label for="totalPagos" style="font-weight: 600;"> Monto a reembolsar: </label>
                                                </div>
                                                <div  class="flex field col-12 md:col-6" style=" justify-content: start; " :style="{color: color}" >
                                                    {{ abonado.toLocaleString("de-DE") }} Gs.
                                                </div>
                                    
                                                <div class="flex field col-12 md:col-6 " >
                                                    <label for="totalPagos" style="font-weight: 600;"> Motivo del reembolso: </label>
                                                </div>
                                                <div  class="flex field col-12 md:col-6" style=" justify-content: start; " :style="{color: color}" >
                                                    <span class="p-inputnumber p-inputwrapper p-inputwrapper-filled p-fluid"  name="input" style="padding: 0rem !important;">
                                                        <input v-model="motivo" class="p-inputtext p-component p-inputnumber-input"  >
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </template>
                                </Card>
                            </div>  
                        </div>   
                    </div>     
                </div>
                <template #footer>
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                        <Button  label="Guardar" :disabled="disabledSubmit" @click="guardarReembolso()" />
                    </div>
                </template>
            </Dialog>
        </div>

        <!--Modal Resgistrar Nuevo Anticipo-->
        <Dialog v-model:visible="dialogRegistrar" @update:visible="closeDialogRegistrar($event)" modal header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Registrar Anticipo</h3>
                </div>
            </template>
    
            <div class="contenedor" >      
                <div class="formgrid grid" >  
                    <div class="field col-12 md:col-12">
                        <div class="field col-12 md:col-12">
                            <Card style="height: 100%;" >
                                <template #title> Detalle del Pedido </template>
                                <template #content>
                                    <div class="formgrid grid">
                                        <div class="flex field col-12 md:col-6 "style="height: 1.5rem;" >
                                            <label for="email" class="w-9rem" style="font-weight: 600;" >Pedido N°</label>
                                        </div>
                                        <div class="flex field col-12 md:col-6 " style="height: 1.5rem;">
                                            <InputGroup>
                                                <InputText v-model="id" placeholder="N° Pedido" />
                                                <Button icon="pi pi-search"  @click="buscarPedido(id)" />
                                            </InputGroup>
                                        </div>
                                        <div class="flex field col-12 md:col-6" style="height: 1.5rem;">
                                            <label   style="font-weight: 600;" >Total Pedido:</label>
                                        </div>
                                        <div class="flex field col-12 md:col-6 "style="height: 1.5rem;" >
                                            <label v-if="pedido"  >{{formatearNumero(pedido.total)}} Gs.</label>
                                            <label v-else   > 0 Gs.</label>
                                        </div>
                                        <div class="flex field col-12 md:col-6 " style="height: 1.5rem;">
                                            <label  style="font-weight: 600;" >Total pagos:</label>
                                        </div>
                                        <div class="flex field col-12 md:col-6 " style="height: 1.5rem;">
                                            <label v-if="pedido"   >{{formatearNumero(pedido.totalPagos)}} Gs.</label>
                                            <label v-else  > 0 Gs.</label>
                                        </div>
                                        <div class="flex field col-12 md:col-6 " style="height: 1.5rem;">
                                            <label style="font-weight: 600;" >Restante:</label>
                                        </div>
                                        <div class="flex field col-12 md:col-6 " style="height: 1.5rem;">
                                            <label v-if="pedido"  >{{ formatearNumero(pedido.total - pedido.totalPagos)}} Gs.</label>
                                            <label v-else > 0 Gs.</label>
                                        </div>
                                    </div>
                                    <div >
                                        <small class="p-error" v-if="searched && !pedido">No existe pedido.</small>
                                    </div>
                                </template>
                            </Card>
                        </div>
                
                        <div class="field col-12 md:col-12">
                            <Card style="height: 100%;" >
                                <template #title> Registrar Pago</template>
                                <template #content>
                                    <div>
                                        <div class="formgrid grid" v-for="item, index in pagosAnticipo" :key="item" >
                                            <div class="field col-12 md:col-6" style="justify-content: start;  ">
                                                <Dropdown class="flex flex-grow-1" style="padding: 0rem !important;" v-model="item.formaPago" :options="formasPago" @change="habilitarInputAnticipo(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   /> 
                                            </div>
                                            <div class="flex field col-12 md:col-5" style=" justify-content: start; " >
                                                <InputNumber class="p-fluid " disabled=true name="input" style="padding: 0rem !important;  " v-model="item.importe" @input="calcularAbonadoAnticipo($event)"  />
                                            </div>
                                            <div v-if="index > 0" class=" field col-12 md:col-1" style=" justify-content: flex-end;">
                                                <Button style="background: none !important; border: none !important;  " icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminarRowAnticipo(index)" />
                                            </div>
                                            <div v-else class=" field col-12 md:col-1" style=" justify-content: flex-end;">
                                                <Button style="background: none !important; border: none !important " icon="pi pi-plus" severity="danger" text rounded aria-label="Cancel" @click="addRowAnticipo()" />
                                            </div>
                                        </div>

                                        <div class="formgrid grid">
                                            <div class="flex field col-12 md:col-6 " >
                                                <label for="totalPagos" style="font-weight: 600;"> Monto a pagar: </label>
                                            </div>
                                            <div  class="flex field col-12 md:col-6" style=" justify-content: start; " :style="{color: color}" >
                                                {{ abonado.toLocaleString("de-DE") }} Gs.
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </Card>
                        </div>  
                    </div>   
                </div>     
            </div>
            <template #footer>
                <div class="card flex" style="justify-content: end;">  
                    <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialogRegistrar()" />
                    <Button  label="Guardar" :disabled="disabledSubmitRegistrar" @click="guardarAnticipo()" />
                </div>
            </template>
        </Dialog>


        <!--Pantalla Principal Lista de Anticipos-->
        <Panel style=" position: relative; width: 100%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Anticipos</h3>
                </div>
            </template>
      
            <template #icons>

                <div class="flex align-items-center">
                    <Button  icon="pi pi-plus " @click="registrarAnticipo()" style=" width: 3rem !important; height: 2.9rem;" />
                    <span class="p-input-icon-left" style="margin-left: 1%;">
                    <i class="pi pi-search" style="top: 35%;"/>
                    <InputText style="padding: 12px !important; padding-left: 40px !important;" class="buscador p-fluid" v-model="filters['global'].value" placeholder="Buscar..."  />
                    </span>

                </div>
            </template>
            
      
            <div >
                <DataTable  :value="anticipos " scrollHeight="400px"  
                :paginator="true" :rows="7" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                    <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
                    <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                        <template #body="slotProps">
                            {{ formatearNumero(slotProps.data.fecha) }}
                        </template>
                    </Column>
                    <Column field="pedido.id" header="Pedido N°" aria-sort="ascending" sortable></Column>
                    <Column  field="total" header="Total" aria-sort="ascending" sortable >
                        <template #body="slotProps">
                            {{ formatearNumero(slotProps.data.total) }}
                        </template>
                    </Column>
                    <Column  field="utilizado" header="Utilizado" aria-sort="ascending" sortable >
                        <template #body="slotProps">
                            {{ formatearNumero(slotProps.data.utilizado) }}
                        </template>
                    </Column>
                    <Column  field="reembolsado" header="Reembolsado" aria-sort="ascending" sortable >
                        <template #body="slotProps">
                            {{ formatearNumero(slotProps.data.reembolsado) }}
                        </template>
                    </Column>
                    <Column  field="saldo" header="Saldo Disponible" aria-sort="ascending" sortable >
                        <template #body="slotProps">
                            {{ formatearNumero(slotProps.data.saldo) }}
                        </template>
                    </Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-sync" severity="success" text rounded aria-label="Cancel" @click="showDialogReembolso(slotProps.data)"  style="height: 2rem !important; width: 2rem !important;" />
                            <Button :disabled="registradoEnCajaActualAbierta(slotProps.data.fecha)"  icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                            
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
  
</template>