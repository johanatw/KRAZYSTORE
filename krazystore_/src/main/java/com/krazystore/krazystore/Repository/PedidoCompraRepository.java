/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.PagoPedidoCompra;
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
    "SELECT a "
            + "FROM PagoPedidoCompra a "
            + "LEFT JOIN a.pedidoCompra p "
            + "WHERE p.id = ?1 "
           )
        List<PagoPedidoCompra> getAnticipos(Long id);
        
        @Query(
    "SELECT c "
            + "FROM CompraEntity c "
            + "JOIN c.pedido pc "
            + "WHERE pc.id = ?1 "
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
}
