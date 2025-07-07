/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.DetalleInventarioDTO;
import com.krazystore.krazystore.DTO.InventarioCreationDTO;
import com.krazystore.krazystore.DTO.InventarioDTO;
import com.krazystore.krazystore.Entity.InventarioEntity;
import com.krazystore.krazystore.Service.InventarioService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/inventarios")
public class InventarioController {
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }
    
    @GetMapping
    public List<InventarioDTO> findAll() {
        return inventarioService.findAll();
    }
    
    @GetMapping("/detalles")
    public List<DetalleInventarioDTO> getDetallesInventarioIniciales(@RequestParam(value="ids") List<Long> ids){
        return inventarioService.getDetallesInventarioIniciales(ids);
    }

    @GetMapping("/{id}")
    public InventarioCreationDTO findById(@PathVariable("id") Long id) {
        return inventarioService.findById(id);
    }
    
    @PostMapping
    public InventarioEntity saveCompra(@RequestBody InventarioCreationDTO inventario)throws Exception {
        return inventarioService.saveInventario(inventario);
    }

    @PutMapping("/{id}")
    public InventarioEntity updateCompra(@PathVariable long id, @RequestBody InventarioCreationDTO inventario)throws Exception {
        return inventarioService.updateInventario(inventario, id);
    }
    
    @PutMapping("/finalizar/{id}")
    public InventarioEntity finalizarInventario(@PathVariable long id, @RequestBody InventarioCreationDTO inventario)throws Exception {
        return inventarioService.finalizarInventario(inventario, id);
    }
    
    @PutMapping("/ajustar/{id}")
    public InventarioEntity ajustarInventario(@PathVariable long id, @RequestBody InventarioCreationDTO inventario)throws Exception {
        return inventarioService.ajustarInventario(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCompra(@PathVariable("id") Long id) {
        inventarioService.deleteInventario(id);
    }
}
