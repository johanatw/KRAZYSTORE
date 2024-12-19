/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.ProductoEntity;

/**
 *
 * @author HP
 */

public class DetalleRecepcionDTO {
    private Long id;
    private ProductoEntity producto;
    private int costo;
    private int cantSolicitado;
    private int cantRecepcionado;

    public DetalleRecepcionDTO(Long id, int costo, int cantSolicitado, int cantRecepcionado) {
        this.id = id;
        this.costo = costo;
        this.cantSolicitado = cantSolicitado;
        this.cantRecepcionado = cantRecepcionado;
    }

    public DetalleRecepcionDTO(Long id, Long idProducto, String producto, int costo, int cantSolicitado, int cantRecepcionado) {
        this.id = id;
        this.producto = new ProductoEntity();
        this.producto.setId(idProducto);
        this.producto.setNombre(producto);
        this.costo = costo;
        this.cantSolicitado = cantSolicitado;
        this.cantRecepcionado = cantRecepcionado;
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

    

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public int getCantSolicitado() {
        return cantSolicitado;
    }

    public void setCantSolicitado(int cantSolicitado) {
        this.cantSolicitado = cantSolicitado;
    }

    public int getCantRecepcionado() {
        return cantRecepcionado;
    }

    public void setCantRecepcionado(int cantRecepcionado) {
        this.cantRecepcionado = cantRecepcionado;
    }

    
}
