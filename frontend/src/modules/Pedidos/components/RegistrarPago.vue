
<template>
    <div class="card flex p-fluid justify-content-center " >
 
        <Dialog v-model:visible="visible" modal @update:visible="prueba($event)" header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
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
                        <template #title> Detalle del Pedido</template>
                        <template #content>
                            <div >
                                <label for="fecha" style="font-weight: 600;" >N° Pedido:</label>
                                    {{ formatearNumero(pedido.id) }}<br>
                                <label for="fecha" style="font-weight: 600;" >Fecha:</label>
                                    {{ formatearNumero(pedido.fecha) }}<br>
                                    <label for="fecha" style="font-weight: 600;"  >Monto total Pedido:</label>
                                    {{ formatearNumero(pedido.total) }} Gs.<br>
                                    <div >
                                <label for="fecha" style="font-weight: 600;" >Total Pagos Asociados:</label>
                                    {{ formatearNumero(pedido.pagado) }} Gs.<br>
                                </div>
                                <div>
                                <label for="fecha" style="font-weight: 600;" >Diferencia:</label>
                                    {{ formatearNumero(montoPendiente) }} Gs.<br>
                                </div>
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
                                        
                                            <Dropdown class="flex flex-grow-1" style="padding: 0rem !important; " v-model="item.formaPago" :options="formasPago" @change="habilitarInput(index, item)" optionLabel="descripcion" placeholder="Seleccione un elemento"   /> 
                                        </div>
                                    
                                        <div class=" field col-12 md:col-5" style=" justify-content: start; " >
                                            
                                            <InputNumber class="p-fluid " disabled=true name="input" style="padding: 0rem !important; " v-model="item.importe" @input="calcularAbonado($event)"  />
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
                                            <label for="totalPagos" style="font-weight: 600;" > Monto total pagado: </label>
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
            <!--
            <div class="field col-12 md:col-6">
                <div class="field col-12 md:col-12">
                    <Card style="height: 100%;" >
                        <template #title> Historial Anticipos</template>
                        <template #content>
                            <div >
                                <DataTable :value="anticipos" >
                                    <Column field="id" header="#" aria-sort="ascending" ></Column>
                                    <Column field="fecha" header="Fecha" aria-sort="ascending">
                                        <template #body="slotProps">
                                            <div >
                                                {{ formatearNumero(slotProps.data.fecha) }} 
                                            </div>
                                        </template> 
                                    </Column>
                                    <Column field="total" header="Importe" aria-sort="ascending">
                                        <template #body="slotProps">
                                            <div >
                                                {{ formatearNumero(slotProps.data.total) }} 
                                            </div>
                                        </template>
                                    </Column>
                                    <Column field="reembolsado" header="Reembolsado" aria-sort="ascending">
                                        <template #body="slotProps">
                                            <div >
                                                {{ formatearNumero(slotProps.data.reembolsado) }} 
                                            </div>
                                        </template>
                                    </Column>
                                    <Column field="saldo" header="Total" aria-sort="ascending">
                                        <template #body="slotProps">
                                            <div >
                                                {{ formatearNumero(slotProps.data.total - slotProps.data.reembolsado) }} 
                                            </div>
                                        </template> 
                                    </Column>
                                </DataTable>
                            </div> 
                        </template>
                    </Card>
                </div>
            </div>
            
            -->
            
            </div>
        </div>
        <template #footer>
            <div class="card flex" style="justify-content: end;">  
                        <Button  label="Cancelar"  style="margin-right: 1%;" @click="closeDialog()" />
                        <Button  label="Guardar" :disabled="disabled" @click="guardarAnticipo()" />
                    </div>
    </template>
    </Dialog>
    </div>
            
</template>

<script setup>

import Button from 'primevue/button';
import { PedidoServices } from '@/services/PedidoServices';
import { AnticipoServices } from '@/services/AnticipoServices';






import Checkbox from 'primevue/checkbox';



import Dialog from 'primevue/dialog';

