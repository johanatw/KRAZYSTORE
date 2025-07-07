/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.ProveedorEntity;
import com.krazystore.krazystore.Service.ProveedorService;
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
@RequestMapping("/api/proveedores")
public class ProveedorController {
    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }
    
    
    @GetMapping
    public List<ProveedorEntity> findAll() {
        return proveedorService.findAll();
    }
    
    @GetMapping("/proveedores_productos")
    public List<ProveedorEntity> findProveedoresProductos() {
        return proveedorService.findProveedoresProductos();
    }
    
    @GetMapping("/proveedores_nacionales_productos")
    public List<ProveedorEntity> findProveedoresNacionalesProductos() {
        return proveedorService.findProveedoresNacionalesProductos();
    }
    
    @GetMapping("/proveedores_importacion")
    public List<ProveedorEntity> findProveedoresImportacion() {
        return proveedorService.findProveedoresImportacion();
    }

    @GetMapping("/{id}")
    public Optional<ProveedorEntity> findById(@PathVariable("id") Long id) {
        return proveedorService.findById(id);
    }
        

    @PostMapping
    public ProveedorEntity saveProveedor(@RequestBody ProveedorEntity proveedor) {
        return proveedorService.saveProveedor(proveedor);
    }

    @PutMapping("/{id}")
    public ProveedorEntity updateProveedor(@PathVariable long id, @RequestBody ProveedorEntity proveedor) {
        return proveedorService.updateProveedor(proveedor, id);
    }

    @DeleteMapping("/{id}")
    public void deleteProveedor(@PathVariable("id") Long id) {
        proveedorService.deleteProveedor(id);
    }
}
