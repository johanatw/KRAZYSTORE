import axios from 'axios';
import router from '@/router';
const CATEGORIA_API_BASE_URL = "http://localhost:7070/api/categorias"
const SUB_CATEGORIA_API_BASE_URL = "http://localhost:7070/api/sub_categorias"
const token = localStorage.getItem('token');

export const CategoriaServices = {

  obtenerCategorias() {

      return axios.get(CATEGORIA_API_BASE_URL,{
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

  obtenerSubCatByIdCat(id) {

    return axios.get(SUB_CATEGORIA_API_BASE_URL+'/categoria/'+id,{
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
export default { CategoriaServices: CategoriaServices };