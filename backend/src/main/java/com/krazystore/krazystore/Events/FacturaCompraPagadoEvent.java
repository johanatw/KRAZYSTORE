/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

/**
 *
 * @author HP
 */
public class FacturaCompraPagadoEvent extends PagoRegistradoEvent {

    public FacturaCompraPagadoEvent(Long facturaId, char estado) {
        super(facturaId, estado);
    }
    
}
