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
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@Entity
@Table(name="compras")
@AllArgsConstructor
@NoArgsConstructor

public class CompraEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private int total;
    @Column(name="nro_factura")
    private String nroFactura;
    @Column
    private String timbrado;
    @Column
    private Date fecha;
    @ManyToOne
    @JoinColumn(name="id_proveedor")
    private ProveedorEntity proveedor;
    @ManyToOne
    @JoinColumn(name="id_pedido")
    private PedidoCompraEntity pedido;
    @Column(name="total_iva")
    private int montoIva;
    @Column(name="total_gravada")
    private int totalGravada;
    @Column(name="total_exentas")
    private int totalExentas;
    @Column
    private char estado;
    @ManyToOne
    @JoinColumn(name="id_recepcion")
    private RecepcionEntity recepcion;

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }

    public int getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(int montoIva) {
        this.montoIva = montoIva;
    }

    public int getTotalGravada() {
        return totalGravada;
    }

    public void setTotalGravada(int totalGravada) {
        this.totalGravada = totalGravada;
    }

    public int getTotalExentas() {
        return totalExentas;
    }

    public void setTotalExentas(int totalExentas) {
        this.totalExentas = totalExentas;
    }

    public PedidoCompraEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoCompraEntity pedido) {
        this.pedido = pedido;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public RecepcionEntity getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(RecepcionEntity recepcion) {
        this.recepcion = recepcion;
    }

    
}
