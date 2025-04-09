/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.CostoEntity;
import com.krazystore.krazystore.Service.PrecioCompraService;
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
@RequestMapping("/api/precios_compra")
public class PrecioCompraController {
    private final PrecioCompraService precioCompraService;

    public PrecioCompraController(PrecioCompraService precioCompraService) {
        this.precioCompraService = precioCompraService;
    }

    @GetMapping
    public List<CostoEntity> findAll() {
        return precioCompraService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CostoEntity> findById(@PathVariable("id") Long id) {
        return precioCompraService.findById(id);
    }

    @GetMapping("/producto/{id}")
    public List<CostoEntity> findPreciosByIdProducto(@PathVariable("id") Long id) {
        return precioCompraService.findPreciosByIdProducto(id);
    }
    
    @PostMapping
    public CostoEntity savePrecioVenta(@RequestBody CostoEntity precioCompra) {
        return precioCompraService.savePrecioCompra(precioCompra);
    }

    @PutMapping("/{id}")
    public CostoEntity updatePrecioVenta(@PathVariable long id, @RequestBody CostoEntity precioCompra) {
        return precioCompraService.updatePrecioCompra(precioCompra, id);
    }

    @DeleteMapping("/{id}")
    public void deletePrecioVenta(@PathVariable("id") Long id) {
        precioCompraService.deletePrecioCompra(id);
    }
}
