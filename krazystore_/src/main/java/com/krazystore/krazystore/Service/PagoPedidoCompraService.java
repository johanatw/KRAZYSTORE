/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.krazystore.krazystore.Service;

import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import com.krazystore.krazystore.Entity.PagoPedidoCompra;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author HP
 */
public interface PagoPedidoCompraService {
    PagoPedidoCompra savePago(PagoPedidoCompra pagoAdelantado);
    List<PagoPedidoCompra> findAll();
    Optional<PagoPedidoCompra> findById(Long id);
    PagoPedidoCompra updatePago(PagoPedidoCompra asnticipoEntity, Long id);
    List<PagoPedidoCompra> updatePagos(List<PagoPedidoCompra> pagos);
    void deletePagoPedidoCompra(Long id);
    List<AplicacionPagoPedidoCompra> findPagosByIdPedidoCompra(Long id);

    public List<AplicacionPagoPedidoCompra> findPagosAplicarByIdPedidoCompra(Long id);
}
