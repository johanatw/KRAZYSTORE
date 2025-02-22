import axios from 'axios';
import router from '@/router';
const REEMBOLSO_API_BASE_URL = "http://localhost:7070/api/reembolsos"
const token = localStorage.getItem('token');

export const ReembolsoServices = {
  obtenerReembolsos() {

      return axios.get(REEMBOLSO_API_BASE_URL,{
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
    saveReembolso(reembolso) {
 
          return axios.post(REEMBOLSO_API_BASE_URL, reembolso,{
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
      getReembolsos(idAnticipo) {

        return axios.get(REEMBOLSO_API_BASE_URL+'/reembolsos/'+idAnticipo,{
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
    deleteReembolso(id){

      return axios.delete(REEMBOLSO_API_BASE_URL+"/"+id,{
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
export default { ReembolsoServices: ReembolsoServices };