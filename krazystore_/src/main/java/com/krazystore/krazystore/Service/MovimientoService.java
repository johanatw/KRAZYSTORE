/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface MovimientoService {
    List<MovimientosDTO> findAll();
    List<PRUEBADTO> pr(Long id);
    Optional<MovimientoEntity> findById(Long id);
    MovimientoEntity saveMovimiento(MovimientoEntity movimientoEntity);
    MovimientoEntity saveMovimiento(MovimientoEntity movimientoEntity, List<PagoEntity> pagos);
    MovimientoEntity saveMovimiento(AnticipoEntity anticipoEntity, List<PagoEntity> pagos);
    MovimientoEntity saveMovimiento(ReembolsoEntity reembolso, List<PagoEntity> pagos);
    void anularVenta(VentaEntity ventaEntity);
    MovimientoEntity updateMovimiento(MovimientoEntity movimientoEntity, Long id);
    MovimientoEntity saveMovimiento(VentaEntity ventaEntity);
    void saveMovimientos(List<MovimientoEntity> movimientos);
    void deleteMovimiento(Long id);
    MovimientoEntity crearMovimiento(AnticipoEntity anticipo);
    MovimientoEntity crearMovimiento(ReembolsoEntity reembolso);
    EstadoEntity getEstadoPagoPedido(Long idPedido);
    MovimientoEntity crearMovimiento(VentaEntity venta, boolean anular);
    void deleteAnticipoConReembolsos(Long id);
    void deleteReembolso(Long id);
    void deleteAnticipo(Long id);
    PedidoMontoPagadoDTO getMontoPagadoPedido(Long id);
    Long validarEliminacionPedido(Long id);
    List<MovimientoEntity> getFacturasPendientes();
    MovimientoEntity savePagosMovimiento(MovimientoEntity movimientoEntity, List<PagoEntity> pagos);
    boolean getEstadoPago(VentaEntity ventaEntity);
    List<MovimientosDTO> findByIdCaja(Long id);
}
