/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
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
public interface DetalleRecepcionRepository extends JpaRepository<DetalleRecepcion, Long> {
   @Query(
  value = "SELECT * FROM detalle_recepcion u WHERE u.id_recepcion = ?1 ORDER BY u.id DESC", 
  nativeQuery = true)
    List<DetalleRecepcion> findByIdRecepcion(Long idRecepcion);
    
    @Transactional
     @Modifying
     @Query(
  value = "DELETE FROM detalle_recepcion u WHERE u.id_recepcion = ?1 ", 
  nativeQuery = true)
    void deleteAllByIdRecepcion(Long idRecepcion);
    
      /* @Query(
    "SELECT new com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO(r.id, r.producto, d.cantidad, "
            + "COALESCE(r.cantRecepcionada, 0) ,COALESCE(r.cantAceptada, 0) ,COALESCE(r.cantRechazada, 0)  )"
            + "FROM DetalleRecepcion r "
            + "JOIN RecepcionEntity t ON r.recepcion = t "
            + "JOIN DetallePedidoCompra d ON d.pedidoCompra = t.pedidoCompra AND d.producto = r.producto "
            + "WHERE r.recepcion.id = ?1 "
           )
        List<DetallePedidoRecepcionDTO> findAllByIdRecepcion(Long idRecepcion); 
        
        @Query(
    "SELECT SUM(COALESCE(r.cantRecepcionada, 0)) "
            + "FROM DetallePedidoCompra d "
            + "LEFT JOIN RecepcionEntity t ON d.pedidoCompra = t.pedidoCompra "
            + "LEFT JOIN DetalleRecepcion r ON d.producto = r.producto AND r.recepcion = t "
            + "WHERE d.producto.id = ?1 AND d.pedidoCompra.id = ?2 "
           )
        Integer getTotalRecepcionado(Long idProducto, Long idPedido); */
}
