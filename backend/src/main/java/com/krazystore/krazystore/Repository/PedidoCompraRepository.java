/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PedidoCompraRepository extends JpaRepository<PedidoCompraEntity, Long> {
        
        @Query(
    "SELECT c "
            + "FROM CompraEntity c "
            + "JOIN c.pedido pc "
            + "WHERE pc.id = ?1 and c.tipoFactura = 'PROD' "
           )
        List<CompraEntity> getFacturas(Long id);

    public List<PedidoCompraEntity> findAllByOrderByIdDesc();
    
    @Query(
    "SELECT CASE WHEN COALESCE(sum(t.cantidad),0) - COALESCE(SUM(r.cantFacturada), 0) = 0 "
            + "THEN TRUE ELSE FALSE END "
            + "FROM PedidoCompraEntity p "
            + "LEFT JOIN DetallePedidoCompra t "
            + "ON t.pedidoCompra = p "
            + "LEFT JOIN t.producto o "
            + "LEFT JOIN (SELECT c.pedido.id AS pedidoID, dc.producto.id AS productoId, SUM(dc.cantidad) AS cantFacturada "
                        + "FROM DetalleCompra dc "
                        + "LEFT JOIN dc.compra c "
                        + "GROUP BY c.pedido.id, dc.producto.id ) r ON r.pedidoID = p.id AND r.productoId = o.id "
            + "WHERE p.id = ?1  "
           )
        boolean esPedidoTotalmenteFacturado(Long idPedido);
        
    @Query(
    "SELECT COALESCE(SUM(dr.cantRecepcionada),0) "
            + "FROM DetalleRecepcion dr "
            + "LEFT JOIN dr.detalleCompra dc "
            + "LEFT JOIN dc.compra c "
            + "LEFT JOIN c.pedido p "
            + "WHERE p.id = ?1 "
           )
        Long totalProductosRecepcionados(Long id);
        
        @Query(
    "SELECT COALESCE(SUM(d.cantidad),0) "
            + "FROM PedidoCompraEntity p "
            + "LEFT JOIN DetallePedidoCompra d ON d.pedidoCompra = p "
            + "LEFT JOIN d.producto o "
            + "WHERE p.id = ?1 AND o.esServicio <> TRUE "
           )
        Long totalProductosPedido(Long id);
        
        @Query(
    "SELECT COALESCE(SUM(d.cantidad),0) "
            + "FROM DetalleCompra d "
            + "LEFT JOIN d.compra c "
            + "LEFT JOIN c.pedido p "
            + "LEFT JOIN d.producto o "
            + "WHERE p.id = ?1 AND o.esServicio <> TRUE "
           )
        Long totalProductosFacturados(Long id);
}
