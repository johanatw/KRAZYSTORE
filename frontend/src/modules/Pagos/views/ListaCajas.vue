<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';
import { FilterMatchMode, FilterOperator } from '@primevue/core/api';
import { AnticipoServices } from '@/services/AnticipoServices';
import { CajaServices } from '@/services/CajaServices';
import Panel from 'primevue/panel';
import router from '@/router';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Card from 'primevue/card';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import { PagoServices } from '@/services/PagoServices';

import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import InputGroup from 'primevue/inputgroup';
import {formatearNumero, existeCajaAbierta, formatearFecha, getEstadoCaja, formatearFechaHora} from '@/utils/utils';

const cajas= ref([]);

const toast = useToast();

async function getCajas() {
    CajaServices.getCajas().then((data) => {
       cajas.value = data.data;
   });
 
}






//Otros

onMounted(() => {
   // name();
    getCajas();
});

async function abrirCaja() {
    const d = await existeCajaAbierta();
    console.log(d);
    if (d) {
        toast.add({ severity:"error", detail: 'Ya existe una caja abierta', life: 3000 });
    } else {
        CajaServices.abrirCaja().then((data) => {
            let id = data.data.id;
            router.push({name: 'movimientos', params: {id}});
        });
    }
}

const verCaja = (caja) =>{
    let id = caja.id;
    router.push({name: 'movimientos', params: {id}});
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
                    <h3 class="font-bold">Cajas</h3>
                </div>
            </template>
      
            <template #icons>
                <div class="flex align-items-center">
                    <Button  label="Abrir Caja" @click="abrirCaja()" />
                </div>
            </template>
      
            <div >
                <DataTable  :value="cajas " scrollHeight="400px"  
                :paginator="true" :rows="7" 
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" 
                currentPageReportTemplate="Mostrando del {first} al {last} de {totalRecords} registros" >
                <template #empty> No hay registros para mostrar. </template>
                <template #loading> Cargando. </template>
                    <Column field="id" sortable header="N°" aria-sort="ascending" ></Column>
                    <Column field="fecha" sortable header="Apertura" aria-sort="ascending" >
                        <template #body="slotProps">
                            {{ formatearFechaHora(slotProps.data.fecha) }}
                        </template>
                    </Column>
                    <Column field="fechaCierre" sortable header="Cierre" aria-sort="ascending" >
                        <template #body="slotProps">
                            <div v-if="slotProps.data.fechaCierre != null">
                                {{ formatearFechaHora(slotProps.data.fechaCierre) }}
                            </div>
                        </template>
                    </Column>
                    <Column  field="estado" header="Estado" aria-sort="ascending" sortable >
                        
                        <template #body="slotProps">
                            <div>
                                {{ getEstadoCaja(slotProps.data.estado) }}
                            </div>
                        </template>
                    </Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-search" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" @click="verCaja(slotProps.data)" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
  
</template>