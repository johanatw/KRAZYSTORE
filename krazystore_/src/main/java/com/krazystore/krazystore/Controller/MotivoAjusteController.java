/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.MotivoAjusteEntity;
import com.krazystore.krazystore.Service.MotivoAjusteService;
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
@RequestMapping("/api/motivos_ajuste")
public class MotivoAjusteController {
    private final MotivoAjusteService motivoAjusteService;
    
    public MotivoAjusteController(MotivoAjusteService motivoAjusteService) {
        this.motivoAjusteService = motivoAjusteService;
    }

    @GetMapping
    public List<MotivoAjusteEntity> findAll() {
        return motivoAjusteService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<MotivoAjusteEntity> findById(@PathVariable("id") Long id) {
        return motivoAjusteService.findById(id);
    }    

    @PostMapping
    public MotivoAjusteEntity saveMotivoAjuste(@RequestBody MotivoAjusteEntity motivoAjuste) {
        return motivoAjusteService.saveMotivoAjuste(motivoAjuste);
    }

    @PutMapping("/{id}")
    public MotivoAjusteEntity updateMotivoAjuste(@PathVariable long id, @RequestBody MotivoAjusteEntity motivoAjuste) {
        return motivoAjusteService.updateMotivoAjuste(motivoAjuste, id);
    }

    @DeleteMapping("/{id}")
    public void deleteMotivoAjuste(@PathVariable("id") Long id) {
        motivoAjusteService.deleteMotivoAjuste(id);
    }
}
