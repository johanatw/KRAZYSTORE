/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.MedioPagoEntity;
import com.krazystore.krazystore.Service.MedioPagoService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
