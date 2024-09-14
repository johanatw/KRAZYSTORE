/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.DTO.Pedido;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.PedidoEntity;
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
@Query(nativeQuery = true)
        List<PedidoDTO> findAllPedidos();
        
        
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
    }


