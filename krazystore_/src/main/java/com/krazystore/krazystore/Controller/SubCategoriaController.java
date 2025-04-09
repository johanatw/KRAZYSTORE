/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.SubCategoriaEntity;
import com.krazystore.krazystore.Service.SubCategoriaService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/sub_categorias")
public class SubCategoriaController {
    private final SubCategoriaService subCatService;

    public SubCategoriaController(SubCategoriaService subCatService) {
        this.subCatService = subCatService;
    }
    
    
    @GetMapping
    public List<SubCategoriaEntity> findAll() {
        return subCatService.findAll();
    }
    
    @GetMapping("/categoria/{id}")
    public List<SubCategoriaEntity> findByIdCategoria(@PathVariable("id") Long id) {
        return subCatService.findByIdCategoria(id);
    }
}
