import axios from 'axios';
const DEPARTAMENTO_API_BASE_URL = "http://localhost:7070/api/departamentos"

export const DepartamentoServices = {
  obtenerDepartamentos() {
    try {
      console.log("depservice");
      return axios.get(DEPARTAMENTO_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

};
export default { DepartamentoServices: DepartamentoServices };
