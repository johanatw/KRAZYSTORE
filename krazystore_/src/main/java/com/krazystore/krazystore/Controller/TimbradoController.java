/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.TimbradoDTO;
import com.krazystore.krazystore.Service.TimbradoService;
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
@RequestMapping("/api/timbrados")
public class TimbradoController {

    public TimbradoController(TimbradoService timbradoService) {
        this.timbradoService = timbradoService;
    }
    private final TimbradoService timbradoService;
    
    @GetMapping("/timbrado_vigente")
    public TimbradoDTO getTimbradoVigente() {
        return timbradoService.getTimbradoDTOVigente();         
    }
    
    
}
