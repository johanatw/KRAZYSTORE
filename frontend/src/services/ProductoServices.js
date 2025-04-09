import axios from 'axios';
import router from '@/router';
const PRODUCTO_API_BASE_URL = "http://localhost:7070/api/productos"
const CATEGORIA_API_BASE_URL = "http://localhost:7070/api/categorias"
const token = localStorage.getItem('token');

export const ProductoServices = {
  obtenerProductos() {

      return axios.get(PRODUCTO_API_BASE_URL,{
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
      }).catch(error => {
        console.log(error.response.status );
        if (error.response && error.response.status === 401) {
          localStorage.removeItem("token");  // Eliminar el token expirado
          router.push({name: 'home'});
          return Promise.reject(error);
      }return Promise.reject(error);
     
    })
    
  },

  modificarPreVenta(id, cantidad) {

      return axios.put(PRODUCTO_API_BASE_URL+'/preventa?id='+id+'&cantidad='+cantidad,{
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

  modificarProducto(id_producto, producto){

    return axios.put(PRODUCTO_API_BASE_URL + "/" + id_producto, producto,{
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

  obtenerProducto(id){

    return axios.get(PRODUCTO_API_BASE_URL + "/" + id,{
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
  saveProducto(producto) {
   
    return axios.post(PRODUCTO_API_BASE_URL, producto,{
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
obtenerProductosByValor(valor) {

  return axios.get(PRODUCTO_API_BASE_URL+'/nombre?nombre='+valor,{
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

  modificarExistencias(productos){

    return axios.put(PRODUCTO_API_BASE_URL + "/" + existencias, productos,{
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
  obtenerCategorias() {

      return axios.get(CATEGORIA_API_BASE_URL,{
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
export default { ProductoServices };