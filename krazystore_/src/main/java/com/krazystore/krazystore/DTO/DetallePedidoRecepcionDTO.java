/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.PedidoCompraEntity;
import com.krazystore.krazystore.Entity.ProductoEntity;

/**
 *
 * @author HP
 */
public class DetallePedidoRecepcionDTO {
    private Long idDetPedido;
    private Long id;
    private ProductoEntity producto;
    private PedidoCompraEntity pedidoCompra;
    private int cantidad;
    private int cantRecepcionada;
    private int cantRecibida;
    private int cantPendiente;
    private int cantAceptada;
    private int cantRechazada;
    private int cantDefectuosa;
    private int subTotal;
    private int costoCompra;

    public DetallePedidoRecepcionDTO(Long id, ProductoEntity producto, PedidoCompraEntity pedidoCompra, 
            int cantidad, long cantRecepcionada, long cantAceptada, long cantRechazada, int subTotal, int costoCompra) {
        this.idDetPedido = id;
        this.producto = producto;
        this.pedidoCompra = pedidoCompra;
        this.cantidad = (int)(long)cantidad;
        this.cantRecepcionada = 0;
        this.cantPendiente = (int)(long)(this.cantidad - cantRecepcionada);
        this.cantRecibida = (int)(long)cantRecepcionada;
        this.cantAceptada = 0;
        this.cantRechazada = 0;
        this.subTotal = subTotal;
        this.costoCompra = costoCompra;
        this.cantDefectuosa = (int)(long)cantRechazada;
    }

    public DetallePedidoRecepcionDTO(Long id, ProductoEntity producto, int cantidad, int cantRecepcionada, 
             int cantAceptada, int cantRechazada) {
        this.id = id;
        this.cantidad = cantidad;
        this.cantRecepcionada = cantRecepcionada;
        this.cantAceptada = cantAceptada;
        this.cantRechazada = cantRechazada;
        
    }

  

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public int getCantDefectuosa() {
        return cantDefectuosa;
    }

    public void setCantDefectuosa(int cantDefectuosa) {
        this.cantDefectuosa = cantDefectuosa;
    }
    
    

    public int getCantRecibida() {
        return cantRecibida;
    }

    public void setCantRecibida(int cantRecibida) {
        this.cantRecibida = cantRecibida;
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
    
    
    

    public Long getIdDetPedido() {
        return idDetPedido;
    }

    public void setIdDetPedido(Long idDetPedido) {
        this.idDetPedido = idDetPedido;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
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

    public int getCantPendiente() {
        return cantPendiente;
    }

    public void setCantPendiente(int cantPendiente) {
        this.cantPendiente = cantPendiente;
    }

    public int getCantAceptada() {
        return cantAceptada;
    }

    public void setCantAceptada(int cantAceptada) {
        this.cantAceptada = cantAceptada;
    }

    public int getCantRechazada() {
        return cantRechazada;
    }

    public void setCantRechazada(int cantRechazada) {
        this.cantRechazada = cantRechazada;
    }
    
    

}


