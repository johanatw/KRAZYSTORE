/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.EntregaEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class EntregaCreationDTO {
    private EntregaEntity entrega;
    private List<DetalleEntregaDTO> detalle;

    public EntregaCreationDTO() {
    }

    public EntregaEntity getEntrega() {
        return entrega;
    }

    public void setEntrega(EntregaEntity entrega) {
        this.entrega = entrega;
    }

    public List<DetalleEntregaDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleEntregaDTO> detalle) {
        this.detalle = detalle;
    }

    
    
    
    
}
