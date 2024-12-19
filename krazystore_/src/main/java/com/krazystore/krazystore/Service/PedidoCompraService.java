/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.PedidoCompraCreationDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PedidoCompraService {
    List<PedidoCompraEntity> findAll();
    Optional<PedidoCompraEntity> findById(Long id);
    PedidoCompraEntity savePedido (PedidoCompraCreationDTO pedido) throws Exception;
    PedidoCompraEntity updatePedido(PedidoCompraCreationDTO pedido, Long id) throws Exception;
    void deletePedido(Long id);
    void actualizarCantidadesRecepcionadas(Long idPedido) throws Exception;
    void modificarCantidadesRecepcionadas(List<DetalleRecepcion> nuevoDetalleRecepcion,List<DetalleRecepcion> anteriorDetalleRecepcion);
}
