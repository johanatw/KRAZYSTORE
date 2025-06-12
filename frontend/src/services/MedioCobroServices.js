import axios from 'axios';
import router from '@/router';
const MEDIOS_COBRO_API_BASE_URL = "http://localhost:7070/api/medios_cobro"
const token = localStorage.getItem('token');


export const MedioCobroServices = {
  obtenerMediosCobro() {
    
      return axios.get(MEDIOS_COBRO_API_BASE_URL,{
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
export default { MedioCobroServices: MedioCobroServices };