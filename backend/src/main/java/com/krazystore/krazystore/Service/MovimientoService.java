/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.EgresoVarioDTO;
import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.IngresoVarioDTO;
import com.krazystore.krazystore.DTO.MovimientoCreationDTO;
import com.krazystore.krazystore.DTO.MovimientoMensualDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.ReembolsoAnticipoCreationDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public interface MovimientoService {
    EstadoPagoPedidoDTO getEstadoPagoPedidoVenta(Long id);
    
    List<MovimientosDTO> findByIdCaja(Long id);
    
    MovimientoEntity saveMovimiento(AnticipoCreationDTO anticipoDTO, CajaEntity caja);
    MovimientoEntity saveMovimiento(ReembolsoAnticipoCreationDTO reembolsoDTO, CajaEntity caja);
    MovimientoEntity saveMovimiento(IngresoVarioDTO ingresoDTO, CajaEntity caja);
    MovimientoEntity saveMovimiento(EgresoVarioDTO egresoDTO, CajaEntity caja);
    MovimientoEntity saveMovimiento(VentaEntity ventaEntity);
    MovimientoEntity saveMovimiento(CompraEntity compraEntity);
    
    MovimientoEntity updateMovimiento(CompraEntity compra);
    MovimientoEntity updateMovimiento(VentaEntity venta);
            
    MovimientoEntity crearMovimiento(AnticipoEntity anticipo, CajaEntity caja);
    MovimientoEntity crearMovimiento(ReembolsoAnticipo reembolso, CajaEntity caja);
    MovimientoEntity crearMovimiento(VentaEntity venta);
    MovimientoEntity crearMovimiento(CompraEntity compraEntity);
    
    MovimientoEntity savePagosPendientes(MovimientoCreationDTO movimientoDTO, CajaEntity caja);
    MovimientoEntity saveCobrosPendientes(MovimientoCreationDTO movimientoDTO, CajaEntity caja);
    
    List<MovimientoEntity> getMovimientosPendientesDePago();
    List<MovimientoEntity> getMovimientosPendientesDeCobro();
    
    void deleteAnticipo(Long id);
    void deleteReembolsos(List<Long> ids);
    void deleteReembolso(Long id);
    void deleteMovimiento(Long id);
    void deleteVenta(Long idVenta);
    void deleteCompra(Long id);

    public List<MovimientoMensualDTO> obtenerIngresosYEgresosPorAño(Integer año);
    List<MovimientoMensualDTO> obtenerIngresosPorAño(Integer año);

    public List<Integer> obtenerAñosDisponibles();

    public List<MovimientoMensualDTO> obtenerEgresosPorAño(Integer año);
}
