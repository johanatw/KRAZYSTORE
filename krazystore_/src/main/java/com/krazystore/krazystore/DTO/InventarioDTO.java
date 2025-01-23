/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;


import com.krazystore.krazystore.Entity.CategoriaEntity;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class InventarioDTO {
    private Long id;
    private char estado;
    private Date fecha;

    public InventarioDTO() {
    }

    
    public InventarioDTO(Long id, char estado, Date fecha) {
        this.id = id;
        this.estado = estado;
        this.fecha = fecha;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    
    
}
