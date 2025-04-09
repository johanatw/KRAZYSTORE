import axios from 'axios';
import router from '@/router';
const EMPRESAS_TRANSPORTE_API_BASE_URL = "http://localhost:7070/api/envios"
const token = localStorage.getItem('token');

export const EmpresasTransporteServices = {
  obtenerEmpresasTransporte() {
 
      return axios.get(EMPRESAS_TRANSPORTE_API_BASE_URL,{
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
export default { EmpresasTransporteServices: EmpresasTransporteServices };