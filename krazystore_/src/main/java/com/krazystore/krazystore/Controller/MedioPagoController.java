/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.MedioPagoEntity;
import com.krazystore.krazystore.Service.MedioPagoService;
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
@RequestMapping("/api/medios_pago")
public class MedioPagoController {

    public MedioPagoController(com.krazystore.krazystore.Service.MedioPagoService medioPagoService) {
        this.medioPagoService = medioPagoService;
    }
    private final MedioPagoService medioPagoService;
    
    @GetMapping
    public List<MedioPagoEntity> findAll() {
        return medioPagoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<MedioPagoEntity> findById(@PathVariable("id") Long id) {
        return medioPagoService.findById(id);
    }
    
    @PostMapping
    public MedioPagoEntity saveMedioPago(@RequestBody MedioPagoEntity timbrado) {
            return medioPagoService.saveMedioPago(timbrado);

    }

    @PutMapping("/{id}")
    public MedioPagoEntity updateMedioPago(@PathVariable long id, @RequestBody MedioPagoEntity timbrado) {
        
            return medioPagoService.updateMedioPago( timbrado, id);
        
    }

    @DeleteMapping("/{id}")
    public void deleteMedioPago(@PathVariable("id") Long id) {
        medioPagoService.deleteMedioPago(id);
    }
}
