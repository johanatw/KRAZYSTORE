/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.PedidoCreationDTO;
import com.krazystore.krazystore.DTO.PedidoDTO;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Service.PedidoService;
import java.util.List;
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
    public PedidoCreationDTO findById(@PathVariable("id") Long id) {
        return pedidoService.findById(id);
    }

    @PostMapping
    public PedidoEntity savePedido(@RequestBody PedidoCreationDTO pedidoCreationDTO) {
        return pedidoService.savePedido(pedidoCreationDTO);
    }
    

    @PutMapping("/{id}")
    public PedidoEntity updatePedido(@PathVariable long id, @RequestBody PedidoCreationDTO pedidoCreationDTO) throws Exception {
        return pedidoService.updatePedido(pedidoCreationDTO, id);
    }

    @PutMapping("/cancelar/{id}")
    public PedidoEntity cancelarPedido(@PathVariable long id, @RequestBody PedidoEntity pedido) throws Exception {
        return pedidoService.cancelarPedido(id);
    }
    
    @DeleteMapping("/{id}")
    public void deletePedido(@PathVariable("id") Long id) {
        pedidoService.deletePedido(id);
    }

    
}
