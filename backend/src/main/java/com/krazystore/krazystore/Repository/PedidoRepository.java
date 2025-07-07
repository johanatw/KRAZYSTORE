/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.EntregasPedidoDTO;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long>{

@Query(
    "SELECT new com.krazystore.krazystore.DTO.PedidoDTO(p.id, p.fecha, p.total, pe.nombre, pe.telefono, p.estadoPedido, p.estadoFacturacion,"
            + "COALESCE(sum(CASE WHEN o.cantStock < t.cantidad THEN (t.cantidad - COALESCE(r.cantFacturada,0) - o.cantStock) ELSE 0 END),0), "
            + "COALESCE(sum(t.cantidad),0) ) "
            + "FROM PedidoEntity p "
           + "LEFT JOIN p.cliente c "
            + "LEFT JOIN c.persona pe "
            + "LEFT JOIN DetallePedidoEntity t "
            + "ON t.pedido = p "
            + "LEFT JOIN t.producto o "
            + "LEFT JOIN (SELECT c.pedido.id AS pedidoID, dc.producto.id AS productoId, c.estado AS estadoVenta ,SUM(dc.cantidad) AS cantFacturada "
                        + "FROM DetalleVentaEntity dc "
                        + "LEFT JOIN dc.venta c "
                        + "GROUP BY c.pedido.id, dc.producto.id, c.estado ) r ON r.pedidoID = p.id AND r.productoId = o.id AND r.estadoVenta <> 'A' "
            + "GROUP BY  p.id, p.fecha, p.total, pe.nombre, pe.telefono, p.estadoPedido "
            + "ORDER BY p.id DESC"
           )
        List<PedidoDTO> findAllPedidos();
    
    @Query(
    "SELECT f "
            + "FROM VentaEntity f "
            + "JOIN f.pedido p "
            + "WHERE p.id = ?1 AND f.estado <> 'A' "
           )
        List<VentaEntity> getFacturas(Long id);
        
    @Query(
    "SELECT a "
            + "FROM AnticipoEntity a "
            + "WHERE a.pedido.id = ?1 "
           )
        List<AnticipoEntity> getAnticipos(Long id);
        
        
        @Query(
    "SELECT CASE WHEN COALESCE(sum(t.cantidad),0) - COALESCE(SUM(r.cantFacturada), 0) = 0 "
            + "THEN TRUE ELSE FALSE END "
            + "FROM PedidoEntity p "
            + "LEFT JOIN DetallePedidoEntity t "
            + "ON t.pedido = p "
            + "LEFT JOIN t.producto o "
            + "LEFT JOIN (SELECT c.pedido.id AS pedidoID, dc.producto.id AS productoId, SUM(dc.cantidad) AS cantFacturada "
                        + "FROM DetalleVentaEntity dc "
                        + "LEFT JOIN dc.venta c "
                        + "WHERE c.estado <> 'A' "
                        + "GROUP BY c.pedido.id, dc.producto.id ) r ON r.pedidoID = p.id AND r.productoId = o.id "
            + "WHERE p.id = ?1  "
           )
        boolean esPedidoTotalmenteFacturado(Long idPedido);
        
        @Query(
    "SELECT new com.krazystore.krazystore.DTO.EntregasPedidoDTO( "
            + "COALESCE(SUM(CASE WHEN e.estado = 'P' THEN de.cantidad END),0), "
            + "COALESCE(SUM(CASE WHEN e.estado = 'E' THEN de.cantidad END),0), "
            + "COALESCE(SUM(CASE WHEN e.estado = 'X' THEN de.cantidad END),0)) "
            + "FROM DetalleEntrega de "
            + "LEFT JOIN de.entrega e "
            + "LEFT JOIN e.venta v "
            + "LEFT JOIN v.pedido p "
            + "WHERE p.id = ?1 and v.estado <> 'A' and e.estado <> 'C' "
           )
        EntregasPedidoDTO findEstadoEntregasPedido(Long id);
        
        @Query(
    "SELECT COALESCE(SUM(d.cantidad),0) "
            + "FROM PedidoEntity p "
            + "LEFT JOIN DetallePedidoEntity d ON d.pedido = p "
            + "LEFT JOIN d.producto o ON o.esServicio = false "
            + "WHERE p.id = ?1 "
           )
        Long totalProductosPedido(Long id);
        
        
}


