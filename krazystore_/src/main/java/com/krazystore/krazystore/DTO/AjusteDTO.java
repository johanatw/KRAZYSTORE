/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import java.util.Date;

/**
 *
 * @author HP
 */
public class AjusteDTO {
    private Long id;
    private Date fecha;
    private String observaciones;
    private char estado;

    public AjusteDTO(Long id, Date fecha, String observaciones) {
        this.id = id;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }

    public AjusteDTO(Long id, Date fecha, String observaciones, char estado) {
        this.id = id;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
}
