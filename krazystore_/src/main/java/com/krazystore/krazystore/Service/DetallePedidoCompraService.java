/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DetallePedidoCompraService {
    List<DetallePedidoCompra> findByIdPedido(Long id);
    List<DetallePedidoCompraDTO> findDTOByIdPedido(Long id);
    Long getCantProductosPedidos(Long id);
    Long getCantProductosRecepcionados(Long id);
    List<DetallePedidoCompra> saveDetalle (List<DetallePedidoCompra> detalle, Long idPedido)throws Exception;
    List<DetallePedidoCompra> updateDetalle(List<DetallePedidoCompra> detalle, Long idPedido) throws Exception;
    void deleteDetalle(Long idPedido);
   }
