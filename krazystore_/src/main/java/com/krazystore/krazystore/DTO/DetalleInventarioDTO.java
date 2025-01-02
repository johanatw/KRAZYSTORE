/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;

/**
 *
 * @author HP
 */
public class DetalleInventarioDTO {
    private Long id;
    private ProductoEntity producto;
    private int cantStock;
    private int cantContada;
    private int diferencia;

    public DetalleInventarioDTO(Long id, Long idProducto, String producto, Long idCategoria, String categoria, int cantStock, int cantContada, int diferencia) {
        this.id = id;
        this.producto = new ProductoEntity();
        this.producto.setId(idProducto);
        this.producto.setNombre(producto);
        this.producto.setCategoria(new CategoriaEntity(idCategoria, categoria));
        this.cantStock = cantStock;
        this.cantContada = cantContada;
        this.diferencia = diferencia;
    }

    public DetalleInventarioDTO(Long idProducto, String producto, Long idCategoria, String categoria, int cantStock, int cantContada) {
        this.producto = new ProductoEntity();
        this.producto.setId(idProducto);
        this.producto.setNombre(producto);
        this.producto.setCategoria(new CategoriaEntity(idCategoria, categoria));
        this.cantStock = cantStock;
        this.cantContada = cantContada;
        this.diferencia = this.cantContada - this.cantStock;
    }
    
    public DetalleInventarioDTO(Long id, Long idProducto, String producto, Long idCategoria, String categoria, int cantStock, int cantContada) {
        this.id = id;
        this.producto = new ProductoEntity();
        this.producto.setId(idProducto);
        this.producto.setNombre(producto);
        this.producto.setCategoria(new CategoriaEntity(idCategoria, categoria));
        this.cantStock = cantStock;
        this.cantContada = cantContada;
        this.diferencia = this.cantContada - this.cantStock;
    }
    

    public DetalleInventarioDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public int getCantStock() {
        return cantStock;
    }

    public void setCantStock(int cantStock) {
        this.cantStock = cantStock;
    }

    public int getCantContada() {
        return cantContada;
    }

    public void setCantContada(int cantContada) {
        this.cantContada = cantContada;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }
    
    
    
    
}
