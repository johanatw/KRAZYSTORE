/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.Events;

/**
 *
 * @author HP
 */

public class RecepcionFacturada {
    private Long idRecepcion;
    private char estado;

    public RecepcionFacturada(Long idRecepcion, char estado) {
        this.idRecepcion = idRecepcion;
        this.estado = estado;
    }

    

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    
    public Long getIdRecepcion() {
        return idRecepcion;
    }

    public void setIdRecepcion(Long idRecepcion) {
        this.idRecepcion = idRecepcion;
    }
    
    
}
