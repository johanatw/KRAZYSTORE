/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PedidoService {
    List<PedidoDTO> findAll();
    Optional<PedidoEntity> findById(Long id);
    PedidoEntity savePedido(PedidoEntity pedidoEntity);
    PedidoEntity updatePedido(PedidoEntity pedidoEntity, Long id);
    void updateEstadoPagoPedido(PedidoEntity pedido, EstadoEntity estado);
    int deletePedido(Long id);
    int verificarPedidoEstado(Long id);
}