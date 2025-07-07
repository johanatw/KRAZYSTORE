/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.TimbradoDTO;
import com.krazystore.krazystore.Entity.TimbradoEntity;
import com.krazystore.krazystore.Service.TimbradoService;
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
@RequestMapping("/api/timbrados")
public class TimbradoController {

    public TimbradoController(TimbradoService timbradoService) {
        this.timbradoService = timbradoService;
    }
    private final TimbradoService timbradoService;
    
    @GetMapping("/timbrado_vigente")
    public TimbradoDTO getTimbradoVigente() {
        return timbradoService.getTimbradoDTOVigente();         
    }
    
    @GetMapping
    public List<TimbradoEntity> findAll() {
        return timbradoService.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<TimbradoEntity> findById(@PathVariable("id") Long id) {
        return timbradoService.findById(id);
    }
    
    @PostMapping
    public TimbradoEntity saveTimbrado(@RequestBody TimbradoEntity timbrado) {
            return timbradoService.saveTimbrado(timbrado);

    }

    @PutMapping("/{id}")
    public TimbradoEntity updateTimbrado(@PathVariable long id, @RequestBody TimbradoEntity timbrado) {
        
            return timbradoService.updateTimbrado(id, timbrado);
        
    }

    @DeleteMapping("/{id}")
    public void deleteTimbrado(@PathVariable("id") Long id) {
        timbradoService.deleteTimbrado(id);
    }
    
}
