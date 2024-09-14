import axios from 'axios';
const ENVIOS_API_BASE_URL = "http://localhost:7070/api/envios"
const COSTOS_ENVIOS_API_BASE_URL = "http://localhost:7070/api/costos_envio"

export const EnvioServices = {
  obtenerEnvios() {
    try {
        console.log("envioservice");
      return axios.get(ENVIOS_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  
  obtenerCostosEnvioByCiudad(id) {
    try {
      return axios.get(COSTOS_ENVIOS_API_BASE_URL+'/ciudad?id='+id);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

};
export default { EnvioServices: EnvioServices };
