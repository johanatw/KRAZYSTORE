import axios from 'axios';
const PAGO_API_BASE_URL = "http://localhost:7070/api/pagos"

export const PagoServices = {
  obtenerPagosPedido(id) {
    try {
        
      return axios.get(PAGO_API_BASE_URL+"/pagos/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  }

};
export default { PagoServices: PagoServices };