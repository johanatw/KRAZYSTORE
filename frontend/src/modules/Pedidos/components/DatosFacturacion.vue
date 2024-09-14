<script setup>
import { ref, onMounted } from 'vue';
import Card from 'primevue/card';
const props = defineProps(['cliente']);
const cliente = ref({});
const tipoDoc = ref();

const setCliente = (client) => {

    cliente.value = client;
    if (cliente.value.tipoDoc.descripcion) {
        tipoDoc.value = cliente.value.tipoDoc.descripcion;
    }
 
 
 

}

defineExpose({
    setCliente
});

</script>
<template>
    

            <Card style="height: 100%;" >
                <template #title> Datos de Facturación </template>
                <template #content> {{cliente.nombre}}
                    {{ cliente.apellido }}<br>
                    {{ tipoDoc }} - 
                    {{ cliente.nroDoc }}
                
                </template>
                


            </Card>

    
        
</template>