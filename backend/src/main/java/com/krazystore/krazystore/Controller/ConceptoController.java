/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.ConceptoEntity;
import com.krazystore.krazystore.Service.ConceptoService;
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
@RequestMapping("/api/conceptos")
public class ConceptoController {
    private final ConceptoService conceptoService;

    public ConceptoController(ConceptoService conceptoService) {
        this.conceptoService = conceptoService;
    }

    @GetMapping("/tipo/{tipo}")
    public List<ConceptoEntity> findAll(@PathVariable("tipo") char tipo) {
        return conceptoService.findByTipo(tipo);
    }
    
    @GetMapping("/conceptos_ingreso_egreso")
    public List<ConceptoEntity> getConceptosIngresoEgreso() {
        return conceptoService.getConceptosIngresoEgreso();
    }

   
}
