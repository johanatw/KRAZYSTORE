/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@Entity
@Table(name="detalle_compra")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private int cantidad;
    @Column(name="sub_total")
    private int subTotal;
    @ManyToOne
    @JoinColumn(name="id_producto")
    private ProductoEntity producto;
    @ManyToOne
    @JoinColumn(name="id_compra")
    private CompraEntity compra;
    @Column(name="costo_compra")
    private int costoCompra;
    @ManyToOne
    @JoinColumn(name = "iva_aplicado")
    private IvaEntity ivaAplicado;
    @ManyToOne
    @JoinColumn(name="id_det_ped")
    private DetallePedidoCompra detallePedido;

    public DetalleCompra(DetalleCompra detalle) {
        this.id = detalle.getId();
        this.cantidad = detalle.getCantidad();
        this.subTotal = detalle.getSubTotal();
        this.producto = detalle.getProducto();
        this.compra = detalle.getCompra();
        this.costoCompra = detalle.getCostoCompra();
    }

    public DetalleCompra(Long id, int cantidad, int subTotal, ProductoEntity producto, int costoCompra) {
        this.id = id;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.producto = producto;
        this.costoCompra = costoCompra;
    }

    public DetalleCompra(Long id, int cantidad, int subTotal, ProductoEntity producto, int costoCompra, IvaEntity ivaAplicado) {
        this.id = id;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.producto = producto;
        this.costoCompra = costoCompra;
        this.ivaAplicado = ivaAplicado;
    }

    public DetalleCompra(Long id, int cantidad, int subTotal, ProductoEntity producto, int costoCompra, IvaEntity ivaAplicado, DetallePedidoCompra detallePedido) {
        this.id = id;
        this.cantidad = cantidad;
        this.subTotal = subTotal;
        this.producto = producto;
        this.costoCompra = costoCompra;
        this.ivaAplicado = ivaAplicado;
        this.detallePedido = detallePedido;
    }

    
    public DetalleCompra(Long id, ProductoEntity producto) {
        this.id = id;
        this.producto = producto;
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

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public CompraEntity getCompra() {
        return compra;
    }

    public void setCompra(CompraEntity compra) {
        this.compra = compra;
    }

    public int getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(int costoCompra) {
        this.costoCompra = costoCompra;
    }

    public IvaEntity getIvaAplicado() {
        return ivaAplicado;
    }

    public void setIvaAplicado(IvaEntity ivaAplicado) {
        this.ivaAplicado = ivaAplicado;
    }

    public DetallePedidoCompra getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoCompra detallePedido) {
        this.detallePedido = detallePedido;
    }

    
    
    
}
