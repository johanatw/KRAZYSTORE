import axios from 'axios';

const CAJA_API_BASE_URL = "http://localhost:7070/api/cajas"

export const CajaServices = {
obtenerMovimientosByPedido(id) {
    try {
        console.log("ciudadservice");
      //return axios.get(MOVIMIENTO_API_BASE_URL+"/prueba/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },obtenerMovimientosByCaja(id) {
    try {
        console.log("ciudadservice");
      return axios.get(CAJA_API_BASE_URL+"/caja/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  deleteAnticipo(id){
  
    return axios.delete(CAJA_API_BASE_URL+"/anticipo/"+id);
  },
  
  deleteReembolso(id){
  
    return axios.delete(CAJA_API_BASE_URL+"/reembolso/"+id);
  },
  deleteMovimiento(id){
  
    return axios.delete(CAJA_API_BASE_URL+"/"+id);
  },
  
  savePagosMovimiento(movimientoCreationDTO) {
    try {
      console.log("movimiento id");
      console.log(movimientoCreationDTO);
      return axios.post(CAJA_API_BASE_URL+'/pagar_factura',movimientoCreationDTO);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  saveReembolso(reembolso) {
    try {
        console.log("pedido", reembolso);
      return axios.post(CAJA_API_BASE_URL+"/reembolso", reembolso);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  
  saveMovimiento(movimientoCreationDTO) {
    try {
      return axios.post(CAJA_API_BASE_URL+"/movimiento",movimientoCreationDTO);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  saveAnticipo(anticipoCreationDTO) {
    try {
      return axios.post(CAJA_API_BASE_URL+"/anticipo",anticipoCreationDTO);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  getCajas() {
    try {
      return axios.get(CAJA_API_BASE_URL);
      
    } catch (error) {
    }
    
  },
  getCajaAbierta() {
    try {
      return axios.get(CAJA_API_BASE_URL+"/caja_abierta");
      
    } catch (error) {
    }
    
  },
  abrirCaja() {
    try {
        
        
      return axios.post(CAJA_API_BASE_URL);
      
    } catch (error) {
    }
    
  },
  obtenerCajaById(id) {
    try {
        console.log("ciudadservice");
      return axios.get(CAJA_API_BASE_URL+"/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  cerraCaja(id) {
    try {
        console.log("ciudadservice");
      return axios.put(CAJA_API_BASE_URL+"/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  
  
  obtenerFacturasPendientes() {
    try {
        
      return axios.get(CAJA_API_BASE_URL+"/pendientes");
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  obtenerPagosPedido(id) {
    try {
        
      return axios.get(CAJA_API_BASE_URL+"/estado_pagos_pedido_venta/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  obtenerPagosPedidoCompra(id) {
    try {
        
      return axios.get(CAJA_API_BASE_URL+"/estado_pagos_pedido_compra/"+id);
      
    } catch (error) {
      console.log("hola");
      console.log(error.name);
    }
    
  },
  saveVenta(ventaCreationDTO) {
    try {
      return axios.post(CAJA_API_BASE_URL+"/venta", ventaCreationDTO);
      
    } catch (error) {
      console.log(error.name);
    }
    
  }

};
export default { CajaServices: CajaServices };