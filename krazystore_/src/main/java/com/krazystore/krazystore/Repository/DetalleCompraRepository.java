/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleCompraDTO;
import com.krazystore.krazystore.Entity.DetalleCompra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    @Query(
  value = "SELECT * FROM detalle_compra u WHERE u.id_compra = ?1 ORDER BY u.id DESC", 
  nativeQuery = true)
    List<DetalleCompra> findAllByIdCompra(Long idCompra); 
    
    @Transactional
     @Modifying
     @Query(
  value = "DELETE FROM detalle_compra u WHERE u.id_compra = ?1 ", 
  nativeQuery = true)
    void deleteAllByIdCompra(Long idCompra);
    
    
    
    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleCompraDTO( " +
       "d.id, d.producto.id, d.producto.nombre, d.ivaAplicado, " +
       "d.cantidad, d.subTotal, d.costoCompra, COALESCE(SUM(dp.id), 0), COALESCE(SUM(dp.cantidad), 0),  " +
       "COALESCE(SUM(dr.cantRecepcionada), 0), " +
        "COALESCE(SUM(dr.cantAceptada), 0), " +
       "COALESCE(dc.cantidadFacturada, 0)) " +
       "FROM DetalleCompra d " +
        "LEFT JOIN d.compra c " +
        "LEFT JOIN c.pedido p " +
        "LEFT JOIN DetallePedidoCompra dp ON dp.pedidoCompra.id = p.id AND dp.producto.id = d.producto.id " +
        "LEFT JOIN RecepcionEntity r ON r.compra.id = c.id " +
        "LEFT JOIN DetalleRecepcion dr ON dr.recepcion.id = r.id AND dr.detallePedido.id = dp.id " +
        "LEFT JOIN (SELECT dc.producto.id AS productoId, f.pedido.id AS pedidoId, " +
       "                 SUM(dc.cantidad) AS cantidadFacturada " +
       "           FROM DetalleCompra dc " +
       "           JOIN dc.compra f " +
      //  "           WHERE f.id = :idCompra " +
       "           GROUP BY dc.producto.id, f.pedido.id) dc ON dc.productoId = d.producto.id AND dc.pedidoId = p.id " +    
       "WHERE c.id = :idCompra " +
        "GROUP BY d.id, d.producto.id, d.producto.nombre, d.ivaAplicado, d.cantidad, d.subTotal, d.costoCompra, dp.id, dp.cantidad, dc.cantidadFacturada ")
    List<DetalleCompraDTO> findDetallesByIdCompra(@Param("idCompra") Long idcompra);
}
