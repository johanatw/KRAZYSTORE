/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.AnticipoCreationDTO;
import com.krazystore.krazystore.DTO.EstadoPagoPedidoDTO;
import com.krazystore.krazystore.DTO.MovimientoCreationDTO;
import com.krazystore.krazystore.DTO.MovimientosDTO;
import com.krazystore.krazystore.DTO.ReembolsoCreationDTO;
import com.krazystore.krazystore.DTO.VentaCreationDTO;
import com.krazystore.krazystore.Entity.CajaEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
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
    public CajaEntity abrirCaja() {
        return cajaService.abrirCaja();
    }
    
    @PutMapping("/{id}")
    public void updateCategoria(@PathVariable long id) {
        cajaService.cerrarCaja(id);
    }
    
    @GetMapping("/estado_pagos_pedido_compra/{id}")
    public EstadoPagoPedidoDTO getEstadoPagoPedidoCompra(@PathVariable("id") Long id) {
        return cajaService.getEstadoPagoPedidoCompra(id);
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
    public MovimientoEntity saveReembolso(@RequestBody ReembolsoCreationDTO reembolsoCreationDTO){
        return cajaService.saveMovimiento(reembolsoCreationDTO);
    }
    
    @PostMapping("/movimiento")
    public MovimientoEntity saveMovimiento(@RequestBody MovimientoCreationDTO movimiento) {
        return cajaService.saveMovimiento(movimiento);
    }
    
    @GetMapping("/pendientes")
    public List<MovimientoEntity> getFacturasPendientes() {
        return cajaService.getFacturasPendientes();
    }
    
    @PostMapping("/pagar_factura")
    public MovimientoEntity savePagosFactura(@RequestBody MovimientoCreationDTO movimiento) {
        return cajaService.savePagosFactura(movimiento);
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


