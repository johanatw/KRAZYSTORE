import axios from 'axios';
const PRODUCTO_API_BASE_URL = "http://localhost:7070/api/productos"

export const ProductoServices = {
  obtenerProductos() {
    try {
      console.log("prodservice");
      return axios.get(PRODUCTO_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

  modificarPreVenta(id, cantidad) {
    try {
      return axios.put(PRODUCTO_API_BASE_URL+'/preventa?id='+id+'&cantidad='+cantidad);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

  modificarProducto(id_producto, producto){
    
    return axios.put(PRODUCTO_API_BASE_URL + "/" + id_producto, producto);
  },

  modificarExistencias(productos){
    
    return axios.put(PRODUCTO_API_BASE_URL + "/" + existencias, productos);
  },

};
export default { ProductoServices };