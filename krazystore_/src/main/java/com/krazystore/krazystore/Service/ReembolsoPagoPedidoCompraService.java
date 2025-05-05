/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.ReembolsoPagoPedidoCompra;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface ReembolsoPagoPedidoCompraService {
    ReembolsoPagoPedidoCompra saveReembolso(ReembolsoPagoPedidoCompra reembolsoPagoPedidoCompra);
    List<Long> getIdReembolsosByIdPagosPedidoCompra(Long id);

    public void deleteReembolsosById(List<Long> ids);

    public Optional<ReembolsoPagoPedidoCompra> findById(Long id);

    public void deleteById(Long id);

    public List<ReembolsoPagoPedidoCompra> findAll();
}
