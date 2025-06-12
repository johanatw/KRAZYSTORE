/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.MedioCobroEntity;
import com.krazystore.krazystore.Service.MedioCobroService;
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
@RequestMapping("/api/medios_cobro")
public class MedioCobroController {

    public MedioCobroController(MedioCobroService medioCobroService) {
        this.medioCobroService = medioCobroService;
    }
    private final MedioCobroService medioCobroService;
    
    @GetMapping
    public List<MedioCobroEntity> findAll() {
        return medioCobroService.findAll();
    }

}
