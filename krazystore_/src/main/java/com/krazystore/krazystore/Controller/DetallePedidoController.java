/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.DetallePedidoCreationRequest;
import com.krazystore.krazystore.DTO.DetallePedidoDTO;
import com.krazystore.krazystore.Entity.DetallePedidoEntity;
import com.krazystore.krazystore.Entity.PedidoEntity;
import com.krazystore.krazystore.Service.DetallePedidoService;
import com.krazystore.krazystore.exception.BadRequestException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/detalle_pedidos")
public class DetallePedidoController {
    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedidoEntity> findAll() {
        return detallePedidoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DetallePedidoEntity> findById(@PathVariable("id") Long id) {
        return detallePedidoService.findById(id);
    }
    
    @GetMapping("/detalle")
    public List<DetallePedidoDTO> findByNroPedido(@RequestParam(value="idPedido") Long id){
        return detallePedidoService.findByNroPedido(id);
    
    }
    
    @GetMapping("/detalles")
    public List<DetallePedidoEntity> findByPedido(@RequestParam(value="idPedido") Long id){
        return detallePedidoService.findByPedido(id);
    
    }
   
    
    @PostMapping("/{idPedido}")
    public Iterable<DetallePedidoEntity> saveDetallePedido(@PathVariable long idPedido, @RequestBody List<DetallePedidoEntity> detalles) {
        
        //return detallePedidoService.saveDetallePedido(idPedido, detalles);
        return null;
    }
 
    @PutMapping("/update/{id}")
    public Iterable<DetallePedidoEntity> updateDetalles(@PathVariable long id, @RequestBody List<DetallePedidoEntity> detalle)throws Exception {
        return null;
        
            //return detallePedidoService.updateDetallesPedido(detalle, id);
        
    }
            
    @DeleteMapping("/{id}")
    public void deleteDetalle(@PathVariable("id") Long id) {
        detallePedidoService.deleteDetallePedido(id);
    }
    
    @DeleteMapping("/items/{ids}")
    public void deleteByIds(@PathVariable("ids") List<Long> ids) {
        System.out.println(ids);
        detallePedidoService.deleteDetallesPedido(ids);
    }
    
    @DeleteMapping("/pedido/{id}")
    public void deleteByPedido(@PathVariable("id") Long id) {
        detallePedidoService.deleteByPedido(id);
    }

    
}
