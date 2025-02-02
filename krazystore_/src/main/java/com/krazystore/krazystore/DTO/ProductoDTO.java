/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.CategoriaEntity;

/**
 *
 * @author HP
 */
public class ProductoDTO {
    private Long id;
    private String nombre;
    private CategoriaEntity categoria;
    private int precio;
    private int costo;
    private Boolean estado;
    private Boolean bajoDemanda;
    private int cantLimBajoDemanda;
    private int cantStock;
    private int cantDisponible;
    private int cantReservada;

    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public ProductoDTO(Long id, String nombre, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public ProductoDTO(Long id, String nombre, int precio, int costo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.costo = costo;
    }

    public ProductoDTO(Long id, String nombre, CategoriaEntity categoria, int precio, int costo, Boolean bajoDemanda, int cantLimBajoDemanda, int cantStock, int cantDisponible, int cantReservada) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.costo = costo;
        this.bajoDemanda = bajoDemanda;
        this.cantLimBajoDemanda = cantLimBajoDemanda;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getBajoDemanda() {
        return bajoDemanda;
    }

    public void setBajoDemanda(Boolean bajoDemanda) {
        this.bajoDemanda = bajoDemanda;
    }

    public int getCantLimBajoDemanda() {
        return cantLimBajoDemanda;
    }

    public void setCantLimBajoDemanda(int cantLimBajoDemanda) {
        this.cantLimBajoDemanda = cantLimBajoDemanda;
    }


    public int getCantStock() {
        return cantStock;
    }

    public void setCantStock(int cantStock) {
        this.cantStock = cantStock;
    }

    public int getCantDisponible() {
        return cantDisponible;
    }

    public void setCantDisponible(int cantDisponible) {
        this.cantDisponible = cantDisponible;
    }

    public int getCantReservada() {
        return cantReservada;
    }

    public void setCantReservada(int cantReservada) {
        this.cantReservada = cantReservada;
    }
    
    
}
