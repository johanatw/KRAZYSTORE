/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.AplicacionPagoPedidoCompra;
import com.krazystore.krazystore.Entity.PagoPedidoCompra;
import com.krazystore.krazystore.Service.PagoPedidoCompraService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/pago_pedido_compra")
public class PagoPedidoCompraController {

    public PagoPedidoCompraController(com.krazystore.krazystore.Service.PagoPedidoCompraService pagoPedidoCompraService) {
        this.pagoPedidoCompraService = pagoPedidoCompraService;
    }
    private final PagoPedidoCompraService pagoPedidoCompraService;
    
    @GetMapping
    public List<PagoPedidoCompra> findAll() {
        return pagoPedidoCompraService.findAll();
    }
    
    @GetMapping("/pedido/{id}")
    public List<AplicacionPagoPedidoCompra> findPagosAplicarByIdPedidoCompra(@PathVariable("id") Long id) {
        return pagoPedidoCompraService.findPagosAplicarByIdPedidoCompra(id);
    }
}
