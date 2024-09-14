/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.EstadoEntity;
import com.krazystore.krazystore.Service.EstadoService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/estados")
public class EstadoController {
    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping
    public List<EstadoEntity> findAll() {
        return estadoService.findAll();
    }
    
    @GetMapping("/tipo")
    public List<EstadoEntity> findByTipo(@RequestParam(value="tipo") String tipo) {
        return estadoService.findByTipo(tipo);
    }
    

    @GetMapping("/{id}")
    public Optional<EstadoEntity> findById(@PathVariable("id") Long id) {
        return estadoService.findById(id);
    }
    

    @PostMapping
    public EstadoEntity saveEstado(@RequestBody EstadoEntity estadoEntity) {
        return estadoService.saveEstado(estadoEntity);
    }

    @PutMapping("/{id}")
    public EstadoEntity updateEstado(@PathVariable long id, @RequestBody EstadoEntity estado) {
        return estadoService.updateEstado(estado, id);
    }

    @DeleteMapping("/{id}")
    public void deleteEstado(@PathVariable("id") Long id) {
        estadoService.deleteEstado(id);
    }

    
}
