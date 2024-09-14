/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.CostoEnvioEntity;
import com.krazystore.krazystore.Service.CostoEnvioService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/costos_envio")
public class CostoEnvio {
    private final CostoEnvioService costoEnvioService;

    public CostoEnvio(CostoEnvioService costoEnvioService) {
        this.costoEnvioService = costoEnvioService;
    }
    @GetMapping
    public List<CostoEnvioEntity> findAll() {
        return costoEnvioService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<CostoEnvioEntity> findById(@PathVariable("id") Long id) {
        return costoEnvioService.findById(id);
    }
    @GetMapping("/ciudad")
    public List<CostoEnvioEntity> findByCiudad(@RequestParam(value="id") Long id) {
        return costoEnvioService.findByCiudad(id);
    }

    @PostMapping
    public CostoEnvioEntity saveCostoEnvio(@RequestBody CostoEnvioEntity costoEnvioEntity) {
        return costoEnvioService.saveCostoEnvio(costoEnvioEntity);
    }

    @PutMapping("/{id}")
    public CostoEnvioEntity updateCostoEnvio(@PathVariable long id, @RequestBody CostoEnvioEntity costoEnvio) {
        return costoEnvioService.updateCostoEnvio(costoEnvio, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCostoEnvio(@PathVariable("id") Long id) {
        costoEnvioService.deleteCostoEnvio(id);
    }
    
}
