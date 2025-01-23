import { CajaServices } from "@/services/CajaServices";
import { ref, onMounted } from 'vue';

const caja = ref();

  export function formatearFecha(value) {
    return new Date(value).toLocaleDateString('en-US', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
};

export function formatearNumero(valor) {
  if(typeof(valor) == "number"){
    return valor.toLocaleString("de-DE");
}
};

export async function existeCajaAbierta() {
   let c = (await CajaServices.getCajaAbierta()).data;
   console.log(c);
   if (c == null ) {
    return false;
   } else {
    return true;
   }
   
}
