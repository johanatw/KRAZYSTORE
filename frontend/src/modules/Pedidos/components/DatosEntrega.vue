<script setup>
import { ref, onMounted } from 'vue';
import Card from 'primevue/card';
const props = defineProps(['entrega','pedido']);
const entrega = ref({});
const envio = ref(false);
const medio = ref();
const ciudad = ref();

const setEntrega = (modo, costoEnvio) => {

if (modo.descripcion === "Envío" ) {
    envio.value = true;
    medio.value = costoEnvio.envio.descripcion;
    ciudad.value = costoEnvio.ciudad.descripcion;
}else{
    entrega.value = modo;
}

}

defineExpose({
    setEntrega
});

</script>
<template>
 

            <Card style="height: 100%;" >
                <template #title> Datos de Entrega </template>
                <template #content > 
                    <div  v-if="envio" >
                        <div class="field grid" >
                            <div class="col flex align-items-center">
                                <i class="pi pi-map-marker" style="font-size: 2rem"/>
                                &nbsp;{{ ciudad }}, Paraguay <br/> 

                            </div>
                            

                        </div>
                        <div class="field grid" >
                            <div class="col flex align-items-center">
                                <i class="pi pi-truck" style="font-size: 2rem"></i>
                            &nbsp;Vía {{ medio }}</div>

                            
                            

                        </div>
                        

                    </div>
                    <div v-else >
                        {{ entrega.descripcion }}

                    </div>
                
                </template>
                


            </Card>


        
</template>