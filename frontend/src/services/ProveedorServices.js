import axios from 'axios';
const PROVEEDORES_API_BASE_URL = "http://localhost:7070/api/proveedores"

export const ProveedorServices = {
  obtenerProveedores() {
    try {
        console.log("ciudadservice");
      return axios.get(PROVEEDORES_API_BASE_URL);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  getProveedor(id){
    console.log(id);
    return axios.get(PROVEEDORES_API_BASE_URL+"/"+id);

  },
  registrarProveedor(proveedor){
     
    return  axios.post(PROVEEDORES_API_BASE_URL, proveedor);
},
modificarProveedor(id, proveedor){
  console.log("id",id,"persona", proveedor);
  return axios.put(PROVEEDORES_API_BASE_URL + "/" + id, proveedor);
},


eliminar(id) {
  return axios.delete(PROVEEDORES_API_BASE_URL + "/" + id);
},

};
export default { ProveedorServices: ProveedorServices };