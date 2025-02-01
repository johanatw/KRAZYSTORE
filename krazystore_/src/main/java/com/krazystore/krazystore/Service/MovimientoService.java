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
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public interface MovimientoService {
    EstadoPagoPedidoDTO getEstadoPagoPedidoCompra(Long id);
    EstadoPagoPedidoDTO getEstadoPagoPedidoVenta(Long id);
    
    List<MovimientosDTO> findByIdCaja(Long id);
    
    MovimientoEntity saveMovimiento(AnticipoCreationDTO anticipoDTO, CajaEntity caja);
    MovimientoEntity saveMovimiento(ReembolsoCreationDTO reembolsoDTO, CajaEntity caja);
    MovimientoEntity saveMovimiento(MovimientoCreationDTO movimientoDTO, CajaEntity caja);
    MovimientoEntity saveMovimiento(VentaEntity ventaEntity);
    MovimientoEntity saveMovimiento(CompraEntity compraEntity);
    
    MovimientoEntity updateMovimiento(CompraEntity compra);
            
    MovimientoEntity crearMovimiento(AnticipoEntity anticipo, CajaEntity caja);
    MovimientoEntity crearMovimiento(ReembolsoEntity reembolso, CajaEntity caja);
    MovimientoEntity crearMovimiento(VentaEntity venta);
    MovimientoEntity crearMovimiento(CompraEntity compraEntity);
    
    MovimientoEntity savePagosFactura(MovimientoCreationDTO movimientoDTO, CajaEntity caja);
    
    List<MovimientoEntity> getFacturasPendientes();
    
    void deleteAnticipo(Long id);
    void deleteReembolsos(List<Long> ids);
    void deleteReembolso(Long id);
    void deleteMovimiento(Long id);
    void deleteVenta(Long idVenta);
    void deleteCompra(Long id);
}
