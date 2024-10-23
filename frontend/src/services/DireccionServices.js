import axios from 'axios';
const DIRECCION_API_BASE_URL = "http://localhost:7070/api/direcciones"

export const DireccionServices = {

  getDireccionesCliente(id){
    console.log(id);
    return axios.get(DIRECCION_API_BASE_URL+"/direcciones/"+id);

  },
  saveDireccion(direccion) {
    try {

      return axios.post(DIRECCION_API_BASE_URL,direccion);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

};
export default { DireccionServices };