/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.ReembolsoPagoPedidoCompra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface ReembolsoPagoPedidoCompraRepository extends JpaRepository<ReembolsoPagoPedidoCompra,Long> {

    
    @Query(
  value = "SELECT r.id FROM reembolsos_pagos_pedido_compra r WHERE r.id_pago_pedido_compra = ?1 ", 
  nativeQuery = true)
    public List<Long> getIdReembolsosByIdPagoPedidoCompra(Long id);
    
}
