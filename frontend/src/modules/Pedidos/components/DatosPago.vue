<script setup>
import { ref, onMounted } from 'vue';
import Card from 'primevue/card';
const props = defineProps(['formaPago']);
const formaPago = ref({});
const icono = ref("pi pi-map-marker");

const setModoPago = (forma) => {
   formaPago.value = forma;
   console.log(formaPago.value);
    if (formaPago.value.descripcion === "Efectivo") {
            icono.value = "pi pi-money-bill"
        } else if (formaPago.value.descripcion === "Transferencia"){
            icono.value = "pi pi-arrow-right-arrow-left"
        } else if (formaPago.value.descripcion === "Giros"){
            icono.value = "pi pi-wallet"
        }





}

defineExpose({
    setModoPago
});

</script>
<template>


            <Card style="height: 100%;" >
                <template #title> Metodo de Pago </template>
                <template #content> 
                    <div class="field grid">
                        <div class="col flex align-items-center">
                            <i :class="icono" style="font-size: 2rem"/>&nbsp;
                                {{ formaPago.descripcion }}

                        </div>
                    </div>
                
                
                </template>
                


            </Card>

        
</template>