import axios from 'axios';
import router from '@/router';
const VENTA_API_BASE_URL = "http://localhost:7070/api/ventas"
const token = localStorage.getItem('token');

export const VentaServices = {
    saveVenta(anticipoCreationDTO) {
          return axios.post(VENTA_API_BASE_URL,anticipoCreationDTO,{
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

      getVentas() {

          return axios.get(VENTA_API_BASE_URL,{
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
        
      },anularVenta(id) {

          return axios.put(VENTA_API_BASE_URL+'/anular/'+id,{},{
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
      getVenta(id){

        return axios.get(VENTA_API_BASE_URL+"/"+id,{
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
export default { VentaServices: VentaServices };