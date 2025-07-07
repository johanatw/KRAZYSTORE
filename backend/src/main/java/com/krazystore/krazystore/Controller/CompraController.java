/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;


import com.krazystore.krazystore.DTO.CompraCreationDTO;
import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Service.CompraService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/compras")
public class CompraController {
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }
    
    @GetMapping
    public List<CompraEntity> findAll() {
        return compraService.findAll();
    }
    
    @GetMapping("/pedidos")
    public List<CompraCreationDTO> findFacturasProductosByIdsPedidos(@RequestParam(value="ids") List<Long> ids) {
        return compraService.findFacturasProductosByIdsPedidos(ids);
    }
    
    @GetMapping("/pedido/{id}")
    public List<CompraCreationDTO> findFacturasProductosByIdPedido(@PathVariable("id") Long id) {
        return compraService.findFacturasByIdPedido(id);
    }

    @GetMapping("/{id}")
    public CompraCreationDTO findById(@PathVariable("id") Long id) {
        return compraService.findById(id);
    }

    @PostMapping
    public CompraEntity saveCompra(@RequestBody CompraCreationDTO compra)throws Exception {
        System.out.println("controllercompra");
        return compraService.saveCompra(compra);
    }

    @PutMapping("/{id}")
    public CompraEntity updateCompra(@PathVariable long id, @RequestBody CompraCreationDTO compra)throws Exception {
        return compraService.updateCompra(compra, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCompra(@PathVariable("id") Long id) {
        compraService.deleteCompra(id);
    }
}
