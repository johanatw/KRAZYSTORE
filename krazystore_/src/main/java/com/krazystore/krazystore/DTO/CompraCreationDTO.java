/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.krazystore.krazystore.DTO;

import com.krazystore.krazystore.Entity.CompraEntity;
import com.krazystore.krazystore.Entity.DetalleCompra;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author HP
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraCreationDTO {
    private CompraEntity compra;
    private List<DetalleCompraDTO> detalle;
    private List<Long> idRecepciones; //recepciones asociadas

    public CompraEntity getCompra() {
        return compra;
    }

    public void setCompra(CompraEntity compra) {
        this.compra = compra;
    }

    public List<DetalleCompraDTO> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleCompraDTO> detalle) {
        this.detalle = detalle;
    }

    public List<Long> getIdRecepciones() {
        return idRecepciones;
    }

    public void setIdRecepciones(List<Long> idRecepciones) {
        this.idRecepciones = idRecepciones;
    }
    
    
}
