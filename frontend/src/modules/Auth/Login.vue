<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import InputText from 'primevue/inputtext';
import Column from 'primevue/column';
import Button from 'primevue/button';

import Password from 'primevue/password';

import { FormField } from '@primevue/forms';

import { AnticipoServices } from '@/services/AnticipoServices';
import { CajaServices } from '@/services/CajaServices';
import { zodResolver } from '@primevue/forms/resolvers/zod';
import { z } from 'zod';
import { reactive } from 'vue';
import Panel from 'primevue/panel';
import { Form } from '@primevue/forms';

import router from '@/router';
import { FormasPagoServices } from '@/services/FormasPagoServices';
import Card from 'primevue/card';
import InputNumber from 'primevue/inputnumber';
import Dropdown from 'primevue/dropdown';
import { PagoServices } from '@/services/PagoServices';
import Toast from 'primevue/toast';
import Dialog from 'primevue/dialog';
import ConfirmDialog from 'primevue/confirmdialog';
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

import Message from 'primevue/message';

import InputGroup from 'primevue/inputgroup';
import {formatearNumero, formatearFecha, existeCajaAbierta} from '@/utils/utils';
import axios from 'axios';
const visible = ref(false);
const  formasPago = ref();
const disabled = ref(true);
const disabledSubmit = ref(true);
const pedido = ref();
const anticipos= ref([]);
const montoReembolsar = ref(0);
const motivo = ref(null);
const confirm = useConfirm();
const toast = useToast();
const pagosAnticipo =ref([ ]);
const pagos =ref([ ]);
const pago =ref({});
const searched =ref(false);
const abonado = ref(0);
const dialogRegistrar = ref(false);
const disabledSubmitRegistrar = ref(true);
const anticipoToDelete = ref();
const color = ref('#4b5563');
const id = ref();
const visibleDeleteDialog = ref(false);
const anticipo = ref();
const cajaAbierta = ref({});
const tipoPedido = ref({cod: 'V', descripcion:'Venta'});
const optionsPedido = ref([{cod: 'V', descripcion:'Venta'}, {cod: 'C', descripcion:'Compra'}]);



//Funciones Registrar Anticipo
const buscarPedido= (id, e) => {
  
    var code = (e.keyCode ? e.keyCode : e.which);
    if (code == 13) { //Enter keycode   
        if (tipoPedido.value.descripcion == 'Venta' ) {
            CajaServices.obtenerPagosPedido(id).then((data) => {
                pedido.value = data.data;
                searched.value = true;
                console.log(data.data);
            });
        } else {
            CajaServices.obtenerPagosPedidoCompra(id).then((data) => {
                pedido.value = data.data;
                searched.value = true;
                console.log(data.data);
            });
        }                     
        
    }
    

}

const addRowAnticipo = () => {
    pagosAnticipo.value.push({formaPago:null , importe: 0});
};

const eliminarRowAnticipo = (index) => {
    if (index > 0) {
        pagosAnticipo.value.splice(index,1);
    }
    calcularAbonadoAnticipo();
};

const habilitarInputAnticipo = (index, item) => {
    let inp = document.getElementsByName("input")[index].firstElementChild;
    if (inp.disabled  ) {
        inp.disabled = false;
    }

    calcularAbonadoAnticipo();
    
};

const calcularAbonadoAnticipo = (event) => {
    if (event !== undefined) {
        console.log(event);
        let valorActual = Number(event.value);
        let valorAnterior = Number(event.formattedValue.replaceAll(".",""));
        abonado.value = abonado.value - valorAnterior + valorActual;
    }else {
        let monto = 0 ;
        pagos.value.forEach(element => {
            monto += element.importe;
        })
        abonado.value = monto;
    }
    calcularDiferenciaRegistrar();
  
};

const habilitarSubmitRegistrar = () =>{
    if (abonado.value > 0 && pedido.value && abonado.value <= (pedido.value.total - pedido.value.totalPagos)) {
        disabledSubmitRegistrar.value = false;
        color.value = '#4b5563';
            
    }else{
        disabledSubmitRegistrar.value = true;
        color.value = 'red';
     
    }
}

const registrarAnticipo = () =>{
    inicializarPagosAnticipo();
    dialogRegistrar.value = true;
    
};

const registradoEnCajaActualAbierta = (fechaRegistro) =>{
    console.log("registradoEnCajaActualAbierta");
    if (cajaAbierta.value != null && fechaRegistro >= cajaAbierta.value.fecha) {
        return true;
    } else {
        return false;
    }
};

