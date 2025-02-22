import axios from 'axios';
import router from '@/router';
const DEPARTAMENTO_API_BASE_URL = "http://localhost:7070/api/departamentos"
const token = localStorage.getItem('token');

export const DepartamentoServices = {
  obtenerDepartamentos() {

      return axios.get(DEPARTAMENTO_API_BASE_URL,{
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
export default { DepartamentoServices: DepartamentoServices };
