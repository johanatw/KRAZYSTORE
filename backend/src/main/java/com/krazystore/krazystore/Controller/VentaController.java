/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.VentaCreationDTO;
import com.krazystore.krazystore.DTO.VentaRecepcionarDTO;
import com.krazystore.krazystore.Entity.VentaEntity;
import com.krazystore.krazystore.Service.VentaService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/ventas")

public class VentaController {
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public List<VentaEntity> findAll() {
        return ventaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<VentaEntity> findById(@PathVariable("id") Long id) {
        return ventaService.findById(id);
    }
    
    @GetMapping("/preparar/{id}")
    public VentaRecepcionarDTO findFacturaPrepararById(@PathVariable("id") Long id) {
        return ventaService.findFacturaPrepararById(id);
    }
 
    @GetMapping("/pedido/{id}")
    public List<VentaCreationDTO> findFacturasByIdPedido(@PathVariable("id") Long id) {
        return ventaService.findByIdPedido(id);
    }

    @PostMapping
    public VentaEntity saveVenta(@RequestBody VentaCreationDTO ventaCreationDTO) {
        return ventaService.saveVenta(ventaCreationDTO.getVenta(), ventaCreationDTO.getDetalle());
    }
    
    @PutMapping("/anular/{id}")
    public int anularVenta(@PathVariable long id, @RequestBody VentaEntity venta) {
        return ventaService.anularFactura(id);
    }
    
    @GetMapping("/pdf")
    public void getAnticipoPdf(@RequestParam(value="id") Long id, HttpServletResponse response) {
        response.setContentType("application/pdf");
        
        ventaService.getFacturaPdf(response, id);
    }
}
