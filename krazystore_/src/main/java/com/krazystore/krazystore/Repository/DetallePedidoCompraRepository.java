/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
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
    
    /*@Query(
    "SELECT new com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO(d.id, d.producto, d.pedidoCompra, d.cantidad, "
            + "SUM(COALESCE(r.cantRecepcionada, 0)),SUM(COALESCE(r.cantAceptada, 0)) ,SUM(COALESCE(r.cantRechazada, 0)), d.subTotal, d.costoCompra  ) "
            + "FROM DetallePedidoCompra d "
            + "LEFT JOIN RecepcionEntity t ON t.pedidoCompra = d.pedidoCompra "
            + "LEFT JOIN DetalleRecepcion r ON d.producto = r.producto AND r.recepcion = t "
            + "WHERE d.pedidoCompra.id = ?1 " 
            + "GROUP BY d.id, d.producto, d.pedidoCompra, d.cantidad"
           )
    public List<DetallePedidoRecepcionDTO> findAllDetalles(Long idPedido);*/
}
