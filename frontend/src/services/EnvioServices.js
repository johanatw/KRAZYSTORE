import axios from 'axios';
import router from '@/router';
const ENVIOS_API_BASE_URL = "http://localhost:7070/api/envios"
const COSTOS_ENVIOS_API_BASE_URL = "http://localhost:7070/api/costos_envio"
const token = localStorage.getItem('token');

export const EnvioServices = {
  obtenerEnvios() {

      return axios.get(ENVIOS_API_BASE_URL,{
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
  
  obtenerCostosEnvioByCiudad(id) {
  
      return axios.get(COSTOS_ENVIOS_API_BASE_URL+'/ciudad?id='+id,{
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
export default { EnvioServices: EnvioServices };
