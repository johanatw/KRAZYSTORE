/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.Entity.TipoDocumentoFiscal;
import com.krazystore.krazystore.Service.TipoDocFiscalService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/tipos_doc_fiscal")
public class TipoDocFiscalController {
    private final TipoDocFiscalService tipoDocService;

    public TipoDocFiscalController(TipoDocFiscalService tipoDocService) {
        this.tipoDocService = tipoDocService;
    }
    
    @GetMapping
    public List<TipoDocumentoFiscal> findAll() {
        return tipoDocService.findAll();
    }
}
