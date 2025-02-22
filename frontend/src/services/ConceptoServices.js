import axios from 'axios';
import router from '@/router';
const CONCEPTO_API_BASE_URL = "http://localhost:7070/api/conceptos"
const token = localStorage.getItem('token');

export const ConceptoServices = {
  obtenerConceptosByTipo(tipo) {
  
      return axios.get(CONCEPTO_API_BASE_URL+'/tipo/'+tipo,{
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
      }).catch(error => {
        if (error.response && error.response.status === 401) {
          localStorage.removeItem("token");  // Eliminar el token expirado
          router.push({name: 'home'});
          return Promise.reject(error);
      }
      return Promise.reject(error);
    })
    
  }
    
  

};
export default { ConceptoServices: ConceptoServices };