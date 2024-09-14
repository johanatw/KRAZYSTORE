/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Service.FormaPagoService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/formas_pago")
public class FormaPagoController {
    private final FormaPagoService formaPagoService;

    public FormaPagoController(FormaPagoService formaPagoService) {
        this.formaPagoService = formaPagoService;
    }

    @GetMapping
    public List<FormaPagoEntity> findAll() {
        return formaPagoService.findAll();
    }
    
    @GetMapping("/sin_anticipo")
    public List<FormaPagoEntity> findAllSinAnticipo() {
        return formaPagoService.findAllSinAnticipo();
    }

    @GetMapping("/{id}")
    public Optional<FormaPagoEntity> findById(@PathVariable("id") Long id) {
        return formaPagoService.findById(id);
    }
    

    @PostMapping
    public FormaPagoEntity saveFormaPago(@RequestBody FormaPagoEntity formaPagoEntity) {
        return formaPagoService.saveFormaPago(formaPagoEntity);
    }

    @PutMapping("/{id}")
    public FormaPagoEntity updateFormaPago(@PathVariable long id, @RequestBody FormaPagoEntity formaPago) {
        return formaPagoService.updateFormaPago(formaPago, id);
    }

    @DeleteMapping("/{id}")
    public void deleteFormaPago(@PathVariable("id") Long id) {
        formaPagoService.deleteFormaPago(id);
    }

    
}
