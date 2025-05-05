/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.ReembolsoPagoPedidoCompra;
import com.krazystore.krazystore.Service.ReembolsoPagoPedidoCompraService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/reembolso_pago_pedido_compra")
public class ReembolsoPagoPedidoCompraController {

    public ReembolsoPagoPedidoCompraController(com.krazystore.krazystore.Service.ReembolsoPagoPedidoCompraService reembolsoPagoPedidoCompraService) {
        this.reembolsoPagoPedidoCompraService = reembolsoPagoPedidoCompraService;
    }
    private final ReembolsoPagoPedidoCompraService reembolsoPagoPedidoCompraService;
    
    @GetMapping
    public List<ReembolsoPagoPedidoCompra> findAll() {
        return reembolsoPagoPedidoCompraService.findAll();
    }
}

