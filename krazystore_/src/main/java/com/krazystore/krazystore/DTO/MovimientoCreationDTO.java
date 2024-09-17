/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.MovimientoEntity;
import com.krazystore.krazystore.Entity.PagoEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class MovimientoCreationDTO {
    private MovimientoEntity movimiento;
    private List<PagoEntity> pago;

    public MovimientoCreationDTO() {
    }

    public MovimientoCreationDTO(MovimientoEntity movimiento, List<PagoEntity> pago) {
        this.movimiento = movimiento;
        this.pago = pago;
    }

   

    public MovimientoEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoEntity movimiento) {
        this.movimiento = movimiento;
    }

    public List<PagoEntity> getPago() {
        return pago;
    }

    public void setPago(List<PagoEntity> pago) {
        this.pago = pago;
    }

   


    
    
}
