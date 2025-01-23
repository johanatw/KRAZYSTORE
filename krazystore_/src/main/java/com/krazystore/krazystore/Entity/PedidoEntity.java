/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import com.krazystore.krazystore.DTO.PedidoDTO;
import jakarta.persistence.*;

import java.util.Date;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "pedidos")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;
    @Column
    private Date fecha;
    @Column
    private int montoIva;
    @Column
    private int total;
    @Column
    private int pagado=0;
    @ManyToOne
    @JoinColumn(name = "costo_envio")
    private CostoEnvioEntity costoEnvio;

    @Column(name = "estado")
    private Character estadoPedido;
    @ManyToOne
    @JoinColumn(name = "forma_pago")
    private FormaPagoEntity formaPago;
    @ManyToOne
    @JoinColumn(name = "modo_entrega")
    private ModoEntregaEntity modoEntrega;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private PersonaEntity cliente;
    @ManyToOne
    @JoinColumn(name = "id_direccion_envio")
    private DireccionEntity direccionEnvio;

    public PedidoEntity() {
    }

    public PedidoEntity(Long id, Date fecha, int montoIva, int total, CostoEnvioEntity costoEnvio, Character estadoPedido, FormaPagoEntity formaPago, ModoEntregaEntity modoEntrega, PersonaEntity cliente) {
        this.id = id;
        this.fecha = fecha;
        this.montoIva = montoIva;
        this.total = total;
        this.costoEnvio = costoEnvio;
        this.estadoPedido = estadoPedido;
        this.formaPago = formaPago;
        this.modoEntrega = modoEntrega;
        this.cliente = cliente;

    }

    public DireccionEntity getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(DireccionEntity direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    
    public int getPagado() {
        return pagado;
    }

    public void setPagado(int pagado) {
        this.pagado = pagado;
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

    public int getMontoIva() {
        return montoIva;
    }

    public void setMontoIva(int montoIva) {
        this.montoIva = montoIva;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public CostoEnvioEntity getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(CostoEnvioEntity costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public Character getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(Character estadoPedido) {
        this.estadoPedido = estadoPedido;
    } 

    public FormaPagoEntity getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPagoEntity formaPago) {
        this.formaPago = formaPago;
    }

    public ModoEntregaEntity getModoEntrega() {
        return modoEntrega;
    }

    public void setModoEntrega(ModoEntregaEntity modoEntrega) {
        this.modoEntrega = modoEntrega;
    }

    public PersonaEntity getCliente() {
        return cliente;
    }

    public void setCliente(PersonaEntity cliente) {
        this.cliente = cliente;
    }


    
    
}
