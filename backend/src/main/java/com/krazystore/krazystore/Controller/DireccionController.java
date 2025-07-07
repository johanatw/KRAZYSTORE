/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.DireccionEntity;
import com.krazystore.krazystore.Service.DireccionService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/direcciones")
public class DireccionController {
    private final DireccionService direccionService;

    public DireccionController(DireccionService direccionService) {
        this.direccionService = direccionService;
    }

    @GetMapping
    public List<DireccionEntity> findAll() {
        return direccionService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DireccionEntity> findById(@PathVariable("id") Long id) {
        return direccionService.findById(id);
    }
    

    @PostMapping
    public DireccionEntity saveDireccion(@RequestBody DireccionEntity direccionEntity) {
        return direccionService.saveDireccion(direccionEntity);
    }

    @PutMapping("/{id}")
    public DireccionEntity updateDireccion(@PathVariable long id, @RequestBody DireccionEntity direccion) {
        return direccionService.updateDireccion(direccion, id);
    }

    @DeleteMapping("/{id}")
    public void deleteDireccion(@PathVariable("id") Long id) {
        direccionService.deleteDireccion(id);
    }
    
    @GetMapping("/direcciones/{id}")
    public List<DireccionEntity> findDireccionesByIdCliente(@PathVariable("id") Long id) {
        return direccionService.findDireccionesByIdCliente(id);
    }
    
}
