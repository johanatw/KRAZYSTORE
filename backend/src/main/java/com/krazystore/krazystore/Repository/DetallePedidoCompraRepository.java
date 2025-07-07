/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface DetallePedidoCompraRepository extends JpaRepository<DetallePedidoCompra, Long> {
    @Query(
  value = "SELECT * FROM detalle_pedido_compra u WHERE u.id_pedido_compra = ?1 ORDER BY u.id DESC", 
  nativeQuery = true)
    List<DetallePedidoCompra> findByIdPedido(Long idPedido);
    
    @Transactional
     @Modifying
     @Query(
  value = "DELETE FROM detalle_pedido_compra u WHERE u.id_pedido_compra = ?1 ", 
  nativeQuery = true)
    void deleteAllByIdPedido(Long idPedido);
    
    @Query(
    "SELECT SUM(COALESCE(d.cantidad, 0)) "
            + "FROM DetallePedidoCompra d "
            + "LEFT JOIN PedidoCompraEntity t ON d.pedidoCompra = t "
            + "WHERE t.id = ?1 " 
           )
    Long getCantProductosPedidos(Long id);

    @Query("SELECT new com.krazystore.krazystore.DTO.DetallePedidoCompraDTO(d.id, d.producto.id, d.producto.nombre, d.producto.tipoIva, "
        + "d.cantidad, d.subTotal, d.costoCompra, "
        + "COALESCE(SUM(dc.cantidad), 0) " 
            + " ) "
        + "FROM DetallePedidoCompra d "
        + "LEFT JOIN d.pedidoCompra p "
        + "LEFT JOIN DetalleCompra dc ON dc.detallePedido = d "
        + "WHERE p.id = ?1 " +
            "GROUP BY d.id, d.producto.id, d.producto.nombre, d.producto.tipoIva, d.cantidad, d.subTotal, d.costoCompra" )
    public List<DetallePedidoCompraDTO> findDetallesDTOByIdPedido(Long id);
}
