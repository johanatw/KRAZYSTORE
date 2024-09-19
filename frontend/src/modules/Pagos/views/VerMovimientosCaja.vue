<script setup>
import CardCliente from "@/modules/Pedidos/components/CardCliente.vue";
import CardDetalle from "@/modules/Pedidos/components/CardDetalle.vue";
import CardEntrega from "@/modules/Pedidos/components/CardEntrega.vue";
import CardPago from "@/modules/Pedidos/components/CardPago.vue";
import MiCard from "@/modules/Pedidos/components/MiCard.vue";
import DetallePedido from '@/modules/Pedidos/components/DetallePedido.vue';
import Button from 'primevue/button';
import {FormasPagoServices} from '@/services/FormasPagoServices';

import { AnticipoServices } from '@/services/AnticipoServices';
import { VentaServices } from '@/services/VentaServices';
import {ReembolsoServices} from '@/services/ReembolsoServices'

import { ConceptoServices } from '@/services/ConceptoServices';
import { ref, onMounted } from "vue";
import Panel from 'primevue/panel';
import {PedidoServices} from '@/services/PedidoServices';
import {CajaServices} from '@/services/CajaServices';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Card from "primevue/card";
import InputNumber from 'primevue/inputnumber';

import Textarea from 'primevue/textarea';

import Dropdown from 'primevue/dropdown';

import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import Tag from 'primevue/tag';
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import {formatearNumero, existeCajaAbierta} from '@/utils/utils';

import router from '@/router';
const pedido = ref({ });
const selectedClient = ref();
const detallePedido = ref({ });
const mensaje = ref([]);
const modoEntrega = ref();
const toast = useToast();
const estadoPedido = ref();
const estadoPago = ref();
const formaPago = ref();
const cardCliente = ref();
const cardEntrega = ref();
const searchCard = ref();
const visible = ref(false);
const formasPagos = ref();
const options = ref(['Ingreso', 'Egreso']);
const conceptos = ref();
const caja = ref({});
const movimiento = ref({});
const movimientos = ref([]);
const cajaAbierta = ref(false);
const envioSelected = ref();
const detalle = ref(null);
const error = ref(false);
const fecha = ref();
const fechaCompleta = ref();
const infoCliente = ref([{
    valor: "Cliente no seleccionado"
}])
const infoEntrega = ref([{
    valor: "Metodo de Entrega no seleccionado"
}])



onMounted(() => {
    getCaja();
    getMovimientos();
    
});
const getConceptos = (tipo) =>{
  let buscarTipo;
  switch (tipo) {
    case 'Ingreso':
      buscarTipo = 'I';
      
      break;

    case 'Egreso':
    buscarTipo = 'E';
    break;
    default:
    buscarTipo = 'I';
      break;
  }
  ConceptoServices.obtenerConceptosByTipo(buscarTipo).then((data) => {
        conceptos.value = data.data;
        //console.log(reembolsos.value);
    });
  

}

const cerrarCaja= () =>{
    let id = caja.value.id;

    CajaServices.cerraCaja(id).then((data) => {
        cajaAbierta.value = false;
        toast.add({ severity:"success", detail: 'Caja Cerrada', life: 3000 });
    });
}

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
        getMovimientos();
        visible.value = false;
        toast.add({ severity:"success", detail: movimiento.value.tipo + ' Guardado', life: 3000 });
        console.log("ok");
    });
}
const nuevoMovimiento = () =>{
  FormasPagoServices.obtenerFormasPagoSinAnticipo().then((data) => {
        formasPagos.value = data.data;
        //console.log(reembolsos.value);
    });

    ConceptoServices.obtenerConceptosByTipo('I').then((data) => {
        conceptos.value = data.data;
        //console.log(reembolsos.value);
    });

    visible.value = true;
}


async function getMovimientos() {
    CajaServices.obtenerMovimientosByCaja(router.currentRoute.value.params.id).then((data) => {
        movimientos.value = data.data;
   

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
<template>
    

<div class="card flex p-fluid justify-content-center " >
    <Toast />
    <Dialog v-model:visible="visible" modal header="Edit Profile" :style="{ width: '30rem' }" :breakpoints="{ '1199px': '75vw', '575px': '90vw' }">
     <template #header>
         <div class="flex align-items-center gap-2">
             <h3 class="font-bold">Reembolsar Anticipo</h3>
         </div>
     </template>
    
     <div >      
      
                        
                      
      <div>
        <div class="flex align-items-center gap-3 mb-2">
        <label for="username" class="font-semibold w-9rem">Tipo transacción</label>
        <Dropdown v-model:model-value="movimiento.tipo" :options="options" @update:model-value="getConceptos(movimiento.tipo)" placeholder="Select a City" checkmark :highlightOnSelect="false" class="w-full md:w-14rem"  />
    </div>
                                <div class="flex align-items-center gap-3 mb-2">
        <label for="username" class="font-semibold w-9rem">Concepto</label>
        <Dropdown v-model:model-value="movimiento.concepto" :options="conceptos" optionLabel="descripcion" placeholder="Select a City" checkmark :highlightOnSelect="false" class="w-full md:w-14rem"  />
    </div>

    <div class="flex align-items-center gap-3 mb-2">
        <label for="email" class="font-semibold w-9rem">Monto</label>
        <InputNumber v-model:model-value="movimiento.monto" inputId="integeronly" />
    </div>
    <div class="flex align-items-center gap-3 mb-2">
        <label for="email" class="font-semibold w-9rem">Medio de pago</label>
        <Dropdown v-model:model-value="movimiento.formaPago" :options="formasPagos" optionLabel="descripcion"  placeholder="Select a City" checkmark :highlightOnSelect="false" class="w-full md:w-14rem" />
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
        
        <DataTable  :value="movimientos" scrollHeight="400px"  
          :paginator="true" :rows="7" :filters="filters"
          paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
          currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
          <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
          <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.fecha) }}
            </template>
        </Column>
          <Column field="concepto"  header="Concepto" aria-sort="ascending" sortable>  
                     
        </Column>
          <Column field="formaPago"  header="Forma" aria-sort="ascending" sortable> 
            
        </Column>
        <Column field="total"  header="Monto" aria-sort="ascending" sortable>    
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.total) }}
            </template>        
        </Column>
        <Column field="ingreso"  header="Ingreso" aria-sort="ascending" sortable>   
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.ingreso) }}
            </template>         
        </Column>
        <Column field="egreso"  header="Egreso" aria-sort="ascending" sortable> 
            <template #body="slotProps">
                {{ formatearNumero(slotProps.data.egreso) }}
            </template>           
        </Column>
        <Column field="factura"  header="N° Factura" aria-sort="ascending" sortable>            
        </Column>

        
          <Column v-if="cajaAbierta" :exportable="false" style="min-width:8rem">
            <template #body="slotProps">
                
                <Button icon="pi pi-times" severity="danger" text rounded aria-label="Cancel"  style="height: 2rem !important; width: 2rem !important;" />
                
                </template>
          </Column>
        </DataTable>
      </div>
           
        </div>

 
    </Panel>
</div>

</template>