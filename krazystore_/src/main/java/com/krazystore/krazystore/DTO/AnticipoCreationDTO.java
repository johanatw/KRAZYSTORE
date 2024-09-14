/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.AnticipoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class AnticipoCreationDTO {
    private AnticipoEntity anticipo;
    private List<PagoEntity> pagos;

    public AnticipoCreationDTO() {
    }

    public AnticipoCreationDTO(AnticipoEntity anticipo, List<PagoEntity> pagos) {
        this.anticipo = anticipo;
        this.pagos = pagos;
    }

    public AnticipoEntity getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(AnticipoEntity anticipo) {
        this.anticipo = anticipo;
    }


    public List<PagoEntity> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoEntity> pagos) {
        this.pagos = pagos;
    }
    
    
}
