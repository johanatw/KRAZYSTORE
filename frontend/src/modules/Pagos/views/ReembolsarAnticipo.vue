
<template>
    <div class="card flex p-fluid justify-content-center " >
  
        <Panel style=" position: relative; width: 80%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Reembolsar Anticipo N°{{ anticipo.id }}</h3>
                </div>
            </template>

            <template #icons>
                <div class="card flex" style="justify-content: end;">   
                    <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" />
                        <Button  label="Guardar" :disabled="disabledSubmit" @click="guardarReembolso()" />
                    </div>  
                </div>
            </template>

            <div class="contenedor" >      
                <div class="formgrid grid" >  

             
                    <div class="field col-12 md:col-12">
                <div class="field col-12 md:col-12">
                    <Card style="height: 100%;" >
                        <template #title> Detalle del Anticipo</template>
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
                                    <div class="flex field col-12 md:col-12" >
                                        <div class="field col-6 md:col-6" style="justify-content: start;  ">
                                        
                                            <Dropdown style="padding: 0rem !important;" v-model="item.formaPago" :options="formasPago" @change="habilitarInput(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   /> 
                                        </div>
                                    
                                        <div class=" field col-5 md:col-5" style=" justify-content: start; " >
                                            
                                            <InputNumber disabled=true name="input" style="padding: 0rem !important;  " v-model="item.importe" @input="calcularAbonado($event)"  />
                                        </div>
                                        <div v-if="index > 0" class=" field col-1 md:col-1" style=" justify-content: flex-end;">
                                            
                                            <Button style="background: none !important; border: none !important;  " icon="pi pi-times" severity="danger" text rounded aria-label="Cancel" @click="eliminarRow(index)" />
                                
                                        </div>
                                        <div v-else class=" field col-1 md:col-1" style=" justify-content: flex-end;">
                                          
                                            <Button style="background: none !important; border: none !important " icon="pi pi-plus" severity="danger" text rounded aria-label="Cancel" @click="addRow()" />
                                        </div>
                                    </div>
                                </div>

                                <div class="formgrid grid">
                                    
                                        <div class="flex field col-12 md:col-6 " >
                                            <label for="totalPagos" style="font-weight: 600;"> Monto a reembolsar: </label>
                                        </div>
                                        <div  class="flex field col-12 md:col-6" style=" justify-content: start; " :style="{color: color}" >
                                            {{ abonado.toLocaleString("de-DE") }}

                                        </div>
                                    
                                        <div class="flex field col-12 md:col-6 " >
                                            <label for="totalPagos" style="font-weight: 600;"> Motivo del reembolso: </label>
                                        </div>
                                        <div  class="flex field col-12 md:col-5" style=" justify-content: center; " :style="{color: color}" >
                                            <InputText type="text" v-model="motivo"  />

                                        </div>
                                    
                                </div>
                            </div>
                        </template>
                    </Card>
                </div>  
            </div>     
            <!--
            <div class="field col-12 md:col-6">
                <div class="field col-12 md:col-12">
                    <Card style="height: 100%;" >
                        <template #title> Historial Reembolsos</template>
                        <template #content>
                            <div >
                                <DataTable :value="reembolsos" >
                                    <Column field="id" header="#" aria-sort="ascending" ></Column>
                                    <Column field="fecha" header="Fecha" aria-sort="ascending">
                                        <template #body="slotProps">
                                            <div >
                                                {{ formatearNumero(slotProps.data.fecha) }} 
                                            </div>
                                        
                                            
                                        </template> 
                                        
                                    </Column>
                                    <Column field="monto" header="Monto" aria-sort="ascending"></Column>
                                    <Column field="motivo" header="Motivo" aria-sort="ascending"></Column>
                                </DataTable>
                            </div> 
                        </template>
                    </Card>
                </div>
            </div> -->
                </div>     
            </div>
        </Panel>
    </div>
            
</template>

