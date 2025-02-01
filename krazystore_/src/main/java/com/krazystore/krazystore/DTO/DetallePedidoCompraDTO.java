/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

/**
 *
 * @author HP
 */
public class DetallePedidoCompraDTO {
    private Long id;
    private Long idProducto;
    private String producto;
    private Long idPedidoCompra;
    private int cantidad;
    private int cantRecepcionada;
    private int subTotal;
    private int costoCompra;

    public DetallePedidoCompraDTO() {
    }

    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, 
            Long idPedidoCompra, int cantidad, int subTotal, int costoCompra, Long cantRecepcionada) {
        this.id = id;
        this.idProducto = idProducto;
        this.producto = producto;
        this.idPedidoCompra = idPedidoCompra;
        this.cantidad = cantidad;
        this.cantRecepcionada = (int)(long) cantRecepcionada;
        this.subTotal = subTotal;
        this.costoCompra = costoCompra;
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

    public Long getIdPedidoCompra() {
        return idPedidoCompra;
    }

    public void setIdPedidoCompra(Long idPedidoCompra) {
        this.idPedidoCompra = idPedidoCompra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantRecepcionada() {
        return cantRecepcionada;
    }

    public void setCantRecepcionada(int cantRecepcionada) {
        this.cantRecepcionada = cantRecepcionada;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public int getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(int costoCompra) {
        this.costoCompra = costoCompra;
    }
    
    
}
