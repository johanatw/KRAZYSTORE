import axios from 'axios';
import router from '@/router';
const TIPO_IVA_API_BASE_URL = "http://localhost:7070/api/tipos_iva"
const token = localStorage.getItem('token');

export const TipoIvaServices = {
  obtenerTiposIva() {
 
      return axios.get(TIPO_IVA_API_BASE_URL,{
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
export default { TipoIvaServices: TipoIvaServices };