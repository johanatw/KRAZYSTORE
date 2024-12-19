import axios from 'axios';
const PEDIDO_COMPRA_API_BASE_URL = "http://localhost:7070/api/pedidos_compra"
const DETALLE_API_BASE_URL = "http://localhost:7070/api/detalle_pedido_compra"

export const PedidoCompraServices = {
  obtenerPedidos() {
    try {
        console.log("ciudadservice");
      return axios.get(PEDIDO_COMPRA_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },

  registrarPedido(pedido){
     
    return  axios.post(PEDIDO_COMPRA_API_BASE_URL, pedido);
},

  getPedido(id){
    console.log(id);
    return axios.get(PEDIDO_COMPRA_API_BASE_URL+"/"+id);

  },
  getDetallePedido(id){
    console.log(id);
    return axios.get(DETALLE_API_BASE_URL+"/"+id);

  },
  modificarPedido(id, pedido){
    console.log("modificarpedidoid",id,"detalles", pedido);
    return axios.put(PEDIDO_COMPRA_API_BASE_URL + "/" + id, pedido);
  },


};
export default { PedidoCompraServices: PedidoCompraServices };