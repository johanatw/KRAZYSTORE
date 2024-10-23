import { CajaServices } from "@/services/CajaServices";
import { ref, onMounted } from 'vue';

const caja = ref();

  export function formatearNumero(valor) {
    if(typeof(valor) == "number"){
      return valor.toLocaleString("de-DE");
  }

  let fecha = new Date(valor);
  let fechaFormateada = fecha.getDate() + '/' + (fecha.getMonth()+1) + '/' +fecha.getFullYear()+' '+ fecha.getHours()+':'+fecha.getMinutes()+':'+fecha.getSeconds();
  return fechaFormateada;
}


export async function existeCajaAbierta() {
   let c = (await CajaServices.getCajaAbierta()).data;
   console.log(c);
   if (c == null ) {
    return false;
   } else {
    return true;
   }
   
}
