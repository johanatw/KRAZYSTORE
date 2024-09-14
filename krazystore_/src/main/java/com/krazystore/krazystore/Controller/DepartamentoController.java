/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.DepartamentoEntity;
import com.krazystore.krazystore.Service.DepartamentoService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/departamentos")
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public List<DepartamentoEntity> findAll() {
        return departamentoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DepartamentoEntity> findById(@PathVariable("id") Long id) {
        return departamentoService.findById(id);
    }
    

    @PostMapping
    public DepartamentoEntity saveDepartamento(@RequestBody DepartamentoEntity departamentoEntity) {
        return departamentoService.saveDepartamento(departamentoEntity);
    }

    @PutMapping("/{id}")
    public DepartamentoEntity updateDepartamento(@PathVariable long id, @RequestBody DepartamentoEntity departamento) {
        return departamentoService.updateDepartamento(departamento, id);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartamento(@PathVariable("id") Long id) {
        departamentoService.deleteDepartamento(id);
    }

    
}
