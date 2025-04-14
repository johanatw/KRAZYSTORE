/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.AjusteCreationDTO;
import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.DTO.EntregaCreationDTO;
import com.krazystore.krazystore.Entity.AjusteStock;
import com.krazystore.krazystore.Entity.EntregaEntity;
import com.krazystore.krazystore.Service.AjusteService;
import com.krazystore.krazystore.Service.EntregaService;
import java.util.List;
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
@RequestMapping("/api/entregas")
public class EntregaController {

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }
    private final EntregaService entregaService;
    
    @GetMapping
    public List<EntregaEntity> findAll() {
        return entregaService.findAll();
    }
    
    @GetMapping("/{id}")
    public EntregaCreationDTO findById(@PathVariable("id") Long id) {
        return entregaService.findById(id);
    }

    @PostMapping
    public EntregaEntity saveEntrega(@RequestBody EntregaCreationDTO entrega) {
        return entregaService.saveEntrega(entrega);
    }

    @PutMapping("/{id}")
    public EntregaEntity updateEntrega(@PathVariable long id, @RequestBody EntregaCreationDTO entrega)throws Exception {
        return entregaService.updateEntrega(entrega, id);
    }
    
    @PutMapping("/reprogramar/{id}")
    public EntregaEntity reprogramarEntrega(@PathVariable long id, @RequestBody EntregaCreationDTO entrega)throws Exception {
        return entregaService.reprogramarEntrega(entrega, id);
    }
    
    @PutMapping("/marcar_como_entregado/{id}")
    public EntregaEntity marcarComoEntregado(@PathVariable long id, @RequestBody EntregaCreationDTO entrega)throws Exception {
        return entregaService.marcarComoEntregado(id);
    }
    
    @PutMapping("/marcar_como_no_entregado/{id}")
    public EntregaEntity marcarComoNoEntregado(@PathVariable long id, @RequestBody EntregaCreationDTO entrega)throws Exception {
        return entregaService.marcarComoNoEntregado(id);
    }
    
    @DeleteMapping("/{id}")
    public void deleteEntrega(@PathVariable("id") Long id) {
        entregaService.deleteEntrega(id);
    }
}
