/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Repository;

import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author HP
 */
@Repository
public interface AplicacionPagoPedidoCompraRepository extends JpaRepository<AplicacionPagoPedidoCompra,Long> {
    @Transactional
    @Modifying
    @Query(
  value = "DELETE FROM aplicacion_pagos_pedido_compra p USING movimientos m WHERE p.id_movimiento = m.id AND m.id = ?1 ", 
  nativeQuery = true)
    void deletePagosPedidoCompraAplicadosByIdMovimiento(Long id);
    
    @Query(
     "SELECT ap FROM AplicacionPagoPedidoCompra ap WHERE ap.movimiento.id = ?1 ")
    List<AplicacionPagoPedidoCompra> findPagosPedidoCompraAplicadosByIdMovimiento(Long id);
}
