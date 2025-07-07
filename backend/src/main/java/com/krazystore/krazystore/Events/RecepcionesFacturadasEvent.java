/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

import java.util.List;

/**
 *
 * @author HP
 */
public class RecepcionesFacturadasEvent {
    private Long idCompra;
    private List<Long> idsRecepciones;

    public RecepcionesFacturadasEvent(Long idCompra, List<Long> idsRecepciones) {
        this.idCompra = idCompra;
        this.idsRecepciones = idsRecepciones;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public List<Long> getIdsRecepciones() {
        return idsRecepciones;
    }

    public void setIdsRecepciones(List<Long> idsRecepciones) {
        this.idsRecepciones = idsRecepciones;
    }
    
    
}
