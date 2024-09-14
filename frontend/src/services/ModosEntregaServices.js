import axios from 'axios';
const MODOS_ENTREGA_API_BASE_URL = "http://localhost:7070/api/modos_entrega"

export const ModosEntregaServices = {
  obtenerModosEntrega() {
    try {
      console.log("entregaservice");
      return axios.get(MODOS_ENTREGA_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  getModoEntrega(id){
    console.log(id);
    return axios.get(MODOS_ENTREGA_API_BASE_URL+"/"+id);

  },

};
export default { ModosEntregaServices: ModosEntregaServices };