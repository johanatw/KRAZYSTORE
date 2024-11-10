import axios from 'axios';
const FORMAS_PAGO_API_BASE_URL = "http://localhost:7070/api/formas_pago"

export const FormasPagoServices = {
  obtenerFormasPago() {
    try {
      console.log("pagoservice");
      return axios.get(FORMAS_PAGO_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  getFormaPago(id){
    console.log(id);
    return axios.get(FORMAS_PAGO_API_BASE_URL+"/"+id);

  },

};
export default { FormasPagoServices: FormasPagoServices };