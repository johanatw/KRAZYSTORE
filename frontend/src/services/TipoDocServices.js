import axios from 'axios';
const TIPO_DOC_API_BASE_URL = "http://localhost:7070/api/tipos_doc"

export const TipoDocServices = {
  obtenerTipoDocs() {
    try {
        console.log("tipodocservice");
      return axios.get(TIPO_DOC_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  
  obtenerTipoDocById(id) {
    try {
      return axios.get(TIPO_DOC_API_BASE_URL+'/'+id);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

};
export default { TipoDocServices: TipoDocServices };
