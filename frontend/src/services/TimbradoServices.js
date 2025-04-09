import axios from 'axios';
import router from '@/router';
const TIMBRADO_API_BASE_URL = "http://localhost:7070/api/timbrados"
const token = localStorage.getItem('token');

export const TimbradoServices = {
  obtenerTimbradoVigente() {
 
      return axios.get(TIMBRADO_API_BASE_URL+'/timbrado_vigente',{
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
export default { TimbradoServices: TimbradoServices };