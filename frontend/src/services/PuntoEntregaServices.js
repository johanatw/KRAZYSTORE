import axios from 'axios';
import router from '@/router';
const PUNTO_ENTREGA_API_BASE_URL = "http://localhost:7070/api/puntos_entrega"
const token = localStorage.getItem('token');

export const PuntoRetiroServices = {
  obtenerPuntosRetiro() {
 
      return axios.get(PUNTO_ENTREGA_API_BASE_URL,{
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
  registrarPuntoRetiro(puntoRetiro){
  
    return  axios.post(PUNTO_ENTREGA_API_BASE_URL, puntoRetiro,{
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
getPuntoRetiro(id){

    return axios.get(PUNTO_ENTREGA_API_BASE_URL+"/"+id,{
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
  modificarPuntoRetiro(id, puntoRetiro){

    return axios.put(PUNTO_ENTREGA_API_BASE_URL + "/" + id, puntoRetiro,{
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
  eliminar(id) {
   
    return axios.delete(PUNTO_ENTREGA_API_BASE_URL + "/" + id,{
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
export default { PuntoRetiroServices: PuntoRetiroServices };