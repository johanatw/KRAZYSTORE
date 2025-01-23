
<template>
    <div class="card" style="position: absolute; top: 0; left: 0; right: 0; width: 100%; align-self: start; ">
        <Menubar :model="items">
            <template #start>
            
                <a  href="#" style="background: none;">
      <img src="@/assets/logo.png"  width="50" height="30">
    </a>
            </template>
            <template #item="{ item, props, hasSubmenu, root }">
                <a v-ripple class="flex align-items-center" v-bind="props.action">
                    <span :class="item.icon" />
                    <span class="ml-2">{{ item.label }}</span>
                    <span v-if="item.shortcut" class="ml-auto border-1 surface-border border-round surface-100 text-xs p-1">{{ item.shortcut }}</span>
                    <i v-if="hasSubmenu" :class="['pi pi-angle-down', { 'pi-angle-down ml-2': root, 'pi-angle-right ml-auto': !root }]"></i>
                </a>
            </template>
         
        </Menubar>
        <Toast />
    </div>
</template>

<script setup>
import { ref } from "vue";

import Menubar from 'primevue/menubar';
import Badge from "primevue/badge";
import InputText from "primevue/inputtext";
import Avatar from "primevue/avatar";
import router from "@/router";
import Toast from 'primevue/toast';


import { useToast } from "primevue/usetoast";

const toast = useToast();


const items = ref([
   
    { 
        label: 'Ventas',
        icon: 'pi pi-bill',
    items: [
            {
                label: 'Facturas de ventas',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'ventas'});
                }
            },
            {
                label: 'Pedidos',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'pedidos'});
                }
            },

        ]
    },
    { 
        label: 'Compras',
        icon: 'pi pi-bill',
        items: [
            {
                label: 'Pedidos',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'pedidos_compras'});
                }
            },
            {
                label: 'Facturas de compras',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'compras'});
                }
            },
            {
                label: 'Recepciones',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'recepciones'});
                }
            },
          

        ]
    },

    { 
        label: 'Caja',
        icon: 'pi pi-bill',
    items: [
            {
                label: 'Cajas',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'cajas'});
                }
            },
            {
                label: 'Anticipos',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'anticipos'});
                }
            },
            {
                label: 'Reembolsos',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'reembolsos'});
                }
            },
            
           
        ]
    },
    { 
        label: 'Inventario',
        icon: 'pi pi-bill',
        items: [
            {
                label: 'Existencias',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'existencias'});
                }
            },
            {
                label: 'Inventario',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'inventario'});
                }
            },
            {
                label: 'Ajustes de Stock',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'ajustes'});
                }
            },

        ]
    },

]);
</script>
<style>
.p-menubar .p-menubar-root-list > .p-menuitem > .p-menuitem-content .p-menuitem-link {
    padding: 0.5rem 0.75rem;
    user-select: none;
}

a, .green {
    text-decoration: none;
    color: rgb(34, 36, 35);
    transition: 0.4s;
    padding: 3px;
}
</style>