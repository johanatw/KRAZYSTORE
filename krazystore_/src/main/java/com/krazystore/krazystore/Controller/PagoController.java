/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.DetallePagoPedidoDTO;
import com.krazystore.krazystore.DTO.PedidoMontoPagadoDTO;
import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Service.PagoService;
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
@RequestMapping("/api/pagos")
public class PagoController {
    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }
    
    @GetMapping
    public List<PagoEntity> findAll() {
        return pagoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<PagoEntity> findById(@PathVariable("id") Long id) {
        return pagoService.findById(id);
    }
    @GetMapping("/pagos/{id}")
    public Optional<PedidoMontoPagadoDTO> getPagosPedido(@PathVariable("id") Long id) {

        return pagoService.getPagosPedido(id);
    }

    @PostMapping
    public PagoEntity savePago(@RequestBody PagoEntity pagoEntity) {
        return pagoService.savePago(pagoEntity);
    }

    @PutMapping("/{id}")
    public PagoEntity updatePago(@PathVariable long id, @RequestBody PagoEntity pago) {
        return pagoService.updatePago(pago, id);
    }

    @DeleteMapping("/{id}")
    public void deletePago(@PathVariable("id") Long id) {
        pagoService.deletePago(id);
    }
    
    @DeleteMapping("/anticipo/{id}")
    public void deletePagosByAnticipo(@PathVariable("id") Long id) {
        pagoService.deletePagosByAnticipo(id);
    }
    
    @DeleteMapping("/reembolso/{id}")
    public void deletePagosByReembolso(@PathVariable("id") Long id) {
        pagoService.deletePagosByReembolso(id);
    }
}
