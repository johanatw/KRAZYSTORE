/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

/**
 *
 * @author HP
 */

public abstract class PagoRegistradoEvent {
    private Long facturaId;
    private char estado;
    
    public PagoRegistradoEvent(Long facturaId, char estado) {
        this.facturaId = facturaId;
        this.estado = estado;
    }

    public Long getFacturaId() {
        return facturaId;
    }

    public char getEstado() {
        return estado;
    }
    
    
    
}
