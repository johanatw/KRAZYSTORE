import axios from 'axios';
import router from '@/router';
const ESTADOS_API_BASE_URL = "http://localhost:7070/api/estados"

export const EstadosServices = {
  obtenerEstados() {

      return axios.get(ESTADOS_API_BASE_URL,{
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
  obtenerEstadosByTipo(tipo) {

      return axios.get(ESTADOS_API_BASE_URL+'/tipo?tipo='+tipo,{
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
export default { EstadosServices: EstadosServices };
