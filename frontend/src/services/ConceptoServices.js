import axios from 'axios';
const CONCEPTO_API_BASE_URL = "http://localhost:7070/api/conceptos"

export const ConceptoServices = {
  obtenerConceptosByTipo(tipo) {
    try {
        console.log("ciudadservice");
      return axios.get(CONCEPTO_API_BASE_URL+'/tipo/'+tipo);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  }
    
  

};
export default { ConceptoServices: ConceptoServices };