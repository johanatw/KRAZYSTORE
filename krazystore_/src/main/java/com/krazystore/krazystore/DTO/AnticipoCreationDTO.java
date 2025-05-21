/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.FormaCobroEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class AnticipoCreationDTO {
    private AnticipoEntity anticipo;
    private List<FormaCobroEntity> cobros;

    public AnticipoCreationDTO() {
    }

    public AnticipoCreationDTO(AnticipoEntity anticipo, List<FormaCobroEntity> cobros) {
        this.anticipo = anticipo;
        this.cobros = cobros;
    }

    public AnticipoEntity getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(AnticipoEntity anticipo) {
        this.anticipo = anticipo;
    }

    public List<FormaCobroEntity> getCobros() {
        return cobros;
    }

    public void setCobros(List<FormaCobroEntity> cobros) {
        this.cobros = cobros;
    }

    
    
    
}
