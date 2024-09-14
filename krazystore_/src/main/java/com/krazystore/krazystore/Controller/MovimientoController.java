/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;


import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.PRUEBADTO;
import com.krazystore.krazystore.DTO.ReembolsoCreationDTO;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Service.MovimientoService;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/api/movimientos")
public class MovimientoController {
    private final MovimientoService movimientoService;
    
    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }
    
    @GetMapping
    public List<MovimientoEntity> findAll() {
        return movimientoService.findAll();
    }
    
    @GetMapping("/prueba/{id}")
    public List<PRUEBADTO> prueba(@PathVariable("id") Long id) {
        return movimientoService.pr(id);
    }

    @GetMapping("/{id}")
    public Optional<MovimientoEntity> findById(@PathVariable("id") Long id) {
        return movimientoService.findById(id);
    }
    

    @PostMapping
    public MovimientoEntity saveMovimiento(@RequestBody MovimientoEntity movimientoEntity) {
        return movimientoService.saveMovimiento(movimientoEntity);
    }
    
    @PostMapping("/anticipo")
    public MovimientoEntity saveMovimiento(@RequestBody AnticipoCreationDTO anticipoCreationDTO){
        
        return movimientoService.saveMovimiento(anticipoCreationDTO.getAnticipo(), anticipoCreationDTO.getPagos());
    }
    
    @PostMapping("/reembolso")
    public MovimientoEntity saveMovimiento(@RequestBody ReembolsoCreationDTO reembolsoCreationDTO) {
        
        return movimientoService.saveMovimiento(reembolsoCreationDTO.getReembolso(),reembolsoCreationDTO.getPagos() );
    }

    @PutMapping("/{id}")
    public MovimientoEntity updateMovimiento(@PathVariable long id, @RequestBody MovimientoEntity movimiento) {
        return movimientoService.updateMovimiento(movimiento, id);
    }

    @DeleteMapping("/{id}")
    public void deleteMovimiento(@PathVariable("id") Long id) {
        movimientoService.deleteMovimiento(id);
    }
    
    @DeleteMapping("/reembolso/{id}")
    public void deleteMovimientoReembolso(@PathVariable("id") Long id) {
        movimientoService.deleteReembolso(id);
    }
    
    @DeleteMapping("/anticipo/{id}")
    public void deleteMovimientoAnticipo(@PathVariable("id") Long id) {
        movimientoService.deleteAnticipo(id);
    }
    
    @DeleteMapping("/reembolsos/anticipo/{id}")
    public void deleteAnticipoConReembolsos(@PathVariable("id") Long id) {
        movimientoService.deleteAnticipoConReembolsos(id);
        
    }
}
