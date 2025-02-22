<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { AnticipoServices } from '@/services/AnticipoServices';
import { CajaServices } from '@/services/CajaServices';
import { AjusteStockServices } from '@/services/AjusteStockServices';
import Panel from 'primevue/panel';
import router from '@/router';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Card from 'primevue/card';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import { PagoServices } from '@/services/PagoServices';
import { InventarioServices } from '@/services/InventarioServices';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import InputGroup from 'primevue/inputgroup';
import {formatearNumero, formatearFecha} from '@/utils/utils';

const ajustes= ref();

const toast = useToast();

async function getAjustes() {
    AjusteStockServices.obtenerAjustes().then((data) => {
       ajustes.value = data.data;
   });
 
}






//Otros

onMounted(() => {
   // name();
    getAjustes();
});


const nuevoAjuste = () =>{
    router.push({name: 'nuevo_ajuste'});
}


const getEstado = (estado) => {
  switch (estado) {
       case 'A':
           return 'Ajustado';

       case 'P':
           return 'Pendiente de Ajuste';

        case 'F':
           return 'Finalizado';

       default:
           return null;
   }
};

const vistaVerAjuste = (id) => {
    router.push({name: 'ver_ajuste', params: {id}});
  
  }


const ajustarInventario = (id) =>{
    AjusteStockServices.ajustar(id).then((data)=> {
        getAjustes();
        //closeDialog();
        //emit('anticipoGuardado', data.data.id);
        
    } );
}

</script>

<style>
.p-card-title {
    font-size: medium;
}
</style>

<template>

    <div class=" flex p-fluid justify-content-center " >
        <Toast />
        <!--Pantalla Principal Lista de Cajas-->
        <Panel style=" position: relative; width: 100%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Ajustes de Stock</h3>
                </div>
            </template>
      
            <template #icons>
                <div class="flex align-items-center">
                    <Button  label="Nuevo" @click="nuevoAjuste()" />
                </div>
            </template>
      
            <div >
                <DataTable  :value="ajustes" scrollHeight="400px"  
                :paginator="true" :rows="7" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                <template #empty> No customers found. </template>
                <template #loading> Loading customers data. Please wait. </template>
                    <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
                    <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                        <template #body="slotProps">
                            {{ formatearFecha(slotProps.data.fecha) }}
                        </template>
                    </Column>
                    <Column  field="estado" header="Estado" aria-sort="ascending" sortable >
                        <template #body="slotProps">
                            {{ getEstado(slotProps.data.estado) }}
                        </template>
                    </Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-search" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" @click="vistaVerAjuste(slotProps.data.id)" />
                            <Button v-if="slotProps.data.estado == 'P'" icon="pi pi-cog" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" @click="ajustarInventario(slotProps.data.id)" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
  
</template>