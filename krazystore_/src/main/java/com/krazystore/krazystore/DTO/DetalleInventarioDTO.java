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
    private Long idProducto;
    private String producto;
    private CategoriaEntity categoria;
    private int stockActual;
    private int stockInicialInventario;
    private int cantContada;
    private int diferencia;

    public DetalleInventarioDTO() {
    }

    public DetalleInventarioDTO(Long id, Long idProducto, String producto, CategoriaEntity categoria, 
            int stockActual, int stockInicialInventario, int cantContada, int diferencia) {
        this.id = id;
        this.idProducto = idProducto;
        this.producto = producto;
        this.categoria = categoria;
        this.stockActual = stockActual;
        this.stockInicialInventario = stockInicialInventario;
        this.cantContada = cantContada;
        this.diferencia = diferencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockInicialInventario() {
        return stockInicialInventario;
    }

    public void setStockInicialInventario(int stockInicialInventario) {
        this.stockInicialInventario = stockInicialInventario;
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
