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
@Table(name = "detalle_pagos")
public class PagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private Long importe;
    @ManyToOne
    @JoinColumn(name = "id_forma_pago")
    private FormaPagoEntity formaPago;
    @ManyToOne
    @JoinColumn(name = "id_movimiento")
    private MovimientoEntity movimiento;
   
    @ManyToOne
    @JoinColumn(name = "id_anticipo")
    private AnticipoEntity anticipo;
 
    @ManyToOne
    @JoinColumn(name = "id_pago_adelantado_pedido_compra")
    private PagoPedidoCompra pagoAdelantadoPedidoCompra;
 

    public PagoEntity() {
    }

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }

    public AnticipoEntity getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(AnticipoEntity anticipo) {
        this.anticipo = anticipo;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImporte() {
        return importe;
    }

    public void setImporte(Long importe) {
        this.importe = importe;
    }

    public FormaPagoEntity getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPagoEntity formaPago) {
        this.formaPago = formaPago;
    }

    public PagoPedidoCompra getPagoAdelantadoPedidoCompra() {
        return pagoAdelantadoPedidoCompra;
    }

    public void setPagoAdelantadoPedidoCompra(PagoPedidoCompra pagoAdelantadoPedidoCompra) {
        this.pagoAdelantadoPedidoCompra = pagoAdelantadoPedidoCompra;
    }
    
    
    
}
