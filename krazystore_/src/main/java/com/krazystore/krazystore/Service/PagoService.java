/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetallePagoPedidoDTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */

public interface PagoService {
    List<PagoEntity> findAll();
    Optional<PagoEntity> findById(Long id);
    PagoEntity savePago(PagoEntity pagoEntity);
    Iterable<PagoEntity> savePagos(MovimientoEntity movimiento, List<PagoEntity> pagos);
    Iterable<PagoEntity> savePagos(VentaEntity venta, List<PagoEntity> pagos);
    Iterable<PagoEntity> revertirPagos(MovimientoEntity movimiento, List<PagoEntity> pagos);
    List<PagoEntity> findByIdMovimiento(Long id);
    Optional<PedidoMontoPagadoDTO> getPagosPedido(Long id);
    PagoEntity updatePago(PagoEntity pagoEntity, Long id);
    void deletePagosByMovimientos(List<Long> ids);
    void deletePagosByMovimiento(Long id);
    void deletePago(Long id);
}
