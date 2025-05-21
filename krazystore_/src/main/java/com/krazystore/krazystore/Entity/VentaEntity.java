/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import jakarta.persistence.*;
import java.util.Date;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "ventas")
public class VentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteEntity cliente;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;
    @Column
    private Integer timbrado;
    @Column
    private String nroFactura;
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Column
    private Date fecha;
    @Column(name="total_iva")
    private int montoIva;
    @Column(name="total_gravada")
    private int totalGravada;
    @Column(name="total_exentas")
    private int totalExentas;
    @Column
    private int montoTotal;
    @Column
    private Character estado;

    public VentaEntity() {
    }

    public VentaEntity(Long id_venta, ClienteEntity cliente, Date fecha, int monto_iva, int monto_total) {
        this.id = id_venta;
        this.cliente = cliente;
        this.fecha = fecha;
        this.montoIva = monto_iva;
        this.montoTotal = monto_total;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }  

    
    public Integer getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(Integer timbrado) {
        this.timbrado = timbrado;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }
    
    

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(int montoIva) {
        this.montoIva = montoIva;
    }

    public int getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(int montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
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
    
    
    
}
