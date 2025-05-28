/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class VentaRecepcionarDTO {
    private VentaEntity venta;
    List<DetalleVentaPrepararDTO> detalle;

    public VentaRecepcionarDTO() {
    }

    public VentaRecepcionarDTO(VentaEntity venta, List<DetalleVentaPrepararDTO> detalle) {
        this.venta = venta;
        this.detalle = detalle;
    }

    public VentaEntity getVenta() {
        return venta;
    }

    public void setVenta(VentaEntity venta) {
        this.venta = venta;
    }

    public List<DetalleVentaPrepararDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleVentaPrepararDTO> detalle) {
        this.detalle = detalle;
    }
    
    
}
