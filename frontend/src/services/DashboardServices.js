import axios from 'axios';
import router from '@/router';
const DASHBOARD_API_BASE_URL = "http://localhost:7070/api/dashboard"
const token = localStorage.getItem('token');

export const DashboardServices = {
  obtenerDashboard(año) {
  
      return axios.get(DASHBOARD_API_BASE_URL+'?año='+año,{
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

  obtenerAñosDisponibles() {
    return axios.get(DASHBOARD_API_BASE_URL+'/años_disponibles',{
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

  obtenerVentasPorCategoriaChart() {
    let mes = '2025-05';
    return axios.get(DASHBOARD_API_BASE_URL+'/ventas_por_categorias?mes='+mes,{
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
obtenerTop10ProductosVendidos() {
    let mes = '2025-05';
    return axios.get(DASHBOARD_API_BASE_URL+'/top_productos?mes='+mes,{
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
obtenerIngresosPorAño(año) {
    return axios.get(DASHBOARD_API_BASE_URL+'/ingresos_año?año='+año,{
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
obtenerEgresosPorAño(año) {
    return axios.get(DASHBOARD_API_BASE_URL+'/egresos_año?año='+año,{
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
export default { DashboardServices: DashboardServices };