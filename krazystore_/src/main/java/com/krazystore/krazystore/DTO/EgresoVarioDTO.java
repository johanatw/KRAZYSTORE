/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.FormaCobroEntity;
import com.krazystore.krazystore.Entity.FormaPagoEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class EgresoVarioDTO {
    private MovimientoEntity movimiento;
    private List<FormaPagoEntity> pagos;

    public EgresoVarioDTO() {
    }

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }

    public List<FormaPagoEntity> getPagos() {
        return pagos;
    }

    public void setPagos(List<FormaPagoEntity> pagos) {
        this.pagos = pagos;
    }
    
    
}
