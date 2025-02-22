import axios from 'axios';
import router from '@/router';
const FORMAS_PAGO_API_BASE_URL = "http://localhost:7070/api/formas_pago"
const token = localStorage.getItem('token');


export const FormasPagoServices = {
  obtenerFormasPago() {
    
      return axios.get(FORMAS_PAGO_API_BASE_URL,{
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
      }).catch(error => {
        if (error.response && error.response.status === 401) {
          localStorage.removeItem("token");  // Eliminar el token expirado
          router.push({name: 'home'});
          return Promise.reject(error);
      }
      return Promise.reject(error);
    })
    
  },
  getFormaPago(id){
    console.log(id);

    return axios.get(FORMAS_PAGO_API_BASE_URL+"/"+id,{
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    }).catch(error => {
      if (error.response && error.response.status === 401) {
        localStorage.removeItem("token");  // Eliminar el token expirado
        router.push({name: 'home'});
        return Promise.reject(error);
    }
    return Promise.reject(error);
  })

  },

};
export default { FormasPagoServices: FormasPagoServices };