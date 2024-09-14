import axios from 'axios';
const REEMBOLSO_API_BASE_URL = "http://localhost:7070/api/reembolsos"

export const ReembolsoServices = {
  obtenerReembolsos() {
    try {
      return axios.get(REEMBOLSO_API_BASE_URL);
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
    saveReembolso(reembolso) {
        try {
            console.log("pedido", reembolso);
          return axios.post(REEMBOLSO_API_BASE_URL, reembolso);
          
        } catch (error) {
          console.log(error.name);
        }
        
      },
      getReembolsos(idAnticipo) {
        //console.log("detalle", pedido);
        try {
          console.log("7");
        return axios.get(REEMBOLSO_API_BASE_URL+'/reembolsos/'+idAnticipo);
        
        } catch (error) {
          console.log("8");
        console.log(error.name);
        }
        
    },
    deleteReembolso(id){
  
      return axios.delete(REEMBOLSO_API_BASE_URL+"/"+id);
    },

};
export default { ReembolsoServices: ReembolsoServices };