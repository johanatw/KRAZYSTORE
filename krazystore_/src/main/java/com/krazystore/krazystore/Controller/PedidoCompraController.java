/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.PedidoCompraCreationDTO;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import com.krazystore.krazystore.Service.PedidoCompraService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/pedidos_compra")
public class PedidoCompraController {
    private final PedidoCompraService pedidoCompraService;

    public PedidoCompraController(PedidoCompraService pedidoCompraService) {
        this.pedidoCompraService = pedidoCompraService;
    }
    
    @GetMapping
    public List<PedidoCompraEntity> findAll() {
        return pedidoCompraService.findAll();
    }

    @GetMapping("/{id}")
    public PedidoCompraCreationDTO findById(@PathVariable("id") Long id) {
        return pedidoCompraService.findById(id);
    }

    @PostMapping
    public PedidoCompraEntity savePedidoCompra(@RequestBody PedidoCompraCreationDTO pedidoCompra)throws Exception {
        return pedidoCompraService.savePedido(pedidoCompra);
    }

    @PutMapping("/{id}")
    public PedidoCompraEntity updatePedidoCompra(@PathVariable long id, @RequestBody PedidoCompraCreationDTO pedidoCompra) throws Exception {
        return pedidoCompraService.updatePedido(pedidoCompra, id);
    }

    @DeleteMapping("/{id}")
    public void deletePedidoCompra(@PathVariable("id") Long id) {
        pedidoCompraService.deletePedido(id);
    }
}
