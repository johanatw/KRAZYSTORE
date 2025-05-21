/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Entity;

import Utils.EstadoPedido;
import com.krazystore.krazystore.DTO.PedidoDTO;
import jakarta.persistence.*;

import java.util.Date;

/**
 *
 * @author HP
 */

@Entity
@Table(name = "pedidos_venta")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;
    @Column
    private Date fecha;
    @Column
    private String observaciones;
    @Column
    private int total;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPedido estadoPedido;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_facturacion")
    private EstadoPedido estadoFacturacion;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteEntity cliente;

    public PedidoEntity() {
    }

    public PedidoEntity(Long id, Date fecha, int montoIva, int total, EstadoPedido estadoPedido, FormaPagoEntity formaPago, ModoEntregaEntity modoEntrega, ClienteEntity cliente) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.estadoPedido = estadoPedido;
        this.cliente = cliente;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoPedido getEstadoFacturacion() {
        return estadoFacturacion;
    }

    public void setEstadoFacturacion(EstadoPedido estadoFacturacion) {
        this.estadoFacturacion = estadoFacturacion;
    }

    
    

    
    
    
}
