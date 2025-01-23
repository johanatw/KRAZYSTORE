/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.MovimientoCreationDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.ReembolsoCreationDTO;
import com.krazystore.krazystore.DTO.VentaCreationDTO;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface CajaService {
    Optional<CajaEntity> getCajaAbierta();
    CajaEntity abrirCaja();
    Optional<CajaEntity> findById(Long id);
    List<CajaEntity> findAll();
    void cerrarCaja(Long id);
    
    List<MovimientosDTO> findByIdCaja(Long id);
    
    EstadoPagoPedidoDTO getEstadoPagoPedidoCompra(Long id);
    EstadoPagoPedidoDTO getEstadoPagoPedidoVenta(Long id);
    
    MovimientoEntity saveMovimiento(AnticipoCreationDTO anticipoCreationDTO);
    MovimientoEntity saveMovimiento(ReembolsoCreationDTO reembolsoDTO);
    MovimientoEntity saveMovimiento(MovimientoCreationDTO movimientoDTO);
    
    MovimientoEntity savePagosFactura(MovimientoCreationDTO movimientoDTO);
    
    void deleteAnticipo(Long id);
    void deleteReembolso(Long id);
    void deleteMovimiento(Long id);
    
    List<MovimientoEntity> getFacturasPendientes();
}
