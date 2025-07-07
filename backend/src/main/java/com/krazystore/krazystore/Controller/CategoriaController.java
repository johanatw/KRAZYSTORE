/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Service.CategoriaService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/categorias")

public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaEntity> findAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<CategoriaEntity> categoria = categoriaService.findById(id);
        return ResponseEntity.ok(categoria);
                
    }
    

    @PostMapping
    public CategoriaEntity saveCategoria(@RequestBody CategoriaEntity categoriaEntity) {
        return categoriaService.saveCategoria(categoriaEntity);
    }

    @PutMapping("/{id}")
    public CategoriaEntity updateCategoria(@PathVariable long id, @RequestBody CategoriaEntity categoria) {
        return categoriaService.updateCategoria(categoria, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoria(@PathVariable("id") Long id) {
        categoriaService.deleteCategoria(id);
    }
    
}
