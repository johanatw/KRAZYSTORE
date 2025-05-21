/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.AjusteStock;
import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;
import com.krazystore.krazystore.Entity.SubCategoriaEntity;

/**
 *
 * @author HP
 */
public class DetalleAjusteDTO {
    private Long id;
    private ProductoDTO producto;
    private int cantidadActual;
    private int cantidadAjustada;
    private int cantidadAnterior;
    private int cantidadFinal;
    private String motivo;

    public DetalleAjusteDTO() {
    }

    public DetalleAjusteDTO(Long id, Long idProducto, String producto, SubCategoriaEntity subCategoria, int cantidadActual, int cantidadAjustada, int cantidadAnterior, int cantidadFinal, String motivo) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto, subCategoria);
        this.cantidadActual = cantidadActual;
        this.cantidadAjustada = cantidadAjustada;
        this.cantidadAnterior = cantidadAnterior;
        this.cantidadFinal = cantidadFinal;
        this.motivo = motivo;
    }

    
    public DetalleAjusteDTO(Long id, Long idProducto, String producto, SubCategoriaEntity subCategoria, int cantidadActual, int cantidadAjustada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto, subCategoria);
        this.cantidadActual = cantidadActual;
        this.cantidadAjustada = cantidadAjustada;
    }

    public DetalleAjusteDTO(Long id, Long idProducto, String producto, SubCategoriaEntity subCategoria, int cantidadAjustada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto, subCategoria);
        this.cantidadAjustada = cantidadAjustada;
    }

    public DetalleAjusteDTO(Long idProducto, String producto, SubCategoriaEntity subCategoria, int cantidadActual, int cantidadAjustada) {
        this.producto = new ProductoDTO(idProducto, producto, subCategoria);
        this.cantidadActual = cantidadActual;
        this.cantidadAjustada = cantidadAjustada;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public int getCantidadAjustada() {
        return cantidadAjustada;
    }

    public void setCantidadAjustada(int cantidadAjustada) {
        this.cantidadAjustada = cantidadAjustada;
    }

    public int getCantidadAnterior() {
        return cantidadAnterior;
    }

    public void setCantidadAnterior(int cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }

    public int getCantidadFinal() {
        return cantidadFinal;
    }

    public void setCantidadFinal(int cantidadFinal) {
        this.cantidadFinal = cantidadFinal;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }


    
}
