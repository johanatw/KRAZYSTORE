/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class ProductoDTO {
    private Long id;
    private String nombre;
    private Long idCategoria;
    private String categoria;
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

    public ProductoDTO(Long id, String nombre, Long idCategoria, String categoria, int precio, int costo, Boolean estado, Boolean preVenta, int cantPreVenta, int cantStock, int cantDisponible, int cantReservada) {
        this.id =  id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.costo = costo;
        this.estado = estado;
        this.bajoDemanda = preVenta;
        this.cantLimBajoDemanda = cantPreVenta;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
