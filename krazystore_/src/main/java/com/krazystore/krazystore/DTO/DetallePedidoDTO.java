/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.IvaEntity;

/**
 *
 * @author HP
 */
public class DetallePedidoDTO {
    private Long id;
    private ProductoDTO producto;
    private int precio;
    private int cantSolicitado;
    private int subTotal;
    private int cantFacturada;
    private int cantEntregada;
  

    public DetallePedidoDTO() {
    }

    public DetallePedidoDTO(Long id, Long idProducto, String nombre, Integer cantStock, IvaEntity tipoIva, Integer precio, 
            Integer cantSolicitada, Integer subtotal, Long cantFacturada) {
        System.out.println("DetallePedidoDTO");
        System.out.println(subtotal);
        this.id = id;
        this.producto = new ProductoDTO(idProducto, nombre, cantStock, tipoIva);
        this.precio = (int)precio;
        this.cantSolicitado = (int)cantSolicitada;
        this.subTotal = (int)subtotal;
        this.cantFacturada = (int)(long)cantFacturada;
    }
    
    public DetallePedidoDTO(Long id, Long idProducto, String nombre, Integer cantStock, IvaEntity tipoIva, Integer precio, 
            Integer cantSolicitada, Integer subtotal, Long cantFacturada, Long cantEntregada) {
        System.out.println("DetallePedidoDTO");
        System.out.println(subtotal);
        this.id = id;
        this.producto = new ProductoDTO(idProducto, nombre, cantStock, tipoIva);
        this.precio = (int)precio;
        this.cantSolicitado = (int)cantSolicitada;
        this.subTotal = (int)subtotal;
        this.cantFacturada = (int)(long)cantFacturada;
        this.cantEntregada = (int)(long)cantEntregada;
    }
    
    public DetallePedidoDTO(Long id, Long idProducto, String nombre, 
            Integer cantSolicitada, Long cantFacturada, Long cantEntregada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, nombre);
        this.cantSolicitado = (int)cantSolicitada;
        this.cantFacturada = (int)(long)cantFacturada;
        this.cantEntregada = (int)(long)cantEntregada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantSolicitado() {
        return cantSolicitado;
    }

    public void setCantSolicitado(int cantSolicitado) {
        this.cantSolicitado = cantSolicitado;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public int getCantFacturada() {
        return cantFacturada;
    }

    public void setCantFacturada(int cantFacturada) {
        this.cantFacturada = cantFacturada;
    }

    public int getCantEntregada() {
        return cantEntregada;
    }

    public void setCantEntregada(int cantEntregada) {
        this.cantEntregada = cantEntregada;
    }

    
}
