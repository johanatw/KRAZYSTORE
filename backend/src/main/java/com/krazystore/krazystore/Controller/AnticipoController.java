/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import com.krazystore.krazystore.Service.AnticipoService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/anticipos")
public class AnticipoController {
    private final AnticipoService anticipoService;

    public AnticipoController(AnticipoService anticipoService) {
        this.anticipoService = anticipoService;
    }
    
    /*@PostMapping
    public String saveAnticipo(@RequestBody AnticipoEntity anticipoEntity) {
        return anticipoService.saveAnticipo(anticipoEntity);
    }*/
    @GetMapping
    public List<AnticipoEntity> findAll() {
        return anticipoService.findAll();
    }
    
    @GetMapping("/{id}")
    public Optional<AnticipoEntity> findById(@PathVariable("id") Long id) {
        return anticipoService.findById(id);
    }
    
    @GetMapping("/pedido/{id}")
    public List<AplicacionAnticipo> findAnticiposAplicarByIdPedidoVenta(@PathVariable("id") Long id) {
        System.out.println("Anticipo controller");
        return anticipoService.findAnticiposAplicarByIdPedidoVenta(id);
    }
    
    @DeleteMapping("/{id}")
    public int deleteAnticipo(@PathVariable("id") Long id) {
        return anticipoService.deleteAnticipo(id);
    }
    
    @DeleteMapping("/reembolsos/{id}")
    public void deleteAnticipoReembolsos(@PathVariable("id") Long id) {
        System.out.println("deleteAntRembolsControlee");
        System.out.println(id);
        anticipoService.deleteAnticipoReembolsos(id);
    }
}
