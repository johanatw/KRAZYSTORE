/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleRecepcionDTO;
import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleRecepcionDTO(d.id, d.recepcion.id, dp.id, dp.producto.id, "
        + "dp.producto.nombre, dp.costoCompra, dp.cantidad, d.cantAceptada, d.cantRechazada, d.cantRecepcionada, "
        + "(SELECT COALESCE(SUM(r.cantRecepcionada), 0) "
        + " FROM DetalleRecepcion r "
        + " JOIN r.detallePedido dt "
        + " WHERE dt.id = dp.id) ) "
        + "FROM DetalleRecepcion d "
        + "JOIN d.detallePedido dp "
        + "WHERE d.recepcion.id = ?1")
    List<DetalleRecepcionDTO> findDetallesByRecepcionId(Long idRecepcion);
    
    @Query("SELECT DISTINCT new com.krazystore.krazystore.DTO.RecepcionDTO(r.id, r.fecha, p.id, r.estado, p.proveedor ) "
        + "FROM RecepcionEntity r "
        + "JOIN DetalleRecepcion d ON d.recepcion = r "
        + "JOIN PedidoCompraEntity p "
        + "ON d.detallePedido.pedidoCompra = p ")
    List<RecepcionDTO> findAllRecepciones();
    
    @Query("SELECT p.id "
        + "FROM PedidoCompraEntity p "
        + "LEFT JOIN DetallePedidoCompra d ON d.pedidoCompra = p "
        + "LEFT JOIN DetalleRecepcion t ON t.detallePedido = d "
        + "LEFT JOIN RecepcionEntity r ON t.recepcion = r "
        + "WHERE r.id = ?1")
    Long getIdPedidoCompraByIdRecepcion(Long idRecepcion);
}
