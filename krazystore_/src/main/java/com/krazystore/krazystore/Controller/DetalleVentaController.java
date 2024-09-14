/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.DetalleVentaCreationRequest;
import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Service.CategoriaService;
import com.krazystore.krazystore.Service.DetalleVentaService;
import com.krazystore.krazystore.Service.VentaService;
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
@RequestMapping("/api/detalle_ventas")

public class DetalleVentaController {
    private final DetalleVentaService detalleVentaService;

    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    public List<DetalleVentaEntity> findAll() {
        return detalleVentaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DetalleVentaEntity> findById(@PathVariable("id") Long id) {
        return detalleVentaService.findById(id);
    }
    
/*
    @PostMapping
    public Iterable<DetalleVentaEntity> saveDetalle(@RequestBody DetalleVentaCreationRequest detalleVentaDTO) {
        return detalleVentaService.saveDetalleVenta(venta,detalle);
    }*/

    @PutMapping("/{id}")
    public DetalleVentaEntity updateDetalle(@PathVariable long id, @RequestBody DetalleVentaEntity detalle) {
        return detalleVentaService.updateDetalleVenta(detalle, id);
    }

    @DeleteMapping("/{id}")
    public void deleteDetalle(@PathVariable("id") Long id) {
        detalleVentaService.deleteDetalleVenta(id);
    }
    
}
