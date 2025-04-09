/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.AjusteStock;
import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;

/**
 *
 * @author HP
 */
public class DetalleAjusteDTO {
    private Long id;
    private AjusteStock ajuste;
    private Long idProducto;
    private String producto;
    private Long idCategoria;
    private String categoria;
    private int cantidadActual;
    private int cantidadAjustada;
    private int stockAnterior;
    private int stockPosterior;
    private String motivo;

    public DetalleAjusteDTO() {
    }

    
    public DetalleAjusteDTO(Long id, AjusteStock ajuste, Long idProducto, String producto, Long idCategoria, String categoria, int cantidadActual, int cantidadAjustada) {
        this.id = id;
        this.ajuste = ajuste;
        this.idProducto = idProducto;
        this.producto = producto;
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.cantidadActual = cantidadActual;
        this.cantidadAjustada = cantidadAjustada;
    }

    public DetalleAjusteDTO(Long id, AjusteStock ajuste, Long idProducto, String producto, Long idCategoria, String categoria, int cantidadAjustada) {
        this.id = id;
        this.ajuste = ajuste;
        this.idProducto = idProducto;
        this.producto = producto;
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.cantidadAjustada = cantidadAjustada;
    }

    public DetalleAjusteDTO(Long idProducto, String producto, Long idCategoria, String categoria, int cantidadActual, int cantidadAjustada) {
        this.idProducto = idProducto;
        this.producto = producto;
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.cantidadActual = cantidadActual;
        this.cantidadAjustada = cantidadAjustada;
    }

    public DetalleAjusteDTO(Long id, Long idProducto, String producto, Long idCategoria, String categoria, int cantidadActual, int cantidadAjustada) {
        this.id = id;
        this.idProducto = idProducto;
        this.producto = producto;
        this.idCategoria = idCategoria;
        this.categoria = categoria;
        this.cantidadActual = cantidadActual;
        this.cantidadAjustada = cantidadAjustada;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AjusteStock getAjuste() {
        return ajuste;
    }

    public void setAjuste(AjusteStock ajuste) {
        this.ajuste = ajuste;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public int getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(int stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public int getStockPosterior() {
        return stockPosterior;
    }

    public void setStockPosterior(int stockPosterior) {
        this.stockPosterior = stockPosterior;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    
}
