import axios from 'axios';
const CIUDAD_API_BASE_URL = "http://localhost:7070/api/ciudades"

export const CiudadServices = {
  obtenerCiudades() {
    try {
        console.log("ciudadservice");
      return axios.get(CIUDAD_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  obtenerCiudadesByDepartamento(id) {
    try {
        console.log("ciudadservice");
      return axios.get(CIUDAD_API_BASE_URL+"/departamento/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },

};
export default { CiudadServices: CiudadServices };