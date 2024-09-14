import axios from 'axios';
const ANTICIPO_API_BASE_URL = "http://localhost:7070/api/anticipos"
const DETALLE_ANTICIPO_API_BASE_URL = "http://localhost:7070/api/detalles_anticipo";


export const AnticipoServices = {
  obtenerAnticipos() {
    try {
      return axios.get(ANTICIPO_API_BASE_URL);
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  saveAnticipo(anticipoCreationDTO) {
    try {
        console.log("anticipo", anticipoCreationDTO);
        
      return axios.post(ANTICIPO_API_BASE_URL,anticipoCreationDTO);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

  getDetalleAnticipo(idAnticipo) {
    //console.log("detalle", pedido);
    try {
      console.log("7");
    return axios.get(DETALLE_ANTICIPO_API_BASE_URL+'/detalle?idAnticipo='+idAnticipo);
    
    } catch (error) {
      console.log("8");
    console.log(error.name);
    }
    
},
getCantidadPagado(idPedido, idProducto) {
  //console.log("detalle", pedido);
  try {
    console.log("7");
  return axios.get(DETALLE_ANTICIPO_API_BASE_URL+'/pagos?idPedido='+idPedido+'&idProducto='+idProducto);
  
  } catch (error) {
    console.log("8");
  console.log(error.name);
  }
  
},
  obtenerAnticipo(id) {
    try {
      console.log("5");
        //console.log("ciudadservice");
      return axios.get(ANTICIPO_API_BASE_URL+"/"+id);
      
    } catch (error) {
      console.log("6");
      //console.log("hola");
      //console.log(error.name);
    }
    
  },
  obtenerAnticipoPdf(id) {
    try {
      console.log("5");
      console.log("pdfservice");
        //console.log("ciudadservice");
      return axios.get(ANTICIPO_API_BASE_URL+"/pdf/"+id);
      
    } catch (error) {
      console.log("6");
      //console.log("hola");
      //console.log(error.name);
    }
    
  },
  getAnticipos(idPedido) {
    //console.log("detalle", pedido);
    try {
      console.log("7");
    return axios.get(ANTICIPO_API_BASE_URL+'/pedido/'+idPedido);
    
    } catch (error) {
      console.log("8");
    console.log(error.name);
    }
    
},
deleteAnticipo(id){
  
  return axios.delete(ANTICIPO_API_BASE_URL+"/"+id);
},
deleteAnticipoReembolsos(id){
  
  return axios.delete(ANTICIPO_API_BASE_URL+"/reembolsos/"+id);
},
verificarEstadoAnticipo(id){
  
  return axios.get(ANTICIPO_API_BASE_URL+"/verificar/"+id);
},

};
export default { AnticipoServices: AnticipoServices };