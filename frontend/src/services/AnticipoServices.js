import axios from 'axios';
import router from '@/router';
const ANTICIPO_API_BASE_URL = "http://localhost:7070/api/anticipos"
const DETALLE_ANTICIPO_API_BASE_URL = "http://localhost:7070/api/detalles_anticipo";
const token = localStorage.getItem('token');

export const AnticipoServices = {
  obtenerAnticipos() {
   
      return axios.get(ANTICIPO_API_BASE_URL,{
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
  saveAnticipo(anticipoCreationDTO) {

      return axios.post(ANTICIPO_API_BASE_URL,anticipoCreationDTO,{
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

  getDetalleAnticipo(idAnticipo) {
    //console.log("detalle", pedido);
 
    return axios.get(DETALLE_ANTICIPO_API_BASE_URL+'/detalle?idAnticipo='+idAnticipo,{
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
getCantidadPagado(idPedido, idProducto) {
  //console.log("detalle", pedido);

    console.log("7");
  return axios.get(DETALLE_ANTICIPO_API_BASE_URL+'/pagos?idPedido='+idPedido+'&idProducto='+idProducto,{
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
  obtenerAnticipo(id) {
  
        //console.log("ciudadservice");
      return axios.get(ANTICIPO_API_BASE_URL+"/"+id,{
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
  obtenerAnticipoPdf(id) {

        //console.log("ciudadservice");
      return axios.get(ANTICIPO_API_BASE_URL+"/pdf/"+id,{
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
  getAnticiposByIdPedidoVenta(idPedido) {
    //console.log("detalle", pedido);

    return axios.get(ANTICIPO_API_BASE_URL+'/pedido_venta/'+idPedido,{
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
getAnticiposByIdRecepcion(id) {

  return axios.get(ANTICIPO_API_BASE_URL+'/recepcion/'+id,{
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
deleteAnticipo(id){
  
  return axios.delete(ANTICIPO_API_BASE_URL+"/"+id,{
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
deleteAnticipoReembolsos(id){
  
  return axios.delete(ANTICIPO_API_BASE_URL+"/reembolsos/"+id,{
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
verificarEstadoAnticipo(id){
 
  return axios.get(ANTICIPO_API_BASE_URL+"/verificar/"+id,{
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
export default { AnticipoServices: AnticipoServices };