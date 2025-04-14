/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetalleEntregaDTO;
import com.krazystore.krazystore.Entity.DetalleEntrega;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface DetalleEntregaRepository extends JpaRepository<DetalleEntrega, Long> {
    @Query("SELECT new com.krazystore.krazystore.DTO.DetalleEntregaDTO( " +
       "d.id, dp.id,dp.producto.id, dp.producto.nombre, " +
       "dp.cantidad, " +
       "COALESCE(dv.cantidadFacturada, 0), " +
       "COALESCE(de.cantidadEntregada, 0), d.cantidad) " +
       "FROM DetalleEntrega d " +
       "JOIN d.entrega e " +
       "LEFT JOIN d.detallePedido dp " +
       "LEFT JOIN dp.pedido p " +
       "LEFT JOIN (SELECT " +
       "                 t.producto.id AS productoId, " +
       "                 tp.id AS pedidoId, " +
       "                 tv.estado AS estadoVenta, " +
       "                 SUM(t.cantidad) AS cantidadFacturada " +
       "           FROM DetalleVentaEntity t " +
       "           LEFT JOIN t.venta tv " +
       "           LEFT JOIN tv.pedido tp " +
       "           GROUP BY productoId, pedidoId, estadoVenta) dv ON dv.pedidoId = p.id AND dv.productoId = dp.producto.id AND dv.estadoVenta <> 'A'  " +
       "LEFT JOIN (SELECT te.id AS detalleId, te.detallePedido.id AS detallePedidoId,  " +
       "                 SUM(te.cantidad) AS cantidadEntregada " +
       "           FROM DetalleEntrega te " +
       "           GROUP BY detalleId, detallePedidoId) de ON de.detallePedidoId = dp.id " +
       "WHERE e.id = :idEntrega")
    List<DetalleEntregaDTO> findDetallesByIdEntrega(@Param("idEntrega") Long idEntrega);
    
}
