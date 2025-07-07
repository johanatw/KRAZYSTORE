/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.ModoEntregaEntity;
import com.krazystore.krazystore.Service.ModoEntregaService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/modos_entrega")
public class ModoEntregaController {
    private final ModoEntregaService modoEntregaService;

    public ModoEntregaController(ModoEntregaService modoEntregaService) {
        this.modoEntregaService = modoEntregaService;
    }

    @GetMapping
    public List<ModoEntregaEntity> findAll() {
        return modoEntregaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ModoEntregaEntity> findById(@PathVariable("id") Long id) {
        return modoEntregaService.findById(id);
    }
    

    @PostMapping
    public ModoEntregaEntity saveModoEntrega(@RequestBody ModoEntregaEntity modoEntregaEntity) {
        return modoEntregaService.saveModoEntrega(modoEntregaEntity);
    }

    @PutMapping("/{id}")
    public ModoEntregaEntity updateModoEntrega(@PathVariable long id, @RequestBody ModoEntregaEntity modoEntrega) {
        return modoEntregaService.updateModoEntrega(modoEntrega, id);
    }

    @DeleteMapping("/{id}")
    public void deleteModoEntrega(@PathVariable("id") Long id) {
        modoEntregaService.deleteModoEntrega(id);
    }

    
}
