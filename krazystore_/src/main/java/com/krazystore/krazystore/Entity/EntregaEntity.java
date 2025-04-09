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

/**
 *
 * @author HP
 */
@Entity
@Table(name="entregas")
public class EntregaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private Date fecha;
    @Column
    private Character estado;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;
    @ManyToOne
    @JoinColumn(name = "id_modo_entrega")
    private ModoEntregaEntity modoEntrega;
    @ManyToOne
    @JoinColumn(name = "id_empresa_transporte")
    private EmpresaTransporte empresaTransporte;
    @ManyToOne
    @JoinColumn(name = "id_punto_entrega")
    private PuntoEntregaEntity puntoEntrega;
    @ManyToOne
    @JoinColumn(name = "id_direccion_envio")
    private DireccionEntity direccionEnvio;

    public EntregaEntity() {
    }

    public EntregaEntity(Long id, Date fecha, Character estado, ModoEntregaEntity modoEntrega, EmpresaTransporte empresaTransporte, PuntoEntregaEntity puntoEntrega, DireccionEntity direccionEnvio) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.modoEntrega = modoEntrega;
        this.empresaTransporte = empresaTransporte;
        this.puntoEntrega = puntoEntrega;
        this.direccionEnvio = direccionEnvio;
    }

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

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public ModoEntregaEntity getModoEntrega() {
        return modoEntrega;
    }

    public void setModoEntrega(ModoEntregaEntity modoEntrega) {
        this.modoEntrega = modoEntrega;
    }

    public EmpresaTransporte getEmpresaTransporte() {
        return empresaTransporte;
    }

    public void setEmpresaTransporte(EmpresaTransporte empresaTransporte) {
        this.empresaTransporte = empresaTransporte;
    }

    public PuntoEntregaEntity getPuntoEntrega() {
        return puntoEntrega;
    }

    public void setPuntoEntrega(PuntoEntregaEntity puntoEntrega) {
        this.puntoEntrega = puntoEntrega;
    }

    public DireccionEntity getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(DireccionEntity direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public PedidoEntity getPedido() {
        return pedido;
    }

    public void setPedido(PedidoEntity pedido) {
        this.pedido = pedido;
    }
    
    

    
}