async function guardarAnticipo() {
    const d = await existeCajaAbierta();
    console.log(d);
    if (d) {
        let fechaAnticipo = new Date();
    let ant = {total: abonado.value, fecha: fechaAnticipo, idPedido: pedido.value.id, tipoPedido: tipoPedido.value.cod};

    let anticipoCreationDTO = {anticipo: ant, pagos: pagosAnticipo.value};
    CajaServices.saveAnticipo(anticipoCreationDTO ).then((data)=> {
        getAnticipos();
        closeDialogRegistrar();
        toast.add({ severity:"success", detail: 'Anticipo registrado', life: 3000 });
    } );
    } else {
        toast.add({ severity:"error", detail: 'No existe una caja abierta', life: 3000 });
    }
};



const calcularDiferenciaRegistrar = () => {
    habilitarSubmitRegistrar();
};

const closeDialogRegistrar = (event) =>{
    if (event == false) {
        reiniciarDialogRegistrar();
    }

    if (event == null) {
        dialogRegistrar.value = false;
        reiniciarDialogRegistrar();
    }
    
}

const reiniciarDialogRegistrar = () =>{
    pedido.value = null;
    pagosAnticipo.value =[];
    color.value = '#4b5563';
    abonado.value = 0;
    disabled.value = true;
    searched.value = false;
    id.value = null;

}


const inicializarPagosAnticipo = () => {
    pago.value.formaPago = null;
    pago.value.importe = 0;
    pagosAnticipo.value.push(pago.value);

};



const verPedidos = () => {
    router.push({name: 'pedidos'});
  
  }

//Funciones Registrar Reembolso

const addRow = () => {
    pagos.value.push({formaPago:null , importe: 0});
};

const irListaPedidos = () => {
    pagos.value.push({formaPago:null , importe: 0});
};

const eliminarRow = (index) => {
    if (index > 0) {
        pagos.value.splice(index,1);
    }
    calcularAbonado();
};

const habilitarInput = (index, item) => {
    let inp = document.getElementsByName("input")[index].firstElementChild;
    if (inp.disabled  ) {
        inp.disabled = false;
    }
   
    if (abonado.value < anticipo.value.saldo ) {
        item.importe = anticipo.value.saldo - abonado.value;
    }
    calcularAbonado();
    
};

const calcularAbonado = (event) => {
    if (event !== undefined) {
        console.log(event);
        let valorActual = Number(event.value);
        let valorAnterior = Number(event.formattedValue.replaceAll(".",""));
        abonado.value = abonado.value - valorAnterior + valorActual;
    }else {
        let monto = 0 ;
        pagos.value.forEach(element => {
            monto += element.importe;
        })
        abonado.value = monto;
    }
    
    calcularDiferencia();
    
  
};

const calcularDiferencia = () => {
    habilitarSubmit();
};

const habilitarSubmit = () =>{
    if (abonado.value > anticipo.value.saldo || abonado.value<1) {
        disabledSubmit.value = true;
        color.value = 'red';
    }else{
        disabledSubmit.value = false;
        color.value = '#4b5563';
    }
}

const showDialogReembolso = (ant) =>{

    anticipo.value = ant;
        
    montoReembolsar.value = ant.saldo;
    inicializarPagosReembolso();
    
    visible.value = true;
}

const inicializarPagosReembolso = () => {
    pago.value.formaPago = null;
    pago.value.importe = 0;
    pagos.value.push(pago.value);

}; 
  
const closeDialog = (event) =>{
    if (event == false) {
        reiniciarDialog();
    }

    if (event == null) {
        visible.value = false;
        reiniciarDialog();
    }
      
}
  
const reiniciarDialog = () =>{
    pagos.value =[];
    color.value = '#4b5563';
    motivo.value = null;
    abonado.value = 0;
    disabled.value = true;
  
}
  
  
const guardarReembolso = () =>{
    if (cajaAbierta.value != null) {
        montoReembolsar.value = abonado.value;
        let fecha = new Date();
        let reembolso = {monto: montoReembolsar.value,
        fecha: fecha,
        motivo: motivo.value,
        anticipo: anticipo.value};
        
        let reembolsoCreationDTO = {reembolso: reembolso, pagos: pagos.value};
        CajaServices.saveReembolso(reembolsoCreationDTO).then((data)=> {
            
            getAnticipos();
            closeDialog();
            toast.add({ severity:"success", detail: 'Reembolso registrado', life: 3000 });
        });
    } else {
        toast.add({ severity:"error", detail: 'No existe una caja abierta', life: 3000 });
    }
    
};



