import axios from 'axios';
import router from '@/router';
const EMPRESAS_DELIVERY_API_BASE_URL = "http://localhost:7070/api/deliverys"
const token = localStorage.getItem('token');

export const EmpresasDeliveryServices = {
  obtenerEmpresasDelivery() {
 
      return axios.get(EMPRESAS_DELIVERY_API_BASE_URL,{
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
  registrarDelivery(delivery){
  
    return  axios.post(EMPRESAS_DELIVERY_API_BASE_URL, delivery,{
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
getDelivery(id){

    return axios.get(EMPRESAS_DELIVERY_API_BASE_URL+"/"+id,{
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
  modificarDelivery(id, delivery){

    return axios.put(EMPRESAS_DELIVERY_API_BASE_URL + "/" + id, delivery,{
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
   
    return axios.delete(EMPRESAS_DELIVERY_API_BASE_URL + "/" + id,{
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
export default { EmpresasDeliveryServices: EmpresasDeliveryServices };