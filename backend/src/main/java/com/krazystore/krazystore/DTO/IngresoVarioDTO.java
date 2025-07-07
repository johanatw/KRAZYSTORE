/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.FormaCobroEntity;
import com.krazystore.krazystore.Entity.MovimientoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class IngresoVarioDTO {
    private MovimientoEntity movimiento;
    private List<FormaCobroEntity> cobros;

    public IngresoVarioDTO() {
    }

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }

    public List<FormaCobroEntity> getCobros() {
        return cobros;
    }

    public void setCobros(List<FormaCobroEntity> cobros) {
        this.cobros = cobros;
    }
    
    
}
