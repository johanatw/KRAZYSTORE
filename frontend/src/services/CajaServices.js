import axios from 'axios';
const MOVIMIENTO_API_BASE_URL = "http://localhost:7070/api/movimientos"

export const CajaServices = {
  obtenerMovimientos() {
    try {
        console.log("ciudadservice");
      return axios.get(MOVIMIENTO_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },obtenerMovimientosByPedido(id) {
    try {
        console.log("ciudadservice");
      return axios.get(MOVIMIENTO_API_BASE_URL+"/prueba/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  deleteAnticipo(id){
  
    return axios.delete(MOVIMIENTO_API_BASE_URL+"/anticipo/"+id);
  },
  deleteAnticipoReembolsos(id){
    
    return axios.delete(MOVIMIENTO_API_BASE_URL+"/reembolsos/anticipo/"+id);
  },
  saveAnticipo(anticipoCreationDTO) {
    try {
        
        
      return axios.post(MOVIMIENTO_API_BASE_URL+"/anticipo",anticipoCreationDTO);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  deleteReembolso(id){
  
    return axios.delete(MOVIMIENTO_API_BASE_URL+"/reembolso/"+id);
  },
  saveReembolso(reembolso) {
    try {
        console.log("pedido", reembolso);
      return axios.post(MOVIMIENTO_API_BASE_URL+"/reembolso", reembolso);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  
  saveMovimiento(movimientoCreationDTO) {
    try {
        
        
      return axios.post(MOVIMIENTO_API_BASE_URL,movimientoCreationDTO);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

};
export default { CajaServices: CajaServices };