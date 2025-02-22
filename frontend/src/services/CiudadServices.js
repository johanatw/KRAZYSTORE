import axios from 'axios';
import router from '@/router';
const CIUDAD_API_BASE_URL = "http://localhost:7070/api/ciudades"
const token = localStorage.getItem('token');

export const CiudadServices = {
  obtenerCiudades() {

      return axios.get(CIUDAD_API_BASE_URL,{
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
  obtenerCiudadesByDepartamento(id) {

      return axios.get(CIUDAD_API_BASE_URL+"/departamento/"+id,{
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
export default { CiudadServices: CiudadServices };