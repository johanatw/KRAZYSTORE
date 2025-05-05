/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.Pedido;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long>{

//    @Query("SELECT p FROM PedidoEntity p LEFT JOIN FETCH p.costoEnvio e "
//            + "LEFT JOIN FETCH p.estadoPedido "
//            + "LEFT JOIN FETCH p.estadoPago "
//            + "LEFT JOIN FETCH p.formaPago "
//            + "LEFT JOIN FETCH p.modoEntrega "
//            + "LEFT JOIN FETCH p.cliente c "
//            + "LEFT JOIN FETCH p.datoFacturacion "
//            
//            )
  
    //@Query(value = "select com.krazystore.krazystore.DTO.PedidoDTO(id,fecha, total, costoEnvio, cliente, telefono) from Person where age = 30")
//@Query(
//  value = "SELECT p.id_pedido as id, c.nombre as cliente, c.telefono as telefono FROM pedidos p LEFT JOIN personas c ON c.id = p.id_cliente ", 
//  nativeQuery = true)
//        List<Pedido> findAllPedidos();
//    }
@Query(
    "SELECT new com.krazystore.krazystore.DTO.PedidoDTO(p.id, p.fecha, p.total, c.nombre, c.telefono, p.estadoPedido, "
            + "COALESCE(sum(CASE WHEN o.cantStock < t.cantidad THEN (t.cantidad - COALESCE(r.cantFacturada,0) - o.cantStock) ELSE 0 END),0), "
            + "COALESCE(sum(t.cantidad),0) ) "
            + "FROM PedidoEntity p "
           + "LEFT JOIN p.cliente c "
            + "LEFT JOIN DetallePedidoEntity t "
            + "ON t.pedido = p "
            + "LEFT JOIN t.producto o "
            + "LEFT JOIN (SELECT c.pedido.id AS pedidoID, dc.producto.id AS productoId, c.estado AS estadoVenta ,SUM(dc.cantidad) AS cantFacturada "
                        + "FROM DetalleVentaEntity dc "
                        + "LEFT JOIN dc.venta c "
                        + "GROUP BY c.pedido.id, dc.producto.id, c.estado ) r ON r.pedidoID = p.id AND r.productoId = o.id AND r.estadoVenta <> 'A' "
            + "GROUP BY  p.id, p.fecha, p.total, c.nombre, c.telefono, p.estadoPedido "
            + "ORDER BY p.id DESC"
           )
        List<PedidoDTO> findAllPedidos();
       /* @Query(
          "SELECT new com.krazystore.krazystore.DTO.PedidoDTO( " +
          "    p.id, p.fecha, p.total, c.nombre, c.telefono, p.estadoPedido, " +
          "    COALESCE(SUM(CASE WHEN o.cantStock < 1 THEN (t.cantidad - COALESCE(r.cantFacturada, 0)) ELSE 0 END), 0), " +
          "    COALESCE(SUM(t.cantidad), 0) " +
          ") " +
          "FROM PedidoEntity p " +
          "LEFT JOIN p.cliente c " +
          "LEFT JOIN DetallePedidoEntity t ON t.pedido = p " +
          "LEFT JOIN t.producto o " +
          "LEFT JOIN ( " +
          "    SELECT dc.producto.id AS productoId, SUM(dc.cantidad) AS cantFacturada " +
          "    FROM DetalleVentaEntity dc " +
          "    JOIN dc.venta c " +
          "    WHERE c.pedido.id = p.id " + 
          "    GROUP BY dc.producto.id " +
          ") r ON r.productoId = o.id " +
          "GROUP BY p.id, p.fecha, p.total, c.nombre, c.telefono, p.estadoPedido " +
          "ORDER BY p.id DESC"
      )
      List<PedidoDTO> findAllPedidos();*/
        
        
        
        @Query(
  value = "SELECT \n" +
"    CASE\n" +
"	WHEN SUM(\n" +
"    CASE \n" +
"        WHEN a.id_pedido IS NOT NULL THEN 1\n" +
"        ELSE 0\n" +
"    END) > 0 THEN TRUE\n" +
"	ELSE FALSE END\n" +
"\n" +
"FROM \n" +
"    pedidos p\n" +
"LEFT JOIN \n" +
"    anticipos a ON p.id_pedido = a.id_pedido\n" +
"WHERE \n" +
"    p.id_pedido = ?1 \n" +
"Group by p.id_pedido ", 
  nativeQuery = true)
    public boolean existenAnticiposAsociados(Long id);
    
    @Query(
    "SELECT f "
            + "FROM VentaEntity f "
            + "JOIN f.pedido p "
            + "WHERE p.id = ?1 "
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
        
        
}


