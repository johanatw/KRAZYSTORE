/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import com.krazystore.krazystore.Entity.PagoPedidoCompra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface PagoPedidoCompraRepository extends JpaRepository<PagoPedidoCompra,Long>  {

    @Query(
   "SELECT new com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra(p) "
           + "FROM PagoPedidoCompra p WHERE p.pedidoCompra.id = ?1 AND p.saldo > 0 ORDER BY p.fecha DESC ")
    public List<AplicacionPagoPedidoCompra> findPagosByIdPedidoCompra(Long id);

    @Query(
   "SELECT new com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra(p) "
           + "FROM PagoPedidoCompra p WHERE p.pedidoCompra.id = ?1 AND p.saldo > 0 ORDER BY p.fecha DESC ")
    public List<AplicacionPagoPedidoCompra> findPagosAplicarByIdPedidoCompra(Long id);
    
}
