/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.EmpresaTransporte;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import com.krazystore.krazystore.Service.EmpresaTransporteService;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/deliverys")
public class EmpresaTransporteController {
    private final EmpresaTransporteService envioService;

    public EmpresaTransporteController(EmpresaTransporteService envioService) {
        this.envioService = envioService;
    }
    @GetMapping
    public List<EmpresaTransporte> findAll() {
        return envioService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<EmpresaTransporte> findById(@PathVariable("id") Long id) {
        return envioService.findById(id);
    }
    

    @PostMapping
    public EmpresaTransporte saveEnvio(@RequestBody EmpresaTransporte envioEntity) {
        return envioService.saveEnvio(envioEntity);
    }

    @PutMapping("/{id}")
    public EmpresaTransporte updateEnvio(@PathVariable long id, @RequestBody EmpresaTransporte envio) {
        return envioService.updateEnvio(envio, id);
    }

    @DeleteMapping("/{id}")
    public void deleteEnvio(@PathVariable("id") Long id) {
        envioService.deleteEnvio(id);
    }

}