<script setup>

import Button from 'primevue/button';
import { AnticipoServices } from '@/services/AnticipoServices';
import { ref, onMounted } from "vue";
import router from '@/router';
import Card from 'primevue/card';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Dropdown from 'primevue/dropdown';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Panel from 'primevue/panel';
import { ReembolsoServices } from '@/services/ReembolsoServices'
import InputText from 'primevue/inputtext';
import RadioButton from 'primevue/radiobutton';
import MiCard from '@/modules/Pedidos/components/MiCard.vue'
import InputNumber from 'primevue/inputnumber';

const reembolsos =ref();
const cardCliente = ref(null);
const selectedClient = ref();
const pagos =ref([ ]);
const pago =ref({});
const disabledSubmit = ref(true);
const disabled = ref(true);
const  formasPago = ref();
const anticipo= ref({});
const abonado = ref(0);
const color = ref('red');
const opReembolsar = ref(null);
const montoReembolsar = ref(0);
const motivo = ref(null);
const infoCliente = ref([{
    valor: "Cliente no seleccionado"
}]);


onMounted(() => {
    AnticipoServices.obtenerAnticipo(router.currentRoute.value.params.id).then((data) => {
        anticipo.value = data.data;
        cardCliente.value.editable = false;
        montoReembolsar.value = data.data.saldo;
        opReembolsar.value = "total";
        getCliente(data.data.cliente);
    });

     ReembolsoServices.getReembolsos(router.currentRoute.value.params.id).then((data) => {
        reembolsos.value = data.data;
        console.log(reembolsos.value);
    });

    FormasPagoServices.obtenerFormasPagoSinAnticipo().then((data) => {
        formasPago.value=data.data;
        
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pagos.value.push(pago.value);
    });
});



const getCliente = (client) =>{
    selectedClient.value = client;
    infoCliente.value = [];
    let valor = {valor: client.nombre};
    infoCliente.value.push(valor);
    valor = {valor: client.telefono};
    infoCliente.value.push(valor);
    if (client.nroDoc) {
        valor = {valor: client.tipoDoc.descripcion +' - '+ client.nroDoc};
        infoCliente.value.push(valor);
    }
    if (client.email) {
        valor = {valor: client.email};
        infoCliente.value.push(valor);
    }

    
}

const calcularFaltante = (event) => {

    if (event !== undefined) {
        console.log(event);
        let valorActual = Number(event.value);
        let valorAnterior = Number(event.formattedValue.replaceAll(".",""));
        montoReembolsar.value = montoReembolsar.value - valorAnterior + valorActual;
    }else{
        montoReembolsar.value = anticipo.value.saldo;
    }
    
    calcularDiferencia();
  
};

/*
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
  
};*/

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
    if (abonado.value > anticipo.value.saldo) {
        disabledSubmit.value = true;
        color.value = 'red';
  }else{
    disabledSubmit.value = false;
    color.value = '#4b5563';
  }
}

const habilitarInputMontoReembolsar = () =>{
    if (opReembolsar.value == "total") {
        montoReembolsar.value = anticipo.value.saldo;
        disabled.value = true;
    } else {
        disabled.value = false;
    }
}

const formatearNumero = (valor) =>{
    if(typeof(valor) == "number"){
        return valor.toLocaleString("de-DE");
    }

    let fecha = new Date(valor);
    let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear();
    return fechaFormateada;
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

const guardarReembolso = () =>{
   montoReembolsar.value = abonado.value;
   let fecha = new Date();
    let reembolso = {monto: montoReembolsar.value,
       fecha: fecha,
       motivo: motivo.value,
       anticipo: anticipo.value};
    
   let reembolsoCreationDTO = {reembolso: reembolso, pagos: pagos.value};
   ReembolsoServices.saveReembolso(reembolsoCreationDTO);
};


</script>
<style>
.p-card-title {
    font-size: medium;
}
</style>