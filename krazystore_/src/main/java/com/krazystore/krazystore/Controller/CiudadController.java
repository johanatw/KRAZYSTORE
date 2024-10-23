/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.CiudadEntity;
import com.krazystore.krazystore.Service.CiudadService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/ciudades")
public class CiudadController {
    private final CiudadService ciudadService;
    
    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    @GetMapping
    public List<CiudadEntity> findAll() {
        return ciudadService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CiudadEntity> findById(@PathVariable("id") Long id) {
        return ciudadService.findById(id);
    }
    
    @GetMapping("/departamento/{id}")
    public List<CiudadEntity> findByIdDepartamento(@PathVariable("id") Long id) {
        return ciudadService.findByIdDepartamento(id);
    }
    

    @PostMapping
    public CiudadEntity saveCiudad(@RequestBody CiudadEntity ciudadEntity) {
        return ciudadService.saveCiudad(ciudadEntity);
    }

    @PutMapping("/{id}")
    public CiudadEntity updateCiudad(@PathVariable long id, @RequestBody CiudadEntity ciudad) {
        return ciudadService.updateCiudad(ciudad, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCiudad(@PathVariable("id") Long id) {
        ciudadService.deleteCiudad(id);
    }

}
