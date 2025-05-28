/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.IvaEntity;
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
    private int cantSolicitada;
    private int cantRecepcionada;
    private int cantAceptada;
    private int cantFacturada;
    private int subTotal;
    private int costoCompra;
    private Boolean esServicio;

    public DetallePedidoCompraDTO() {
    }

    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, IvaEntity tipoIva, int cantidad, int subTotal, int costoCompra, long cantFacturada ) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto, tipoIva);
        this.cantidad = cantidad;
        this.cantSolicitada = cantidad;
        this.cantFacturada = (int)cantFacturada;
        this.subTotal = subTotal;
        this.costoCompra = costoCompra;
    }


    public DetallePedidoCompraDTO(Long id, Long cantSolicitada, Long cantRecepcionada,Long cantAceptada, Long cantFacturada) {
        this.id = id;
        this.cantSolicitada = (int)(long)cantSolicitada;
        this.cantRecepcionada = (int)(long)cantRecepcionada;
        this.cantFacturada = (int)(long)cantFacturada;
        this.cantAceptada = (int)(long)cantAceptada;
    }

   
    
    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, 
            PedidoCompraEntity pedidoCompra, int cantidad, int subTotal, int costoCompra, Long cantRecepcionada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto);
        this.pedidoCompra = pedidoCompra;
        this.cantSolicitada = cantidad;
        this.cantRecepcionada = (int)(long) cantRecepcionada;
        this.subTotal = subTotal;
        this.costoCompra = costoCompra;
        
    }
    
    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, IvaEntity tipoIva,
            PedidoCompraEntity pedidoCompra, int cantidad, int subTotal, int costoCompra, Long cantRecepcionada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto, tipoIva);
        this.pedidoCompra = pedidoCompra;
        this.cantSolicitada = cantidad;
        this.cantRecepcionada = (int)(long) cantRecepcionada;
        this.subTotal = subTotal;
        this.costoCompra = costoCompra;
    }
    
    
    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto);
    }
    
    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, int costoCompra) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto);
        this.costoCompra = costoCompra;
    }
    
    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, IvaEntity tipoIva,
            PedidoCompraEntity pedidoCompra, int cantidad, int subTotal, int costoCompra, Boolean esServicio,
            Long cantRecepcionada, Long cantFacturada, Long cantAceptada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto, tipoIva);
        this.pedidoCompra = pedidoCompra;
        this.cantSolicitada = cantidad;
        this.cantRecepcionada = (int)(long) cantRecepcionada;
        this.cantAceptada = (int)(long) cantAceptada;
        this.cantFacturada = (int)(long) cantFacturada;
        this.subTotal = subTotal;
        this.costoCompra = costoCompra;
        this.cantidad = cantidad;
        this.esServicio = esServicio;
    }
    
    

    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto, 
            int cantidad, int costoCompra, Long cantRecepcionada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto);
        this.cantSolicitada = cantidad;
        this.cantRecepcionada = (int)(long)cantRecepcionada;
        this.costoCompra = costoCompra;
    }
    
    public DetallePedidoCompraDTO(Long id, Long idProducto, String producto,
            int cantidadSolicitado, int costoCompra, 
            Long cantAceptada, Long cantFacturada, Long cantRecepcionada) {
        this.id = id;
        this.producto = new ProductoDTO(idProducto, producto);
        this.cantSolicitada = cantidadSolicitado;
        this.cantAceptada = (int)(long) cantAceptada;
        this.cantFacturada = (int)(long) cantFacturada;
        this.costoCompra = costoCompra;
        this.cantRecepcionada = (int)(long)cantRecepcionada;
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


    public int getCantSolicitada() {
        return cantSolicitada;
    }

    public void setCantSolicitada(int cantSolicitada) {
        this.cantSolicitada = cantSolicitada;
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

    public int getCantFacturada() {
        return cantFacturada;
    }

    public void setCantFacturada(int cantFacturada) {
        this.cantFacturada = cantFacturada;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantAceptada() {
        return cantAceptada;
    }

    public void setCantAceptada(int cantAceptada) {
        this.cantAceptada = cantAceptada;
    }

    public Boolean getEsServicio() {
        return esServicio;
    }

    public void setEsServicio(Boolean esServicio) {
        this.esServicio = esServicio;
    }
    
    
}
