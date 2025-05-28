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

/**
 *
 * @author HP
 */
@Entity
@Table(name="detalle_entregas")
public class DetalleEntrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_entrega")
    private EntregaEntity entrega;
    @ManyToOne
    @JoinColumn(name = "id_det_pedido")
    private DetallePedidoEntity detallePedido;
    @ManyToOne
    @JoinColumn(name="id_det_venta")
    private DetalleVentaEntity detalleVenta;
    @Column
    private Integer cantidad;

    public DetalleEntrega() {
    }

    public DetalleEntrega(Long id, DetallePedidoEntity detallePedido, Integer cantidad) {
        this.id = id;
        this.detallePedido = detallePedido;
        this.cantidad = cantidad;
    }

    public DetalleEntrega(Long id, DetalleVentaEntity detalleVenta, Integer cantidad) {
        this.id = id;
        this.detalleVenta = detalleVenta;
        this.cantidad = cantidad;
    }
    

    public DetalleEntrega(Long id, EntregaEntity entrega, DetallePedidoEntity detallePedido, Integer cantidad) {
        this.id = id;
        this.entrega = entrega;
        this.detallePedido = detallePedido;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntregaEntity getEntrega() {
        return entrega;
    }

    public void setEntrega(EntregaEntity entrega) {
        this.entrega = entrega;
    }

    public DetallePedidoEntity getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoEntity detallePedido) {
        this.detallePedido = detallePedido;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public DetalleVentaEntity getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVentaEntity detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
    
    
    
}