//Funciones Anticipos

async function getAnticipos() {
  await AnticipoServices.obtenerAnticipos().then((data) => {
        console.log(data.data);
       anticipos.value = data.data;
   });
 
}
const confirm2 = (id) => {
   
   confirm.require({
       message: 'Eliminar el anticipo '+ id + '?',
       header: 'Confirmacion',
       icon: 'pi pi-info-circle',
       rejectLabel: 'Cancelar',
       acceptLabel: 'Eliminar',
       rejectClass: 'p-button-secondary p-button-outlined',
       acceptClass: 'p-button-danger',
       accept: () => {
           verificarEstadoAnticipo(id);
       },
       
   });
};

const messageError = (msg) => {
    console.log('messageError invocado');
    confirm.require({
        group: 'errorAnticipoUtilizado',
        header: 'Ocurrio un error',
        message: msg,

        accept: () => {
            //getDetalle();
           
            
            
        },
    });
};

const deleteAnticipo = (id) =>{
    const cantidad= 1;
    const index = anticipos.value.findIndex((loopVariable) => loopVariable.id === id);
    anticipoToDelete.value = id;
    CajaServices.deleteAnticipo(id).then((response)=>{

        anticipos.value.splice(index,cantidad);
        visibleDeleteDialog.value= false;
        toast.add({ severity: 'info', summary: 'Confirmed', detail: 'Record deleted', life: 5000 });

    })

}

const verificarEstadoAnticipo = (id) =>{
    const index = anticipos.value.findIndex((loopVariable) => loopVariable.id === id);
    
    if (anticipos.value[index].reembolsado > 0) {
        visibleDeleteDialog.value = true;
        anticipoToDelete.value = id;
    } else {
        deleteAnticipo(id);
    }
    
   
}

const isAnticipoUtilizado = (utilizado) =>{

    if (utilizado > 0) {
        return true;
    } 

    return false;
    
   
}

const username = ref('');
const password = ref('');

async function login() {
      try {
        console.log(username.value);
        console.log(password.value);
        const response = await axios.post('http://localhost:7070/api/auth/login', {
          username: username.value,
          password: password.value
        });

        // Guardar token y datos de usuario en localStorage
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('username', response.data.username);
        localStorage.setItem('roles', JSON.stringify(response.data.roles));

        const payload = JSON.parse(atob(response.data.token.split('.')[1])); // Decodifica el JWT
        const expirationTime = payload.exp * 1000; // Convertir a milisegundos
        console.log("expiracion");
        console.log(expirationTime);
    
        verPedidos();
        

      } catch (error) {
        console.log("error");
    console.log(error);
      }
    }


//Otros

onMounted(() => {

});

const resolver =  zodResolver(
    z.object({
        username: z.string().min(1, { message: 'Username is required.' }),
        password: z.string().min(1, { message: 'Password is required.' })
    })
);

const onFormSubmit = ({ valid, values }) => {
    if (valid) {

        console.log(values.username);
        username.value = values.username;
        password.value = values.password;
        login();
    }
};

</script>
<template>
    <div class="card flex" style="justify-content: center; background-color: pink; " >
    <Card style="width: 25rem; overflow: hidden; border: solid, pink; " >
 
        <template #title>
            <div class="flex" style="justify-content: center; font-weight: bolder; ">
                Log in
            </div>
           
        </template>
        <template #content>
            <div class="card flex justify-center" style="flex-direction: column;" >
        <Form :resolver @submit="onFormSubmit" class="flex flex-col gap-4 w-full sm:w-56" style="flex-direction: column;" >
            <FormField v-slot="$field" as="section" name="username" initialValue="" class="flex flex-col gap-2" style="justify-content: center;" >
                <InputText type="text" placeholder="Username" />
                <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
            </FormField>
            <FormField v-slot="$field" asChild name="password" initialValue="" class="flex flex-col gap-2" >
                <section class="flex flex-col gap-2" style="justify-content: center;">
                    <Password type="text" placeholder="Password" :feedback="false" toggleMask fluid />
                    <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{ $field.error?.message }}</Message>
                </section>
            </FormField>
            <Button type="submit" severity="secondary" label="Submit" class="flex flex-col gap-2"/>
        </Form>
    </div>
        </template>
   
    </Card>
</div>
    
</template>
