/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.*;

/**
 *
 * @author HP
 */
 @Entity
 @Table(name = "detalle_ventas")
public class DetalleVentaEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_det_venta")
    private Long idDetVent;
     @ManyToOne
    @JoinColumn(name = "id_venta")
    private VentaEntity venta;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private ProductoEntity producto;
    @ManyToOne
    @JoinColumn(name = "iva_aplicado")
    private IvaEntity ivaAplicado;
    @Column
    private int cantidad;
    @Column
    private int precio;
    
    @Column
    private int subTotal;

    public DetalleVentaEntity() {
    }

    public DetalleVentaEntity(long id_det_vent, VentaEntity venta, ProductoEntity producto, int cantidad, int precio, int sub_total) {
        this.idDetVent = id_det_vent;
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subTotal = sub_total;
    }
    
    public DetalleVentaEntity(DetalleVentaEntity detalle) {
        this.idDetVent = detalle.getIdDetVent();
        this.venta = detalle.getVenta();
        this.producto = detalle.getProducto();
        this.cantidad = detalle.getCantidad();
        this.precio = detalle.getPrecio();
        this.subTotal = detalle.getSubTotal();
    }

    public DetalleVentaEntity(ProductoEntity producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public DetalleVentaEntity(Long idDetVenta, ProductoEntity producto, Integer cantFacturada) {
        this.idDetVent = idDetVenta;
        this.producto = producto;
        this.cantidad = cantFacturada;
    }

    
    public long getIdDetVent() {
        return idDetVent;
    }

    public void setIdDetVent(long idDetVent) {
        this.idDetVent = idDetVent;
    }

    public VentaEntity getVenta() {
        return venta;
    }

    public void setVenta(VentaEntity venta) {
        this.venta = venta;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }


    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public IvaEntity getIvaAplicado() {
        return ivaAplicado;
    }

    public void setIvaAplicado(IvaEntity ivaAplicado) {
        this.ivaAplicado = ivaAplicado;
    }

    
    
    
}
