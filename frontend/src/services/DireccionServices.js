import axios from 'axios';
import router from '@/router';
const DIRECCION_API_BASE_URL = "http://localhost:7070/api/direcciones"
const token = localStorage.getItem('token');

export const DireccionServices = {

  getDireccionesCliente(id){

    return axios.get(DIRECCION_API_BASE_URL+"/direcciones/"+id,{
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
  saveDireccion(direccion) {

      return axios.post(DIRECCION_API_BASE_URL,direccion,{
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
export default { DireccionServices };