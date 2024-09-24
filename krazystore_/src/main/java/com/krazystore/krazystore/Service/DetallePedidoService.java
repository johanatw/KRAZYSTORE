/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetallePedidoCreationRequest;
import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DetallePedidoService {
    List<DetallePedidoEntity> findAll();
    Optional<DetallePedidoEntity> findById(Long id);
    List<DetallePedidoEntity> findByPedido(Long idPedido);
    List<DetallePedidoDTO> findByNroPedido(Long idPedido);
    Iterable<DetallePedidoEntity> saveDetalle(DetallePedidoCreationRequest detallePedidoDTO);
    Iterable<DetallePedidoEntity> saveDetallePedido(Long idPedido, List<DetallePedidoEntity> detalles);
    DetallePedidoEntity updateDetallePedido(DetallePedidoEntity detallePedido, Long id);
    Iterable<DetallePedidoEntity> updateDetallesPedido(List<DetallePedidoEntity> detallePedido, Long id)throws Exception;
    void updateDetallesFacturadas(List<DetalleVentaEntity> detalles, PedidoEntity pedido);
    void disminuirProductosFacturados(List<DetalleVentaEntity> detalles, PedidoEntity pedido);
    void deleteByPedido(Long id);
    void deleteDetallesPedido(List<Long> ids);
    void deleteDetallePedido(Long id);
}
