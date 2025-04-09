/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.DTO.RecepcionCreationDTO;
import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface RecepcionRepository extends JpaRepository<RecepcionEntity, Long> {
    @Query(
    "SELECT COALESCE(SUM(r.cantRecepcionada), 0) "
            + "FROM DetalleRecepcion r "
            + "JOIN DetallePedidoCompra d ON r.detallePedido = d "
            + "WHERE d.producto.id = ?2 AND d.pedidoCompra.id = ?1 "
           )
        Integer getTotalRecepcionadoPorProducto(Long pedidoId, Long productoId);
        
        @Query("SELECT new com.krazystore.krazystore.DTO.RecepcionDTO(r.id, r.fecha, "
        + " (SELECT new com.krazystore.krazystore.DTO.DetalleRecepcionDTO(d.id, p.producto, p.costoCompra ,p.cantidad, d.cantRecepcionada) "
        + "  FROM DetalleRecepcion d "
                + "LEFT JOIN DetallePedidoCompra p ON p = d.detallePedido WHERE d.recepcion.id = r.id )) "
        + "FROM RecepcionEntity r WHERE r.id = ?1 ")
        Optional<RecepcionDTO> findRecepcionWithDetails(Long idRecepcion);
        
    @Query("SELECT DISTINCT new com.krazystore.krazystore.DTO.RecepcionDTO(r.id, r.fecha, p.id, r.estado, p.proveedor ) "
        + "FROM RecepcionEntity r "
        + "JOIN DetalleRecepcion d ON d.recepcion = r "
        + "JOIN d.detallePedido dp "
        + "JOIN dp.pedidoCompra p "
        + "WHERE r.id = ?1")
    Optional<RecepcionDTO> findRecepcion(Long idRecepcion);

   /* @Query("SELECT new com.krazystore.krazystore.DTO.DetalleRecepcionDTO(d.id, d.recepcion.id, dp.id, dp.producto.id, "
        + "dp.producto.nombre, dp.costoCompra, dp.cantidad, d.cantAceptada, d.cantRechazada, d.cantRecepcionada, "
        + "(SELECT COALESCE(SUM(r.cantAceptada), 0) "
        + " FROM DetalleRecepcion r "
        + " JOIN r.detallePedido dt "
        + " WHERE dt.id = dp.id), "
        + "(SELECT COALESCE(SUM(dc.cantidad), 0) "
        + " FROM DetalleCompra dc "
        + " JOIN dc.compra f "
        + " JOIN f.pedido n ON n.id = s.id "
        + " WHERE f.pedido.id = ?1 AND dc.producto = d.producto)) "
        + "FROM DetalleRecepcion d "
        + "JOIN d.detallePedido dp "
        + "JOIN dp.pedido s " 
        + "WHERE d.recepcion.id = ?1")
    List<DetalleRecepcionDTO> findDetallesByRecepcionId(Long idRecepcion);*/
    
    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleRecepcionDTO( " +
       "d.id, d.recepcion.id, dp.id, dp.producto.id, " +
       "dp.producto.nombre, dp.costoCompra, dp.cantidad, d.cantAceptada, " +
       "d.cantRechazada, d.cantRecepcionada, " +
       "COALESCE(dr.cantAceptadaTotal, 0), " +
       "COALESCE(dc.totalComprado, 0), " +
        "COALESCE(dr.totalRecepcionado, 0)) " +
       "FROM DetalleRecepcion d " +
       "JOIN d.detallePedido dp " +
       "JOIN dp.pedidoCompra s " +
       "LEFT JOIN (SELECT dt.id AS detalleId, SUM(r.cantAceptada) AS cantAceptadaTotal,  SUM(r.cantRecepcionada) AS totalRecepcionado" +
       "           FROM DetalleRecepcion r " +
       "           JOIN r.detallePedido dt " +
       "           GROUP BY dt.id) dr ON dr.detalleId = dp.id " +
       "LEFT JOIN (SELECT dc.producto.id AS productoId, f.pedido.id AS pedidoId, SUM(dc.cantidad) AS totalComprado " +
       "           FROM DetalleCompra dc " +
       "           JOIN dc.compra f " +
       "           GROUP BY dc.producto.id, f.pedido.id) dc ON dc.productoId = dp.producto.id AND dc.pedidoId = s.id " +
       "WHERE d.recepcion.id = :idRecepcion")
    List<DetalleRecepcionDTO> findDetallesByRecepcionId(@Param("idRecepcion") Long idRecepcion);
    
    
    @Query("SELECT DISTINCT new com.krazystore.krazystore.DTO.RecepcionDTO(r.id, r.fecha, p.id, r.estado, p.proveedor ) "
        + "FROM RecepcionEntity r "
        + "JOIN DetalleRecepcion d ON d.recepcion = r "
        + "JOIN PedidoCompraEntity p "
        + "ON d.detallePedido.pedidoCompra = p "
        + "ORDER BY r.id DESC ")
    List<RecepcionDTO> findAllRecepciones();
    
    @Query("SELECT p.id "
        + "FROM PedidoCompraEntity p "
        + "LEFT JOIN DetallePedidoCompra d ON d.pedidoCompra = p "
        + "LEFT JOIN DetalleRecepcion t ON t.detallePedido = d "
        + "LEFT JOIN RecepcionEntity r ON t.recepcion = r "
        + "WHERE r.id = ?1")
    Long getIdPedidoCompraByIdRecepcion(Long idRecepcion);
    
    /*@Query("SELECT new com.krazystore.krazystore.DTO.RecepcionCreationDTO"
            + "(SELECT new com.krazystore.krazystore.DTO.RecepcionDTO(r.id, r.fecha, p.id ), "
        + " (SELECT new com.krazystore.krazystore.DTO.DetalleRecepcionDTO(dp.id, dp.producto.id, "
            + "dp.producto.nombre, dr.cantAceptada, dr.cantRechazada, dr.cantRecepcionada) "
        + "  FROM DetalleRecepcion dr "
                + "LEFT JOIN DetallePedidoCompra dp ON dp.id = dr.detallePedido.id WHERE dr.recepcion.id = r.id )"
            + ") "
        + "FROM RecepcionEntity r "
            + "JOIN  DetalleRecepcion d ON d.recepcion.id = r.id "
            + "JOIN d.detallePedido dt "
            + "JOIN dt.pedido p "
            + "WHERE p.id = ?1 ")
        List<RecepcionCreationDTO> findRecepcionesByIdPedido(Long idPedido);*/
        
    @Query("SELECT new com.krazystore.krazystore.DTO.RecepcionDTO( " +
       "r.id, r.fecha, p.id, r.estado, c) " +
       "FROM RecepcionEntity r " +
       "JOIN DetalleRecepcion dr ON dr.recepcion.id = r.id " +
       "JOIN dr.detallePedido dp " +
       "JOIN dp.pedidoCompra p " +
        "LEFT JOIN r.compra c " +
       "WHERE p.id = :idPedido " +
        "GROUP BY r.id, r.fecha, p.id, c")
    List<RecepcionDTO> findRecepcionesByIdPedido(@Param("idPedido") Long idPedido);
    
 
      
      @Modifying
        @Query("UPDATE RecepcionEntity r SET r.compra.id = :idCompra, r.estado = :estado WHERE r.id IN :ids")
        void asociarRecepcionesFactura(@Param("idCompra") Long idCompra, @Param("ids") List<Long> ids, @Param("estado") Character estado);
    
    
}
