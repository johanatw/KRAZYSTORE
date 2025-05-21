
<template>
    <div class="card" style=" width: 100%;  margin-bottom: 1rem; ">
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
            <template #end>
                <div class="card flex justify-center">
        
        <Button variant="text" type="button" :label="nombre ? nombre : ' '" @click="toggle" class="min-w-48" />


        <Popover ref="op">
            <div class="flex flex-col gap-4 w-[25rem]">
                <div>
                    <ul class="list-none p-0 m-0 flex flex-col gap-4">
                        <li v-for="opcion in opciones" :key="opcion.label" class="flex items-center gap-2 px-2 py-3 hover:bg-emphasis cursor-pointer rounded-border" @click="logout()">
                            <div>
                                <span class="font-medium">{{ opcion.label }}</span>
                          
                            </div>
     
                        </li>
                    </ul>
                </div>
            </div>
        </Popover>
    </div>

    </template>
         
        </Menubar>
        <Toast />
    </div>
</template>

<script setup>
import { ref } from "vue";
import Popover from "primevue/popover";
import Menubar from 'primevue/menubar';
import Badge from "primevue/badge";
import InputText from "primevue/inputtext";
import Avatar from "primevue/avatar";
import Button from "primevue/button";
import router from "@/router";
import Toast from 'primevue/toast';
import SplitButton from "primevue/splitbutton";
import { AuthServices } from '@/services/AuthServices';
import { useToast } from "primevue/usetoast";

const toast = useToast();

const opciones = [
    {
        label: 'Cerrar sesión',
        command: () => {
            logout();
        }
    },

];

const username = localStorage.getItem('username');
const nombre = localStorage.getItem('nombre').toUpperCase();
const items = ref([
   
{ 
        label: 'Dashboard',
        icon: 'pi pi-bill',
        command: () => {
                    router.push({name: 'graficos'});
                }
    },
    { 
        label: 'Ventas',
        icon: 'pi pi-bill',
    items: [
                {
                label: 'Entregas',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'entregas'});
                }
            },
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

            {
                label: 'Clientes',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'clientes'});
                }
            },
            {
                label: 'Productos',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'productos_venta'});
                }
            },
          
        ]
    },
    { 
        label: 'Compras',
        icon: 'pi pi-bill',
        items: [
            
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
            {
                label: 'Pedidos',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'pedidos_compras'});
                }
            },
            {
                label: 'Proveedores',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'proveedores'});
                }
            },
            {
                label: 'Productos',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'productos_compra'});
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
                label: 'Anticipos Clientes',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'anticipos_clientes'});
                }
            },
            {
                label: 'Reembolsos Clientes',
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
                label: 'Baja de Stock',
                icon: 'pi pi-star',
                command: () => {
                    router.push({name: 'ajustes'});
                }
            },

        ]
    },

]);

const logout = () =>{

    AuthServices.logout();
    router.push({name: 'home'});


}

const op = ref();

const toggle = (event) => {
    op.value.toggle(event);
}
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