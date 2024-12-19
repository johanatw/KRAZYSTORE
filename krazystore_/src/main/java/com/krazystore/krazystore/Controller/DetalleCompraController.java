/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;


import com.krazystore.krazystore.Entity.DetalleCompra;
import com.krazystore.krazystore.Service.DetalleCompraService;
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
@RequestMapping("/api/detalle_compra")
public class DetalleCompraController {
    private final DetalleCompraService detalleService;

    public DetalleCompraController(DetalleCompraService detalleService) {
        this.detalleService = detalleService;
    }

    @GetMapping("/{id}")
    public List<DetalleCompra> findByIdCompra(@PathVariable("id") Long id) {
        return detalleService.findByIdCompra(id);
    }
        
}
