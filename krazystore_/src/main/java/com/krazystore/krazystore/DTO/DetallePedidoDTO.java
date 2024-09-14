/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class DetallePedidoDTO {
    private long id;
    private String nombre;
    private int precio;
    private int cantidad;
    private int subtotal;
    private boolean preVenta;
    private int cantLimBajoDemanda;
    private int cantStock;
    private int cantDisponible;
    private int cantReservada;

    public DetallePedidoDTO() {
    }

    public DetallePedidoDTO(long id, String producto, int precio, int cantidad, int subtotal, boolean preVenta, int cantLimBajoDemanda, int cantStock, int cantDisponible, int cantReservada) {
        this.id = id;
        this.nombre = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.preVenta = preVenta;
        this.cantLimBajoDemanda = cantLimBajoDemanda;
        this.cantStock = cantStock;
        this.cantDisponible = cantDisponible;
        this.cantReservada = cantReservada;
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

    

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isPreVenta() {
        return preVenta;
    }

    public void setPreVenta(boolean preVenta) {
        this.preVenta = preVenta;
    }
    
    
}
