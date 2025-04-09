import axios from 'axios';
import router from '@/router';
const TIPO_DOC_API_BASE_URL = "http://localhost:7070/api/tipos_doc"
const TIPO_DOC_FISCAL_API_BASE_URL = "http://localhost:7070/api/tipos_doc_fiscal"
const token = localStorage.getItem('token');

export const TipoDocServices = {
  obtenerTipoDocs() {
 
      return axios.get(TIPO_DOC_API_BASE_URL,{
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
    
  },
  
  obtenerTipoDocById(id) {

      return axios.get(TIPO_DOC_API_BASE_URL+'/'+id,{
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
    
  },
  obtenerTiposDocFiscal() {
 
    return axios.get(TIPO_DOC_FISCAL_API_BASE_URL,{
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
  
},

};
export default { TipoDocServices: TipoDocServices };
