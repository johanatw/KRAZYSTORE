/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.PagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class ReembolsoCreationDTO {
    private ReembolsoEntity reembolso;
    private List<PagoEntity> pagos;

    public ReembolsoCreationDTO() {
    }

    public ReembolsoEntity getReembolso() {
        return reembolso;
    }

    public void setReembolso(ReembolsoEntity reembolso) {
        this.reembolso = reembolso;
    }

    public List<PagoEntity> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoEntity> pagos) {
        this.pagos = pagos;
    }
    
    
}
