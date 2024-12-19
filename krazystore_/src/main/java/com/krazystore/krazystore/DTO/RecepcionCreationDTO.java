/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DetalleRecepcion;
import com.krazystore.krazystore.Entity.RecepcionEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecepcionCreationDTO {
    private RecepcionEntity recepcion;
    private List<DetalleRecepcion> detalle;

    public RecepcionEntity getRecepcion() {
        return recepcion;
    }

    public void setRecepcion(RecepcionEntity recepcion) {
        this.recepcion = recepcion;
    }

    public List<DetalleRecepcion> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleRecepcion> detalle) {
        this.detalle = detalle;
    }
    
    
}
