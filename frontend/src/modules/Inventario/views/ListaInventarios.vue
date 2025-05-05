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
import { InventarioServices } from '@/services/InventarioServices';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import Toast from 'primevue/toast';
import { useToast } from "primevue/usetoast";
import InputGroup from 'primevue/inputgroup';
import {formatearNumero, formatearFecha} from '@/utils/utils';

const inventarios= ref();

const toast = useToast();

async function getInventarios() {
    InventarioServices.obtenerInventarios().then((data) => {
       inventarios.value = data.data;
       console.log(inventarios.value);
   });
 
}

onMounted(() => {
    getInventarios();
});


const nuevoInventario = () =>{
    router.push({name: 'nuevo_inventario'});
}

const verListaInventariar = (id) =>{
    router.push({name: 'ver_inventario', params: {id}});
}

const verInventario = (id) =>{
    router.push({name: 'ver_inventario', params: {id}});
}

const registrarConteo = (id) =>{
    router.push({name: 'registrar_conteo', params: {id}});
}

const isFinalizado = (estado) => {
  switch (estado) {
        case 'P':
           return true;

       default:
           return false;
   }
};

const isEnCurso = (estado) => {
  switch (estado) {
        case 'S':
           return true;

       default:
           return false;
   }
};

const getEstado = (estado) => {
  switch (estado) {
       case 'A':
           return 'Ajustado';

       case 'S':
           return 'En curso';

        case 'P':
           return 'Pendiente de ajuste';

       default:
           return null;
   }
};

const ajustarInventario = (id) =>{
    InventarioServices.ajustarInventario(id).then((data)=> {
        getInventarios();
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
                    <h3 class="font-bold">Inventarios</h3>
                </div>
            </template>
      
            <template #icons>
                <div class="flex align-items-center">
                    <Button  label="Nuevo" @click="nuevoInventario()" />
                </div>
            </template>
      
            <div >
                <DataTable  :value="inventarios" scrollHeight="400px"  
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
                            {{ getEstado(slotProps.data.estado) }}
                        </template>
                    </Column>
                    <Column :exportable="false" style="min-width:8rem">
                        <template #body="slotProps">
                            <Button icon="pi pi-eye" v-tooltip="'Visualizar'" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" @click="verInventario(slotProps.data.id)" />
                            <Button :disabled="!isEnCurso(slotProps.data.estado)" icon="pi pi-pen-to-square" v-tooltip="'Registrar'" text rounded aria-label="Search" severity="success" style="height: 2rem !important; width: 2rem !important;" @click="registrarConteo(slotProps.data.id)" />
                            <Button :disabled="!isFinalizado(slotProps.data.estado)" icon="pi pi-cog" v-tooltip="'Ajustar'" severity="info" text rounded aria-label="Search" style="height: 2rem !important; width: 2rem !important;" @click="ajustarInventario(slotProps.data.id)" />
                        </template>
                    </Column>
                </DataTable>
            </div>
        </Panel>
    </div>
  
</template>