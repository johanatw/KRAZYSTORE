import axios from 'axios';
const ESTADOS_API_BASE_URL = "http://localhost:7070/api/estados"

export const EstadosServices = {
  obtenerEstados() {
    try {
      return axios.get(ESTADOS_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  obtenerEstadosByTipo(tipo) {
    try {
      console.log("obtenerservice");
      console.log(tipo);
      return axios.get(ESTADOS_API_BASE_URL+'/tipo?tipo='+tipo);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

};
export default { EstadosServices: EstadosServices };
