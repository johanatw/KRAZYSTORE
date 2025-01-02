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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@Entity
@Table(name="detalle_recepcion")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleRecepcion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private int cantRecepcionada;
    @Column
    private int cantAceptada;
    @Column
    private int cantRechazada;
    @ManyToOne
    @JoinColumn(name="id_recepcion")
    private RecepcionEntity recepcion;
    @ManyToOne
    @JoinColumn(name="id_det_ped")
    private DetallePedidoCompra detallePedido;

    public DetalleRecepcion(DetalleRecepcion detalle) {
        this.id = detalle.getId();
        this.cantRecepcionada = detalle.getCantRecepcionada();
        this.cantAceptada = detalle.getCantAceptada();
        this.cantRechazada = detalle.getCantRechazada();
        this.recepcion = detalle.getRecepcion();
        this.detallePedido = detalle.getDetallePedido();
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantRecepcionada() {
        return cantRecepcionada;
    }

    public void setCantRecepcionada(int cantRecepcionada) {
        this.cantRecepcionada = cantRecepcionada;
    }

    public int getCantAceptada() {
        return cantAceptada;
    }

    public void setCantAceptada(int cantAceptada) {
        this.cantAceptada = cantAceptada;
    }

    public int getCantRechazada() {
        return cantRechazada;
    }

    public void setCantRechazada(int cantRechazada) {
        this.cantRechazada = cantRechazada;
    }

    public RecepcionEntity getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(RecepcionEntity recepcion) {
        this.recepcion = recepcion;
    }

    public DetallePedidoCompra getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoCompra detallePedido) {
        this.detallePedido = detallePedido;
    }

    
    
    
            
}
