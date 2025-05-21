/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krazystore.krazystore.Controller;

import com.krazystore.krazystore.DTO.ProductoDTO;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Service.ProductoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
   

    @GetMapping
    public List<ProductoDTO> findAll() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ProductoEntity> findById(@PathVariable("id") Long id) {
        return productoService.findById(id);
    }
    
    @GetMapping("/servicio_entrega")
    public Optional<ProductoDTO> getServicioTransporte() {
        return productoService.getServicioTransporte();
    }
    
    @GetMapping("/costo_envio")
    public Optional<ProductoDTO> getCostoEnvio() {
        return productoService.getCostoEnvio();
    }
    
    @GetMapping("/servicios")
    public List<ProductoDTO> getServicios() {
        return productoService.getServicios();
    }
    
    @GetMapping("/nombre")
    public List<ProductoDTO> findByNombre(@RequestParam(value="nombre") String nombre ){
        return productoService.buscarPorNombre(nombre);
    }

    @PostMapping
    public ProductoEntity saveProducto(@RequestBody ProductoDTO productoDTO) {
        return productoService.saveProducto(productoDTO);
    }

    @PutMapping("/{id}")
    public ProductoEntity updateProducto(@PathVariable long id, @RequestBody ProductoEntity producto) {
        return productoService.updateProducto(producto, id);
    }
    
    @PutMapping("/preventa")
    public ProductoEntity updatePreVenta (@RequestParam(value="id") Long id, @RequestParam(value="cantidad") Integer cantPreVenta){
        return productoService.updatePreVenta(id, cantPreVenta);
    }
    
    @PutMapping("/existencias")
    public void updateExistencias (@RequestBody List<ProductoEntity> productos){
        productoService.updateExistencias(productos);
    }

    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable("id") Long id) {
        productoService.deleteProducto(id);
    }
}
