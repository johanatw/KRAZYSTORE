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
    private Integer cantRecepcionada;
    private Integer cantPendiente;
    private Long idCompra;

    public DetalleCompraDTO(Long id, int cantidad, Long idProducto, String producto, long cantRecepcionada) {
        this.id = id;
        this.cantidad = cantidad;
        this.producto = new ProductoDTO(idProducto,producto);
        this.cantRecepcionada = (int)cantRecepcionada;
    }
    
    
    public DetalleCompraDTO(Long id, int cantidad, Long idProducto, String producto, long cantRecepcionada, Long idCompra) {
        this.id = id;
        this.cantidad = cantidad;
        this.cantPendiente = cantidad - (int)cantRecepcionada;
        this.producto = new ProductoDTO(idProducto,producto);
        this.cantRecepcionada = (int)cantRecepcionada;
        this.idCompra = idCompra;
    }

    public DetalleCompraDTO(Long id, Integer cantidad, Long idProducto, String producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.producto = new ProductoDTO(idProducto,producto);
    }

    public DetalleCompraDTO(Long id,int cantidad, Long idProducto, String producto, IvaEntity ivaAplicado, IvaEntity iva,
            int subTotal, int costoCompra) {
        this.id = id;
        this.ivaAplicado = ivaAplicado;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.producto = new ProductoDTO(idProducto,producto, iva);
        this.costoCompra = costoCompra;
    }
    
    
    public DetalleCompraDTO(Long id, Long idProducto, String producto, IvaEntity ivaAplicado, IvaEntity iva,
            int cantidad, int subTotal, int costoCompra,Long idDetallePedidoCompra, 
            Long cantSolicitada, Long cantRecepcionada, Long cantAceptada, Long cantFacturada) {
        this.id = id;
        this.ivaAplicado = ivaAplicado;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.producto = new ProductoDTO(idProducto,producto, iva);
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

    public Integer getCantRecepcionada() {
        return cantRecepcionada;
    }

    public void setCantRecepcionada(Integer cantRecepcionada) {
        this.cantRecepcionada = cantRecepcionada;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getCantPendiente() {
        return cantPendiente;
    }

    public void setCantPendiente(Integer cantPendiente) {
        this.cantPendiente = cantPendiente;
    }

    
    
}
