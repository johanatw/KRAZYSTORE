import axios from 'axios';
const COMPRA_API_BASE_URL = "http://localhost:7070/api/compras"
const DETALLE_API_BASE_URL = "http://localhost:7070/api/detalle_compra"

export const CompraServices = {
  obtenerCompras() {
    try {
        console.log("ciudadservice");
      return axios.get(COMPRA_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  registrarCompra(pedido){
     
    return  axios.post(COMPRA_API_BASE_URL, pedido);
},

  getCompra(id){
    console.log(id);
    return axios.get(COMPRA_API_BASE_URL+"/"+id);

  },
  getDetalleCompra(id){
    console.log(id);
    return axios.get(DETALLE_API_BASE_URL+"/"+id);

  },
  modificarCompra(id, pedido){
    
    return axios.put(COMPRA_API_BASE_URL + "/" + id, pedido);
  },

};
export default { CompraServices: CompraServices };