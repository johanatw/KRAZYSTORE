/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

import Utils.TipoEvento;
import com.krazystore.krazystore.Entity.AplicacionAnticipo;
import java.util.List;

/**
 *
 * @author HP
 */
public class AplicacionAnticiposEvent {
    private List<AplicacionAnticipo> anticiposAplicados;
    private final TipoEvento tipoEvento;

    public AplicacionAnticiposEvent(List<AplicacionAnticipo> anticiposAplicados, TipoEvento tipoEvento) {
        this.anticiposAplicados = anticiposAplicados;
        this.tipoEvento = tipoEvento;
    }

    public List<AplicacionAnticipo> getAnticiposAplicados() {
        return anticiposAplicados;
    }

    public void setAnticiposAplicados(List<AplicacionAnticipo> anticiposAplicados) {
        this.anticiposAplicados = anticiposAplicados;
    }
    
    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }
    
}
