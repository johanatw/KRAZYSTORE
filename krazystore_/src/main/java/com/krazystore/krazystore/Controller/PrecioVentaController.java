/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.PrecioVentaEntity;
import com.krazystore.krazystore.Service.PrecioVentaService;
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
@RequestMapping("/api/precios_venta")
public class PrecioVentaController {
    private final PrecioVentaService precioVentaService;

    public PrecioVentaController(PrecioVentaService categoriaService) {
        this.precioVentaService = categoriaService;
    }

    @GetMapping
    public List<PrecioVentaEntity> findAll() {
        return precioVentaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<PrecioVentaEntity> findById(@PathVariable("id") Long id) {
        return precioVentaService.findById(id);
    }

    @GetMapping("/producto/{id}")
    public List<PrecioVentaEntity> findPreciosByIdProducto(@PathVariable("id") Long id) {
        return precioVentaService.findPreciosByIdProducto(id);
    }
    
    @PostMapping
    public PrecioVentaEntity savePrecioVenta(@RequestBody PrecioVentaEntity precioVenta) {
        return precioVentaService.savePrecioVenta(precioVenta);
    }

    @PutMapping("/{id}")
    public PrecioVentaEntity updatePrecioVenta(@PathVariable long id, @RequestBody PrecioVentaEntity precioVenta) {
        return precioVentaService.updatePrecioVenta(precioVenta, id);
    }

    @DeleteMapping("/{id}")
    public void deletePrecioVenta(@PathVariable("id") Long id) {
        precioVentaService.deletePrecioVenta(id);
    }
}
