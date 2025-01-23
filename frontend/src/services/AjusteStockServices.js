import axios from 'axios';
const AJUSTE_API_BASE_URL = "http://localhost:7070/api/ajustes"

export const AjusteStockServices = {
  obtenerAjustes() {
    try {
        console.log("ciudadservice");
      return axios.get(AJUSTE_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  getProductosParaAjuste() {
    try {
        console.log("ciudadservice");
      return axios.get(AJUSTE_API_BASE_URL+"/detalles");
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  registrarAjuste(pedido){
     
    return  axios.post(AJUSTE_API_BASE_URL, pedido);
},

  getAjuste(id){
    console.log(id);
    return axios.get(AJUSTE_API_BASE_URL+"/"+id);

  },

  modificarAjuste(id, pedido){
    
    return axios.put(AJUSTE_API_BASE_URL + "/" + id, pedido);
  },
  ajustar(id){
    
    return axios.put(AJUSTE_API_BASE_URL + "/ajustar/" + id);
  },

};
export default { AjusteStockServices: AjusteStockServices };