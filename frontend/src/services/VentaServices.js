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
        
      },

};
export default { VentaServices: VentaServices };