<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from 'primevue/api';
import {PedidoServices} from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';
import { CajaServices } from '@/services/CajaServices';
import Panel from 'primevue/panel';
import router from '@/router';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Card from 'primevue/card';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
const disabled = ref(true);
const disabledSubmit = ref(true);
import { ReembolsoServices } from '@/services/ReembolsoServices';
import { PagoServices } from '@/services/PagoServices';
import Toast from 'primevue/toast';
import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import RadioButton from 'primevue/radiobutton';
const visible = ref(false);
import Listbox from 'primevue/listbox';
const  formasPago = ref();

import SplitButton from 'primevue/splitbutton';

const pedidos = ref();
const pedido = ref();
const anticipos= ref([]);
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';
const montoReembolsar = ref(0);
const motivo = ref(null);
const reembolsos =ref();
const opciones = ref([{id: 1, descripcion: 'Facturar productos disponibles en stock.'},
                    {id: 2, descripcion: 'Registrar anticipo para productos no disponibles en stock.'}]);
const confirm = useConfirm();
const toast = useToast();
const pagos =ref([ ]);
const pago =ref({});
const searched =ref(false);
const noExistePedido =ref(false);
const abonado = ref(0);
const dialogRegistrar = ref(false);
const disabledSubmitRegistrar = ref(true);
const anticipoToDelete = ref();
const color = ref('#4b5563');
const id = ref();
const visibleDeleteDialog = ref(false);
const selectedOpcion = ref();
const idPedidoSelected = ref();
const anticipo = ref();
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
           // deleteAnticipo(id);
            
        },
        
    });
};
onMounted(() => {
 
  getAnticipos();
  getFormasPago();
    
});



 async function getPedidos() {
  

   await PedidoServices.getPedidos().then((data) => {
        pedidos.value = data.data;

        console.log(pedidos.value);
    });
  
}

async function getAnticipos() {
  

  await AnticipoServices.obtenerAnticipos().then((data) => {
       anticipos.value = data.data;

       //console.log(pedidos.value);
   });
 
}

const getPedidos2= () => {
  

  PedidoServices.getPedidos().then((data) => {
       pedidos.value = data.data;

       console.log(pedidos.value);
   });
 
}

const buscarPedido= (id) => {
  
    PagoServices.obtenerPagosPedido(id).then((data) => {
       pedido.value = data.data;
        searched.value = true;
       console.log(data.data);
   });
 
}

const deleteAnticipo = (id) =>{
    const cantidad= 1;
    const index = anticipos.value.findIndex((loopVariable) => loopVariable.id === id);
    anticipoToDelete.value = id;
    CajaServices.deleteAnticipo(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      
        anticipos.value.splice(index,cantidad);
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
      
            
        })

   
}

const verificarEstadoAnticipo = (id) =>{
   AnticipoServices.verificarEstadoAnticipo(id).then((response)=>{
      console.log("response");
      console.log(response.data);
      if (response.data > 0) {
        visibleDeleteDialog.value = true;
        anticipoToDelete.value = id;
      } else {
        deleteAnticipo(id);
      }
            
        })

   
}

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

const verificarItemsPedido = (id, cantSinStock, cantItems) =>{
  idPedidoSelected.value = id;
  if (cantItems === cantSinStock ) {
    selectedOpcion.value = {id: 2};
    addPago();
  } else if (( cantSinStock > 0) &&  cantItems !== cantSinStock){
    visible.value = true;
  }

}

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

