/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.PedidoCompraEntity;

/**
 *
 * @author HP
 */
public class DetallePedidoCompraDTO {
    private Long id;
    private ProductoDTO producto;
    private PedidoCompraEntity pedidoCompra;
    private int cantidad;
    private int cantRecepcionada;
    private int subTotal;
    private int costoCompra;

    public DetallePedidoCompraDTO() {
    }

    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, 
            PedidoCompraEntity pedidoCompra, int cantidad, int subTotal, int costoCompra, Long cantRecepcionada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto);
        this.pedidoCompra = pedidoCompra;
        this.cantidad = cantidad;
        this.cantRecepcionada = (int)(long) cantRecepcionada;
        this.subTotal = subTotal;
        this.costoCompra = costoCompra;
    }

    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, 
            int cantidad, int costoCompra, Long cantRecepcionada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto);
        this.cantidad = cantidad;
        this.cantRecepcionada = (int)(long)cantRecepcionada;
        this.costoCompra = costoCompra;
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

    public PedidoCompraEntity getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompraEntity pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
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
