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
public class DetalleCompraDTO {
    private Long id;
    private int cantidad;
    private int subTotal;
    private ProductoDTO producto;
    private int costoCompra;
    private IvaEntity ivaAplicado;
    private DetallePedidoCompraDTO detallePedido;

    public DetalleCompraDTO(Long id, Long idProducto, String producto, IvaEntity iva,
            int cantidad, int subTotal, int costoCompra,Long idDetallePedidoCompra, 
            Long cantSolicitada, Long cantRecepcionada, Long cantAceptada, Long cantFacturada) {
        this.id = id;
        this.ivaAplicado = iva;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.producto = new ProductoDTO(idProducto,producto);
        this.costoCompra = costoCompra;
        this.detallePedido = new DetallePedidoCompraDTO(idDetallePedidoCompra,cantSolicitada,cantRecepcionada,cantAceptada,cantFacturada);
    }

    public DetalleCompraDTO() {
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public int getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(int costoCompra) {
        this.costoCompra = costoCompra;
    }

    public DetallePedidoCompraDTO getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoCompraDTO detallePedido) {
        this.detallePedido = detallePedido;
    }

    public IvaEntity getIvaAplicado() {
        return ivaAplicado;
    }

    public void setIvaAplicado(IvaEntity ivaAplicado) {
        this.ivaAplicado = ivaAplicado;
    }

    
    
}
