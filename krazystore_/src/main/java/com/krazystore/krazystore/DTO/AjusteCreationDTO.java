/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.AjusteStock;
import java.util.List;

/**
 *
 * @author HP
 */
public class AjusteCreationDTO {
    private AjusteStock ajuste;
    private List<DetalleAjusteDTO> detalle;

    public AjusteCreationDTO() {
    }

    public AjusteStock getAjuste() {
        return ajuste;
    }

    public void setAjuste(AjusteStock ajuste) {
        this.ajuste = ajuste;
    }

    
    

    public List<DetalleAjusteDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleAjusteDTO> detalle) {
        this.detalle = detalle;
    }
    
    
}
