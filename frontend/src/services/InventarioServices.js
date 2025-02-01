import axios from 'axios';
const INVENTARIO_API_BASE_URL = "http://localhost:7070/api/inventarios"

export const InventarioServices = {
  obtenerInventarios() {
    try {
        console.log("ciudadservice");
      return axios.get(INVENTARIO_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  getDetallesInventarioIniciales() {
    try {
        console.log("ciudadservice");
      return axios.get(INVENTARIO_API_BASE_URL+"/detalles");
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  obtenerDetallesCompletos(id) {
    try {
        console.log("ciudadservice");
      return axios.get(INVENTARIO_API_BASE_URL+"/detalles/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  registrarInventario(pedido){
     
    return  axios.post(INVENTARIO_API_BASE_URL, pedido);
},

  getInventario(id){
    console.log(id);
    return axios.get(INVENTARIO_API_BASE_URL+"/"+id);

  },

  modificarInventario(id, pedido){
    
    return axios.put(INVENTARIO_API_BASE_URL + "/" + id, pedido);
  },
  ajustarInventario(id){
    
    return axios.put(INVENTARIO_API_BASE_URL + "/ajustar/" + id);
  },

  finalizarInventario(id){
    
    return axios.put(INVENTARIO_API_BASE_URL + "/finalizar/" + id);
  },

};
export default { InventarioServices: InventarioServices };