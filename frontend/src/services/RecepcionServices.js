import axios from 'axios';
const RECEPCION_API_BASE_URL = "http://localhost:7070/api/recepciones"
const DETALLE_API_BASE_URL = "http://localhost:7070/api/detalle_recepcion"

export const RecepcionServices = {
  obtenerRecepciones() {
    try {
        console.log("ciudadservice");
      return axios.get(RECEPCION_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  registrarRecepcion(pedido){
     
    return  axios.post(RECEPCION_API_BASE_URL, pedido);
},

  getRecepcion(id){
    console.log(id);
    return axios.get(RECEPCION_API_BASE_URL+"/"+id);

  },
  getDetalleRecepcion(id){
    console.log(id);
    return axios.get(DETALLE_API_BASE_URL+"/"+id);

  },
  modificarRecepcion(id, pedido){
    
    return axios.put(RECEPCION_API_BASE_URL+ "/" + id, pedido);
  },

};
export default { RecepcionServices: RecepcionServices };