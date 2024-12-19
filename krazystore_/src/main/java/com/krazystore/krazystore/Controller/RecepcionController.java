/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.RecepcionCreationDTO;
import com.krazystore.krazystore.DTO.RecepcionDTO;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import com.krazystore.krazystore.Service.RecepcionService;
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
@RequestMapping("/api/recepciones")
public class RecepcionController {
    private final RecepcionService recepcionService;

    public RecepcionController(RecepcionService recepcionService) {
        this.recepcionService = recepcionService;
    }
    
    @GetMapping
    public List<RecepcionDTO> findAll() {
        return recepcionService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RecepcionDTO> findById(@PathVariable("id") Long id) {
        return recepcionService.findById(id);
    }

    @PostMapping
    public RecepcionEntity saveRecepcion(@RequestBody RecepcionCreationDTO recepcion) {
        return recepcionService.saveRecepcion(recepcion);
    }

    @PutMapping("/{id}")
    public RecepcionEntity updateRecepcion(@PathVariable long id, @RequestBody RecepcionCreationDTO recepcion) {
        return recepcionService.updateRecepcion(recepcion, id);
    }

    @DeleteMapping("/{id}")
    public void deleteRecepcion(@PathVariable("id") Long id) {
        recepcionService.deleteRecepcion(id);
    }
}
