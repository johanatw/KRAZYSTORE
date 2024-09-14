import axios from 'axios';
const PEDIDO_API_BASE_URL = "http://localhost:7070/api/pedidos";
const DETALLE_PEDIDO_API_BASE_URL = "http://localhost:7070/api/detalle_pedidos";

export const PedidoServices = {
 getPedidos() {
    try {
        console.log("pedidoservice");
      return axios.get(PEDIDO_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  getPedido(id){
    console.log(id);
    return axios.get(PEDIDO_API_BASE_URL+"/"+id);

  },

  

 savePedido(pedido) {
    try {
        console.log("pedido", pedido);
      return axios.post(PEDIDO_API_BASE_URL, pedido);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

  registrarDetallePedido(detallePedido){
    console.log("registrardetallepedido", detallePedido);
    return axios.post(DETALLE_PEDIDO_API_BASE_URL, detallePedido);
},

registrarDetallesPedido(idPedido, detalles){
  console.log("registrardetallepedido", detalles);
  return axios.post(DETALLE_PEDIDO_API_BASE_URL+'/'+idPedido, detalles);
},
 getDetalle(pedido) {
        
        try {
        return axios.get(DETALLE_PEDIDO_API_BASE_URL+'/detalles?idPedido='+pedido);
        
        } catch (error) {
        console.log(error.name);
        }
        
    },
    getDetallePedido(pedido) {
      console.log("detalle", pedido);
      try {
      return axios.get(DETALLE_PEDIDO_API_BASE_URL+'/detalles?idPedido='+pedido);
      
      } catch (error) {
      console.log(error.name);
      }
      
  },
    deleteDetallesPedido(idPedido){
      console.log("deletedetallespedido", idPedido);
      return axios.delete(DETALLE_PEDIDO_API_BASE_URL+"/pedido/"+idPedido);
  },

  deleteDetallesByIds(ids){
    console.log("deletedetallespedido", ids);
    return axios.delete(DETALLE_PEDIDO_API_BASE_URL+"/items/"+ids);
},

deletePedido(idPedido){
    console.log("deletepedido",idPedido);
    return axios.delete(PEDIDO_API_BASE_URL+"/"+idPedido);
},

modificarDetalle(id, detalles){
  console.log("modificardetalleid",id,"detalles", detalles);
  return axios.put(DETALLE_PEDIDO_API_BASE_URL + "/update/" + id, detalles);
},

modificarDetalles(id, detalles){
  return axios.put(DETALLE_PEDIDO_API_BASE_URL + "/update/" + id, detalles);
},
modificarPedido(id, pedido){
  console.log("modificarpedidoid",id,"detalles", pedido);
  return axios.put(PEDIDO_API_BASE_URL + "/" + id, pedido);
},
};
export default { PedidoServices: PedidoServices };