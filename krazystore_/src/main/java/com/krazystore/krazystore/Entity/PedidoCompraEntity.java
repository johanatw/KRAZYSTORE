/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import Utils.EstadoPedido;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "pedidos_compra")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoCompraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;
    @Column
    private Date fecha;
    @Column
    private int total;
    @Column
    private String observaciones;
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private ProveedorEntity proveedor;
    @Column(name = "estado")
    private Character estadoPedido;
    @Column(name = "estado_facturacion")
    private Character estadoFacturacion;
    @Column(name = "tipo_pedido")
    private Character tipoPedido;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Character getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(Character estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Character getEstadoFacturacion() {
        return estadoFacturacion;
    }

    public void setEstadoFacturacion(Character estadoFacturacion) {
        this.estadoFacturacion = estadoFacturacion;
    }

    
    public Character getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(Character tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    


}
