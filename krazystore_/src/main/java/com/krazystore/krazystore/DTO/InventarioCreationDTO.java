/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.CategoriaEntity;
import com.krazystore.krazystore.Entity.DetalleInventario;
import com.krazystore.krazystore.Entity.InventarioEntity;
import java.util.List;

/**
 *
 * @author HP
 */
public class InventarioCreationDTO {
    InventarioEntity inventario;
    private List<DetalleInventarioDTO> detalle;
    private List<CategoriaEntity> filtrosInventario;

    public InventarioEntity getInventario() {
        return inventario;
    }

    public void setInventario(InventarioEntity inventario) {
        this.inventario = inventario;
    }

    public List<DetalleInventarioDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleInventarioDTO> detalle) {
        this.detalle = detalle;
    }


    public List<CategoriaEntity> getFiltrosInventario() {
        return filtrosInventario;
    }

    public void setFiltrosInventario(List<CategoriaEntity> filtrosInventario) {
        this.filtrosInventario = filtrosInventario;
    }

    
    
    
}
