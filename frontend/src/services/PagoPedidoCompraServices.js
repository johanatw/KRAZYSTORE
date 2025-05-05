import axios from 'axios';
import router from '@/router';
const PAGO_PEDIDO_COMPRA_API_BASE_URL = "http://localhost:7070/api/pago_pedido_compra"

export const PagoPedidoCompraServices = {
  obtenerPagosPedidoCompra(id) {

      return axios.get(PAGO_PEDIDO_COMPRA_API_BASE_URL,{
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
  findPagosAplicarByIdPedidoCompra(id) {

    return axios.get(PAGO_PEDIDO_COMPRA_API_BASE_URL+'/pedido/'+id,{
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
export default { PagoPedidoCompraServices: PagoPedidoCompraServices };