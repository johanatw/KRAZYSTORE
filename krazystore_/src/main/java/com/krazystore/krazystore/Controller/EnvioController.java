/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.EnvioEntity;
import com.krazystore.krazystore.Service.EnvioService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/envios")
public class EnvioController {
    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }
    @GetMapping
    public List<EnvioEntity> findAll() {
        return envioService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<EnvioEntity> findById(@PathVariable("id") Long id) {
        return envioService.findById(id);
    }
    

    @PostMapping
    public EnvioEntity saveEnvio(@RequestBody EnvioEntity envioEntity) {
        return envioService.saveEnvio(envioEntity);
    }

    @PutMapping("/{id}")
    public EnvioEntity updateEnvio(@PathVariable long id, @RequestBody EnvioEntity envio) {
        return envioService.updateEnvio(envio, id);
    }

    @DeleteMapping("/{id}")
    public void deleteEnvio(@PathVariable("id") Long id) {
        envioService.deleteEnvio(id);
    }

}
