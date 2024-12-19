/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.DetallePedidoCompra;
import com.krazystore.krazystore.Service.DetallePedidoCompraService;
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
@RequestMapping("/api/detalle_pedido_compra")
public class DetallePedidoCompraController {
    private final DetallePedidoCompraService detalleService;

    public DetallePedidoCompraController(DetallePedidoCompraService detalleService) {
        this.detalleService = detalleService;
    }

    @GetMapping("/{id}")
    public List<DetallePedidoCompra> findByIdPedido(@PathVariable("id") Long id) {
        return detalleService.findByIdPedido(id);
    }
}
