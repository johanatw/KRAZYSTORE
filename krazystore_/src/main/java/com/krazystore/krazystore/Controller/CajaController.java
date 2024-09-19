/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Service.CajaService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/cajas")
public class CajaController {
    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }
    
    
    @GetMapping
    public List<CajaEntity> findAll() {
        return cajaService.findAll();
    }
    
    @GetMapping("/caja_abierta")
    public Optional<CajaEntity> getCajaAbierta() {
        return cajaService.getCajaAbierta();         
    }
    
    @GetMapping("/{id}")
    public Optional<CajaEntity> findById(@PathVariable("id") Long id) {
        return cajaService.findById(id);
                
    }
    
    @PostMapping
    public CajaEntity abrirCaja() {
        return cajaService.abrirCaja();
    }
    
    @PutMapping("/{id}")
    public void updateCategoria(@PathVariable long id) {
        cajaService.cerrarCaja(id);
    }
    
}


