import axios from 'axios';
import router from '@/router';
const PRECIOS_API_BASE_URL = "http://localhost:7070/api/precios_compra"


export const PreciosCompraServices = {
  obtenerPrecios() {

      return axios.get(PRECIOS_API_BASE_URL,{
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
  getPrecio(id){

    return axios.get(PRECIOS_API_BASE_URL+"/"+id,{
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
  getPreciosProducto(id){

    return axios.get(PRECIOS_API_BASE_URL+"/producto/"+id,{
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

  registrarPrecio(precio){
  
    return  axios.post(PRECIOS_API_BASE_URL, precio,{
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
modificarPrecio(id, precio){

  return axios.put(PRECIOS_API_BASE_URL + "/" + id, precio,{
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
 
  return axios.delete(PRECIOS_API_BASE_URL + "/" + id,{
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
export default { PreciosCompraServices: PreciosCompraServices };