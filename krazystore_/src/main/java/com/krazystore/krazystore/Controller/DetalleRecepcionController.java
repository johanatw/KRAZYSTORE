/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;


import com.krazystore.krazystore.DTO.DetallePedidoRecepcionDTO;
import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Service.DetalleRecepcionService;
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
@RequestMapping("/api/detalle_recepcion")
public class DetalleRecepcionController {
    private final DetalleRecepcionService detalleService;

    public DetalleRecepcionController(DetalleRecepcionService detalleService) {
        this.detalleService = detalleService;
    }

    @GetMapping("/{id}")
    public List<DetalleRecepcion> findByIdRecepcion(@PathVariable("id") Long id) {
        return detalleService.findByIdRecepcion(id);
    }
}
