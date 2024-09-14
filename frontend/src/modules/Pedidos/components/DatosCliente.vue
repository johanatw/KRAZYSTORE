<script setup>
import { ref, onMounted } from 'vue';
import Card from 'primevue/card';


const cliente = ref({});

const setCliente = (client) => {

    cliente.value = client;
 
}


defineExpose({
    setCliente
});

</script>
<template>


            <Card style="height: 100%;" >
                <template #title> Datos del Cliente </template>
                <template #content> {{ cliente.nombre }} 
                    {{ cliente.apellido }}<br/>
                    {{ cliente.telefono }}
                
                </template>
                


            </Card>

        
        
</template>