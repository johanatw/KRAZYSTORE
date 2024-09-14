
<template>
    <Card >
        <template #title>
            <div class="flex justify-content-between ">
                <div class="flex align-content-center flex-wrap" style="font-weight: bolder;">
                    {{ titulo }}
                </div>
                <div v-if="editable">
                    <Button class="pi pi-pencil" link @click="emit('edit')" />
                    
                </div>
               
                    
            
            </div>
            
        </template>
        <template #content>
            <p class="m-0">
                <div v-for="v in contenido">
                    {{ v.valor }}
                </div>
                
            </p>
        </template>
    </Card>
</template>

<script setup>

import Button from 'primevue/button';

import Card from 'primevue/card';
import { ref, onMounted } from "vue";
import { defineExpose } from 'vue';
const props = defineProps(['titulo','contenido']);
const emit = defineEmits(['edit']);
const editable = ref(true);

const prueba = ref('')
const conte = ref([{
    valor: "johana Pineda"
},{
    valor: "0985569264"
}])



onMounted(() => {
    
    prueba.value = "Johana Pineda \n 0985569264"
  });

  defineExpose({
    editable,
});

</script>

<style>

</style>
