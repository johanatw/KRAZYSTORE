/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetallePedidoCompraDTO;
import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
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
public interface DetallePedidoCompraRepository extends JpaRepository<DetallePedidoCompra, Long> {
    @Query(
  value = "SELECT * FROM detalle_pedido_compra u WHERE u.id_pedido_compra = ?1 ORDER BY u.id DESC", 
  nativeQuery = true)
    List<DetallePedidoCompra> findByIdPedido(Long idPedido);
    
    /*@Query("SELECT new com.krazystore.krazystore.DTO.DetallePedidoCompraDTO(d.id, d.producto.id, d.producto.nombre, d.producto.tipoIva,d.pedidoCompra, "
        + "d.cantidad, d.subTotal, d.costoCompra, "
        + "(SELECT COALESCE(SUM(r.cantRecepcionada), 0) "
        + " FROM DetalleRecepcion r "
        + " JOIN r.detallePedido dt "
        + " WHERE dt.id = d.id),  "
        + "(SELECT COALESCE(SUM(dc.cantidad), 0) "
        + " FROM DetalleCompra dc "
        + " JOIN dc.compra f "
        + " WHERE f.pedido.id = ?1 AND dc.producto = d.producto), "
        + "(SELECT COALESCE(SUM(r.cantAceptada), 0) "
        + " FROM DetalleRecepcion r "
        + " JOIN r.detallePedido dt "
        + " WHERE dt.id = d.id) ) "
        + "FROM DetallePedidoCompra d "
        + "WHERE d.pedidoCompra.id = ?1")
    List<DetallePedidoCompraDTO> findDetallesByIdPedido(Long idPedido);*/
    
    /*@Query("SELECT new com.krazystore.krazystore.DTO.DetallePedidoCompraDTO( " +
       "d.id, d.producto.id, d.producto.nombre, d.producto.tipoIva, d.pedidoCompra, " +
       "d.cantidad, d.subTotal, d.costoCompra, d.producto.esServicio, " +
       "COALESCE(dr.cantRecepcionada, 0), " +
       "COALESCE(dc.cantidadComprada, 0), " +
       "COALESCE(dr.cantAceptada, 0)) " +
       "FROM DetallePedidoCompra d " +
       "LEFT JOIN (SELECT dt.id AS detalleId, " +
       "                 SUM(r.cantRecepcionada) AS cantRecepcionada, " +
       "                 SUM(r.cantAceptada) AS cantAceptada " +
       "           FROM DetalleRecepcion r " +
       "           JOIN r.detallePedido dt " +
       "           GROUP BY dt.id) dr ON dr.detalleId = d.id " +
       "LEFT JOIN (SELECT dc.producto.id AS productoId, f.pedido.id AS pedidoId, " +
       "                 SUM(dc.cantidad) AS cantidadComprada " +
       "           FROM DetalleCompra dc " +
       "           JOIN dc.compra f " +
       "           GROUP BY dc.producto.id, f.pedido.id) dc ON dc.productoId = d.producto.id AND dc.pedidoId = :idPedido " +
       "WHERE d.pedidoCompra.id = :idPedido")
    List<DetallePedidoCompraDTO> findDetallesByIdPedido(@Param("idPedido") Long idPedido);*/
    
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
    
   /* @Query(
    "SELECT SUM(COALESCE(d.cantRecepcionada, 0)) "
            + "FROM PedidoCompraEntity p "
            + "LEFT JOIN DetallePedidoCompra t ON t.pedidoCompra = p "
            + "LEFT JOIN DetalleRecepcion d ON d.detallePedido = t "
            + "WHERE p.id = ?1 " 
           )
    Long getCantProductosRecepcionados(Long idPedido);*/
    
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
    
    /*@Query("SELECT new com.krazystore.krazystore.DTO.DetallePedidoCompraDTO( " +
       "dp.id, dp.producto.id, " +
       "dp.producto.nombre, dp.producto.tipoIva, dp.costoCompra, " +
       "COALESCE(SUM(dr.cantAceptada), 0), COALESCE(SUM(dr.cantRecepcionada), 0) ) " +
       "FROM DetalleRecepcion dr " +
       "JOIN dr.detallePedido dp " +
       "JOIN dr.recepcion r " + 
       "WHERE r.id IN :ids " +
       "GROUP BY dp.id, dp.producto.id, dp.producto.nombre, dp.producto.tipoIva, dp.costoCompra" )
    List<DetallePedidoCompraDTO> findDetalleFacturarByIdsRecepciones(@Param("ids") List<Long> ids);*/

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