const verPedido = (id) =>{
    router.push({name: 'VisualizarPedido', params: {id}});
    
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

const habilitarInputAnticipo = (index, item) => {
   let inp = document.getElementsByName("input")[index].firstElementChild;
   if (inp.disabled  ) {
        inp.disabled = false;
    }
   

    calcularAbonadoAnticipo();
    
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

const calcularDiferencia = () => {
  habilitarSubmit();
};

const calcularDiferenciaRegistrar = () => {
  habilitarSubmitRegistrar();
};

const habilitarSubmit = () =>{
    if (abonado.value > anticipo.value.saldo) {
        disabledSubmit.value = true;
        color.value = 'red';
  }else{
    disabledSubmit.value = false;
    color.value = '#4b5563';
  }
}

const habilitarSubmitRegistrar = () =>{
    if (abonado.value > 0 && pedido.value && abonado.value <= (pedido.value.total - pedido.value.totalPagos)) {
        disabledSubmitRegistrar.value = false;
    color.value = '#4b5563';
        
  }else{
    disabledSubmitRegistrar.value = true;
        color.value = 'red';
        
  }
}

const verAnticipo = (id) =>{
    console.log("veranticipofuncion");
    router.push({name: 'verAnticipo', params: {id}});
}

const irReembolsos = () =>{
   
    router.push({name: 'reembolsos'});
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
    router.push({name: 'nuevo_pedido'});
}
const formatearNumero = (valor) =>{
    console.log('formatear');
    console.log(valor);
    if(typeof(valor) == "number"){
        return valor.toLocaleString("de-DE");
    }

    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear();
    return fechaFormateada;
}


const showDialogReembolso = (ant) =>{
  console.log('showDialog');
  console.log(ant);
  anticipo.value = ant;
        
        montoReembolsar.value = ant.saldo;
  getFormasPago();
    getReembolsos(ant);
    
    visible.value = true;
}

const getFormasPago = () => {
    FormasPagoServices.obtenerFormasPagoSinAnticipo().then((data) => {
        formasPago.value=data.data;
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pagos.value.push(pago.value);

    });
};

const getReembolsos = (anticipo) => {
    
  ReembolsoServices.getReembolsos(anticipo.id).then((data) => {
        reembolsos.value = data.data;
        console.log(reembolsos.value);
    });
};
const prueba = (event) =>{
    if (event == false) {
        reiniciarDialog();
    }
}

const closeDialog = () =>{
    visible.value = false;
    reiniciarDialog();
}

const reiniciarDialog = () =>{
    pagos.value =[];
 color.value = '#4b5563';
 motivo.value = null;
 abonado.value = 0;
 disabled.value = true;

}

const showTemplate = () => {
  confirm.require({
        group: 'headless',
        header: '¡Reembolso Realizado con Éxito!',
        message: 'Please confirm to proceed.',
        
        accept: () => {
          getAnticipos();
        },
        reject: () => {
          router.push({name: 'anticipos'});
        }
    });
};

const guardarReembolso = () =>{
   montoReembolsar.value = abonado.value;
   let fecha = new Date();
    let reembolso = {monto: montoReembolsar.value,
       fecha: fecha,
       motivo: motivo.value,
       anticipo: anticipo.value};
    
   let reembolsoCreationDTO = {reembolso: reembolso, pagos: pagos.value};
   CajaServices.saveReembolso(reembolsoCreationDTO).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
        
        showTemplate();
        closeDialog();
        
        
    } );
};

const deleteAnticipoReembolsos = () =>{
    const cantidad= 1;
    const index = anticipos.value.findIndex((loopVariable) => loopVariable.id === anticipoToDelete.value);
    console.log("deleteRembolsos");
    console.log(anticipoToDelete.value);
    CajaServices.deleteAnticipoReembolsos(anticipoToDelete.value).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
        anticipos.value.splice(index,cantidad);
        visibleDeleteDialog.value= false;
            toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });
        
    } );
    
};

const registrarAnticipo = () =>{
    dialogRegistrar.value = true;
    
};

const guardarAnticipo = () =>{
console.log(pedido.value);
let fechaAnticipo = new Date();
let ant = {total: abonado.value, fecha: fechaAnticipo, pedido: pedido.value};


let anticipoCreationDTO = {anticipo: ant, pagos: pagos.value};
CajaServices.saveAnticipo(anticipoCreationDTO ).then((data)=> {
    console.log("saveanticipothen");
    console.log("data");
    closeDialog();
    emit('anticipoGuardado', data.data.id);
    
} );



}
</script>
<style>
.p-card-title {
    font-size: medium;
}
</style>

