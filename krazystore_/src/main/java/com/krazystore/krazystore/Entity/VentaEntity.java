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
    private PersonaEntity cliente;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;
    @ManyToOne
    @JoinColumn(name = "id_timbrado")
    private TimbradoEntity timbrado;
    @Column
    private String nroFactura;
    @Column
    private Date fecha;
    @Column
    private int montoIva;
    @Column
    private int montoTotal;
    @Column
    private Character estado;

    public VentaEntity() {
    }

    public VentaEntity(Long id_venta, PersonaEntity cliente, Date fecha, int monto_iva, int monto_total) {
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

    
    public TimbradoEntity getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(TimbradoEntity timbrado) {
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

    public PersonaEntity getCliente() {
        return cliente;
    }

    public void setCliente(PersonaEntity cliente) {
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
    
    
    
}
