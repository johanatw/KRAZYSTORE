/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.TipoDocEntity;
import com.krazystore.krazystore.Service.TipoDocService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/tipos_doc")
public class TipoDocController {
    private final TipoDocService tipoDocService;

    public TipoDocController(TipoDocService tipoDocService) {
        this.tipoDocService = tipoDocService;
    }
    
    @GetMapping
    public List<TipoDocEntity> findAll() {
        return tipoDocService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<TipoDocEntity> findById(@PathVariable("id") Long id) {
        return tipoDocService.findById(id);
    }
    

    @PostMapping
    public TipoDocEntity saveTipoDoc(@RequestBody TipoDocEntity tipoDocEntity) {
        return tipoDocService.saveTipoDoc(tipoDocEntity);
    }

    @PutMapping("/{id}")
    public TipoDocEntity updateTipoDoc(@PathVariable long id, @RequestBody TipoDocEntity tipoDoc) {
        return tipoDocService.updateTipoDoc(tipoDoc, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTipoDoc(@PathVariable("id") Long id) {
        tipoDocService.deleteTipoDoc(id);
    }
    
}
