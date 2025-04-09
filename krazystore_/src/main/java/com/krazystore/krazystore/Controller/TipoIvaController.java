/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.IvaEntity;
import com.krazystore.krazystore.Service.TipoIvaService;
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
@RequestMapping("/api/tipos_iva")
public class TipoIvaController {

    public TipoIvaController(TipoIvaService tipoIvaService) {
        this.tipoIvaService = tipoIvaService;
    }
    private final TipoIvaService tipoIvaService;
    
    @GetMapping
    public List<IvaEntity> findAll() {
        return tipoIvaService.findAll();
    }
}