import { ref, onMounted } from "vue";
import { watch } from 'vue'
import router from '@/router';
import Card from 'primevue/card';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Dropdown from 'primevue/dropdown';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Panel from 'primevue/panel';
import SearchCliente from '@/modules/Pedidos/components/SearchCliente.vue'
import MiCard from '@/modules/Pedidos/components/MiCard.vue'
import InputNumber from 'primevue/inputnumber';
import { defineExpose } from 'vue';
const searchCard = ref(null);
const selectedClient = ref();
const pedidosAnticipados = ref([]);
const anticipos = ref();
const pagos =ref([ ]);
const pago =ref({});
const pedido = ref({});
const disabled = ref(true);
const visible = ref(false);
const key = ref(0);
const montoPendiente = ref(0);
const selectedAnticipados = ref();
const emit = defineEmits(['anticipoGuardado']);
const  formasPago = ref();
const metaKey = ref(true);
const color = ref('#4b5563');
const total = ref(0);
const abonado = ref(0);
const cambio = ref(0);
const faltante = ref(0);
const infoCliente = ref([{
    valor: "Cliente no seleccionado"
}]);
const opciones = ref([
    'Seña', 'Total'
]);

/*
onMounted(() => {
    PedidoServices.getDetallePedido(router.currentRoute.value.params.id).then((data) => {
        
    FormasPagoServices.obtenerFormasPago().then((data) => {
        formasPago.value=data.data;
        pago.value.formaPago = null;
        pago.value.importe = 0;
        pagos.value.push(pago.value);

    });
        
    });

    PedidoServices.getPedido(router.currentRoute.value.params.id).then((data) => {
        pedido.value = data.data;
        getCliente(data.data.cliente);

    });

    AnticipoServices.getAnticipos(router.currentRoute.value.params.id).then((data) => {
        anticipos.value = data.data;
        

    });

});*/


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
 total.value = 0;
 abonado.value = 0;
 disabled.value = true;

}

const showDialog = (idPedido) =>{
    getFormasPago();
    getAnticipos(idPedido);
    getPedido(idPedido);
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

const getAnticipos = (idPedido) => {

    AnticipoServices.getAnticipos(idPedido).then((data) => {
        anticipos.value = data.data;
        

    });
};

const getPedido = (idPedido) => {
    PedidoServices.getPedido(idPedido).then((data) => {
        pedido.value = data.data;
        montoPendiente.value = pedido.value.total - pedido.value.pagado;

    });
};

const habilitarInput = (index, item) => {
    document.getElementsByName("input")[index].firstElementChild.disabled = false;
    console.log(item);
    if (faltante.value > 0) {
        item.importe = faltante.value;
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
    console.log('antesdetalleanti');
    console.log(selectedAnticipados.value);
  faltante.value = total.value - abonado.value;
  cambio.value = abonado.value - total.value; 
  habilitarSubmit();
};


const habilitarSubmit = () =>{
    if (abonado.value > montoPendiente.value || abonado.value < 1 ) {
        disabled.value = true;
        color.value = 'red';
  }else{
    disabled.value = false;
    color.value = '#4b5563';
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

const guardarAnticipo = () =>{

    let fechaAnticipo = new Date();
    let ant = {total: abonado.value, fecha: fechaAnticipo, cliente: selectedClient.value, pedido: pedido.value};


    let anticipoCreationDTO = {anticipo: ant, pagos: pagos.value};
    AnticipoServices.saveAnticipo(anticipoCreationDTO ).then((data)=> {
        console.log("saveanticipothen");
        console.log("data");
        closeDialog();
        emit('anticipoGuardado', data.data.id);
        
    } );

    
    
}

    
const verAnticipo = (id) =>{
    console.log("veranticipofuncion");
    //router.push({name: 'verAnticipo', params: {id}});
}

defineExpose({
    showDialog,
});

</script>
<style>
/*
.p-tabview .p-tabview-nav li .p-tabview-nav-link {
    
    border-width: 0 0 1px 0;
   
    background: #ffffff;
    color: #64748b;
    padding: 1rem 1.125rem;
    font-weight: 600;
    border-top-right-radius: 6px;
    border-top-left-radius: 6px;
    transition: background-color 0.2s, color 0.2s, border-color 0.2s, box-shadow 0.2s, outline-color 0.2s;
    margin: 0 0 -1px 0;
    outline-color: transparent;
}
.p-divider.p-divider-horizontal {
    margin: 2rem 0;
    padding: 0 1rem;
}
span{
  padding: 0rem !important;
}

.p-inputtext {
    padding: 0.5rem !important;
}

.field{
    margin: 0% !important;
    margin-bottom: 0.2rem !important;
}*/
</style>