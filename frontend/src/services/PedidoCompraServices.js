import axios from 'axios';
import router from '@/router';
const PEDIDO_COMPRA_API_BASE_URL = "http://localhost:7070/api/pedidos_compra"
const DETALLE_API_BASE_URL = "http://localhost:7070/api/detalle_pedido_compra"
const token = localStorage.getItem('token');

export const PedidoCompraServices = {
  obtenerPedidos() {

      return axios.get(PEDIDO_COMPRA_API_BASE_URL,{
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

  registrarPedido(pedido){
 
    return  axios.post(PEDIDO_COMPRA_API_BASE_URL, pedido,{
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

  getPedido(id){

    return axios.get(PEDIDO_COMPRA_API_BASE_URL+"/"+id,{
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
  getDetallePedido(id){

    return axios.get(DETALLE_API_BASE_URL+"/"+id,{
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
  modificarPedido(id, pedido){

    return axios.put(PEDIDO_COMPRA_API_BASE_URL + "/" + id, pedido,{
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
  deletePedido(id){
    
    return axios.delete(PEDIDO_COMPRA_API_BASE_URL+"/"+id,{
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
export default { PedidoCompraServices: PedidoCompraServices };