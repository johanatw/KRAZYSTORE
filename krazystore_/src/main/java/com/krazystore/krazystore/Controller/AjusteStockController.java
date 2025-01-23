/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.AjusteCreationDTO;
import com.krazystore.krazystore.DTO.DetalleAjusteDTO;
import com.krazystore.krazystore.Entity.AjusteStock;
import com.krazystore.krazystore.Service.AjusteService;
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
@RequestMapping("/api/ajustes")
public class AjusteStockController {

    public AjusteStockController(AjusteService ajusteService) {
        this.ajusteService = ajusteService;
    }
    private final AjusteService ajusteService;
    
    @GetMapping
    public List<AjusteStock> findAll() {
        return ajusteService.findAll();
    }
    
    @GetMapping("/detalles")
    public List<DetalleAjusteDTO> obtenerProductosParaAjuste(){
        return ajusteService.obtenerProductosParaAjuste();
    }

    @GetMapping("/{id}")
    public AjusteCreationDTO findById(@PathVariable("id") Long id) {
        return ajusteService.findById(id);
    }

    @PostMapping
    public AjusteStock saveAjuste(@RequestBody AjusteCreationDTO ajuste)throws Exception {
        return ajusteService.saveAjuste(ajuste);
    }

    @PutMapping("/{id}")
    public AjusteStock updateAjuste(@PathVariable long id, @RequestBody AjusteCreationDTO ajuste)throws Exception {
        return ajusteService.updateAjuste(ajuste, id);
    }
    
    @PutMapping("/ajustar/{id}")
    public AjusteStock ajustar(@PathVariable long id)throws Exception {
        return ajusteService.ajustar(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAjuste(@PathVariable("id") Long id) {
        ajusteService.deleteAjuste(id);
    }
}
