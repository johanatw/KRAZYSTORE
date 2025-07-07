/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.EgresoVarioDTO;
import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.IngresoVarioDTO;
import com.krazystore.krazystore.DTO.MovimientoCreationDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.ReembolsoAnticipoCreationDTO;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Service.CajaService;
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
@RequestMapping("/api/cajas")
public class CajaController {
    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }
    
    
    @GetMapping
    public List<CajaEntity> findAll() {
        return cajaService.findAll();
    }
    
    @GetMapping("/caja_abierta")
    public Optional<CajaEntity> getCajaAbierta() {
        return cajaService.getCajaAbierta();         
    }
    
    @GetMapping("/{id}")
    public Optional<CajaEntity> findById(@PathVariable("id") Long id) {
        return cajaService.findById(id);
                
    }
    
    @PostMapping
    public CajaEntity abrirCaja(@RequestBody CajaEntity caja) {
        return cajaService.abrirCaja();
    }
    
    @PutMapping("/cerrar/{id}")
    public void cerrarCaja(@PathVariable("id") Long id, @RequestBody CajaEntity caja) {
        cajaService.cerrarCaja(id);
    }
    
    @GetMapping("/estado_pagos_pedido_venta/{id}")
    public EstadoPagoPedidoDTO getEstadoPagoPedidoVenta(@PathVariable("id") Long id) {
        return cajaService.getEstadoPagoPedidoVenta(id);
    }
    
    @PostMapping("/anticipo")
    public MovimientoEntity saveAnticipo(@RequestBody AnticipoCreationDTO anticipoCreationDTO){
        return cajaService.saveMovimiento(anticipoCreationDTO);
    }
    
    @PostMapping("/reembolso")
    public MovimientoEntity saveReembolso(@RequestBody ReembolsoAnticipoCreationDTO reembolsoCreationDTO){
        return cajaService.saveMovimiento(reembolsoCreationDTO);
    }
    
    
    @PostMapping("/ingreso_vario")
    public MovimientoEntity saveMovimiento(@RequestBody IngresoVarioDTO ingreso) {
        return cajaService.saveMovimiento(ingreso);
    }
    
    @PostMapping("/egreso_vario")
    public MovimientoEntity saveMovimiento(@RequestBody EgresoVarioDTO egreso) {
        return cajaService.saveMovimiento(egreso);
    }
    
    @GetMapping("/pendientes_pago")
    public List<MovimientoEntity> getMovimientosPendientesDePago() {
        return cajaService.getMovimientosPendientesDePago();
    }
    
    @GetMapping("/pendientes_cobro")
    public List<MovimientoEntity> getMovimientosPendientesDeCobro() {
        return cajaService.getMovimientosPendientesDeCobro();
    }
    
    @PostMapping("/pagos_pendientes")
    public MovimientoEntity savePagosPendientes(@RequestBody MovimientoCreationDTO movimiento) {
        return cajaService.savePagosPendientes(movimiento);
    }
    
    @PostMapping("/cobros_pendientes")
    public MovimientoEntity saveCobrosPendientes(@RequestBody MovimientoCreationDTO movimiento) {
        return cajaService.saveCobrosPendientes(movimiento);
    }
    
    @DeleteMapping("/{id}")
    public void deleteMovimiento(@PathVariable("id") Long id) {
        cajaService.deleteMovimiento(id);
    }
    
    @DeleteMapping("/anticipo/{id}")
    public void deleteAnticipo(@PathVariable("id") Long id) {
        cajaService.deleteAnticipo(id);
    }
    
    @DeleteMapping("/reembolso/{id}")
    public void deleteReembolso(@PathVariable("id") Long id) {
        cajaService.deleteReembolso(id);
    }
    
    @GetMapping("/caja/{id}")
    public List<MovimientosDTO> findByIdCaja(@PathVariable("id") Long id) {
        return cajaService.findByIdCaja(id);
    }
    
 
}


