import axios from 'axios';
import router from '@/router';
const MODOS_ENTREGA_API_BASE_URL = "http://localhost:7070/api/modos_entrega"

export const ModosEntregaServices = {
  obtenerModosEntrega() {
 
      return axios.get(MODOS_ENTREGA_API_BASE_URL,{
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
  getModoEntrega(id){

    return axios.get(MODOS_ENTREGA_API_BASE_URL+"/"+id,{
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
export default { ModosEntregaServices: ModosEntregaServices };