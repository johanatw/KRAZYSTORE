import axios from 'axios';
import router from '@/router';
const PROVEEDORES_API_BASE_URL = "http://localhost:7070/api/proveedores"
const token = localStorage.getItem('token');

export const ProveedorServices = {
  obtenerProveedores() {

      return axios.get(PROVEEDORES_API_BASE_URL,{
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
  obtenerProveedoresProductos() {

      return axios.get(PROVEEDORES_API_BASE_URL+"/proveedores_productos",{
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
  obtenerProveedoresNacionalesProductos() {

      return axios.get(PROVEEDORES_API_BASE_URL+"/proveedores_nacionales_productos",{
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
  obtenerProveedoresImportacion() {

      return axios.get(PROVEEDORES_API_BASE_URL+"/proveedores_importacion",{
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
  getProveedor(id){

    return axios.get(PROVEEDORES_API_BASE_URL+"/"+id,{
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
  registrarProveedor(proveedor){

    return  axios.post(PROVEEDORES_API_BASE_URL, proveedor,{
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
modificarProveedor(id, proveedor){

  return axios.put(PROVEEDORES_API_BASE_URL + "/" + id, proveedor,{
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

  return axios.delete(PROVEEDORES_API_BASE_URL + "/" + id,{
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
export default { ProveedorServices: ProveedorServices };