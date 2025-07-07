/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
import com.krazystore.krazystore.Service.ReembolsoService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api/reembolsos")
public class ReembolsoController {
    private final ReembolsoService reembolsoService;
    
    public ReembolsoController(ReembolsoService reembolsoService) {
        this.reembolsoService = reembolsoService;
    }
    
    @GetMapping
    public List<ReembolsoAnticipo> findAll() {
        return reembolsoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ReembolsoAnticipo> findById(@PathVariable("id") Long id) {
        return reembolsoService.findById(id);
    }
    
    @GetMapping("/reembolsos/{id}")
    public List<ReembolsoAnticipo> findByIdAnticipo(@PathVariable("id") Long id) {
        return reembolsoService.findByIdAnticipo(id);
    }

    /*@PostMapping
    public ReembolsoAnticipo saveReembolso(@RequestBody ReembolsoAnticipoCreationDTO reembolsoCreationDTO) {
        
        return reembolsoService.saveReembolso(reembolsoCreationDTO.getReembolso(),reembolsoCreationDTO.getPagos() );
    }*/
    
   

    @PutMapping("/{id}")
    public ReembolsoAnticipo updateReembolso(@PathVariable long id, @RequestBody ReembolsoAnticipo reembolso) {
        return reembolsoService.updateReembolso(reembolso, id);
    }

    @DeleteMapping("/{id}")
    public void deleteReembolso(@PathVariable("id") Long id) {
        reembolsoService.deleteReembolsoAnticipo(id);
    }
}
