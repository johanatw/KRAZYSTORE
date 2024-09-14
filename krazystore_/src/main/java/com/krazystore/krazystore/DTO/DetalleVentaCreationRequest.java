/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.DetalleVentaEntity;
import com.krazystore.krazystore.Entity.VentaEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class DetalleVentaCreationRequest {
    private VentaEntity venta;
    private List<DetalleVentaEntity> detalle;

    public DetalleVentaCreationRequest() {
    }

    public DetalleVentaCreationRequest(VentaEntity venta, List<DetalleVentaEntity> detalleVenta) {
        this.venta = venta;
        this.detalle = detalleVenta;
    }

    public VentaEntity getVenta() {
        return venta;
    }

    public void setVenta(VentaEntity venta) {
        this.venta = venta;
    }

    public List<DetalleVentaEntity> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleVentaEntity> detalle) {
        this.detalle = detalle;
    }
    
    

   
    
    
}
