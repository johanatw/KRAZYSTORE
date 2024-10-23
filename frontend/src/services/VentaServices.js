import axios from 'axios';
const VENTA_API_BASE_URL = "http://localhost:7070/api/ventas"

export const VentaServices = {
    saveVenta(anticipoCreationDTO) {
        try {
            console.log("anticipo", anticipoCreationDTO);
            
          return axios.post(VENTA_API_BASE_URL,anticipoCreationDTO);
          
        } catch (error) {
          console.log(error.name);
        }
        
      },

      getVentas() {
        try {
            //console.log("anticipo", anticipoCreationDTO);
            
          return axios.get(VENTA_API_BASE_URL);
          
        } catch (error) {
          console.log(error.name);
        }
        
      },anularVenta(id) {
        try {
            
            
          return axios.post(VENTA_API_BASE_URL+'/anular/'+id);
          
        } catch (error) {
          console.log(error.name);
        }
        
      },
      getVenta(id){
        console.log(id);
        return axios.get(VENTA_API_BASE_URL+"/"+id);
    
      },

};
export default { VentaServices: VentaServices };