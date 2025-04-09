/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.PuntoEntregaEntity;
import com.krazystore.krazystore.Service.PuntoEntregaService;
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
@RequestMapping("/api/puntos_entrega")
public class PuntoEntregaController {

    public PuntoEntregaController(PuntoEntregaService puntoEntregaService) {
        this.puntoEntregaService = puntoEntregaService;
    }
    private final PuntoEntregaService puntoEntregaService;
    
    @GetMapping
    public List<PuntoEntregaEntity> findAll() {
        return puntoEntregaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<PuntoEntregaEntity> findById(@PathVariable("id") Long id) {
        return puntoEntregaService.findById(id);
    }

    @PostMapping
    public PuntoEntregaEntity savePuntoEntrega(@RequestBody PuntoEntregaEntity puntoEntregaEntity) {
        return puntoEntregaService.savePuntoEntrega(puntoEntregaEntity);
    }

    @PutMapping("/{id}")
    public PuntoEntregaEntity updatePuntoEntrega(@PathVariable long id, @RequestBody PuntoEntregaEntity puntoEntrega) {
        return puntoEntregaService.updatePuntoEntrega(puntoEntrega, id);
    }

    @DeleteMapping("/{id}")
    public void deletePuntoEntrega(@PathVariable("id") Long id) {
        puntoEntregaService.deletePuntoEntrega(id);
    }
    
}
