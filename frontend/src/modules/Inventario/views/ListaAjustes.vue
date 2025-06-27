<script setup>
//Importaciones
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { AnticipoServices } from '@/services/AnticipoServices';
import { CajaServices } from '@/services/CajaServices';
import { AjusteStockServices } from '@/services/AjusteStockServices';

import {InputGroupAddon} from 'primevue';
import {InputText} from 'primevue';
import Panel from 'primevue/panel';
import router from '@/router';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Card from 'primevue/card';
import InputNumber from 'primevue/inputnumber';
import { Tag } from 'primevue';
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

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

//Nuevo ajuste
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

const getSeverity = (estado) => {
    switch (estado) {
        case 'S':
           return 'background-color: rgb(202, 241, 216); color: rgb(24, 138, 66); font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;';

       case 'P':
           return 'background-color: rgb(254, 221, 199); color: rgb(174, 81, 15); font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;';

       case 'A':
           return 'background-color: rgb(215, 227, 552); color: rgb(50, 111, 252); font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;';

        default:
            return null;
    }
};

//Pendiente de ajuste
const isPendienteDeAjuste = (estado) => {
  switch (estado) {
       case 'P':
           return true;
       default:
           return false;
   }
};

const vistaVerAjuste = (id) => {
    router.push({name: 'ver_ajuste', params: {id}});
  
}


const ajustarInventario = (id) =>{
    router.push({name: 'ajuste_baja_stock', params: {id}});
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
        <Panel style=" position: relative; width: 90%;" >
            <template #header>
                <div class="flex align-items-center gap-2">
                    <h3 class="font-bold">Bajas de Stock</h3>
                </div>
            </template>
      
            <template #icons>
                
        <div class="flex align-items-center">
            <Button  label="Nuevo" @click="nuevoAjuste()" style="margin-right: 1% ;"/>
          <InputGroup>
            <InputText v-model="filters['global'].value" placeholder="Buscar..." />
            <InputGroupAddon>
              <i class="pi pi-search" />
            </InputGroupAddon>
        </InputGroup>
        </div>
    
      </template>
      
            <div >
                <DataTable  :value="ajustes" scrollHeight="400px"  
                :paginator="true" :rows="7" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                <template #empty> No hay registros para mostrar. </template>
                <template #loading> Cargando. </template>
                    <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
                    <Column field="fecha" sortable header="Fecha" aria-sort="ascending" >
                        <template #body="slotProps">
                            {{ formatearFecha(slotProps.data.fecha) }}
                        </template>
                    </Column>
                    <Column  field="estado" header="Estado" aria-sort="ascending" sortable >
                        <template #body="slotProps">
                            <Tag :style="getSeverity(slotProps.data.estado)" style=" font-weight: bold; font-size: 12px; padding: 0.25rem 0.4rem;" >{{ getEstado(slotProps.data.estado)}}</Tag>
                        </template>
                    </Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-eye" v-tooltip="'Visualizar'" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" @click="vistaVerAjuste(slotProps.data.id)" />
                            <Button :disabled="!isPendienteDeAjuste(slotProps.data.estado)" v-tooltip="'Ajustar'" severity="info" icon="pi pi-cog" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" @click="ajustarInventario(slotProps.data.id)" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
  
</template>