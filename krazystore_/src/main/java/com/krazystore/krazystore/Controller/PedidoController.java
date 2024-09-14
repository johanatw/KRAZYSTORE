/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Service.PedidoService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoDTO> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<PedidoEntity> findById(@PathVariable("id") Long id) {
        return pedidoService.findById(id);
    }
    
    @GetMapping("/pedido/{id}")
    public int verificarPedidoEstado(@PathVariable("id") Long id) {
        return pedidoService.verificarPedidoEstado(id);
    }

    @PostMapping
    public PedidoEntity savePedido(@RequestBody PedidoEntity pedidoEntity) {
        return pedidoService.savePedido(pedidoEntity);
    }

    @PutMapping("/{id}")
    public PedidoEntity updatePedido(@PathVariable long id, @RequestBody PedidoEntity pedido) {
        return pedidoService.updatePedido(pedido, id);
    }

    @DeleteMapping("/{id}")
    public int deletePedido(@PathVariable("id") Long id) {
        return pedidoService.deletePedido(id);
    }

    
}
