/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.DTO.ProductoExistenciasDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface DetallePedidoService {
    List<DetallePedidoEntity> findAll();
    Optional<DetallePedidoEntity> findById(Long id);
    List<DetallePedidoDTO> findDTOByIdPedido(Long id);
    List<DetallePedidoDTO> findByNroPedido(Long idPedido);
    List<ProductoExistenciasDTO> saveDetallePedido(Long idPedido, List<DetallePedidoEntity> detalles);
    List<ProductoExistenciasDTO> updateDetallesPedido(List<DetallePedidoEntity> detallePedido, Long id)throws Exception;
    void deleteByPedido(Long id);

    public List<ProductoExistenciasDTO> getProductosRevertirReservas(Long id);
}
