import axios from 'axios';
import router from '@/router';
const PEDIDO_API_BASE_URL = "http://localhost:7070/api/pedidos";
const DETALLE_PEDIDO_API_BASE_URL = "http://localhost:7070/api/detalle_pedidos";
const token = localStorage.getItem('token');

export const PedidoServices = {
 async getPedidos() {
  try {
    const response = await axios.get(PEDIDO_API_BASE_URL,{
      headers: {
        'Authorization': 'Bearer ' +  localStorage.getItem('token')
      }
    })
    return response;
  } catch (error) {
    console.log(error);
    if (error.response && error.response.status === 401) {
      localStorage.removeItem("token");  // Eliminar el token expirado
      router.push({name: 'home'});
      return;
    }
    throw error;
    
    
  }
    
  },
  getPedido(id){
 
    return axios.get(PEDIDO_API_BASE_URL+"/"+id,{
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

  

 savePedido(pedido) {

      return axios.post(PEDIDO_API_BASE_URL, pedido,{
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

  registrarDetallePedido(detallePedido){

    return axios.post(DETALLE_PEDIDO_API_BASE_URL, detallePedido,{
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

registrarDetallesPedido(idPedido, detalles){

  return axios.post(DETALLE_PEDIDO_API_BASE_URL+'/'+idPedido, detalles,{
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
 getDetalle(pedido) {
        

        return axios.get(DETALLE_PEDIDO_API_BASE_URL+'/detalles?idPedido='+pedido,{
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
    getDetallePedido(pedido) {

      return axios.get(DETALLE_PEDIDO_API_BASE_URL+'/detalles?idPedido='+pedido,{
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
    deleteDetallesPedido(idPedido){

      return axios.delete(DETALLE_PEDIDO_API_BASE_URL+"/pedido/"+idPedido,{
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

  deleteDetallesByIds(ids){
  
    return axios.delete(DETALLE_PEDIDO_API_BASE_URL+"/items/"+ids,{
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

deletePedido(idPedido){

    return axios.delete(PEDIDO_API_BASE_URL+"/"+idPedido,{
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

modificarDetalle(id, detalles){

  return axios.put(DETALLE_PEDIDO_API_BASE_URL + "/update/" + id, detalles,{
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

modificarDetalles(id, detalles){

  return axios.put(DETALLE_PEDIDO_API_BASE_URL + "/update/" + id, detalles,{
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

  return axios.put(PEDIDO_API_BASE_URL + "/" + id, pedido,{
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
cancelarPedido(id, pedido){
  console.log(id);
  console.log(pedido);
  return axios.put(PEDIDO_API_BASE_URL + "/cancelar/" + id, {},{
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
export default { PedidoServices: PedidoServices };