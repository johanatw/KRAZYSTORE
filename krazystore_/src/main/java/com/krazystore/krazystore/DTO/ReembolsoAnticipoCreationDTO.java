/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.FormaCobroEntity;
import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Entity.ReembolsoAnticipo;
import java.util.List;

/**
 *
 * @author HP
 */
public class ReembolsoAnticipoCreationDTO {
    private ReembolsoAnticipo reembolso;
    private List<FormaPagoEntity> pagos;

    public ReembolsoAnticipoCreationDTO() {
    }

    public ReembolsoAnticipo getReembolso() {
        return reembolso;
    }

    public void setReembolso(ReembolsoAnticipo reembolso) {
        this.reembolso = reembolso;
    }

    public List<FormaPagoEntity> getPagos() {
        return pagos;
    }

    public void setPagos(List<FormaPagoEntity> pagos) {
        this.pagos = pagos;
    }

    
    
}