<template>










  <div class="card flex p-fluid justify-content-center " >

    <div class="card flex p-fluid justify-content-center " >
  <ConfirmDialog group="headless">
    <template #container="{ message, acceptCallback, rejectCallback }">
      
        <div class="flex flex-column align-items-center p-5 surface-overlay border-round">
          
            <div class="border-circle bg-primary inline-flex justify-content-center align-items-center h-4rem w-4rem ">
                <i class="pi pi-check text-4xl"></i>
            </div>
            <span class="font-bold text-2xl block mb-2 mt-4">{{ message.header }}</span>
            
            <div class="flex align-items-center gap-2 mt-4">
                <Button label="OK" @click="acceptCallback" class="w-8rem"  ></Button>
                
            </div>
        </div>
    </template>
</ConfirmDialog>
<ConfirmDialog ></ConfirmDialog>
<Toast />
<Dialog v-model:visible="visibleDeleteDialog" modal  header="Edit Profile" :style="{ width: '50rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
     <template #header>
         <div class="flex align-items-center gap-2">
             <h3 class="font-bold">Este anticipo tiene reembolsos asociados</h3>
         </div>
     </template>

    
     <div > 
      <div class="flex align-items-center mb-3">
        Si continúas, también serán eliminados. ¿Deseas proceder?"
      </div> 
      
            </div>
 <template #footer>
     <div class="card flex" style="justify-content: end;">  
      <Button label="Aceptar" style="margin-right: 1%;" @click="deleteAnticipoReembolsos()" />
                 <Button  label="Cancelar"  @click="visibleDeleteDialog=false"  />
                 
                 
             </div>
</template>
</Dialog>


 <!--Modal Reembolsar Anticipo-->
 <Dialog v-model:visible="visible" modal @update:visible="prueba($event)" header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
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
<Dialog v-model:visible="dialogRegistrar" modal header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
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
                                <div class="formgrid grid" v-for="item, index in pagos" :key="item" >
                                    
                                        <div class="field col-12 md:col-6" style="justify-content: start;  ">
                                        
                                            <Dropdown class="flex flex-grow-1" style="padding: 0rem !important;" v-model="item.formaPago" :options="formasPago" @change="habilitarInputAnticipo(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   /> 
                                        </div>
                                    
                                        <div class="flex field col-12 md:col-5" style=" justify-content: start; " >
                                            
                                            <InputNumber class="p-fluid " disabled=true name="input" style="padding: 0rem !important;  " v-model="item.importe" @input="calcularAbonadoAnticipo($event)"  />
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
                 <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                 <Button  label="Guardar" :disabled="disabledSubmitRegistrar" @click="guardarAnticipo()" />
             </div>
</template>
</Dialog>

    <Panel style=" position: relative; width: 100%;" >
      <template #header>
        <div class="flex align-items-center gap-2">
            <h3 class="font-bold">Anticipos</h3>
        </div>
      </template>
      
        
        
    
    
      <template #icons>
        <div class="flex align-items-center">
            <Button  label="Nuevo" @click="registrarAnticipo()" />
        <span class="p-input-icon-left" style="margin-left: 1%;">
          <i class="pi pi-search" style="top: 35%;"/>
          <InputText style="padding: 12px !important; padding-left: 40px !important;" class="buscador p-fluid" v-model="filters['global'].value" placeholder="Buscar..."  />
        </span>

        </div>
        
    
      </template>
      
  
      <div class="card">
        
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
          
          <Column field="pedido.id" header="Pedido N°" aria-sort="ascending" sortable>
            
              

            </Column>
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
                <Button icon="pi pi-search" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="confirm2(slotProps.data.id)"  style="height: 2rem !important; width: 2rem !important;" />
                <Button icon="pi pi-sync" severity="success" text rounded aria-label="Cancel" @click="showDialogReembolso(slotProps.data)"  style="height: 2rem !important; width: 2rem !important;" />
                </template>
          </Column>
        </DataTable>
      </div>
      
    </Panel>
    
  </div>
  
</template>